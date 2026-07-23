package com.aiworld.rpg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiworld.rpg.data.local.GameSaveEntity
import com.aiworld.rpg.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavesUiState(
    val saves: List<GameSaveEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SavesViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavesUiState())
    val uiState: StateFlow<SavesUiState> = _uiState.asStateFlow()

    init {
        loadSaves()
    }

    private fun loadSaves() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            gameRepository.getAllSaves().collect { saves ->
                _uiState.value = _uiState.value.copy(
                    saves = saves,
                    isLoading = false
                )
            }
        }
    }

    fun deleteSave(id: String) {
        viewModelScope.launch {
            try {
                gameRepository.deleteSave(id)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
