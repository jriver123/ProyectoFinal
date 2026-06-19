package com.example.proyectofinal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.proyectofinal.data.model.HeroProgressEntity
import com.example.proyectofinal.data.model.UsuarioLoginEntity

@Database(entities = [UsuarioLoginEntity::class, HeroProgressEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioLoginDao(): UsuarioLoginDao
    abstract fun heroProgressDao(): HeroProgressDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS hero_progress (
                        userId INTEGER NOT NULL,
                        heroId INTEGER NOT NULL,
                        level INTEGER NOT NULL,
                        currentXP INTEGER NOT NULL,
                        nextLevelXP INTEGER NOT NULL,
                        hpStat INTEGER NOT NULL,
                        attackStat INTEGER NOT NULL,
                        defenseStat INTEGER NOT NULL,
                        luckStat INTEGER NOT NULL,
                        attacksCsv TEXT NOT NULL,
                        PRIMARY KEY(userId, heroId)
                    )
                    """.trimIndent()
                )
            }
        }
    }
}