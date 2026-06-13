'use client'

import { useState, useEffect, useCallback } from 'react'
import { accountService } from '@/services/account.service'
import type { Account, CreateAccountRequest, UpdateAccountRequest } from '@/models/models'

export function useAccounts() {
  const [accounts, setAccounts] = useState<Account[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetch = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await accountService.findAll()
      setAccounts(data)
    } catch {
      setError('Erro ao carregar contas.')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetch() }, [fetch])

  const create = useCallback(async (payload: CreateAccountRequest) => {
    await accountService.create(payload)
    await fetch()
  }, [fetch])

  const update = useCallback(async (payload: UpdateAccountRequest) => {
    await accountService.update(payload)
    await fetch()
  }, [fetch])

  const remove = useCallback(async (id: string) => {
    await accountService.remove(id)
    await fetch()
  }, [fetch])

  return { accounts, loading, error, create, update, remove }
}
