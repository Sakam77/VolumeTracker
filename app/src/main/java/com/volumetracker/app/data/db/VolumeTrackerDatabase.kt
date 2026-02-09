package com.volumetracker.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.volumetracker.app.data.db.dao.*
import com.volumetracker.app.data.db.entity.*

@Database(
    entities = [
        AlertHistoryEntity::class,
        WatchlistEntity::class,
        CustomAlertEntity::class,
        VolumeSnapshotEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class VolumeTrackerDatabase : RoomDatabase() {
    
    abstract fun alertHistoryDao(): AlertHistoryDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun customAlertDao(): CustomAlertDao
    abstract fun volumeSnapshotDao(): VolumeSnapshotDao
    
    companion object {
        const val DATABASE_NAME = "volume_tracker_db"
    }
}
