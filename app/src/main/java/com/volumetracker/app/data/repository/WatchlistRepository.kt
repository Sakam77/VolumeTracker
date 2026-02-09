package com.volumetracker.app.data.repository

import com.volumetracker.app.data.db.dao.WatchlistDao
import com.volumetracker.app.data.db.entity.WatchlistEntity
import com.volumetracker.app.domain.model.Token
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepository @Inject constructor(
    private val watchlistDao: WatchlistDao
) {
    
    fun getWatchlistFlow(): Flow<List<WatchlistEntity>> {
        return watchlistDao.getAllFlow()
    }
    
    suspend fun isInWatchlist(address: String): Boolean {
        return watchlistDao.exists(address)
    }
    
    suspend fun addToWatchlist(token: Token) {
        val entity = WatchlistEntity(
            tokenAddress = token.address,
            tokenName = token.name,
            tokenSymbol = token.symbol,
            tokenLogo = token.logo,
            addedTimestamp = System.currentTimeMillis()
        )
        watchlistDao.insert(entity)
    }
    
    suspend fun removeFromWatchlist(address: String) {
        watchlistDao.deleteByAddress(address)
    }
    
    suspend fun getWatchlistItems(): List<WatchlistEntity> {
        return watchlistDao.getAll()
    }
}
