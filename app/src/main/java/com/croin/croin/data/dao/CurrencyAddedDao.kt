package com.croin.croin.data.dao

import android.arch.persistence.room.*
import com.croin.croin.data.entity.CurrencyAdded

@Dao

interface CurrencyAddedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyAdded(currencyAdded: CurrencyAdded)

    @Update
    fun updateCurrencyAdded(currencyAdded: CurrencyAdded)

    @Delete
    fun deleteCurrencyAdded(currencyAdded: CurrencyAdded)

    @Query("SELECT * FROM currency_added WHERE preferred")
    fun getCurrencyAddedByPreferred(): CurrencyAdded

    @Query("SELECT * FROM currency_added ORDER BY preferred")
    fun getCurrenciesAdded(): List<CurrencyAdded>
}