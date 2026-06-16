'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { useState, useRef, useEffect } from 'react'
import { useAuth } from '@/features/auth/hooks/useAuth'
import { useTheme } from '@/lib/theme'
import {
  LayoutDashboard, Wallet, Banknote, Tag, BookOpen,
  Settings, LogOut, Moon, Sun,
} from 'lucide-react'

const navItems = [
  { href: '/dashboard', label: 'Dashboard', icon: <LayoutDashboard className="h-5 w-5" /> },
  { href: '/accounts',  label: 'Contas',     icon: <Wallet className="h-5 w-5" /> },
  { href: '/transactions', label: 'Transações', icon: <Banknote className="h-5 w-5" /> },
  { href: '/categories', label: 'Categorias', icon: <Tag className="h-5 w-5" /> },
  { href: '/journals',  label: 'Diários',    icon: <BookOpen className="h-5 w-5" /> },
]

export default function Sidebar() {
  const pathname = usePathname()
  const { logout } = useAuth()
  const { isDark, toggle } = useTheme()
  const [settingsOpen, setSettingsOpen] = useState(false)
  const settingsRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (settingsRef.current && !settingsRef.current.contains(e.target as Node)) {
        setSettingsOpen(false)
      }
    }
    if (settingsOpen) document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [settingsOpen])

  return (
    <aside className="fixed top-[var(--header-height)] left-0 z-30 flex h-[calc(100vh-var(--header-height))] w-[var(--sidebar-width)] flex-col border-r border-[var(--border)] bg-[var(--card)] py-4">
      <nav className="flex flex-1 flex-col items-center gap-1 px-2">
        {navItems.map(({ href, label, icon }) => {
          const active = pathname === href || pathname.startsWith(href + '/')
          return (
            <Link
              key={href}
              href={href}
              title={label}
              className={`flex h-10 w-10 items-center justify-center rounded-xl transition-colors ${
                active
                  ? 'bg-[var(--primary)] text-white'
                  : 'text-[var(--muted)] hover:bg-[var(--surface-hover)] hover:text-[var(--primary)]'
              }`}
            >
              {icon}
            </Link>
          )
        })}
      </nav>

      <div className="flex flex-col items-center gap-1 px-2" ref={settingsRef}>
        {settingsOpen && (
          <div className="absolute left-[calc(var(--sidebar-width)+8px)] bottom-16 w-52 rounded-xl border border-[var(--border)] bg-[var(--card)] shadow-lg p-3 z-50">
            <p className="text-xs font-semibold uppercase tracking-wide text-[var(--muted)] mb-3">Aparência</p>
            <button
              onClick={toggle}
              className="flex w-full items-center justify-between rounded-lg px-3 py-2 text-sm text-[var(--text)] hover:bg-[var(--surface-hover)] transition-colors"
            >
              <span className="flex items-center gap-2">
                {isDark
                  ? <Moon className="h-4 w-4 text-[var(--primary)]" />
                  : <Sun className="h-4 w-4 text-[var(--warning)]" />}
                {isDark ? 'Modo Escuro' : 'Modo Claro'}
              </span>
              <div className={`relative inline-flex h-5 w-9 items-center rounded-full transition-colors ${isDark ? 'bg-[var(--primary)]' : 'bg-[var(--border)]'}`}>
                <span className={`inline-block h-3.5 w-3.5 transform rounded-full bg-white shadow transition-transform ${isDark ? 'translate-x-4' : 'translate-x-0.5'}`} />
              </div>
            </button>
          </div>
        )}

        <button
          onClick={() => setSettingsOpen((v) => !v)}
          title="Configurações"
          className={`flex h-10 w-10 items-center justify-center rounded-xl transition-colors ${
            settingsOpen
              ? 'bg-[var(--primary-light)] text-[var(--primary)]'
              : 'text-[var(--muted)] hover:bg-[var(--surface-hover)] hover:text-[var(--primary)]'
          }`}
        >
          <Settings className="h-5 w-5" />
        </button>

        <button
          onClick={logout}
          title="Sair"
          className="flex h-10 w-10 items-center justify-center rounded-xl text-[var(--muted)] hover:bg-[var(--danger-light)] hover:text-[var(--danger)] transition-colors"
        >
          <LogOut className="h-5 w-5" />
        </button>
      </div>
    </aside>
  )
}
