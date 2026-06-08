package com.example.proyectofinal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectofinal.data.model.UsuarioLoginEntity

@Dao
interface UsuarioLoginDao {

    // Obtener el usuario guardado (solo uno)
    @Query("SELECT * FROM usuario_login LIMIT 1")
    suspend fun getUsuario(): UsuarioLoginEntity?

    // Insertar o reemplazar usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioLoginEntity)

    // Borrar todos los usuarios (logout)
    @Query("DELETE FROM usuario_login")
    suspend fun clear()
}
