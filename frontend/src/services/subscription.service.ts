import api from '@/lib/api'
import type { Subscription, UpdateSubscriptionRequest } from '@/models/models'

export const subscriptionService = {
  async findAll(): Promise<Subscription[]> {
    const { data } = await api.get<Subscription[]>('/subscription/list')
    return data
  },

  async findByTransaction(transactionId: string): Promise<Subscription[]> {
    const { data } = await api.get<Subscription[]>(`/subscription/by-transaction/${transactionId}`)
    return data
  },

  async update(payload: UpdateSubscriptionRequest): Promise<void> {
    await api.patch('/subscription', payload)
  },

  async payCurrentPeriod(id: string): Promise<Subscription> {
    const { data } = await api.patch<Subscription>(`/subscription/${id}/pay`)
    return data
  },

  async remove(id: string): Promise<void> {
    await api.delete(`/subscription/${id}`)
  },
}
