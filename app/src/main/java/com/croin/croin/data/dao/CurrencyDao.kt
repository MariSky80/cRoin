package com.croin.croin.data.dao

import android.arch.persistence.room.*
import com.croin.croin.data.entity.Currency

@Dao

interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currency: Currency)

    @Update
    fun updateCurrency(currency: Currency)

    @Delete
    fun deleteCurrency(currency: Currency)

    @Query("SELECT * FROM currencies WHERE iso == :iso")
    fun getCurrencyByIso(iso: String): List<Currency>

    @Query("SELECT * FROM currencies")
    fun getCurrencies(): List<Currency>
}