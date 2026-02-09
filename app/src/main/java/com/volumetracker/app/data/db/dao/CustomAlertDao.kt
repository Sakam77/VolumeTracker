package com.volumetracker.app.data.db.dao

import androidx.room.*
import com.volumetracker.app.data.db.entity.CustomAlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomAlertDao {
    
    @Query("SELECT * FROM custom_alert WHERE enabled = 1")
    fun getAllEnabledFlow(): Flow<List<CustomAlertEntity>>
    
    @Query("SELECT * FROM custom_alert")
    suspend fun getAll(): List<CustomAlertEntity>
    
    @Query("SELECT * FROM custom_alert WHERE tokenAddress = :address")
    suspend fun getByAddress(address: String): CustomAlertEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: CustomAlertEntity)
    
    @Update
    suspend fun update(alert: CustomAlertEntity)
    
    @Delete
    suspend fun delete(alert: CustomAlertEntity)
    
    @Query("DELETE FROM custom_alert WHERE tokenAddress = :address")
    suspend fun deleteByAddress(address: String)
}
