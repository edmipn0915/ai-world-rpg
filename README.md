# 幻境 AI World - 基於大模型的 RPG 世界模擬遊戲

## 專案結構

```
├── android-app/          # Android 用戶端應用
│   ├── app/
│   │   ├── build.gradle.kts
│   │   └── src/main/
│   │       ├── AndroidManifest.xml
│   │       ├── java/com/aiworld/rpg/
│   │       │   ├── AIWorldApplication.kt
│   │       │   ├── MainActivity.kt
│   │       │   ├── data/           # 資料層（Room + Retrofit）
│   │       │   ├── di/             # Hilt 依賴注入
│   │       │   ├── domain/         # 領域模型
│   │       │   ├── service/        # LLM 服務
│   │       │   └── ui/             # Jetpack Compose UI
│   │       └── res/                # 資源檔
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── gradle/
│
├── ai-world-backend/     # 雲端模型代理後端
│   ├── src/
│   │   ├── app/
│   │   │   ├── api/chat/route.ts   # 統一聊天端點
│   │   │   ├── layout.tsx
│   │   │   └── page.tsx
│   │   └── lib/
│   │       ├── models.ts           # 模型配置
│   │       └── auth.ts             # 驗證
│   ├── package.json
│   ├── next.config.js
│   └── tsconfig.json
│
└── .trae/documents/      # 設計文件
    └── ai-world-rpg-plan.md
```

## 功能特色

- 🎭 **多種世界類型**：奇幻、科幻、現實、自訂世界
- 🤖 **多模型支援**：OpenAI、DeepSeek、Gemini、LongCat + 本地 GGUF
- 💬 **AI NPC 對話**：NPC 擁有記憶、目標、情感
- ⚔️ **半自動戰鬥**：策略性回合制戰鬥
- 💾 **本地存檔**：Room 資料庫 + JSON 檔案

## 快速開始

### Android 應用

**方法一：Android Studio**
1. 使用 Android Studio 開啟 `android-app/` 目錄
2. 等待 Gradle 同步完成
3. 連接裝置或啟動模擬器
4. 點擊執行

**方法二：GitHub Actions 自動打包**
1. 將專案推送至 GitHub
2. 進入 Actions 頁面，觸發「Build Debug APK」
3. 建置完成後，在 Artifacts 下載 `app-debug.apk`
4. 或每次 push 至 main 分支會自動建立 Release 並附加 APK

### 後端部署至 IGA Pages

```bash
cd ai-world-backend
npm install

# 登入 Volcengine
iga login

# 部署
iga pages deploy --name ai-world-backend
```

設定環境變數（在 IGA Pages 控制台）：
- `OPENAI_API_KEY`
- `DEEPSEEK_API_KEY`
- `GEMINI_API_KEY`
- `LONGCAT_API_KEY`
- `AUTH_TOKEN`（選填）

## 技術架構

### Android
- **UI**：Jetpack Compose + Material 3
- **架構**：MVVM + Repository
- **依賴注入**：Hilt
- **資料庫**：Room
- **網路**：Retrofit + OkHttp
- **本地 LLM**：llama.cpp (GGUF)

### 後端
- **框架**：Next.js 14 (API Routes)
- **部署**：IGA Pages
- **功能**：雲端模型代理、統一聊天端點

## 待完成項目

- [ ] llama.cpp Android NDK 綁定（本地模型）
- [ ] 完整世界生成邏輯（串接 AI）
- [ ] NPC AI 人格系統
- [ ] 戰鬥系統細節
- [ ] 雲端存檔同步
- [ ] 模型下載管理

## 授權

本專案為開源專案，完全免費使用。
