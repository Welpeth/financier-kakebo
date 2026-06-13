'use client'

import { useState, useEffect } from 'react'
import Modal from '@/components/ui/Modal'
import Input from '@/components/ui/Input'
import Select from '@/components/ui/Select'
import Button from '@/components/ui/Button'
import type { Category, CreateCategoryRequest, UpdateCategoryRequest, Journal } from '@/models/models'

interface CategoryFormProps {
  open: boolean
  onClose: () => void
  onSubmit: (data: CreateCategoryRequest | UpdateCategoryRequest) => Promise<void>
  initial?: Category | null
  journals: Journal[]
}

export default function CategoryForm({ open, onClose, onSubmit, initial, journals }: CategoryFormProps) {
  const [name, setName] = useState('')
  const [journalId, setJournalId] = useState('')
  const [journalError, setJournalError] = useState('')
  const [loading, setLoading] = useState(false)

  const journalOptions = journals.map((j) => ({ label: j.name, value: j.id }))

  useEffect(() => {
    setJournalError('')
    if (initial) {
      setName(initial.name)
      setJournalId(initial.journal?.id ?? '')
    } else {
      setName('')
      setJournalId(journals[0]?.id ?? '')
    }
  }, [initial, open, journals])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setJournalError('')

    if (!initial && !journalId) {
      setJournalError('Preencha o campo obrigatório')
      return
    }

    setLoading(true)
    try {
      const selectedJournal = journals.find((j) => j.id === journalId)
      const payload = initial
        ? ({ id: initial.id, name } as UpdateCategoryRequest)
        : ({ name, journal: selectedJournal! } as CreateCategoryRequest)
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
      title={initial ? 'Editar Categoria' : 'Nova Categoria'}
      footer={
        <>
          <Button variant="ghost" type="button" onClick={onClose} size="sm">Cancelar</Button>
          <Button type="submit" form="category-form" loading={loading} size="sm">
            {initial ? 'Salvar' : 'Criar'}
          </Button>
        </>
      }
    >
      <form id="category-form" onSubmit={handleSubmit} className="flex flex-col gap-4">
        <Input
          id="cat-name"
          label="Nome"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Ex: Alimentação"
          required
        />
        {!initial && (
          journals.length === 0 ? (
            <div className="flex flex-col gap-1">
              <span className="text-sm font-medium text-[var(--text)]">Diário</span>
              <p className="text-sm text-[var(--muted)] rounded-lg border border-[var(--border)] px-3 py-2">
                Nenhum diário encontrado
              </p>
              {journalError && <p className="text-xs text-[var(--danger)]">{journalError}</p>}
            </div>
          ) : (
            <Select
              id="cat-journal"
              label="Diário"
              options={journalOptions}
              value={journalId}
              onChange={(e) => { setJournalId(e.target.value); setJournalError('') }}
              error={journalError}
            />
          )
        )}
      </form>
    </Modal>
  )
}
