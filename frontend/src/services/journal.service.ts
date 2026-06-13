import api from '@/lib/api'
import type { Journal, CreateJournalRequest, UpdateJournalRequest, LedgerEntry } from '@/models/models'

export const journalService = {
  async findAll(): Promise<Journal[]> {
    const { data } = await api.get<Journal[]>('/journal/list')
    return data
  },

  async findById(id: string): Promise<Journal> {
    const { data } = await api.get<Journal>(`/journal/${id}`)
    return data
  },

  async create(payload: CreateJournalRequest): Promise<Journal> {
    const { data } = await api.post<Journal>('/journal', payload)
    return data
  },

  async update(payload: UpdateJournalRequest): Promise<void> {
    await api.patch('/journal', payload)
  },

  async remove(id: string): Promise<void> {
    await api.delete(`/journal/${id}`)
  },

  async findLedgerEntries(journalId: string): Promise<LedgerEntry[]> {
    const { data } = await api.get<LedgerEntry[]>(`/ledger-entry/list/${journalId}`)
    return data
  },
}
