'use client'

import { useEffect, useState, useCallback } from 'react'
import { useParams, useRouter } from 'next/navigation'
import { accountService } from '@/services/account.service'
import PageHeader from '@/components/layout/PageHeader'
import Button from '@/components/ui/Button'
import Badge from '@/components/ui/Badge'
import Spinner from '@/components/ui/Spinner'
import AccountCardList from '@/features/accounts/components/AccountCardList'
import type { Account } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const typeLabel: Record<string, string> = { CHECKING: 'Corrente', SAVINGS: 'Poupança' }

export default function AccountDetailPage() {
  const { id } = useParams<{ id: string }>()
  const router = useRouter()
  const [account, setAccount] = useState<Account | null>(null)
  const [loading, setLoading] = useState(true)

  const load = useCallback(async () => {
    try {
      const data = await accountService.findById(id)
      setAccount(data)
    } catch {
      setAccount(null)
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => { load() }, [load])

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Spinner size="lg" />
      </div>
    )
  }

  if (!account) {
    return (
      <div className="flex flex-col items-center gap-4 py-16 text-[var(--muted)]">
        <p>Conta não encontrada.</p>
        <Button variant="ghost" size="sm" onClick={() => router.push('/accounts')}>← Voltar</Button>
      </div>
    )
  }

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => router.push('/accounts')}>← Contas</Button>
        <PageHeader title={account.name} />
      </div>

      <div className="grid grid-cols-3 gap-4">
        <div className="rounded-2xl bg-[var(--primary)] text-white px-6 py-5 flex flex-col gap-1 shadow-md">
          <span className="text-xs font-medium opacity-70 uppercase tracking-wide">Saldo</span>
          <span className="text-3xl font-bold tracking-tight">{fmt.format(account.balance)}</span>
        </div>
        <div className="rounded-2xl bg-[var(--card)] border border-[var(--border)] px-6 py-5 flex flex-col gap-1">
          <span className="text-xs font-medium text-[var(--muted)] uppercase tracking-wide">Tipo</span>
          <span className="text-xl font-semibold text-[var(--text)]">{typeLabel[account.type] ?? account.type}</span>
        </div>
        <div className="rounded-2xl bg-[var(--card)] border border-[var(--border)] px-6 py-5 flex flex-col gap-2">
          <span className="text-xs font-medium text-[var(--muted)] uppercase tracking-wide">Status</span>
          <Badge variant={account.isActive ? 'success' : 'neutral'}>
            {account.isActive ? 'Ativa' : 'Inativa'}
          </Badge>
        </div>
      </div>

      <div className="bg-[var(--card)] rounded-2xl border border-[var(--border)] p-6 shadow-sm">
        <AccountCardList account={account} />
      </div>
    </div>
  )
}
