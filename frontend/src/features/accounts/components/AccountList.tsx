'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import Table from '@/components/ui/Table'
import Badge from '@/components/ui/Badge'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import EmptyState from '@/components/ui/EmptyState'
import { useConfirm, ConfirmDialog } from '@/components/ui/ConfirmDialog'
import AccountForm from './AccountForm'
import { useAccounts } from '../hooks/useAccounts'
import type { Account, CreateAccountRequest, UpdateAccountRequest } from '@/models/models'

const typeLabel: Record<string, string> = { CHECKING: 'Corrente', SAVINGS: 'Poupança' }

export default function AccountList() {
  const { accounts, loading, create, update, remove } = useAccounts()
  const router = useRouter()
  const { confirm, confirmState, closeConfirm } = useConfirm()
  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<Account | null>(null)

  const handleSubmit = async (data: CreateAccountRequest | UpdateAccountRequest) => {
    if ('id' in data) await update(data as UpdateAccountRequest)
    else await create(data as CreateAccountRequest)
  }

  const openEdit = (account: Account) => { setEditing(account); setFormOpen(true) }
  const openCreate = () => { setEditing(null); setFormOpen(true) }
  const closeForm = () => { setEditing(null); setFormOpen(false) }

  return (
    <>
      <div className="mb-4 flex items-center justify-end">
        <Button size="sm" onClick={openCreate}>+ Nova Conta</Button>
      </div>

      {loading ? (
        <div className="flex justify-center py-16"><Spinner size="lg" /></div>
      ) : accounts.length === 0 ? (
        <EmptyState
          message="Nenhuma conta cadastrada"
          description="Crie sua primeira conta para começar."
          action={<Button size="sm" onClick={openCreate}>+ Nova Conta</Button>}
        />
      ) : (
        <Table>
          <Table.Head>
            <Table.Th>Nome</Table.Th>
            <Table.Th>Tipo</Table.Th>
            <Table.Th>Saldo</Table.Th>
            <Table.Th>Status</Table.Th>
            <Table.Th className="text-right">Ações</Table.Th>
          </Table.Head>
          <Table.Body>
            {accounts.map((acc) => (
              <Table.Tr key={acc.id}>
                <Table.Td className="font-medium">{acc.name}</Table.Td>
                <Table.Td>{typeLabel[acc.type] ?? acc.type}</Table.Td>
                <Table.Td>
                  {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(acc.balance)}
                </Table.Td>
                <Table.Td>
                  <Badge variant={acc.isActive ? 'success' : 'neutral'}>
                    {acc.isActive ? 'Ativa' : 'Inativa'}
                  </Badge>
                </Table.Td>
                <Table.Td className="text-right">
                  <div className="flex justify-end gap-2">
                    <Button size="sm" variant="ghost" onClick={() => router.push(`/accounts/${acc.id}`)}>Cartões</Button>
                    <Button size="sm" variant="ghost" onClick={() => openEdit(acc)}>Editar</Button>
                    <Button size="sm" variant="danger" onClick={() => confirm({ message: `Excluir a conta "${acc.name}"? Esta ação não pode ser desfeita.`, onConfirm: () => remove(acc.id) })}>Excluir</Button>
                  </div>
                </Table.Td>
              </Table.Tr>
            ))}
          </Table.Body>
        </Table>
      )}

      <AccountForm
        open={formOpen}
        onClose={closeForm}
        onSubmit={handleSubmit}
        initial={editing}
      />
      <ConfirmDialog state={confirmState} onClose={closeConfirm} />

    </>
  )
}
