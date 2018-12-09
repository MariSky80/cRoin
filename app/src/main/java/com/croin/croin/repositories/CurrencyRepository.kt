package com.croin.croin.repositories

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.croin.croin.database.dao.CurrencyDao
import com.croin.croin.database.entity.Currency


class CurrencyRepository(private val currencyDao: CurrencyDao) {
    val allCurrencies: LiveData<List<Currency>> = currencyDao.getCurrencies()

    val preferred: LiveData<Currency> = currencyDao.getCurrencyByPreferred()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun noFavorites() {
        currencyDao.setNoFavorites()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(currency: Currency) {
        currencyDao.insert(currency)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updated(currency: Currency) {
        currencyDao.update(currency)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(currency: Currency) {
        currencyDao.delete(currency)
    }
}