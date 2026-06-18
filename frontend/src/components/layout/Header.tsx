'use client'

import { useState } from 'react'
import NotificationBell from '@/features/notifications/components/NotificationBell'

export default function Header() {
  const [search, setSearch] = useState('')

  return (
    <header
      className="fixed top-0 left-0 right-0 z-40 flex h-[var(--header-height)] items-center gap-4 border-b border-[var(--border)] bg-[var(--card)] px-4"
    >
      {/* Logo */}
      <div className="flex h-8 w-8 items-center justify-center shrink-0 select-none">
        <img src="/icons/kakebo_icon.svg" alt="Kakebo" className="h-8 w-8 object-contain" />
      </div>

      {/* Search */}
      <div className="relative flex-1 max-w-md">
        <svg
          className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-[var(--muted)]"
          fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2"
        >
          <circle cx="11" cy="11" r="8" /><path d="M21 21l-4.35-4.35" />
        </svg>
        <input
          type="text"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Buscar..."
          className="w-full rounded-lg border border-[var(--border)] bg-[var(--surface)] py-2 pl-9 pr-3 text-sm outline-none
            placeholder:text-[var(--muted)] text-[var(--text)] focus:border-[var(--primary)] focus:bg-[var(--card)] transition-colors"
        />
      </div>

      <div className="ml-auto flex items-center gap-2">
        {/* Bell */}
        <NotificationBell />

        {/* Avatar */}
        <div className="h-8 w-8 rounded-full bg-[var(--primary)] flex items-center justify-center text-white text-xs font-bold select-none">
          U
        </div>
      </div>
    </header>
  )
}
