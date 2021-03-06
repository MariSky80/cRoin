package com.croin.croin.database.dao

import android.arch.persistence.room.*
import com.croin.croin.database.entity.Calculation

@Dao

/**
 * Interface CalculationDao
 * It's between repository and entity.
 * And is used to make select queries to database.
 */
interface CalculationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(calculation: Calculation)

    @Update
    fun update(calculation: Calculation)

    @Delete
    fun delete(calculation: Calculation)

    @Query("SELECT * FROM calculations WHERE id == :id")
    fun getCalculationById(id: String): Calculation

    @Query("SELECT * FROM calculations WHERE id == :id")
    fun getCalculationByRecognitionId(id: String): Calculation

    @Query("SELECT * FROM calculations")
    fun getCalculations(): List<Calculation>
}