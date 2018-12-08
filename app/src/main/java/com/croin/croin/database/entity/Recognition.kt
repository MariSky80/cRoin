package com.croin.croin.database.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "recognitions")

data class Recognition(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val name: String,
        val comment: String,
        val image: String,
        val quantity: Double,
        val location: String,
        @ColumnInfo(name = "created_at")
        val createdAt: Date,
        @ColumnInfo(name = "updated_at")
        val updatedAt: Date
)