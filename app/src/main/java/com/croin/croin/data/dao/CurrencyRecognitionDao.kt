package com.croin.croin.data.dao

import android.arch.persistence.room.*
import com.croin.croin.data.entity.CurrencyRecogniton

@Dao

interface CurrencyRecognitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyRecognition(currencyRecogniton: CurrencyRecogniton)

    @Update
    fun updateCurrencyRecognition(currencyRecogniton: CurrencyRecogniton)

    @Delete
    fun deleteCurrencyRecognition(currencyRecogniton: CurrencyRecogniton)

    @Query("SELECT * FROM currency_recognition, currencies WHERE currencies.id == :id")
    fun getCurrencyRecognitionByCId(id: Int): List<CurrencyRecogniton>

    @Query("SELECT * FROM currency_recognition, recognitions WHERE recognitions.id == :id")
    fun getCurrencyRecognitionByRId(id: Int): List<CurrencyRecogniton>

    @Query("SELECT * FROM currency_recognition, recognitions, currencies WHERE recognitions.id == :recognition_id AND currencies.id == :currency_id")
    fun getCurrencyRecognitionByRIdCid(recognition_id: Int, currency_id: Int): List<CurrencyRecogniton>


}