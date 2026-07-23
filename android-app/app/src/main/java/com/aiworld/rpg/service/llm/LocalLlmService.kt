package com.aiworld.rpg.service.llm

import com.aiworld.rpg.domain.model.ChatMessage
import com.aiworld.rpg.domain.model.ChatResponse
import com.aiworld.rpg.domain.model.MessageRole
import com.aiworld.rpg.domain.model.ModelInfo
import com.aiworld.rpg.domain.model.ModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalLlmService @Inject constructor() : LlmService {

    private val loadedModels = mutableListOf<ModelInfo>()

    override val modelInfo: ModelInfo
        get() = ModelInfo("local-gguf", "本地 GGUF 模型", ModelProvider.LOCAL, isLocal = true)

    fun getAvailableModels(): List<ModelInfo> {
        return if (loadedModels.isEmpty()) {
            listOf(ModelInfo("local-default", "本地模型（需下載）", ModelProvider.LOCAL, isLocal = true))
        } else {
            loadedModels
        }
    }

    fun registerModel(modelPath: String, modelName: String) {
        val model = ModelInfo(
            id = "local-${System.currentTimeMillis()}",
            name = modelName,
            provider = ModelProvider.LOCAL,
            isLocal = true
        )
        loadedModels.add(model)
    }

    override suspend fun generate(messages: List<ChatMessage>): ChatResponse {
        val prompt = buildPrompt(messages)
        val response = callLlamaCpp(prompt)
        return ChatResponse(
            message = ChatMessage(
                id = "msg-${System.currentTimeMillis()}",
                role = MessageRole.ASSISTANT,
                content = response
            )
        )
    }

    override suspend fun generateStream(messages: List<ChatMessage>): Flow<String> = flow {
        val prompt = buildPrompt(messages)
        callLlamaCppStreaming(prompt).collect { token ->
            emit(token)
        }
    }.flowOn(Dispatchers.IO)

    private fun buildPrompt(messages: List<ChatMessage>): String {
        return messages.joinToString("\n") { msg ->
            when (msg.role) {
                MessageRole.SYSTEM -> "系統：${msg.content}"
                MessageRole.USER -> "玩家：${msg.content}"
                MessageRole.ASSISTANT -> "遊戲master：${msg.content}"
            }
        }
    }

    private suspend fun callLlamaCpp(prompt: String): String {
        // TODO: 綁定 llama.cpp Android 原生庫
        return "［本地模型回應］這是一個模擬回應。實際使用時需要整合 llama.cpp Android 綁定。"
    }

    private fun callLlamaCppStreaming(prompt: String): Flow<String> = flow {
        // TODO: 串流支援
        emit("［本地模型串流回應］")
    }
}
