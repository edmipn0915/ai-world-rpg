export type MessageRole = "system" | "user" | "assistant";

export interface ChatMessage {
  id?: string;
  role: MessageRole;
  content: string;
  timestamp?: number;
}

export interface ChatRequest {
  messages: ChatMessage[];
  modelId: string;
  temperature?: number;
  maxTokens?: number;
}

export interface ChatResponse {
  message: ChatMessage;
  tokensUsed?: number;
}

interface ModelConfig {
  id: string;
  name: string;
  provider: "openai" | "deepseek" | "gemini" | "longcat";
  baseUrl: string;
  apiKeyEnv: string;
  modelName: string;
}

export const MODEL_CONFIGS: Record<string, ModelConfig> = {
  "gpt-4o": {
    id: "gpt-4o",
    name: "GPT-4o",
    provider: "openai",
    baseUrl: "https://api.openai.com/v1",
    apiKeyEnv: "OPENAI_API_KEY",
    modelName: "gpt-4o",
  },
  "gpt-4o-mini": {
    id: "gpt-4o-mini",
    name: "GPT-4o Mini",
    provider: "openai",
    baseUrl: "https://api.openai.com/v1",
    apiKeyEnv: "OPENAI_API_KEY",
    modelName: "gpt-4o-mini",
  },
  "deepseek-chat": {
    id: "deepseek-chat",
    name: "DeepSeek Chat",
    provider: "deepseek",
    baseUrl: "https://api.deepseek.com/v1",
    apiKeyEnv: "DEEPSEEK_API_KEY",
    modelName: "deepseek-chat",
  },
  "gemini-1.5-pro": {
    id: "gemini-1.5-pro",
    name: "Gemini 1.5 Pro",
    provider: "gemini",
    baseUrl: "https://generativelanguage.googleapis.com/v1beta",
    apiKeyEnv: "GEMINI_API_KEY",
    modelName: "gemini-1.5-pro",
  },
  "longcat-chat": {
    id: "longcat-chat",
    name: "LongCat Chat",
    provider: "longcat",
    baseUrl: "https://api.longcat.chat/v1",
    apiKeyEnv: "LONGCAT_API_KEY",
    modelName: "longcat-chat",
  },
};

export function getModelConfig(modelId: string): ModelConfig | null {
  return MODEL_CONFIGS[modelId] || null;
}

export function getApiKey(config: ModelConfig): string | undefined {
  return process.env[config.apiKeyEnv];
}
