'use client'

import Table from '@/components/ui/Table'
import Badge from '@/components/ui/Badge'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import EmptyState from '@/components/ui/EmptyState'
import { useConfirm, ConfirmDialog } from '@/components/ui/ConfirmDialog'
import { useInstallments } from '../hooks/useInstallments'
import type { Transaction } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

const installmentTypeLabel: Record<string, string> = {
  PRICE: 'Price — parcelas fixas',
  SAC: 'SAC — parcelas decrescentes',
}

function formatDate(value: string | Date | null | undefined) {
  if (!value) return '—'
  const d = Array.isArray(value)
    ? new Date((value as number[])[0], (value as number[])[1] - 1, (value as number[])[2])
    : new Date(value)
  return d.toLocaleDateString('pt-BR')
}

interface InstallmentListProps {
  transaction: Transaction
}

export default function InstallmentList({ transaction }: InstallmentListProps) {
  const { purchase, installments, loading, pay } = useInstallments(transaction.id)
  const { confirm, confirmState, closeConfirm } = useConfirm()

  const paid = installments.filter((inst) => inst.paid).length

  if (loading) {
    return <div className="flex justify-center py-16"><Spinner size="lg" /></div>
  }

  return (
    <>
      {purchase && (
        <div className="mb-6 flex flex-wrap gap-6 rounded-xl bg-[var(--surface)] px-6 py-4 text-sm">
          <div className="flex flex-col gap-0.5">
            <span className="text-xs text-[var(--muted)]">Total</span>
            <span className="font-semibold text-[var(--text)]">{fmt.format(purchase.totalAmount)}</span>
          </div>
          <div className="flex flex-col gap-0.5">
            <span className="text-xs text-[var(--muted)]">Juros</span>
            <span className="font-medium text-[var(--text)]">{Number(purchase.interestRate ?? 0).toFixed(2)}% a.m.</span>
          </div>
          <div className="flex flex-col gap-0.5">
            <span className="text-xs text-[var(--muted)]">Tipo</span>
            <span className="font-medium text-[var(--text)]">
              {installmentTypeLabel[transaction.installmentType ?? ''] ?? '—'}
            </span>
          </div>
          <div className="ml-auto flex flex-col items-end gap-0.5">
            <span className="text-xs text-[var(--muted)]">Pagas</span>
            <span className="font-medium text-[var(--text)]">{paid}/{installments.length}</span>
          </div>
        </div>
      )}

      {installments.length === 0 ? (
        <EmptyState
          message="Nenhuma parcela encontrada"
          description="As parcelas desta transação ainda não foram geradas."
        />
      ) : (
        <Table>
          <Table.Head>
            <Table.Th>Nº</Table.Th>
            <Table.Th>Valor</Table.Th>
            <Table.Th>Vencimento</Table.Th>
            <Table.Th>Status</Table.Th>
            <Table.Th>Pago em</Table.Th>
            <Table.Th className="text-right">Ação</Table.Th>
          </Table.Head>
          <Table.Body>
            {installments.map((inst) => (
              <Table.Tr key={inst.id}>
                <Table.Td className="font-medium">{inst.installmentNumber}ª</Table.Td>
                <Table.Td>{fmt.format(inst.amount)}</Table.Td>
                <Table.Td className="text-[var(--muted)]">{formatDate(inst.dueDate)}</Table.Td>
                <Table.Td>
                  <Badge variant={inst.paid ? 'success' : 'warning'}>
                    {inst.paid ? 'Paga' : 'Pendente'}
                  </Badge>
                </Table.Td>
                <Table.Td className="text-[var(--muted)]">{formatDate(inst.paidAt)}</Table.Td>
                <Table.Td className="text-right">
                  {!inst.paid && (
                    <Button size="sm" variant="ghost" onClick={() => confirm({ title: 'Marcar parcela como paga', message: `Confirmar pagamento da ${inst.installmentNumber}ª parcela de ${fmt.format(inst.amount)}?`, confirmLabel: 'Marcar paga', variant: 'warning', onConfirm: () => pay(inst.id) })}>
                      Marcar paga
                    </Button>
                  )}
                </Table.Td>
              </Table.Tr>
            ))}
          </Table.Body>
        </Table>
      )}
      <ConfirmDialog state={confirmState} onClose={closeConfirm} />
    </>
  )
}
