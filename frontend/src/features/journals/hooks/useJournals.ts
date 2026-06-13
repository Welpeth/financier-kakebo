'use client'

import { useState, useEffect, useCallback } from 'react'
import { journalService } from '@/services/journal.service'
import type { Journal, CreateJournalRequest, UpdateJournalRequest } from '@/models/models'

export function useJournals() {
  const [journals, setJournals] = useState<Journal[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetch = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await journalService.findAll()
      setJournals(data)
    } catch {
      setError('Erro ao carregar diários.')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetch() }, [fetch])

  const create = useCallback(async (payload: CreateJournalRequest) => {
    await journalService.create(payload)
    await fetch()
  }, [fetch])

  const update = useCallback(async (payload: UpdateJournalRequest) => {
    await journalService.update(payload)
    await fetch()
  }, [fetch])

  const remove = useCallback(async (id: string) => {
    await journalService.remove(id)
    await fetch()
  }, [fetch])

  return { journals, loading, error, create, update, remove }
}
