package com.croin.croin.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index

@Entity(indices = [Index("preferred")],
        tableName = "currency_added",
        foreignKeys = [ForeignKey(entity = Currency::class,
                parentColumns = ["id"],
                childColumns = ["id"])]
)

data class CurrencyAdded(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val preferred: Boolean
)