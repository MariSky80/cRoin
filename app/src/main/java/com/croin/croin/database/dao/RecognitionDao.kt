package com.croin.croin.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.croin.croin.database.entity.Recognition

@Dao

interface RecognitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recogniton: Recognition)

    @Update
    fun update(recogniton: Recognition)

    @Delete
    fun delete(recogniton: Recognition)

    @Query("SELECT * FROM recognitions WHERE id == :id")
    fun getRecognitionById(id: Int): LiveData<Recognition>

    @Query("SELECT * FROM recognitions ORDER BY created_at")
    fun getRecognitions(): LiveData<List<Recognition>>
}