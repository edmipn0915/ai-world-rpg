package com.aiworld.rpg.ui.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiworld.rpg.domain.model.ModelInfo
import com.aiworld.rpg.domain.model.ModelProvider
import com.aiworld.rpg.service.llm.ModelRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class SettingsUiState(
    val backendUrl: String = "",
    val selectedModelId: String = "gpt-4o-mini",
    val useLocalModel: Boolean = false,
    val autoSave: Boolean = true,
    val availableModels: List<ModelInfo> = emptyList()
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val modelRouter: ModelRouter
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    companion object {
        private val BACKEND_URL_KEY = stringPreferencesKey("backend_url")
        private val SELECTED_MODEL_KEY = stringPreferencesKey("selected_model")
        private val USE_LOCAL_MODEL_KEY = booleanPreferencesKey("use_local_model")
        private val AUTO_SAVE_KEY = booleanPreferencesKey("auto_save")
    }

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val prefs = context.dataStore.data.first()
            val models = modelRouter.getAvailableModels()
            _uiState.value = _uiState.value.copy(
                backendUrl = prefs[BACKEND_URL_KEY] ?: "https://your-backend.iga.pages/",
                selectedModelId = prefs[SELECTED_MODEL_KEY] ?: "gpt-4o-mini",
                useLocalModel = prefs[USE_LOCAL_MODEL_KEY] ?: false,
                autoSave = prefs[AUTO_SAVE_KEY] ?: true,
                availableModels = models
            )
        }
    }

    fun setBackendUrl(url: String) {
        viewModelScope.launch {
            context.dataStore.edit { it[BACKEND_URL_KEY] = url }
            _uiState.value = _uiState.value.copy(backendUrl = url)
        }
    }

    fun setModel(modelId: String) {
        viewModelScope.launch {
            context.dataStore.edit { it[SELECTED_MODEL_KEY] = modelId }
            _uiState.value = _uiState.value.copy(selectedModelId = modelId)
        }
    }

    fun setUseLocalModel(useLocal: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { it[USE_LOCAL_MODEL_KEY] = useLocal }
            _uiState.value = _uiState.value.copy(useLocalModel = useLocal)
        }
    }

    fun setAutoSave(autoSave: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { it[AUTO_SAVE_KEY] = autoSave }
            _uiState.value = _uiState.value.copy(autoSave = autoSave)
        }
    }
}
