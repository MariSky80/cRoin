package com.croin.croin.data.dao

import android.arch.persistence.room.*
import com.croin.croin.data.entity.Calculation

@Dao

interface CalculationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCalculation(calculation: Calculation)

    @Update
    fun updateCalculation(calculation: Calculation)

    @Delete
    fun deleteCalculation(calculation: Calculation)

    @Query("SELECT * FROM calculations WHERE id == :id")
    fun getCalculationById(id: String): Calculation

    @Query("SELECT * FROM calculations WHERE id == :id")
    fun getCalculationByRecognitionId(id: String): Calculation

    @Query("SELECT * FROM calculations")
    fun getCalculations(): List<Calculation>
}