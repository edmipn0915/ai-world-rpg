package com.aiworld.rpg.domain.model

data class World(
    val id: String,
    val name: String,
    val type: WorldType,
    val description: String,
    val locations: List<Location> = emptyList(),
    val factions: List<Faction> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class WorldType(val displayName: String) {
    FANTASY("奇幻世界"),
    SCI_FI("科幻世界"),
    REALITY("現實世界"),
    CUSTOM("自訂世界")
}

data class Location(
    val id: String,
    val name: String,
    val description: String,
    val type: LocationType,
    val connections: List<String> = emptyList()
)

enum class LocationType {
    TOWN, DUNGEON, WILDERNESS, CASTLE, SHIP, SPACE_STATION
}

data class Faction(
    val id: String,
    val name: String,
    val description: String,
    val alignment: Alignment
)

enum class Alignment {
    GOOD, NEUTRAL, EVIL, CHAOTIC
}

data class Character(
    val id: String,
    val name: String,
    val race: String,
    val characterClass: String,
    val attributes: Attributes,
    val backstory: String,
    val worldId: String,
    val currentLocationId: String? = null
)

data class Attributes(
    val strength: Int = 10,
    val dexterity: Int = 10,
    val constitution: Int = 10,
    val intelligence: Int = 10,
    val wisdom: Int = 10,
    val charisma: Int = 10
)

data class Npc(
    val id: String,
    val name: String,
    val description: String,
    val personality: String,
    val goals: String,
    val currentLocationId: String,
    val factionId: String? = null,
    val relationshipWithPlayer: Int = 0,
    val memory: List<String> = emptyList()
)

data class ChatMessage(
    val id: String,
    val role: MessageRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageRole {
    USER, ASSISTANT, SYSTEM
}

data class GameSave(
    val id: String,
    val name: String,
    val world: World,
    val character: Character,
    val npcs: List<Npc> = emptyList(),
    val messageHistory: List<ChatMessage> = emptyList(),
    val currentLocationId: String,
    val updatedAt: Long = System.currentTimeMillis()
)

data class ModelInfo(
    val id: String,
    val name: String,
    val provider: ModelProvider,
    val isLocal: Boolean = false
)

enum class ModelProvider(val displayName: String) {
    LOCAL("本地模型"),
    OPENAI("OpenAI"),
    DEEPSEEK("DeepSeek"),
    GEMINI("Gemini"),
    LONGCAT("LongCat")
}

data class ChatRequest(
    val messages: List<ChatMessage>,
    val modelId: String,
    val temperature: Float = 0.7f,
    val maxTokens: Int = 1000
)

data class ChatResponse(
    val message: ChatMessage,
    val tokensUsed: Int = 0
)
