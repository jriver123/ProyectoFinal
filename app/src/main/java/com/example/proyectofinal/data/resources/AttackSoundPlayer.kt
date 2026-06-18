package com.example.proyectofinal.data.resources

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.proyectofinal.R

// ============================================================================
// MAPEO CENTRALIZADO DE SONIDOS POR ID DE ATAQUE
// ============================================================================
//
// Sonidos de ataques exitosos (mapeados por ID de ataque)
private val attackSounds: Map<Int, Int> = mapOf(
    1 to R.raw.golpe1,
    7 to R.raw.fireball1,
    3 to R.raw.headbutt1

)

private val attackMissSounds: Map<Int, Int> = mapOf(
    1 to R.raw.golpe1_miss,
    7 to R.raw.fireball1_miss,
    3 to R.raw.headbutt1_miss

)

fun playAttackSound(context: Context, attackId: Int) {
    val soundRes = attackSounds[attackId]
    if (soundRes == null) {
        Log.d("AttackSoundPlayer", "No hay sonido configurado para el ataque con ID=$attackId")
        return
    }
    playSoundResource(context, soundRes, "AttackSound_$attackId")
}


fun playAttackMissSound(context: Context, attackId: Int) {
    val soundRes = attackMissSounds[attackId]
    if (soundRes == null) {
        Log.d("AttackMissSound", "No hay sonido de fallo configurado para el ataque con ID=$attackId")
        return
    }
    playSoundResource(context, soundRes, "AttackMissSound_$attackId")
}

private fun playSoundResource(context: Context, soundRes: Int, tag: String) {
    val mediaPlayer = MediaPlayer.create(context, soundRes)
    if (mediaPlayer == null) {
        Log.e(tag, "No se pudo crear MediaPlayer para $tag")
        return
    }

    mediaPlayer.setOnCompletionListener { player ->
        player.release()
    }
    mediaPlayer.setOnErrorListener { player, _, _ ->
        player.release()
        true
    }

    mediaPlayer.start()
}
