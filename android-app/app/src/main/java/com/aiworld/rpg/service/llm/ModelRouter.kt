package com.aiworld.rpg.service.llm

import com.aiworld.rpg.domain.model.ModelInfo
import com.aiworld.rpg.domain.model.ModelProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelRouter @Inject constructor(
    private val localLlmService: LocalLlmService,
    private val remoteLlmService: RemoteLlmService
) {
    fun getService(modelInfo: ModelInfo): LlmService {
        return when {
            modelInfo.isLocal || modelInfo.provider == ModelProvider.LOCAL -> localLlmService
            else -> remoteLlmService
        }
    }

    fun getAvailableModels(): List<ModelInfo> {
        return localLlmService.getAvailableModels() + listOf(
            ModelInfo("gpt-4o", "GPT-4o", ModelProvider.OPENAI),
            ModelInfo("gpt-4o-mini", "GPT-4o Mini", ModelProvider.OPENAI),
            ModelInfo("deepseek-chat", "DeepSeek Chat", ModelProvider.DEEPSEEK),
            ModelInfo("gemini-1.5-pro", "Gemini 1.5 Pro", ModelProvider.GEMINI),
            ModelInfo("longcat-chat", "LongCat Chat", ModelProvider.LONGCAT)
        )
    }
}
