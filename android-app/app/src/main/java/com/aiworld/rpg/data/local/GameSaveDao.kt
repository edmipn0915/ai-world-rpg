package com.aiworld.rpg.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameSaveDao {
    @Query("SELECT * FROM game_saves ORDER BY updatedAt DESC")
    fun getAllSaves(): Flow<List<GameSaveEntity>>

    @Query("SELECT * FROM game_saves WHERE id = :id")
    suspend fun getSaveById(id: String): GameSaveEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSave(save: GameSaveEntity)

    @Update
    suspend fun updateSave(save: GameSaveEntity)

    @Delete
    suspend fun deleteSave(save: GameSaveEntity)

    @Query("DELETE FROM game_saves WHERE id = :id")
    suspend fun deleteSaveById(id: String)
}
