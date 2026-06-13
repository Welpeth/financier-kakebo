import api from '@/lib/api'
import type { Transaction, CreateTransactionRequest, UpdateTransactionRequest } from '@/models/models'

export const transactionService = {
  async findAll(): Promise<Transaction[]> {
    const { data } = await api.get<Transaction[]>('/transaction/list')
    return data
  },

  async findById(id: string): Promise<Transaction> {
    const { data } = await api.get<Transaction>(`/transaction/${id}`)
    return data
  },

  async create(payload: CreateTransactionRequest): Promise<Transaction> {
    const { data } = await api.post<Transaction>('/transaction', payload)
    return data
  },

  async update(payload: UpdateTransactionRequest): Promise<void> {
    await api.patch('/transaction', payload)
  },

  async remove(id: string): Promise<void> {
    await api.delete(`/transaction/${id}`)
  },
}
