'use client'

import { useState, useEffect } from 'react'
import { CheckCircle } from 'lucide-react'
import Table from '@/components/ui/Table'
import Badge from '@/components/ui/Badge'
import Button from '@/components/ui/Button'
import Spinner from '@/components/ui/Spinner'
import EmptyState from '@/components/ui/EmptyState'
import { useConfirm, ConfirmDialog } from '@/components/ui/ConfirmDialog'
import { installmentService } from '@/services/installment.service'
import type { Installment } from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

function formatDate(value: string | Date | number[] | null | undefined) {
  if (!value) return '—'
  const d = Array.isArray(value)
    ? new Date((value as number[])[0], (value as number[])[1] - 1, (value as number[])[2])
    : new Date(value as string | Date)
  return d.toLocaleDateString('pt-BR')
}

interface GroupedInstallments {
  purchaseId: string
  transactionDescription: string
  installments: Installment[]
}

function groupByPurchase(installments: Installment[]): GroupedInstallments[] {
  const map = new Map<string, GroupedInstallments>()
  for (const inst of installments) {
    const purchaseId = inst.installmentPurchase?.id ?? 'unknown'
    const description = (inst.installmentPurchase as any)?.transaction?.description ?? 'Sem descrição'
    if (!map.has(purchaseId)) {
      map.set(purchaseId, { purchaseId, transactionDescription: description, installments: [] })
    }
    map.get(purchaseId)!.installments.push(inst)
  }
  return Array.from(map.values())
}

interface CardInstallmentListProps {
  cardId: string
}

export default function CardInstallmentList({ cardId }: CardInstallmentListProps) {
  const [installments, setInstallments] = useState<Installment[]>([])
  const [loading, setLoading] = useState(true)
  const { confirm, confirmState, closeConfirm } = useConfirm()

  const load = () => {
    setLoading(true)
    installmentService.findByCard(cardId)
      .then(setInstallments)
      .catch(() => setInstallments([]))
      .finally(() => setLoading(false))
  }

  useEffect(() => { load() }, [cardId])

  const pay = async (installmentId: string) => {
    await installmentService.pay(installmentId)
    load()
  }

  if (loading) {
    return <div className="flex justify-center py-16"><Spinner size="lg" /></div>
  }

  if (installments.length === 0) {
    return (
      <EmptyState
        message="Nenhuma parcela encontrada"
        description="Este cartão não possui parcelas pendentes ou pagas."
      />
    )
  }

  const groups = groupByPurchase(installments)

  return (
    <div className="flex flex-col gap-8">
      {groups.map((group) => {
        const paid = group.installments.filter((i) => i.paid).length
        const total = group.installments.reduce((sum, i) => sum + i.amount, 0)
        return (
          <div key={group.purchaseId}>
            <div className="mb-3 flex items-center justify-between">
              <div>
                <h3 className="font-semibold text-[var(--text)]">{group.transactionDescription}</h3>
                <p className="text-xs text-[var(--muted)] mt-0.5">
                  {paid}/{group.installments.length} pagas · Total: {fmt.format(total)}
                </p>
              </div>
            </div>
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
                {group.installments.map((inst) => (
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
                          <CheckCircle className="h-3.5 w-3.5 mr-1" />Marcar paga
                        </Button>
                      )}
                    </Table.Td>
                  </Table.Tr>
                ))}
              </Table.Body>
            </Table>
          </div>
        )
      })}
      <ConfirmDialog state={confirmState} onClose={closeConfirm} />
    </div>
  )
}
