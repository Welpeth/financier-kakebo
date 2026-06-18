'use client'

import Modal from '@/components/ui/Modal'
import Button from '@/components/ui/Button'
import EmptyState from '@/components/ui/EmptyState'
import { Check } from 'lucide-react'
import type { Notification } from '@/models/models'

const currency = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

function parseLocalDate(value: string | Date | number[] | null | undefined): Date | null {
  if (!value) return null
  if (Array.isArray(value)) {
    return new Date(value[0], value[1] - 1, value[2])
  }
  return new Date(value as string | Date)
}

function formatDate(value: string | Date | number[] | null | undefined): string {
  const d = parseLocalDate(value)
  return d ? d.toLocaleDateString('pt-BR') : '—'
}

function startOfToday(): Date {
  const now = new Date()
  return new Date(now.getFullYear(), now.getMonth(), now.getDate())
}

interface NotificationModalProps {
  open: boolean
  onClose: () => void
  notifications: Notification[]
  loading: boolean
  onMarkRead: (id: string) => void
  onMarkAllRead: () => void
}

function NotificationRow({
  notification,
  overdue,
  onMarkRead,
}: {
  notification: Notification
  overdue: boolean
  onMarkRead: (id: string) => void
}) {
  return (
    <div
      className={`flex items-start justify-between gap-3 rounded-lg border px-3 py-2.5 ${
        notification.read
          ? 'border-[var(--border)] opacity-60'
          : 'border-[var(--border)] bg-[var(--surface)]'
      }`}
    >
      <div className="min-w-0">
        <p className="text-sm font-medium text-[var(--text)] truncate">{notification.title}</p>
        <p className="mt-0.5 text-xs text-[var(--muted)]">{notification.message}</p>
        <p className={`mt-1 text-xs font-medium ${overdue ? 'text-[var(--danger)]' : 'text-[var(--muted)]'}`}>
          {currency.format(notification.amount)} · vence {formatDate(notification.dueDate)}
        </p>
      </div>
      {!notification.read && (
        <button
          onClick={() => onMarkRead(notification.id)}
          title="Marcar como lida"
          className="shrink-0 rounded-lg p-1.5 text-[var(--muted)] hover:bg-[var(--card)] hover:text-[var(--primary)] transition-colors"
        >
          <Check className="h-4 w-4" />
        </button>
      )}
    </div>
  )
}

export default function NotificationModal({
  open,
  onClose,
  notifications,
  loading,
  onMarkRead,
  onMarkAllRead,
}: NotificationModalProps) {
  const today = startOfToday()
  const overdue = notifications.filter((n) => {
    const d = parseLocalDate(n.dueDate)
    return d !== null && d < today
  })
  const dueSoon = notifications.filter((n) => {
    const d = parseLocalDate(n.dueDate)
    return d !== null && d >= today
  })

  const hasUnread = notifications.some((n) => !n.read)

  return (
    <Modal
      open={open}
      onClose={onClose}
      title="Contas a pagar"
      footer={
        notifications.length > 0 ? (
          <Button variant="ghost" size="sm" onClick={onMarkAllRead} disabled={!hasUnread}>
            Marcar todas como lidas
          </Button>
        ) : undefined
      }
    >
      {loading ? (
        <p className="py-8 text-center text-sm text-[var(--muted)]">Carregando…</p>
      ) : notifications.length === 0 ? (
        <EmptyState
          message="Nenhuma conta a vencer"
          description="Você está em dia com suas parcelas e assinaturas."
        />
      ) : (
        <div className="space-y-4">
          {overdue.length > 0 && (
            <section>
              <h3 className="mb-2 text-xs font-semibold uppercase tracking-wide text-[var(--danger)]">
                Vencidas
              </h3>
              <div className="space-y-2">
                {overdue.map((n) => (
                  <NotificationRow key={n.id} notification={n} overdue onMarkRead={onMarkRead} />
                ))}
              </div>
            </section>
          )}
          {dueSoon.length > 0 && (
            <section>
              <h3 className="mb-2 text-xs font-semibold uppercase tracking-wide text-[var(--muted)]">
                Próximos dias
              </h3>
              <div className="space-y-2">
                {dueSoon.map((n) => (
                  <NotificationRow key={n.id} notification={n} overdue={false} onMarkRead={onMarkRead} />
                ))}
              </div>
            </section>
          )}
        </div>
      )}
    </Modal>
  )
}
