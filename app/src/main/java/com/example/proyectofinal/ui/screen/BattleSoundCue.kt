package com.example.proyectofinal.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.proyectofinal.data.resources.playAttackSound
import com.example.proyectofinal.data.resources.playAttackMissSound

sealed class BattleSoundCue {
    data class AttackHit(val attackId: Int, val cueId: Long = System.nanoTime()) : BattleSoundCue()
    data class AttackMiss(val attackId: Int, val cueId: Long = System.nanoTime()) : BattleSoundCue()
}

@Composable
fun rememberAttackSoundPlayer(): (BattleSoundCue) -> Unit {
    val context = LocalContext.current
    return { cue ->
        when (cue) {
            is BattleSoundCue.AttackHit -> playAttackSound(context, cue.attackId)
            is BattleSoundCue.AttackMiss -> playAttackMissSound(context, cue.attackId)
        }
    }
}

