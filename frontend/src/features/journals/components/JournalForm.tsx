'use client'

import { useState, useEffect } from 'react'
import Modal from '@/components/ui/Modal'
import Input from '@/components/ui/Input'
import Button from '@/components/ui/Button'
import type { Journal, CreateJournalRequest, UpdateJournalRequest } from '@/models/models'

interface JournalFormProps {
  open: boolean
  onClose: () => void
  onSubmit: (data: CreateJournalRequest | UpdateJournalRequest) => Promise<void>
  initial?: Journal | null
}

export default function JournalForm({ open, onClose, onSubmit, initial }: JournalFormProps) {
  const [name, setName] = useState('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    setName(initial?.name ?? '')
  }, [initial, open])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      const payload = initial
        ? ({ id: initial.id, name } as UpdateJournalRequest)
        : ({ name } as CreateJournalRequest)
      await onSubmit(payload)
      onClose()
    } finally {
      setLoading(false)
    }
  }

  return (
    <Modal
      open={open}
      onClose={onClose}
      title={initial ? 'Editar Diário' : 'Novo Diário'}
      footer={
        <>
          <Button variant="ghost" type="button" onClick={onClose} size="sm">Cancelar</Button>
          <Button type="submit" form="journal-form" loading={loading} size="sm">
            {initial ? 'Salvar' : 'Criar'}
          </Button>
        </>
      }
    >
      <form id="journal-form" onSubmit={handleSubmit} className="flex flex-col gap-4">
        <Input
          id="j-name"
          label="Nome"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Ex: Janeiro 2026"
          required
        />
        <p className="text-xs text-[var(--muted)]">
          O valor total é calculado automaticamente com base nas transações vinculadas.
        </p>
      </form>
    </Modal>
  )
}
