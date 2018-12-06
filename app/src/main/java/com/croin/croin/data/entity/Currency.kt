package com.croin.croin.data.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "currencies",indices = [Index("iso")])

data class Currency(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val name: String,
        val iso: String,
        val symbol: String
)