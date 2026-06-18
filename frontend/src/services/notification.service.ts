import api from '@/lib/api'
import type { Notification } from '@/models/models'

export const notificationService = {
  async findAll(): Promise<Notification[]> {
    const { data } = await api.get<Notification[]>('/notification/list')
    return data
  },

  async markRead(id: string): Promise<void> {
    await api.patch(`/notification/${id}/read`)
  },

  async markAllRead(): Promise<void> {
    await api.patch('/notification/read-all')
  },
}
