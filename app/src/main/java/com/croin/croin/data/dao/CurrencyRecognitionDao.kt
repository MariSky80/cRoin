package com.croin.croin.data.dao

import android.arch.persistence.room.*
import com.croin.croin.data.entity.CurrencyRecognition

@Dao

interface CurrencyRecognitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyRecognition(CurrencyRecognition: CurrencyRecognition)

    @Update
    fun updateCurrencyRecognition(CurrencyRecognition: CurrencyRecognition)

    @Delete
    fun deleteCurrencyRecognition(CurrencyRecognition: CurrencyRecognition)

    @Query("SELECT * FROM currency_recognition, currencies WHERE currencies.id == :id")
    fun getCurrencyRecognitionByCId(id: Int): List<CurrencyRecognition>

    @Query("SELECT * FROM currency_recognition, recognitions WHERE recognitions.id == :id")
    fun getCurrencyRecognitionByRId(id: Int): List<CurrencyRecognition>

    @Query("SELECT * FROM currency_recognition, recognitions, currencies WHERE recognitions.id == :recognition_id AND currencies.id == :currency_id")
    fun getCurrencyRecognitionByRIdCid(recognition_id: Int, currency_id: Int): List<CurrencyRecognition>


}