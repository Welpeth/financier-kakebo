'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { ArrowUpRight, Pencil, Trash2 } from 'lucide-react'
import Table from '@/components/ui/Table'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import EmptyState from '@/components/ui/EmptyState'
import { useConfirm, ConfirmDialog } from '@/components/ui/ConfirmDialog'
import LedgerEntryForm from './LedgerEntryForm'
import { useLedgerEntries } from '../hooks/useLedgerEntries'
import type { Journal, Transaction, LedgerEntry, CreateLedgerEntryRequest, UpdateLedgerEntryRequest } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

interface LedgerEntryListProps {
  journal: Journal
  transactions: Transaction[]
  onTotalChange?: () => void
}

export default function LedgerEntryList({ journal, transactions, onTotalChange }: LedgerEntryListProps) {
  const { entries, loading, create, update, remove } = useLedgerEntries(journal.id)
  const router = useRouter()
  const { confirm, confirmState, closeConfirm } = useConfirm()
  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<LedgerEntry | null>(null)

  const handleSubmit = async (data: CreateLedgerEntryRequest | UpdateLedgerEntryRequest) => {
    if ('journal' in data) await create(data as CreateLedgerEntryRequest)
    else await update(data as UpdateLedgerEntryRequest)
    onTotalChange?.()
  }

  const handleRemove = async (id: string) => {
    await remove(id)
    onTotalChange?.()
  }

  const openEdit = (entry: LedgerEntry) => { setEditing(entry); setFormOpen(true) }
  const openCreate = () => { setEditing(null); setFormOpen(true) }
  const closeForm = () => { setEditing(null); setFormOpen(false) }

  return (
    <>
      <div className="mb-4 flex items-center justify-between">
        <h3 className="text-sm font-semibold text-[var(--muted)] uppercase tracking-wide">Lançamentos</h3>
        <Button size="sm" onClick={openCreate}>+ Novo Lançamento</Button>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><Spinner size="lg" /></div>
      ) : entries.length === 0 ? (
        <EmptyState
          message="Nenhum lançamento neste diário"
          description="Registre um lançamento vinculando uma transação existente."
          action={<Button size="sm" onClick={openCreate}>+ Novo Lançamento</Button>}
        />
      ) : (
        <Table>
          <Table.Head>
            <Table.Th>Nome</Table.Th>
            <Table.Th>Transação</Table.Th>
            <Table.Th>Valor</Table.Th>
            <Table.Th>Data</Table.Th>
            <Table.Th className="text-right">Ações</Table.Th>
          </Table.Head>
          <Table.Body>
            {entries.map((entry) => (
              <Table.Tr key={entry.id}>
                <Table.Td className="font-medium">{entry.name}</Table.Td>
                <Table.Td className="text-[var(--muted)]">{entry.transaction?.description ?? '—'}</Table.Td>
                <Table.Td className="font-semibold text-[var(--primary)]">
                  {entry.transaction?.amount != null ? fmt.format(entry.transaction.amount) : '—'}
                </Table.Td>
                <Table.Td className="text-[var(--muted)]">
                  {entry.finalDate ? new Date(entry.finalDate).toLocaleDateString('pt-BR') : '—'}
                </Table.Td>
                <Table.Td className="text-right">
                  <div className="flex justify-end gap-2">
                    {entry.transaction?.id && (
                      <Button size="sm" variant="ghost" onClick={() => router.push(`/transactions/${entry.transaction.id}`)}>
                        <ArrowUpRight className="h-3.5 w-3.5 mr-1" />Ver Transação
                      </Button>
                    )}
                    <Button size="sm" variant="ghost" onClick={() => openEdit(entry)}><Pencil className="h-3.5 w-3.5 mr-1" />Editar</Button>
                    <Button size="sm" variant="danger" onClick={() => confirm({ message: `Excluir o lançamento "${entry.name}"? Esta ação não pode ser desfeita.`, onConfirm: () => handleRemove(entry.id) })}><Trash2 className="h-3.5 w-3.5 mr-1" />Excluir</Button>
                  </div>
                </Table.Td>
              </Table.Tr>
            ))}
          </Table.Body>
        </Table>
      )}

      <LedgerEntryForm
        open={formOpen}
        onClose={closeForm}
        onSubmit={handleSubmit}
        initial={editing}
        journal={journal}
        transactions={transactions}
      />
      <ConfirmDialog state={confirmState} onClose={closeConfirm} />
    </>
  )
}
