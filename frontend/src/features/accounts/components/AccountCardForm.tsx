'use client'

import { useState, useEffect } from 'react'
import Modal from '@/components/ui/Modal'
import Input from '@/components/ui/Input'
import Select from '@/components/ui/Select'
import Button from '@/components/ui/Button'
import type { AccountCard, CreateAccountCardRequest, UpdateAccountCardRequest, CardType } from '@/models/models'

const cardTypeOptions = [
  { label: 'Crédito', value: 'CREDIT' },
  { label: 'Débito', value: 'DEBIT' },
]

interface AccountCardFormProps {
  open: boolean
  onClose: () => void
  onSubmit: (data: Omit<CreateAccountCardRequest, 'account'> | UpdateAccountCardRequest) => Promise<void>
  initial?: AccountCard | null
}

export default function AccountCardForm({ open, onClose, onSubmit, initial }: AccountCardFormProps) {
  const [name, setName] = useState('')
  const [type, setType] = useState<CardType>('CREDIT')
  const [creditLimit, setCreditLimit] = useState('')
  const [expirationMonth, setExpirationMonth] = useState('')
  const [expirationYear, setExpirationYear] = useState('')
  const [isActive, setIsActive] = useState(true)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (initial) {
      setName(initial.name)
      setType(initial.type)
      setCreditLimit(String(initial.creditLimit))
      setExpirationMonth(String(initial.expirationMonth))
      setExpirationYear(String(initial.expirationYear))
      setIsActive(initial.isActive)
    } else {
      setName('')
      setType('CREDIT')
      setCreditLimit('')
      setExpirationMonth('')
      setExpirationYear('')
      setIsActive(true)
    }
  }, [initial, open])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      const base = {
        name,
        type,
        creditLimit: Number(creditLimit),
        expirationMonth: Number(expirationMonth),
        expirationYear: Number(expirationYear),
        isActive,
      }
      const payload = initial
        ? ({ id: initial.id, ...base } as UpdateAccountCardRequest)
        : (base as Omit<CreateAccountCardRequest, 'account'>)
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
      title={initial ? 'Editar Cartão' : 'Novo Cartão'}
      footer={
        <>
          <Button variant="ghost" type="button" onClick={onClose} size="sm">Cancelar</Button>
          <Button type="submit" form="card-form" loading={loading} size="sm">
            {initial ? 'Salvar' : 'Criar'}
          </Button>
        </>
      }
    >
      <form id="card-form" onSubmit={handleSubmit} className="flex flex-col gap-4">
        <Input
          id="card-name"
          label="Nome do cartão"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Ex: Nubank Gold"
          required
        />
        <Select
          id="card-type"
          label="Tipo"
          options={cardTypeOptions}
          value={type}
          onChange={(e) => setType(e.target.value as CardType)}
        />
        <Input
          id="card-limit"
          label="Limite (R$)"
          type="number"
          step="0.01"
          value={creditLimit}
          onChange={(e) => setCreditLimit(e.target.value)}
          placeholder="0,00"
          required
        />
        <div className="grid grid-cols-2 gap-3">
          <Input
            id="card-month"
            label="Mês de expiração"
            type="number"
            min="1"
            max="12"
            value={expirationMonth}
            onChange={(e) => setExpirationMonth(e.target.value)}
            placeholder="MM"
            required
          />
          <Input
            id="card-year"
            label="Ano de expiração"
            type="number"
            min={new Date().getFullYear()}
            value={expirationYear}
            onChange={(e) => setExpirationYear(e.target.value)}
            placeholder="AAAA"
            required
          />
        </div>
        <label className="flex items-center gap-2 text-sm text-[var(--text)]">
          <input
            type="checkbox"
            checked={isActive}
            onChange={(e) => setIsActive(e.target.checked)}
            className="accent-[var(--primary)]"
          />
          Cartão ativo
        </label>
      </form>
    </Modal>
  )
}
