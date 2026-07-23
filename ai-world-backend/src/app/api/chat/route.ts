import { NextRequest, NextResponse } from "next/server";
import { ChatRequest, ChatResponse, getModelConfig, getApiKey } from "@/lib/models";
import { validateAuth } from "@/lib/auth";

export async function POST(req: NextRequest) {
  if (!validateAuth(req)) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  let body: ChatRequest;
  try {
    body = await req.json();
  } catch {
    return NextResponse.json({ error: "Invalid JSON body" }, { status: 400 });
  }

  const { messages, modelId, temperature = 0.7, maxTokens = 1000 } = body;

  if (!messages || !Array.isArray(messages) || messages.length === 0) {
    return NextResponse.json({ error: "Messages array is required" }, { status: 400 });
  }

  if (!modelId) {
    return NextResponse.json({ error: "modelId is required" }, { status: 400 });
  }

  const config = getModelConfig(modelId);
  if (!config) {
    return NextResponse.json(
      { error: `Unknown model: ${modelId}`, availableModels: Object.keys(getModelConfig) },
      { status: 400 }
    );
  }

  const apiKey = getApiKey(config);
  if (!apiKey) {
    return NextResponse.json(
      { error: `API key not configured for model: ${config.name}` },
      { status: 500 }
    );
  }

  try {
    const response = await callLlmApi(config.baseUrl, config.modelName, apiKey, messages, temperature, maxTokens);
    return NextResponse.json(response);
  } catch (error: any) {
    console.error(`[Chat API] Error calling ${config.name}:`, error.message);
    return NextResponse.json(
      { error: `Failed to call ${config.name}: ${error.message}` },
      { status: 502 }
    );
  }
}

async function callLlmApi(
  baseUrl: string,
  modelName: string,
  apiKey: string,
  messages: any[],
  temperature: number,
  maxTokens: number
): Promise<ChatResponse> {
  const formattedMessages = messages.map((msg) => ({
    role: msg.role,
    content: msg.content,
  }));

  const isGemini = baseUrl.includes("generativelanguage.googleapis.com");

  let url: string;
  let requestBody: any;
  let headers: Record<string, string>;

  if (isGemini) {
    url = `${baseUrl}/models/${modelName}:generateContent?key=${apiKey}`;
    requestBody = {
      contents: formattedMessages.map((msg) => ({
        role: msg.role === "assistant" ? "model" : msg.role === "system" ? "user" : msg.role,
        parts: [{ text: msg.content }],
      })),
      generationConfig: {
        temperature,
        maxOutputTokens: maxTokens,
      },
    };
    headers = { "Content-Type": "application/json" };
  } else {
    url = `${baseUrl}/chat/completions`;
    requestBody = {
      model: modelName,
      messages: formattedMessages,
      temperature,
      max_tokens: maxTokens,
    };
    headers = {
      "Content-Type": "application/json",
      Authorization: `Bearer ${apiKey}`,
    };
  }

  const res = await fetch(url, {
    method: "POST",
    headers,
    body: JSON.stringify(requestBody),
  });

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(`API returned ${res.status}: ${errorText}`);
  }

  const data = await res.json();

  let content: string;
  if (isGemini) {
    content = data?.candidates?.[0]?.content?.parts?.[0]?.text ?? "";
  } else {
    content = data?.choices?.[0]?.message?.content ?? "";
  }

  return {
    message: {
      id: `msg-${Date.now()}`,
      role: "assistant",
      content,
      timestamp: Date.now(),
    },
    tokensUsed: data?.usage?.total_tokens ?? 0,
  };
}
