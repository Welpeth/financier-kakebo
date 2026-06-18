'use client'

import { useState, useEffect } from 'react'
import { Bell } from 'lucide-react'
import { useNotifications } from '../hooks/useNotifications'
import NotificationModal from './NotificationModal'

const SESSION_FLAG = 'kakebo:notif-seen'

export default function NotificationBell() {
  const { notifications, unreadCount, loading, markRead, markAllRead } = useNotifications()
  const [open, setOpen] = useState(false)

  // Auto-open once per login/session when there are notifications.
  useEffect(() => {
    if (loading) return
    if (notifications.length === 0) return
    if (sessionStorage.getItem(SESSION_FLAG)) return
    setOpen(true)
    sessionStorage.setItem(SESSION_FLAG, '1')
  }, [loading, notifications.length])

  return (
    <>
      <button
        onClick={() => setOpen(true)}
        className="relative flex h-8 w-8 items-center justify-center rounded-lg text-[var(--muted)] hover:bg-[var(--surface)] transition-colors"
        title="Notificações"
      >
        <Bell className="h-4 w-4" />
        {unreadCount > 0 && (
          <span className="absolute top-1 right-1 h-2 w-2 rounded-full bg-[var(--danger)] ring-2 ring-[var(--card)]" />
        )}
      </button>

      <NotificationModal
        open={open}
        onClose={() => setOpen(false)}
        notifications={notifications}
        loading={loading}
        onMarkRead={markRead}
        onMarkAllRead={markAllRead}
      />
    </>
  )
}
