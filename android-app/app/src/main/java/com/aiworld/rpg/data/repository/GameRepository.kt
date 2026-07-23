package com.aiworld.rpg.data.repository

import com.aiworld.rpg.data.local.GameSaveDao
import com.aiworld.rpg.data.local.GameSaveEntity
import com.aiworld.rpg.domain.model.GameSave
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val gameSaveDao: GameSaveDao
) {
    fun getAllSaves(): Flow<List<GameSaveEntity>> = gameSaveDao.getAllSaves()

    suspend fun getSaveById(id: String): GameSaveEntity? = gameSaveDao.getSaveById(id)

    suspend fun saveGame(save: GameSaveEntity) = gameSaveDao.insertSave(save)

    suspend fun deleteSave(id: String) = gameSaveDao.deleteSaveById(id)
}
