package com.volumetracker.app.data.repository

import com.volumetracker.app.data.db.dao.AlertHistoryDao
import com.volumetracker.app.data.db.dao.CustomAlertDao
import com.volumetracker.app.data.db.entity.AlertHistoryEntity
import com.volumetracker.app.data.db.entity.CustomAlertEntity
import com.volumetracker.app.domain.model.AlertItem
import com.volumetracker.app.domain.model.CustomAlert
import com.volumetracker.app.domain.model.Token
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertRepository @Inject constructor(
    private val alertHistoryDao: AlertHistoryDao,
    private val customAlertDao: CustomAlertDao,
    private val tokenRepository: TokenRepository
) {
    
    fun getAlertHistoryFlow(): Flow<List<AlertHistoryEntity>> {
        return alertHistoryDao.getAllFlow()
    }
    
    suspend fun saveAlert(
        token: Token,
        priceAtAlert: Double,
        volumeMultiplier: Double
    ): Long {
        val entity = AlertHistoryEntity(
            tokenAddress = token.address,
            tokenName = token.name,
            tokenSymbol = token.symbol,
            tokenLogo = token.logo,
            priceAtAlert = priceAtAlert,
            currentPrice = token.price,
            volumeMultiplier = volumeMultiplier,
            marketCap = token.marketCap,
            timestamp = System.currentTimeMillis()
        )
        return alertHistoryDao.insert(entity)
    }
    
    suspend fun updateAlertPrice(alertId: Long, currentPrice: Double) {
        val alert = alertHistoryDao.getById(alertId)
        if (alert != null) {
            alertHistoryDao.insert(alert.copy(currentPrice = currentPrice))
        }
    }
    
    suspend fun getLatestAlertForToken(address: String): AlertHistoryEntity? {
        return alertHistoryDao.getLatestForToken(address)
    }
    
    // Custom Alerts
    fun getCustomAlertsFlow(): Flow<List<CustomAlert>> {
        return customAlertDao.getAllEnabledFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun getCustomAlert(address: String): CustomAlert? {
        return customAlertDao.getByAddress(address)?.toDomain()
    }
    
    suspend fun saveCustomAlert(alert: CustomAlert) {
        customAlertDao.insert(alert.toEntity())
    }
    
    suspend fun deleteCustomAlert(address: String) {
        customAlertDao.deleteByAddress(address)
    }
    
    suspend fun cleanOldAlerts(daysToKeep: Int = 7) {
        val cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L)
        alertHistoryDao.deleteOlderThan(cutoffTime)
    }
    
    private fun CustomAlertEntity.toDomain() = CustomAlert(
        tokenAddress = tokenAddress,
        tokenName = tokenName,
        tokenSymbol = tokenSymbol,
        volumeThreshold = volumeThreshold,
        enabled = enabled
    )
    
    private fun CustomAlert.toEntity() = CustomAlertEntity(
        tokenAddress = tokenAddress,
        tokenName = tokenName,
        tokenSymbol = tokenSymbol,
        volumeThreshold = volumeThreshold,
        enabled = enabled,
        createdTimestamp = System.currentTimeMillis()
    )
}
