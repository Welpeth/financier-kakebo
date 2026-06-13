export default function AuthLayout({ children }: { children: React.ReactNode }) {
  return (
    <main className="flex min-h-screen items-center justify-center bg-[var(--surface)]">
      <div className="w-full max-w-sm rounded-2xl bg-[var(--card)] p-8 shadow-sm border border-[var(--border)]">
        <div className="mb-8 text-center">
          <div className="mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-xl bg-[var(--primary)] text-white text-xl font-bold">
            K
          </div>
          <h1 className="text-xl font-bold text-[var(--text)]">Kakebo</h1>
          <p className="mt-1 text-sm text-[var(--muted)]">Controle financeiro pessoal</p>
        </div>
        {children}
      </div>
    </main>
  )
}
