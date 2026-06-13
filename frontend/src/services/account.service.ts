import api from '@/lib/api'
import type { Account, CreateAccountRequest, UpdateAccountRequest } from '@/models/models'

export const accountService = {
  async findAll(): Promise<Account[]> {
    const { data } = await api.get<Account[]>('/account/list')
    return data
  },

  async findById(id: string): Promise<Account> {
    const { data } = await api.get<Account>(`/account/${id}`)
    return data
  },

  async create(payload: CreateAccountRequest): Promise<Account> {
    const { data } = await api.post<Account>('/account', payload)
    return data
  },

  async update(payload: UpdateAccountRequest): Promise<void> {
    await api.patch('/account', payload)
  },

  async remove(id: string): Promise<void> {
    await api.delete(`/account/${id}`)
  },
}
