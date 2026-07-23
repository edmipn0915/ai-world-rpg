package com.aiworld.rpg.service.llm

import com.aiworld.rpg.data.remote.BackendApi
import com.aiworld.rpg.domain.model.ChatMessage
import com.aiworld.rpg.domain.model.ChatRequest
import com.aiworld.rpg.domain.model.ChatResponse
import com.aiworld.rpg.domain.model.MessageRole
import com.aiworld.rpg.domain.model.ModelInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteLlmService @Inject constructor(
    private val backendApi: BackendApi
) : LlmService {

    override val modelInfo: ModelInfo
        get() = ModelInfo("remote", "雲端模型", com.aiworld.rpg.domain.model.ModelProvider.OPENAI)

    override suspend fun generate(messages: List<ChatMessage>): ChatResponse {
        val request = ChatRequest(
            messages = messages,
            modelId = "gpt-4o-mini"
        )
        val response = backendApi.chat(request)
        return response
    }

    override suspend fun generateStream(messages: List<ChatMessage>): Flow<String> = flow {
        val request = ChatRequest(
            messages = messages,
            modelId = "gpt-4o-mini"
        )
        backendApi.chatStream(request).collect { token ->
            emit(token)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun generateWithModel(messages: List<ChatMessage>, modelId: String): ChatResponse {
        val request = ChatRequest(
            messages = messages,
            modelId = modelId
        )
        return backendApi.chat(request)
    }

    suspend fun generateStreamWithModel(messages: List<ChatMessage>, modelId: String): Flow<String> = flow {
        val request = ChatRequest(
            messages = messages,
            modelId = modelId
        )
        backendApi.chatStream(request).collect { token ->
            emit(token)
        }
    }.flowOn(Dispatchers.IO)
}
