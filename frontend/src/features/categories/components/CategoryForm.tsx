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
}

export default function CategoryForm({ open, onClose, onSubmit, initial }: CategoryFormProps) {
  const [name, setName] = useState('')
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (initial) {
      setName(initial.name)
    } else {
      setName('')
    }
  }, [initial, open])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    setLoading(true)
    try {
      const payload = initial
        ? ({ id: initial.id, name } as UpdateCategoryRequest)
        : ({ name } as CreateCategoryRequest)
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
      </form>
    </Modal>
  )
}
