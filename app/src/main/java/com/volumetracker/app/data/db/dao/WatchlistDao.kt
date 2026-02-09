package com.volumetracker.app.data.db.dao

import androidx.room.*
import com.volumetracker.app.data.db.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    
    @Query("SELECT * FROM watchlist ORDER BY addedTimestamp DESC")
    fun getAllFlow(): Flow<List<WatchlistEntity>>
    
    @Query("SELECT * FROM watchlist ORDER BY addedTimestamp DESC")
    suspend fun getAll(): List<WatchlistEntity>
    
    @Query("SELECT * FROM watchlist WHERE tokenAddress = :address")
    suspend fun getByAddress(address: String): WatchlistEntity?
    
    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE tokenAddress = :address)")
    suspend fun exists(address: String): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WatchlistEntity)
    
    @Delete
    suspend fun delete(item: WatchlistEntity)
    
    @Query("DELETE FROM watchlist WHERE tokenAddress = :address")
    suspend fun deleteByAddress(address: String)
}
