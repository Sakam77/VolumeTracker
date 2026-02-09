package com.volumetracker.app.data.db.dao

import androidx.room.*
import com.volumetracker.app.data.db.entity.VolumeSnapshotEntity

@Dao
interface VolumeSnapshotDao {
    
    @Query("SELECT * FROM volume_snapshot WHERE tokenAddress = :address ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLatestForToken(address: String, limit: Int = 30): List<VolumeSnapshotEntity>
    
    @Query("SELECT * FROM volume_snapshot WHERE tokenAddress = :address AND timestamp >= :since ORDER BY timestamp ASC")
    suspend fun getForTokenSince(address: String, since: Long): List<VolumeSnapshotEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(snapshot: VolumeSnapshotEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(snapshots: List<VolumeSnapshotEntity>)
    
    @Query("DELETE FROM volume_snapshot WHERE timestamp < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)
    
    @Query("DELETE FROM volume_snapshot WHERE tokenAddress = :address")
    suspend fun deleteForToken(address: String)
}
