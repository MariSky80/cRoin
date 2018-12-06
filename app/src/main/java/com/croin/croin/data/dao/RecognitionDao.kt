package com.croin.croin.data.dao

import android.arch.persistence.room.*
import com.croin.croin.data.entity.Recognition

@Dao

interface RecognitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecognition(recogniton: Recognition)

    @Update
    fun updateRecognition(recogniton: Recognition)

    @Delete
    fun deleteRecognition(recogniton: Recognition)

    @Query("SELECT * FROM recognitions WHERE id == :id")
    fun getRecognitionById(id: String): List<Recognition>

    @Query("SELECT * FROM recognitions ORDER BY created_at")
    fun getRecognitions(): List<Recognition>
}