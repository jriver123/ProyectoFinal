package com.example.proyectofinal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectofinal.data.model.HeroProgressEntity

@Dao
interface HeroProgressDao {

    @Query("SELECT * FROM hero_progress WHERE userId = :userId")
    suspend fun getByUser(userId: Long): List<HeroProgressEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: HeroProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(progress: List<HeroProgressEntity>)

    @Query("DELETE FROM hero_progress WHERE userId = :userId")
    suspend fun clearByUser(userId: Long)
}

