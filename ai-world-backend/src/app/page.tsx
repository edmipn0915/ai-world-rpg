export default function Home() {
  return (
    <main style={{ padding: "2rem", fontFamily: "system-ui, sans-serif" }}>
      <h1>幻境 AI World - 後端服務</h1>
      <p>雲端模型代理 API</p>
      <h2>可用端點</h2>
      <ul>
        <li><code>POST /api/chat</code> - 統一聊天端點</li>
      </ul>
    </main>
  );
}
