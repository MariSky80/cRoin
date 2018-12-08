package com.croin.croin.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "currencies")

data class Currency(
        @PrimaryKey
        val id: String,
        val name: String,
        val symbol: String?,
        val preferred: Boolean
)