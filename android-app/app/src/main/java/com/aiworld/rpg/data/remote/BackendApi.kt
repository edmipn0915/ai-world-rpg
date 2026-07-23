package com.aiworld.rpg.data.remote

import com.aiworld.rpg.domain.model.ChatRequest
import com.aiworld.rpg.domain.model.ChatResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendApi {
    @POST("/api/chat")
    suspend fun chat(@Body request: ChatRequest): ChatResponse

    @POST("/api/chat/stream")
    suspend fun chatStream(@Body request: ChatRequest): Flow<String>
}
