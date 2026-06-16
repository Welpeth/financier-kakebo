'use client'

import { useState, useEffect } from 'react'
import { useRouter } from 'next/navigation'
import Table from '@/components/ui/Table'
import Badge from '@/components/ui/Badge'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import EmptyState from '@/components/ui/EmptyState'
import { useConfirm, ConfirmDialog } from '@/components/ui/ConfirmDialog'
import TransactionForm from './TransactionForm'
import { useTransactions } from '../hooks/useTransactions'
import { useAccounts } from '@/features/accounts/hooks/useAccounts'
import { accountCardService } from '@/services/accountCard.service'
import { categoryService } from '@/services/category.service'
import { journalService } from '@/services/journal.service'
import type { Transaction, CreateTransactionRequest, UpdateTransactionRequest, AccountCard, Category, Journal } from '@/models/models'

const typeLabel: Record<string, string> = { CASH: 'Dinheiro', DEBIT: 'Débito', CREDIT: 'Crédito', PIX: 'PIX' }
const typeBadge: Record<string, 'success' | 'neutral' | 'warning' | 'danger'> = {
  CASH: 'neutral', DEBIT: 'success', CREDIT: 'warning', PIX: 'success',
}
const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

export default function TransactionList() {
  const { transactions, loading, create, update, remove } = useTransactions()
  const { accounts } = useAccounts()
  const router = useRouter()
  const { confirm, confirmState, closeConfirm } = useConfirm()
  const [cards, setCards] = useState<AccountCard[]>([])
  const [categories, setCategories] = useState<Category[]>([])
  const [journals, setJournals] = useState<Journal[]>([])
  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<Transaction | null>(null)

  useEffect(() => {
    accountCardService.findAll().then(setCards).catch(() => {})
    categoryService.findAll().then(setCategories).catch(() => {})
    journalService.findAll().then(setJournals).catch(() => {})
  }, [])

  const handleSubmit = async (data: CreateTransactionRequest | UpdateTransactionRequest) => {
    if ('id' in data) await update(data as UpdateTransactionRequest)
    else await create(data as CreateTransactionRequest)
  }

  const openEdit = (tx: Transaction) => { setEditing(tx); setFormOpen(true) }
  const openCreate = () => { setEditing(null); setFormOpen(true) }
  const closeForm = () => { setEditing(null); setFormOpen(false) }

  return (
    <>
      <div className="mb-4 flex items-center justify-end">
        <Button size="sm" onClick={openCreate}>+ Nova Transação</Button>
      </div>

      {loading ? (
        <div className="flex justify-center py-16"><Spinner size="lg" /></div>
      ) : transactions.length === 0 ? (
        <EmptyState
          message="Nenhuma transação registrada"
          description="Registre sua primeira transação financeira."
          action={<Button size="sm" onClick={openCreate}>+ Nova Transação</Button>}
        />
      ) : (
        <Table>
          <Table.Head>
            <Table.Th>Descrição</Table.Th>
            <Table.Th>Tipo</Table.Th>
            <Table.Th>Parcelas</Table.Th>
            <Table.Th>Conta</Table.Th>
            <Table.Th>Categoria</Table.Th>
            <Table.Th>Valor</Table.Th>
            <Table.Th>Juros</Table.Th>
            <Table.Th>Data</Table.Th>
            <Table.Th className="text-right">Ações</Table.Th>
          </Table.Head>
          <Table.Body>
            {transactions.map((tx) => (
              <Table.Tr key={tx.id}>
                <Table.Td className="font-medium">{tx.description}</Table.Td>
                <Table.Td>
                  <Badge variant={typeBadge[tx.type] ?? 'neutral'}>{typeLabel[tx.type] ?? tx.type}</Badge>
                </Table.Td>
                <Table.Td>
                  {tx.type === 'CREDIT' && tx.subscription && (
                    <Badge variant="neutral">Assinatura</Badge>
                  )}
                  {tx.type === 'CREDIT' && !tx.subscription && tx.installment > 1 && (
                    <span className="text-sm text-[var(--muted)]">{tx.installment}x</span>
                  )}
                </Table.Td>
                <Table.Td className="text-[var(--muted)]">{tx.account?.name ?? '—'}</Table.Td>
                <Table.Td className="text-[var(--muted)]">{tx.category?.name ?? '—'}</Table.Td>
                <Table.Td className="font-medium">{fmt.format(tx.amount)}</Table.Td>
                <Table.Td className="text-[var(--muted)]">
                  {tx.fee ? `${Number(tx.fee).toFixed(2)}%` : '—'}
                </Table.Td>
                <Table.Td className="text-[var(--muted)]">
                  {new Date(tx.createdAt).toLocaleDateString('pt-BR')}
                </Table.Td>
                <Table.Td className="text-right">
                  <div className="flex justify-end gap-2">
                    {tx.type === 'CREDIT' && !tx.subscription && (
                      <Button size="sm" variant="ghost" onClick={() => router.push(`/transactions/${tx.id}`)}>
                        Ver Parcelas
                      </Button>
                    )}
                    <Button size="sm" variant="ghost" onClick={() => openEdit(tx)}>Editar</Button>
                    <Button size="sm" variant="danger" onClick={() => confirm({ message: `Excluir a transação "${tx.description}"? Esta ação não pode ser desfeita.`, onConfirm: () => remove(tx.id) })}>Excluir</Button>
                  </div>
                </Table.Td>
              </Table.Tr>
            ))}
          </Table.Body>
        </Table>
      )}

      <TransactionForm
        open={formOpen}
        onClose={closeForm}
        onSubmit={handleSubmit}
        initial={editing}
        accounts={accounts}
        cards={cards}
        categories={categories}
        journals={journals}
      />
      <ConfirmDialog state={confirmState} onClose={closeConfirm} />
    </>
  )
}
