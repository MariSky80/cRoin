package com.croin.croin.repositories

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.croin.croin.database.dao.RecognitionDao
import com.croin.croin.database.entity.Recognition

/**
 * @author Maricel Bros Maimó
 *
 * RecognitionRepository class.
 *
 * Comunicates ViewModel with Dao objects.
 *
 */
class RecognitionRepository(private val recognitionDao: RecognitionDao) {

    //Gets LiveData of all recognitions of database.
    val allRecognitions: LiveData<List<Recognition>> = recognitionDao.getRecognitions()

    /**
     * Gets recognition by id
     * @param int id
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun recognition(id: Int) {
        recognitionDao.getRecognitionById(id)
    }


    /**
     * Insert
     * @param Recognition identity object
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(recognition: Recognition) {
        recognitionDao.insert(recognition)
    }

    /**
     * Updated
     * @param Recognition identity object
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updated(recognition: Recognition) {
        recognitionDao.update(recognition)
    }


    /**
     * Delete
     * @param Recognition identity object
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(recognition: Recognition) {
        recognitionDao.delete(recognition)
    }
}