'use client'

import { useEffect, useState } from 'react'
import { useParams, useRouter } from 'next/navigation'
import { accountCardService } from '@/services/accountCard.service'
import PageHeader from '@/components/layout/PageHeader'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import CardInstallmentList from '@/features/accounts/components/CardInstallmentList'
import type { AccountCard, AvailableLimitResponse } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

export default function CardDetailPage() {
  const { id: accountId, cardId } = useParams<{ id: string; cardId: string }>()
  const router = useRouter()
  const [card, setCard] = useState<AccountCard | null>(null)
  const [limit, setLimit] = useState<AvailableLimitResponse | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      accountCardService.findById(cardId),
      accountCardService.getAvailableLimit(cardId),
    ])
      .then(([c, l]) => { setCard(c); setLimit(l) })
      .catch(() => setCard(null))
      .finally(() => setLoading(false))
  }, [cardId])

  if (loading) {
    return <div className="flex items-center justify-center h-64"><Spinner size="lg" /></div>
  }

  if (!card) {
    return (
      <div className="flex flex-col items-center gap-4 py-16 text-[var(--muted)]">
        <p>Cartão não encontrado.</p>
        <Button variant="ghost" size="sm" onClick={() => router.push(`/accounts/${accountId}`)}>← Voltar</Button>
      </div>
    )
  }

  const isLimitReached = limit && limit.availableLimit <= 0

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center gap-4">
        <Button variant="ghost" size="sm" onClick={() => router.push(`/accounts/${accountId}`)}>← Conta</Button>
        <PageHeader title={card.name} />
      </div>

      <div className="rounded-2xl bg-[var(--primary)] text-white px-8 py-6 flex items-center gap-12 shadow-md">
        <div className="flex flex-col gap-1">
          <span className="text-sm font-medium opacity-80 uppercase tracking-wide">Limite Total</span>
          <span className="text-3xl font-bold tracking-tight">{fmt.format(card.creditLimit)}</span>
        </div>
        {limit && (
          <>
            <div className="flex flex-col gap-1">
              <span className="text-sm font-medium opacity-80 uppercase tracking-wide">Utilizado</span>
              <span className="text-2xl font-semibold">{fmt.format(limit.usedAmount)}</span>
            </div>
            <div className="flex flex-col gap-1">
              <span className="text-sm font-medium opacity-80 uppercase tracking-wide">Disponível</span>
              <span className={`text-2xl font-semibold ${isLimitReached ? 'text-red-300' : ''}`}>
                {fmt.format(limit.availableLimit)}
              </span>
              {isLimitReached && (
                <span className="text-xs text-red-300 font-medium">Limite atingido</span>
              )}
            </div>
          </>
        )}
      </div>

      <div className="bg-[var(--card)] rounded-2xl border border-[var(--border)] p-6 shadow-sm">
        <h2 className="text-base font-semibold text-[var(--text)] mb-4">Parcelas do Cartão</h2>
        <CardInstallmentList cardId={cardId} />
      </div>
    </div>
  )
}
