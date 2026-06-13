'use client'

import { useState, useEffect, useCallback } from 'react'
import { transactionService } from '@/services/transaction.service'
import type { Transaction, CreateTransactionRequest, UpdateTransactionRequest } from '@/models/models'

export function useTransactions() {
  const [transactions, setTransactions] = useState<Transaction[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetch = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await transactionService.findAll()
      setTransactions(data)
    } catch {
      setError('Erro ao carregar transações.')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetch() }, [fetch])

  const create = useCallback(async (payload: CreateTransactionRequest) => {
    await transactionService.create(payload)
    await fetch()
  }, [fetch])

  const update = useCallback(async (payload: UpdateTransactionRequest) => {
    await transactionService.update(payload)
    await fetch()
  }, [fetch])

  const remove = useCallback(async (id: string) => {
    await transactionService.remove(id)
    await fetch()
  }, [fetch])

  return { transactions, loading, error, create, update, remove }
}
