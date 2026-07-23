package com.aiworld.rpg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiworld.rpg.domain.model.ChatMessage
import com.aiworld.rpg.domain.model.GameSave
import com.aiworld.rpg.domain.model.MessageRole
import com.aiworld.rpg.domain.model.ModelInfo
import com.aiworld.rpg.domain.model.Npc
import com.aiworld.rpg.domain.model.World
import com.aiworld.rpg.domain.model.WorldType
import com.aiworld.rpg.service.llm.ModelRouter
import com.aiworld.rpg.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameUiState(
    val isLoading: Boolean = false,
    val isGenerating: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val currentLocation: String = "",
    val currentNpc: Npc? = null,
    val error: String? = null
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val modelRouter: ModelRouter,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var currentWorld: World? = null
    private var currentModel: ModelInfo = ModelInfo("gpt-4o-mini", "GPT-4o-mini", com.aiworld.rpg.domain.model.ModelProvider.OPENAI)

    fun loadGame(saveId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val save = gameRepository.getSaveById(saveId)
                if (save != null) {
                    // TODO: 從 JSON 反序列化
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentLocation = save.currentLocationId
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "找不到存檔"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun createNewGame(worldType: WorldType, characterName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val world = generateWorld(worldType)
                currentWorld = world
                val welcomeMessage = ChatMessage(
                    id = "msg-${System.currentTimeMillis()}",
                    role = MessageRole.SYSTEM,
                    content = "歡迎來到 ${world.name}！${world.description}"
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    messages = listOf(welcomeMessage),
                    currentLocation = world.locations.firstOrNull()?.name ?: "未知地點"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun sendMessage(content: String) {
        val userMessage = ChatMessage(
            id = "msg-${System.currentTimeMillis()}",
            role = MessageRole.USER,
            content = content
        )
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMessage,
            isGenerating = true
        )

        viewModelScope.launch {
            try {
                val service = modelRouter.getService(currentModel)
                val response = service.generate(_uiState.value.messages)
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + response.message,
                    isGenerating = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isGenerating = false,
                    error = "生成失敗：${e.message}"
                )
            }
        }
    }

    fun setModel(model: ModelInfo) {
        currentModel = model
    }

    private suspend fun generateWorld(worldType: WorldType): World {
        val prompt = buildString {
            append("請創建一個${worldType.displayName}，包含：\n")
            append("1. 世界名稱\n")
            append("2. 世界描述（100-200字）\n")
            append("3. 至少3個地點\n")
            append("4. 至少2個派系\n")
        }
        return World(
            id = "world-${System.currentTimeMillis()}",
            name = "未命名世界",
            type = worldType,
            description = "一個充滿冒險的世界",
            locations = emptyList(),
            factions = emptyList()
        )
    }
}
