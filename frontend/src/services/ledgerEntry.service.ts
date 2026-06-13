import api from '@/lib/api'
import type { LedgerEntry, CreateLedgerEntryRequest, UpdateLedgerEntryRequest } from '@/models/models'

export const ledgerEntryService = {
  async findAll(): Promise<LedgerEntry[]> {
    const { data } = await api.get<LedgerEntry[]>('/ledger-entry/list')
    return data
  },

  async findByJournal(journalId: string): Promise<LedgerEntry[]> {
    const { data } = await api.get<LedgerEntry[]>(`/ledger-entry/list/${journalId}`)
    return data
  },

  async findById(id: string): Promise<LedgerEntry> {
    const { data } = await api.get<LedgerEntry>(`/ledger-entry/${id}`)
    return data
  },

  async create(payload: CreateLedgerEntryRequest): Promise<LedgerEntry> {
    const { data } = await api.post<LedgerEntry>('/ledger-entry', payload)
    return data
  },

  async update(payload: UpdateLedgerEntryRequest): Promise<void> {
    await api.patch('/ledger-entry', payload)
  },

  async remove(id: string): Promise<void> {
    await api.delete(`/ledger-entry/${id}`)
  },
}
