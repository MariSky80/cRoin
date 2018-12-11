package com.croin.croin.database.converter

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Class that converts timestamp date from database to application and viceversa.
 */
class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}