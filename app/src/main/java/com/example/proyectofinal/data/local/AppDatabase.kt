package com.example.proyectofinal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.proyectofinal.data.model.UsuarioLoginEntity

@Database(entities = [UsuarioLoginEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioLoginDao(): UsuarioLoginDao
}