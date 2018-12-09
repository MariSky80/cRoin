package com.croin.croin.repositories

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.croin.croin.database.dao.RecognitionDao
import com.croin.croin.database.entity.Recognition


class RecognitionRepository(private val recognitionDao: RecognitionDao) {

    val allRecognitions: LiveData<List<Recognition>> = recognitionDao.getRecognitions()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun recognition(id: Int) {
        recognitionDao.getRecognitionById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(recognition: Recognition) {
        recognitionDao.insert(recognition)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updated(recognition: Recognition) {
        recognitionDao.update(recognition)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(recognition: Recognition) {
        recognitionDao.delete(recognition)
    }
}