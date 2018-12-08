package com.croin.croin.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.croin.croin.database.converter.DateTypeConverter
import com.croin.croin.database.dao.*
import com.croin.croin.database.entity.*
import com.croin.croin.utilities.DATABASE_NAME
import com.croin.croin.utilities.DATABASE_VERSION

@Database(
            entities = [
                Calculation::class,
                CurrencyRecognition::class,
                Currency::class,
                Recognition::class
            ],
            version = DATABASE_VERSION
         )

@TypeConverters(DateTypeConverter::class)


abstract class CroinDatabase : RoomDatabase() {

    abstract fun calculationDao(): CalculationDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun currencyRecognitionDao(): CurrencyRecognitionDao
    abstract fun recognitionDao(): RecognitionDao


    companion object {

        @Volatile
        private var INSTANCE: CroinDatabase? = null

        fun getDatabase(context: Context): CroinDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CroinDatabase::class.java,
                        DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}