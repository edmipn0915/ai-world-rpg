package com.aiworld.rpg.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GameSaveEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameSaveDao(): GameSaveDao
}
