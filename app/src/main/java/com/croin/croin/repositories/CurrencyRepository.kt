package com.croin.croin.repositories

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.croin.croin.database.dao.CurrencyDao
import com.croin.croin.database.entity.Currency

/**
 * @author Maricel Bros Maimó
 *
 * CurrencyRepository class.
 *
 * Comunicates ViewModel with Dao objects.
 *
 */
class CurrencyRepository(private val currencyDao: CurrencyDao) {

    //Get list of currencies
    val allCurrencies: LiveData<List<Currency>> = currencyDao.getCurrencies()

    //Gets preferred currency if exists one.
    val preferred: LiveData<Currency> = currencyDao.getCurrencyByPreferred()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun noFavorites() {
        currencyDao.setNoFavorites()
    }

    /**
     * Insert
     * @param CurrencyDao identity object
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(currency: Currency) {
        currencyDao.insert(currency)
    }


    /**
     * Updated
     * @param CurrencyDao identity object
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updated(currency: Currency) {
        currencyDao.update(currency)
    }


    /**
     * Delete
     * @param CurrencyDao identity object
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(currency: Currency) {
        currencyDao.delete(currency)
    }
}