package com.example.cryptowatchlist.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.cryptowatchlist.data.local.entity.UpdateTimeEntity

@Dao
interface UpdateTimeDao {
    @Upsert
    suspend fun upsertUpdateTime(updateTime: UpdateTimeEntity)

    @Query("SELECT * FROM update_time WHERE id = :id")
    suspend fun getUpdateTimeById(id: Int): UpdateTimeEntity?

    @Query("DELETE FROM update_time WHERE id = :id")
    suspend fun deleteUpdateTimeById(id: Int)
}
