package com.croin.croin.data


import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.croin.croin.data.converter.DateTypeConverter
import com.croin.croin.data.dao.*
import com.croin.croin.data.entity.*
import com.croin.croin.utilities.DATABASE_NAME

@Database(entities = [Calculation::class, CurrencyRecognition::class, Recognition::class],
            version = 1, exportSchema = false)

@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calculationDao(): CalculationDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun currencyRecognitionDao(): CurrencyRecognitionDao
    abstract fun recognitionDao(): RecognitionDao


    companion object {

        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java,
                            DATABASE_NAME)
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}