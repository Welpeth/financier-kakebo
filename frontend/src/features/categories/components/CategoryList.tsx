'use client'

import { useState } from 'react'
import Table from '@/components/ui/Table'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import EmptyState from '@/components/ui/EmptyState'
import CategoryForm from './CategoryForm'
import { useCategories } from '../hooks/useCategories'
import { useJournals } from '@/features/journals/hooks/useJournals'
import type { Category, CreateCategoryRequest, UpdateCategoryRequest } from '@/models/models'

export default function CategoryList() {
  const { categories, loading, create, update, remove } = useCategories()
  const { journals } = useJournals()
  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<Category | null>(null)

  const handleSubmit = async (data: CreateCategoryRequest | UpdateCategoryRequest) => {
    if ('id' in data) await update(data as UpdateCategoryRequest)
    else await create(data as CreateCategoryRequest)
  }

  const openEdit = (cat: Category) => { setEditing(cat); setFormOpen(true) }
  const openCreate = () => { setEditing(null); setFormOpen(true) }
  const closeForm = () => { setEditing(null); setFormOpen(false) }

  return (
    <>
      <div className="mb-4 flex items-center justify-end">
        <Button size="sm" onClick={openCreate}>+ Nova Categoria</Button>
      </div>

      {loading ? (
        <div className="flex justify-center py-16"><Spinner size="lg" /></div>
      ) : categories.length === 0 ? (
        <EmptyState
          message="Nenhuma categoria cadastrada"
          description="Organize suas transações criando categorias."
          action={<Button size="sm" onClick={openCreate}>+ Nova Categoria</Button>}
        />
      ) : (
        <Table>
          <Table.Head>
            <Table.Th>Nome</Table.Th>
            <Table.Th>Diário</Table.Th>
            <Table.Th>Criado em</Table.Th>
            <Table.Th className="text-right">Ações</Table.Th>
          </Table.Head>
          <Table.Body>
            {categories.map((cat) => (
              <Table.Tr key={cat.id}>
                <Table.Td className="font-medium">{cat.name}</Table.Td>
                <Table.Td className="text-[var(--muted)]">{cat.journal?.name ?? '—'}</Table.Td>
                <Table.Td className="text-[var(--muted)]">
                  {new Date(cat.createdAt).toLocaleDateString('pt-BR')}
                </Table.Td>
                <Table.Td className="text-right">
                  <div className="flex justify-end gap-2">
                    <Button size="sm" variant="ghost" onClick={() => openEdit(cat)}>Editar</Button>
                    <Button size="sm" variant="danger" onClick={() => remove(cat.id)}>Excluir</Button>
                  </div>
                </Table.Td>
              </Table.Tr>
            ))}
          </Table.Body>
        </Table>
      )}

      <CategoryForm
        open={formOpen}
        onClose={closeForm}
        onSubmit={handleSubmit}
        initial={editing}
        journals={journals}
      />
    </>
  )
}
