'use client'

import { useState, useCallback } from 'react'
import Button from './Button'

interface ConfirmOptions {
  title?: string
  message: string
  confirmLabel?: string
  variant?: 'danger' | 'warning'
  onConfirm: () => void | Promise<void>
}

interface ConfirmState extends ConfirmOptions {
  open: boolean
}

const CLOSED: ConfirmState = { open: false, message: '', onConfirm: () => {} }

export function useConfirm() {
  const [state, setState] = useState<ConfirmState>(CLOSED)

  const confirm = useCallback((options: ConfirmOptions) => {
    setState({ ...options, open: true })
  }, [])

  const close = useCallback(() => setState(CLOSED), [])

  return { confirm, confirmState: state, closeConfirm: close }
}

interface ConfirmDialogProps {
  state: ConfirmState
  onClose: () => void
}

export function ConfirmDialog({ state, onClose }: ConfirmDialogProps) {
  const [loading, setLoading] = useState(false)

  if (!state.open) return null

  const isDanger = !state.variant || state.variant === 'danger'

  const handleConfirm = async () => {
    setLoading(true)
    try {
      await state.onConfirm()
      onClose()
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/40 backdrop-blur-sm" onClick={onClose} />
      <div className="relative z-10 w-full max-w-sm rounded-2xl bg-[var(--card)] shadow-xl border border-[var(--border)] p-6">
        <h2 className="text-base font-semibold text-[var(--text)] mb-2">
          {state.title ?? (isDanger ? 'Confirmar exclusão' : 'Confirmar ação')}
        </h2>
        <p className="text-sm text-[var(--muted)] mb-6">{state.message}</p>
        <div className="flex justify-end gap-2">
          <Button variant="ghost" size="sm" onClick={onClose} disabled={loading}>Cancelar</Button>
          <Button variant={isDanger ? 'danger' : 'primary'} size="sm" loading={loading} onClick={handleConfirm}>
            {state.confirmLabel ?? (isDanger ? 'Excluir' : 'Confirmar')}
          </Button>
        </div>
      </div>
    </div>
  )
}
