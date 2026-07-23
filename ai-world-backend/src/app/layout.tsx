export const metadata = {
  title: "AI World Backend",
  description: "Cloud model proxy for AI World RPG",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="zh-TW">
      <body>{children}</body>
    </html>
  );
}
