package com.volumetracker.app.data.db.dao

import androidx.room.*
import com.volumetracker.app.data.db.entity.AlertHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertHistoryDao {
    
    @Query("SELECT * FROM alert_history ORDER BY timestamp DESC")
    fun getAllFlow(): Flow<List<AlertHistoryEntity>>
    
    @Query("SELECT * FROM alert_history ORDER BY timestamp DESC")
    suspend fun getAll(): List<AlertHistoryEntity>
    
    @Query("SELECT * FROM alert_history WHERE id = :id")
    suspend fun getById(id: Long): AlertHistoryEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: AlertHistoryEntity): Long
    
    @Delete
    suspend fun delete(alert: AlertHistoryEntity)
    
    @Query("DELETE FROM alert_history WHERE timestamp < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)
    
    @Query("SELECT * FROM alert_history WHERE tokenAddress = :address ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestForToken(address: String): AlertHistoryEntity?
}
