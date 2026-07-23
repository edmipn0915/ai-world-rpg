# AI World RPG - 實作計畫

## 摘要

開發一款基於大模型的 Android RPG 世界模擬應用「幻境」（AI World），以及對應的雲端模型代理後端，部署至 IGA Pages。

## 當前狀態

- 工作目錄為全新空目錄
- 尚未建立任何專案
- 需要從零開始建立 Android 應用與後端

## 設計決策

### Android 應用
- **UI 框架**：Jetpack Compose
- **架構**：MVVM + Repository
- **本地 LLM**：llama.cpp (GGUF)
- **雲端 LLM**：OpenAI / DeepSeek / Gemini / LongCat
- **資料庫**：Room + JSON 檔案
- **語言**：正體中文為主

### 後端
- **用途**：雲端模型代理（統一管理 API key，Android 應用透過後端呼叫）
- **部署**：IGA Pages
- **框架**：Next.js (API Routes)

---

## 實作步驟

### Phase 1: 建立 Android 專案

#### 1.1 初始化專案
- 使用 Android Studio 或指令建立新專案
- 設定 package name：`com.aiworld.rpg`
- 最低 SDK：API 26 (Android 8.0)
- 語言：Kotlin

#### 1.2 設定依賴
- Jetpack Compose BOM
- Hilt (依賴注入)
- Room (資料庫)
- Retrofit (網路請求)
- Kotlin Coroutines
- Navigation Compose
- DataStore (偏好設定)

#### 1.3 建立專案結構
```
com.aiworld.rpg/
├── data/
│   ├── local/          # Room DAO, Entity
│   ├── remote/         # Retrofit API
│   └── repository/     # Repository 實作
├── domain/
│   ├── model/          # 領域模型
│   └── usecase/       # 使用案例
├── ui/
│   ├── theme/          # Compose 主題
│   ├── components/     # 共用元件
│   ├── screen/         # 畫面
│   └── viewmodel/      # ViewModel
├── service/
│   ├── llm/            # LLM 服務（本地 + 雲端）
│   └── game/           # 遊戲邏輯服務
└── di/                 # Hilt 模組
```

### Phase 2: 實作核心功能

#### 2.1 主題與 UI 基礎
- 深色主題設定
- 導航架構（Navigation Compose）
- 共用元件（對話氣泡、角色卡片、地圖方塊）

#### 2.2 世界生成
- 世界類型選擇畫面（奇幻/科幻/現實/自訂）
- AI 生成世界（地點、歷史、文化、派系）
- 世界地圖視覺化

#### 2.3 角色創建
- 預設模板選擇
- AI 生成角色建議
- 手動調整（名稱、屬性、外貌、背景故事）

#### 2.4 LLM 服務層
- LLM 介面抽象化
- 本地 GGUF 推理（llama.cpp Android 綁定）
- 雲端 API 代理（透過後端）
- 模型切換邏輯

#### 2.5 NPC 系統
- NPC 生成（AI 人格、目標、情感）
- 對話系統（自由文字輸入）
- NPC 記憶與關係系統
- NPC 自主行為模擬

#### 2.6 戰鬥系統
- 回合制/半自動戰鬥
- 技能與屬性計算
- AI 根據情境生成戰鬥描述

#### 2.7 存檔系統
- Room 資料庫儲存結構化資料
- JSON 檔案儲存完整遊戲狀態
- 存檔/讀檔功能

### Phase 3: 建立後端（IGA Pages）

#### 3.1 初始化 Next.js 專案
```bash
npx create-next-app@latest ai-world-backend --typescript --tailwind --eslint --app --src-dir
cd ai-world-backend
```

#### 3.2 實作 API 路由
- `POST /api/chat` - 統一聊天端點
- 支援模型參數：`model` (openai/deepseek/gemini/longcat)
- 請求轉發至對應的雲端 API
- 回應格式統一

#### 3.3 環境變數管理
- 各雲端 API key 儲存於 IGA Pages 環境變數
- 请求驗證（簡易 token 驗證）

### Phase 4: 部署後端

#### 4.1 登入與部署
```bash
npm i -g @iga-pages/cli@latest
iga login
iga pages deploy --name ai-world-backend
```

#### 4.2 設定環境變數
- 在 IGA Pages 控制台設定各雲端 API key

#### 4.3 測試 API
- 驗證各模型端點正常運作

### Phase 5: 整合測試

#### 5.1 Android 連接後端
- 設定後端 URL
- 測試雲端模型透過後端代理
- 測試離線模式（本地模型）

#### 5.2 完整遊戲循環測試
- 建立角色 → 生成世界 → 探索 → 對話 → 戰鬥 → 存檔

## 檔案清單

### Android 專案
| 檔案 | 用途 |
|------|------|
| `app/build.gradle.kts` | 專案依賴設定 |
| `app/src/main/AndroidManifest.xml` | 應用宣告 |
| `ui/theme/Theme.kt` | 深色主題 |
| `ui/screen/WorldSelectScreen.kt` | 世界選擇畫面 |
| `ui/screen/CharacterCreateScreen.kt` | 角色創建畫面 |
| `ui/screen/GameScreen.kt` | 主遊戲畫面 |
| `service/llm/LlmService.kt` | LLM 抽象介面 |
| `service/llm/LocalLlmService.kt` | 本地模型服務 |
| `service/llm/RemoteLlmService.kt` | 雲端模型服務 |
| `service/llm/ModelRouter.kt` | 模型路由邏輯 |
| `data/local/AppDatabase.kt` | Room 資料庫 |
| `data/remote/BackendApi.kt` | 後端 API 介面 |
| `di/AppModule.kt` | Hilt 依賴注入模組 |

### 後端專案
| 檔案 | 用途 |
|------|------|
| `src/app/api/chat/route.ts` | 統一聊天端點 |
| `src/lib/models.ts` | 模型配置與轉發邏輯 |
| `src/lib/auth.ts` | 簡易驗證 |
| `.env.local` | 本地環境變數（不提交） |

## 假設與決策

1. **模型切換**：預設使用雲端，用戶可隨時手動切換
2. **本地模型**：首次使用提示用戶下載 GGUF 模型（約 1-4GB）
3. **後端驗證**：使用簡易 token，未來可擴展為完整 OAuth
4. **IGA Pages**：使用 Next.js，支援 API Routes
5. **語言**：正體中文為主要語言，UI 字串可抽換

## 驗證步驟

1. ✅ Android 專案可成功編譯執行
2. ✅ 世界生成功能正常
3. ✅ 角色創建流程完成
4. ✅ 本地模型可載入並生成回應
5. ✅ 雲端模型透過後端代理正常運作
6. ✅ NPC 對話有記憶功能
7. ✅ 戰鬥系統運作正常
8. ✅ 存檔/讀檔功能正常
9. ✅ 後端成功部署至 IGA Pages
10. ✅ API 端點回應正確

## 風險與注意事項

- llama.cpp Android 綁定需要 NDK 設定，可能較複雜
- 本地模型（7B+）需要 4GB+ RAM，需提示用戶
- GGUF 模型下載需考慮儲存空間和網路流量
- 多個雲端 API 需注意費率和限流
- IGA Pages 免費方案可能有請求限制
