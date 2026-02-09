package com.volumetracker.app.di

import android.content.Context
import androidx.room.Room
import com.volumetracker.app.data.db.VolumeTrackerDatabase
import com.volumetracker.app.data.db.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): VolumeTrackerDatabase {
        return Room.databaseBuilder(
            context,
            VolumeTrackerDatabase::class.java,
            VolumeTrackerDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideAlertHistoryDao(database: VolumeTrackerDatabase): AlertHistoryDao {
        return database.alertHistoryDao()
    }
    
    @Provides
    fun provideWatchlistDao(database: VolumeTrackerDatabase): WatchlistDao {
        return database.watchlistDao()
    }
    
    @Provides
    fun provideCustomAlertDao(database: VolumeTrackerDatabase): CustomAlertDao {
        return database.customAlertDao()
    }
    
    @Provides
    fun provideVolumeSnapshotDao(database: VolumeTrackerDatabase): VolumeSnapshotDao {
        return database.volumeSnapshotDao()
    }
}
