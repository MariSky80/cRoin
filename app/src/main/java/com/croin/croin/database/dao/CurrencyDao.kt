package com.croin.croin.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.croin.croin.database.entity.Currency

@Dao
/**
 * Interface CurrencyDao
 * It's between repository and entity.
 * And is used to make select queries to database.
 */
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currency: Currency)

    @Update
    fun update(currency: Currency)

    @Delete
    fun delete(currency: Currency)

    @Query("SELECT * FROM currencies WHERE id == :id")
    fun getCurrencyById(id: String): Currency

    @Query("UPDATE currencies SET preferred = 0")
    fun setNoFavorites()

    @Query("SELECT * FROM currencies WHERE preferred LIMIT 1")
    fun getCurrencyByPreferred(): LiveData<Currency>

    @Query("SELECT * FROM currencies")
    fun getCurrencies():  LiveData<List<Currency>>

}