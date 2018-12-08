package com.croin.croin.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(
        tableName = "currency_recognition",
        foreignKeys = [ForeignKey(entity = Recognition::class,
                            parentColumns = ["id"],
                            childColumns = ["id"]),
                        ForeignKey(entity = Currency::class,
                            parentColumns = ["id"],
                            childColumns = ["id"])]
)

data class CurrencyRecognition(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val value: Float
)