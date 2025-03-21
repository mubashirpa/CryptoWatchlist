package com.example.cryptowatchlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "update_time")
data class UpdateTimeEntity(
    @PrimaryKey val id: Int,
    val lastUpdated: Long,
)
