'use client'

import { useEffect, useState } from 'react'
import { useParams, useRouter } from 'next/navigation'
import { transactionService } from '@/services/transaction.service'
import PageHeader from '@/components/layout/PageHeader'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import InstallmentList from '@/features/transactions/components/InstallmentList'
import type { Transaction } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

const typeLabel: Record<string, string> = {
  CASH: 'Dinheiro', DEBIT: 'Débito', CREDIT: 'Crédito', PIX: 'PIX',
}

export default function TransactionDetailPage() {
  const { id } = useParams<{ id: string }>()
  const router = useRouter()
  const [transaction, setTransaction] = useState<Transaction | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    transactionService.findById(id)
      .then(setTransaction)
      .catch(() => setTransaction(null))
      .finally(() => setLoading(false))
  }, [id])

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner size="lg" />
      </div>
    )
  }

  if (!transaction) {
    return (
      <div className="flex flex-col items-center gap-4 py-16 text-[var(--muted)]">
        <p>Transação não encontrada.</p>
        <Button variant="ghost" size="sm" onClick={() => router.push('/transactions')}>← Voltar</Button>
      </div>
    )
  }

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => router.push('/transactions')}>← Transações</Button>
        <PageHeader title={transaction.description} />
      </div>

      <div className="rounded-2xl bg-[var(--primary)] text-white px-8 py-6 flex items-center gap-8 shadow-md">
        <div className="flex flex-col gap-1">
          <span className="text-sm font-medium opacity-80 uppercase tracking-wide">Valor</span>
          <span className="text-4xl font-bold tracking-tight">{fmt.format(transaction.amount)}</span>
          <span className="text-xs opacity-60 mt-1">{typeLabel[transaction.type]} · {transaction.installment}x</span>
        </div>
      </div>

      <div className="bg-[var(--card)] rounded-2xl border border-[var(--border)] p-6 shadow-sm">
        <InstallmentList transaction={transaction} />
      </div>
    </div>
  )
}
