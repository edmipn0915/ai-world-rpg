package com.aiworld.rpg.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_saves")
data class GameSaveEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val worldJson: String,
    val characterJson: String,
    val currentLocationId: String,
    val updatedAt: Long = System.currentTimeMillis()
)
