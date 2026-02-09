package com.volumetracker.app.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.volumetracker.app.MainActivity
import com.volumetracker.app.R
import com.volumetracker.app.domain.model.Token
import com.volumetracker.app.domain.usecase.DetectVolumeSpikeUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@HiltWorker
class VolumeSpikeWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val detectVolumeSpikeUseCase: DetectVolumeSpikeUseCase
) : CoroutineWorker(context, workerParams) {
    
    override suspend fun doWork(): Result {
        Timber.d("VolumeSpikeWorker started")
        
        return try {
            // Detect volume spikes
            val spikes = detectVolumeSpikeUseCase.detectSpikes()
            
            // Send notifications for each spike
            spikes.forEach { token ->
                sendNotification(token)
            }
            
            Timber.i("VolumeSpikeWorker completed: ${spikes.size} spikes detected")
            Result.success()
            
        } catch (e: Exception) {
            Timber.e(e, "VolumeSpikeWorker failed")
            Result.retry()
        }
    }
    
    private fun sendNotification(token: Token) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Create notification channel (for Android O+)
        createNotificationChannel(notificationManager)
        
        // Create intent to open app
        val intent = Intent(context, MainActivity::class).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("token_address", token.address)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            token.address.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Build notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText("${token.symbol} • ${String.format("%.1f", token.volume24h / 1000)}x volume • $${String.format("%.6f", token.price)}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()
        
        notificationManager.notify(token.address.hashCode(), notification)
    }
    
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notification_channel_description)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 500, 200, 500)
        }
        notificationManager.createNotificationChannel(channel)
    }
    
    companion object {
        private const val CHANNEL_ID = "volume_spike_alerts"
        private const val WORK_NAME = "volume_spike_monitor"
        
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            
            val workRequest = PeriodicWorkRequestBuilder<VolumeSpikeWorker>(
                repeatInterval = 2,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
            
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
            
            Timber.i("VolumeSpikeWorker scheduled")
        }
        
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
            Timber.i("VolumeSpikeWorker cancelled")
        }
    }
}
