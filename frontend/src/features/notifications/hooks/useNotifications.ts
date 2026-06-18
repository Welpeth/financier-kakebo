'use client'

import { useState, useEffect, useCallback } from 'react'
import { notificationService } from '@/services/notification.service'
import type { Notification } from '@/models/models'

export function useNotifications() {
  const [notifications, setNotifications] = useState<Notification[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetch = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await notificationService.findAll()
      setNotifications(data)
    } catch {
      setError('Erro ao carregar notificações.')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetch() }, [fetch])

  const markRead = useCallback(async (id: string) => {
    await notificationService.markRead(id)
    setNotifications((prev) =>
      prev.map((n) => (n.id === id ? { ...n, read: true } : n)))
  }, [])

  const markAllRead = useCallback(async () => {
    await notificationService.markAllRead()
    setNotifications((prev) => prev.map((n) => ({ ...n, read: true })))
  }, [])

  const unreadCount = notifications.filter((n) => !n.read).length

  return { notifications, unreadCount, loading, error, markRead, markAllRead, refetch: fetch }
}
