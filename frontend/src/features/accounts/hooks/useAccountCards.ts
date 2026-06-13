'use client'

import { useState, useEffect, useCallback } from 'react'
import { accountCardService } from '@/services/accountCard.service'
import type { AccountCard, CreateAccountCardRequest, UpdateAccountCardRequest } from '@/models/models'

export function useAccountCards(accountId: string) {
  const [cards, setCards] = useState<AccountCard[]>([])
  const [loading, setLoading] = useState(true)

  const load = useCallback(async () => {
    setLoading(true)
    try {
      const data = await accountCardService.findByAccount(accountId)
      setCards(data)
    } catch {
      setCards([])
    } finally {
      setLoading(false)
    }
  }, [accountId])

  useEffect(() => {
    if (accountId) load()
  }, [accountId, load])

  const create = async (payload: Omit<CreateAccountCardRequest, 'account'>) => {
    await accountCardService.create({ ...payload, account: { id: accountId } as any })
    await load()
  }

  const update = async (payload: UpdateAccountCardRequest) => {
    await accountCardService.update(payload)
    await load()
  }

  const remove = async (id: string) => {
    await accountCardService.remove(id)
    await load()
  }

  return { cards, loading, create, update, remove, reload: load }
}
