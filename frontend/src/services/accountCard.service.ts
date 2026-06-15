import api from '@/lib/api'
import type { AccountCard, AvailableLimitResponse, CreateAccountCardRequest, UpdateAccountCardRequest } from '@/models/models'

export const accountCardService = {
  async findAll(): Promise<AccountCard[]> {
    const { data } = await api.get<AccountCard[]>('/account-card/list')
    return data
  },

  async findByAccount(accountId: string): Promise<AccountCard[]> {
    const { data } = await api.get<AccountCard[]>(`/account-card/list/${accountId}`)
    return data
  },

  async findById(id: string): Promise<AccountCard> {
    const { data } = await api.get<AccountCard>(`/account-card/${id}`)
    return data
  },

  async create(payload: CreateAccountCardRequest): Promise<AccountCard> {
    const { data } = await api.post<AccountCard>('/account-card', payload)
    return data
  },

  async update(payload: UpdateAccountCardRequest): Promise<void> {
    await api.patch('/account-card', payload)
  },

  async getAvailableLimit(id: string): Promise<AvailableLimitResponse> {
    const { data } = await api.get<AvailableLimitResponse>(`/account-card/${id}/available-limit`)
    return data
  },

  async remove(id: string): Promise<void> {
    await api.delete(`/account-card/${id}`)
  },
}
