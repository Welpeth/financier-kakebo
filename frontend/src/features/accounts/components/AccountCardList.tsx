'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import Table from '@/components/ui/Table'
import Badge from '@/components/ui/Badge'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import EmptyState from '@/components/ui/EmptyState'
import { useConfirm, ConfirmDialog } from '@/components/ui/ConfirmDialog'
import AccountCardForm from './AccountCardForm'
import { useAccountCards } from '../hooks/useAccountCards'
import { accountCardService } from '@/services/accountCard.service'
import type { Account, AccountCard, AvailableLimitResponse, CreateAccountCardRequest, UpdateAccountCardRequest } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const cardTypeLabel: Record<string, string> = { CREDIT: 'Crédito', DEBIT: 'Débito' }

interface AccountCardListProps {
  account: Account
}

export default function AccountCardList({ account }: AccountCardListProps) {
  const { cards, loading, create, update, remove } = useAccountCards(account.id)
  const { confirm, confirmState, closeConfirm } = useConfirm()
  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<AccountCard | null>(null)
  const [limits, setLimits] = useState<Record<string, AvailableLimitResponse>>({})
  const router = useRouter()

  useEffect(() => {
    if (cards.length === 0) return
    const creditCards = cards.filter((c) => c.type === 'CREDIT')
    Promise.all(
      creditCards.map((c) =>
        accountCardService.getAvailableLimit(c.id)
          .then((limit) => ({ id: c.id, limit }))
          .catch(() => null)
      )
    ).then((results) => {
      const map: Record<string, AvailableLimitResponse> = {}
      results.forEach((r) => { if (r) map[r.id] = r.limit })
      setLimits(map)
    })
  }, [cards])

  const handleSubmit = async (data: Omit<CreateAccountCardRequest, 'account'> | UpdateAccountCardRequest) => {
    if ('id' in data) await update(data as UpdateAccountCardRequest)
    else await create(data as Omit<CreateAccountCardRequest, 'account'>)
  }

  const openEdit = (card: AccountCard) => { setEditing(card); setFormOpen(true) }
  const openCreate = () => { setEditing(null); setFormOpen(true) }
  const closeForm = () => { setEditing(null); setFormOpen(false) }

  return (
    <>
      <div className="mb-4 flex items-center justify-between">
        <h2 className="text-base font-semibold text-[var(--text)]">Cartões</h2>
        <Button size="sm" onClick={openCreate}>+ Novo Cartão</Button>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><Spinner size="lg" /></div>
      ) : cards.length === 0 ? (
        <EmptyState
          message="Nenhum cartão cadastrado"
          description="Adicione cartões de crédito ou débito a esta conta."
          action={<Button size="sm" onClick={openCreate}>+ Novo Cartão</Button>}
        />
      ) : (
        <Table>
          <Table.Head>
            <Table.Th>Nome</Table.Th>
            <Table.Th>Tipo</Table.Th>
            <Table.Th>Limite Total</Table.Th>
            <Table.Th>Disponível</Table.Th>
            <Table.Th>Validade</Table.Th>
            <Table.Th>Status</Table.Th>
            <Table.Th className="text-right">Ações</Table.Th>
          </Table.Head>
          <Table.Body>
            {cards.map((card) => {
              const limit = limits[card.id]
              const isLimitReached = limit && limit.availableLimit <= 0
              return (
                <Table.Tr key={card.id}>
                  <Table.Td className="font-medium">{card.name}</Table.Td>
                  <Table.Td>
                    <Badge variant={card.type === 'CREDIT' ? 'warning' : 'success'}>
                      {cardTypeLabel[card.type] ?? card.type}
                    </Badge>
                  </Table.Td>
                  <Table.Td>{fmt.format(card.creditLimit)}</Table.Td>
                  <Table.Td>
                    {card.type === 'CREDIT' && limit ? (
                      <span className={`text-sm font-medium ${isLimitReached ? 'text-red-500' : 'text-[var(--primary)]'}`}>
                        {fmt.format(limit.availableLimit)}
                        {isLimitReached && <span className="ml-1 text-xs">(Limite atingido)</span>}
                      </span>
                    ) : (
                      <span className="text-[var(--muted)]">—</span>
                    )}
                  </Table.Td>
                  <Table.Td className="text-[var(--muted)]">
                    {String(card.expirationMonth).padStart(2, '0')}/{card.expirationYear}
                  </Table.Td>
                  <Table.Td>
                    <Badge variant={card.isActive ? 'success' : 'neutral'}>
                      {card.isActive ? 'Ativo' : 'Inativo'}
                    </Badge>
                  </Table.Td>
                  <Table.Td className="text-right">
                    <div className="flex justify-end gap-2">
                      {card.type === 'CREDIT' && (
                        <Button size="sm" variant="ghost" onClick={() => router.push(`/accounts/${account.id}/cards/${card.id}`)}>
                          Ver Faturas
                        </Button>
                      )}
                      <Button size="sm" variant="ghost" onClick={() => openEdit(card)}>Editar</Button>
                      <Button size="sm" variant="danger" onClick={() => confirm({ message: `Excluir o cartão "${card.name}"? Esta ação não pode ser desfeita.`, onConfirm: () => remove(card.id) })}>Excluir</Button>
                    </div>
                  </Table.Td>
                </Table.Tr>
              )
            })}
          </Table.Body>
        </Table>
      )}

      <AccountCardForm
        open={formOpen}
        onClose={closeForm}
        onSubmit={handleSubmit}
        initial={editing}
      />
      <ConfirmDialog state={confirmState} onClose={closeConfirm} />
    </>
  )
}
