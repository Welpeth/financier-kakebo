'use client'

import { useState, useEffect } from 'react'
import { installmentPurchaseService } from '@/services/installmentPurchase.service'
import { installmentService } from '@/services/installment.service'
import type { Installment, InstallmentPurchase } from '@/models/models'

export function useInstallments(transactionId: string | null) {
  const [purchase, setPurchase] = useState<InstallmentPurchase | null>(null)
  const [installments, setInstallments] = useState<Installment[]>([])
  const [loading, setLoading] = useState(false)

  const load = async (txId: string) => {
    setLoading(true)
    try {
      const purchases = await installmentPurchaseService.findByTransaction(txId)
      if (!purchases[0]) return
      setPurchase(purchases[0])
      const items = await installmentService.findByPurchase(purchases[0].id)
      setInstallments(items)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    if (!transactionId) { setPurchase(null); setInstallments([]); return }
    load(transactionId)
  }, [transactionId])

  const pay = async (installmentId: string) => {
    await installmentService.pay(installmentId)
    if (purchase) {
      const items = await installmentService.findByPurchase(purchase.id)
      setInstallments(items)
    }
  }

  return { purchase, installments, loading, pay }
}
