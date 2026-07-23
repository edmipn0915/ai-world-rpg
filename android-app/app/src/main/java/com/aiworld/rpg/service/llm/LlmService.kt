package com.aiworld.rpg.service.llm

import com.aiworld.rpg.domain.model.ChatMessage
import com.aiworld.rpg.domain.model.ChatResponse
import com.aiworld.rpg.domain.model.ModelInfo
import kotlinx.coroutines.flow.Flow

interface LlmService {
    val modelInfo: ModelInfo
    suspend fun generate(messages: List<ChatMessage>): ChatResponse
    suspend fun generateStream(messages: List<ChatMessage>): Flow<String>
}

interface LlmServiceFactory {
    fun create(modelInfo: ModelInfo): LlmService
}
