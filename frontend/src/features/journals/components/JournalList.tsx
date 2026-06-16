'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { BookOpen, Pencil, Trash2 } from 'lucide-react'
import Table from '@/components/ui/Table'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import EmptyState from '@/components/ui/EmptyState'
import { useConfirm, ConfirmDialog } from '@/components/ui/ConfirmDialog'
import JournalForm from './JournalForm'
import { useJournals } from '../hooks/useJournals'
import type { Journal, CreateJournalRequest, UpdateJournalRequest } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

export default function JournalList() {
  const { journals, loading, create, update, remove } = useJournals()
  const router = useRouter()
  const { confirm, confirmState, closeConfirm } = useConfirm()
  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<Journal | null>(null)

  const handleSubmit = async (data: CreateJournalRequest | UpdateJournalRequest) => {
    if ('id' in data) await update(data as UpdateJournalRequest)
    else await create(data as CreateJournalRequest)
  }

  const openEdit = (j: Journal) => { setEditing(j); setFormOpen(true) }
  const openCreate = () => { setEditing(null); setFormOpen(true) }
  const closeForm = () => { setEditing(null); setFormOpen(false) }

  return (
    <>
      <div className="mb-4 flex items-center justify-end">
        <Button size="sm" onClick={openCreate}>+ Novo Diário</Button>
      </div>

      {loading ? (
        <div className="flex justify-center py-16"><Spinner size="lg" /></div>
      ) : journals.length === 0 ? (
        <EmptyState
          message="Nenhum diário cadastrado"
          description="Crie diários para organizar seus lançamentos financeiros."
          action={<Button size="sm" onClick={openCreate}>+ Novo Diário</Button>}
        />
      ) : (
        <Table>
          <Table.Head>
            <Table.Th>Nome</Table.Th>
            <Table.Th>Valor Total</Table.Th>
            <Table.Th>Criado em</Table.Th>
            <Table.Th>Atualizado em</Table.Th>
            <Table.Th className="text-right">Ações</Table.Th>
          </Table.Head>
          <Table.Body>
            {journals.map((j) => (
              <Table.Tr key={j.id}>
                <Table.Td className="font-medium">{j.name}</Table.Td>
                <Table.Td className="font-medium text-[var(--primary)]">
                  {fmt.format(j.totalValue)}
                </Table.Td>
                <Table.Td className="text-[var(--muted)]">
                  {new Date(j.createdAt).toLocaleDateString('pt-BR')}
                </Table.Td>
                <Table.Td className="text-[var(--muted)]">
                  {new Date(j.updatedAt).toLocaleDateString('pt-BR')}
                </Table.Td>
                <Table.Td className="text-right">
                  <div className="flex justify-end gap-2">
                    <Button size="sm" variant="ghost" onClick={() => router.push(`/journals/${j.id}`)}><BookOpen className="h-3.5 w-3.5 mr-1" />Ver Entradas</Button>
                    <Button size="sm" variant="ghost" onClick={() => openEdit(j)}><Pencil className="h-3.5 w-3.5 mr-1" />Editar</Button>
                    <Button size="sm" variant="danger" onClick={() => confirm({ message: `Excluir o diário "${j.name}"? Todos os lançamentos serão removidos.`, onConfirm: () => remove(j.id) })}><Trash2 className="h-3.5 w-3.5 mr-1" />Excluir</Button>
                  </div>
                </Table.Td>
              </Table.Tr>
            ))}
          </Table.Body>
        </Table>
      )}

      <JournalForm open={formOpen} onClose={closeForm} onSubmit={handleSubmit} initial={editing} />
      <ConfirmDialog state={confirmState} onClose={closeConfirm} />
    </>
  )
}
