'use client'

import { useState } from 'react'
import Modal from '@/components/ui/Modal'
import Table from '@/components/ui/Table'
import Badge from '@/components/ui/Badge'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import EmptyState from '@/components/ui/EmptyState'
import AccountCardForm from './AccountCardForm'
import { useAccountCards } from '../hooks/useAccountCards'
import type { Account, AccountCard, CreateAccountCardRequest, UpdateAccountCardRequest } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })
const cardTypeLabel: Record<string, string> = { CREDIT: 'Crédito', DEBIT: 'Débito' }

interface AccountCardsModalProps {
  open: boolean
  onClose: () => void
  account: Account
}

export default function AccountCardsModal({ open, onClose, account }: AccountCardsModalProps) {
  const { cards, loading, create, update, remove } = useAccountCards(account.id)
  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<AccountCard | null>(null)

  const handleSubmit = async (data: Omit<CreateAccountCardRequest, 'account'> | UpdateAccountCardRequest) => {
    if ('id' in data) await update(data as UpdateAccountCardRequest)
    else await create(data as Omit<CreateAccountCardRequest, 'account'>)
  }

  const openEdit = (card: AccountCard) => { setEditing(card); setFormOpen(true) }
  const openCreate = () => { setEditing(null); setFormOpen(true) }
  const closeCardForm = () => { setEditing(null); setFormOpen(false) }

  return (
    <>
      <Modal
        open={open}
        onClose={onClose}
        title={`Cartões — ${account.name}`}
        footer={
          <Button size="sm" onClick={openCreate}>+ Novo Cartão</Button>
        }
      >
        {loading ? (
          <div className="flex justify-center py-8"><Spinner size="lg" /></div>
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
              <Table.Th>Limite</Table.Th>
              <Table.Th>Validade</Table.Th>
              <Table.Th>Status</Table.Th>
              <Table.Th className="text-right">Ações</Table.Th>
            </Table.Head>
            <Table.Body>
              {cards.map((card) => (
                <Table.Tr key={card.id}>
                  <Table.Td className="font-medium">{card.name}</Table.Td>
                  <Table.Td>
                    <Badge variant={card.type === 'CREDIT' ? 'warning' : 'success'}>
                      {cardTypeLabel[card.type] ?? card.type}
                    </Badge>
                  </Table.Td>
                  <Table.Td>{fmt.format(card.creditLimit)}</Table.Td>
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
                      <Button size="sm" variant="ghost" onClick={() => openEdit(card)}>Editar</Button>
                      <Button size="sm" variant="danger" onClick={() => remove(card.id)}>Excluir</Button>
                    </div>
                  </Table.Td>
                </Table.Tr>
              ))}
            </Table.Body>
          </Table>
        )}
      </Modal>

      <AccountCardForm
        open={formOpen}
        onClose={closeCardForm}
        onSubmit={handleSubmit}
        initial={editing}
      />
    </>
  )
}
