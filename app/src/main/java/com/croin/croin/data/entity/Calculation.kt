package com.croin.croin.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(
        tableName = "calculations",
        foreignKeys = [ForeignKey(entity = Recognition::class,
                parentColumns = ["id"],
                childColumns = ["id"])]
)

data class Calculation(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        @ColumnInfo(name = "people_share")
        val peopleShare: Int,
        val change: Double,
        val difference: Double,
        val apportionment: Double
)