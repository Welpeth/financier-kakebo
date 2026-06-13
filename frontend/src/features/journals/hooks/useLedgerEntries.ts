'use client'

import { useState, useEffect, useCallback } from 'react'
import { ledgerEntryService } from '@/services/ledgerEntry.service'
import type { LedgerEntry, CreateLedgerEntryRequest, UpdateLedgerEntryRequest } from '@/models/models'

export function useLedgerEntries(journalId: string) {
  const [entries, setEntries] = useState<LedgerEntry[]>([])
  const [loading, setLoading] = useState(true)

  const load = useCallback(async () => {
    setLoading(true)
    try {
      const data = await ledgerEntryService.findByJournal(journalId)
      setEntries(data)
    } catch {
      setEntries([])
    } finally {
      setLoading(false)
    }
  }, [journalId])

  useEffect(() => {
    if (journalId) load()
  }, [journalId, load])

  const create = async (payload: CreateLedgerEntryRequest) => {
    await ledgerEntryService.create(payload)
    await load()
  }

  const update = async (payload: UpdateLedgerEntryRequest) => {
    await ledgerEntryService.update(payload)
    await load()
  }

  const remove = async (id: string) => {
    await ledgerEntryService.remove(id)
    await load()
  }

  return { entries, loading, create, update, remove, reload: load }
}
