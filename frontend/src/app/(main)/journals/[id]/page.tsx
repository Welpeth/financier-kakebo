'use client'

import { useEffect, useState, useCallback } from 'react'
import { useParams, useRouter } from 'next/navigation'
import { journalService } from '@/services/journal.service'
import { transactionService } from '@/services/transaction.service'
import PageHeader from '@/components/layout/PageHeader'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import LedgerEntryList from '@/features/journals/components/LedgerEntryList'
import type { Journal, Transaction } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

export default function JournalDetailPage() {
  const { id } = useParams<{ id: string }>()
  const router = useRouter()
  const [journal, setJournal] = useState<Journal | null>(null)
  const [transactions, setTransactions] = useState<Transaction[]>([])
  const [loadingJournal, setLoadingJournal] = useState(true)

  const loadJournal = useCallback(async () => {
    try {
      const j = await journalService.findById(id)
      setJournal(j)
    } catch {
      setJournal(null)
    } finally {
      setLoadingJournal(false)
    }
  }, [id])

  useEffect(() => {
    loadJournal()
    transactionService.findAll().then(setTransactions).catch(() => {})
  }, [loadJournal])

  if (loadingJournal) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner size="lg" />
      </div>
    )
  }

  if (!journal) {
    return (
      <div className="flex flex-col items-center gap-4 py-16 text-[var(--muted)]">
        <p>Diário não encontrado.</p>
        <Button variant="ghost" size="sm" onClick={() => router.push('/journals')}>← Voltar</Button>
      </div>
    )
  }

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => router.push('/journals')}>← Diários</Button>
        <PageHeader title={journal.name} />
      </div>

      {/* Total value highlight */}
      <div className="rounded-2xl bg-[var(--primary)] text-white px-8 py-6 flex flex-col gap-1 shadow-md w-fit min-w-[220px]">
        <span className="text-sm font-medium opacity-80 uppercase tracking-wide">Valor Total</span>
        <span className="text-4xl font-bold tracking-tight">{fmt.format(journal.totalValue)}</span>
        <span className="text-xs opacity-60 mt-1">Soma dos lançamentos do diário</span>
      </div>

      {/* Ledger entries */}
      <div className="bg-[var(--card)] rounded-2xl border border-[var(--border)] p-6 shadow-sm">
        <LedgerEntryList
          journal={journal}
          transactions={transactions}
          onTotalChange={loadJournal}
        />
      </div>
    </div>
  )
}
