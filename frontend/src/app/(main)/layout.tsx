import Header from '@/components/layout/Header'
import Sidebar from '@/components/layout/Sidebar'

export default function MainLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-[var(--surface)]">
      <Header />
      <Sidebar />
      <main
        className="overflow-y-auto pt-[var(--header-height)] pl-[var(--sidebar-width)]"
        style={{ minHeight: '100vh' }}
      >
        <div className="p-8">{children}</div>
      </main>
    </div>
  )
}
