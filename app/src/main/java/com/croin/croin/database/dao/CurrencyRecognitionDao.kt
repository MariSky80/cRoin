package com.croin.croin.database.dao

import android.arch.persistence.room.*
import com.croin.croin.database.entity.CurrencyRecognition

@Dao

interface CurrencyRecognitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(CurrencyRecognition: CurrencyRecognition)

    @Update
    fun update(CurrencyRecognition: CurrencyRecognition)

    @Delete
    fun delete(CurrencyRecognition: CurrencyRecognition)

    @Query("SELECT * FROM currency_recognition, currencies WHERE currencies.id == :id")
    fun getCurrencyRecognitionByCId(id: String): List<CurrencyRecognition>

    @Query("SELECT * FROM currency_recognition, recognitions WHERE recognitions.id == :id")
    fun getCurrencyRecognitionByRId(id: Int): List<CurrencyRecognition>

    @Query("SELECT * FROM currency_recognition, recognitions, currencies WHERE recognitions.id == :recognition_id AND currencies.id == :currency_id")
    fun getCurrencyRecognitionByRIdCid(recognition_id: Int, currency_id: String): List<CurrencyRecognition>


}