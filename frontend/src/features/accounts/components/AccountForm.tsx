'use client'

import { useState, useEffect } from 'react'
import Modal from '@/components/ui/Modal'
import Input from '@/components/ui/Input'
import Select from '@/components/ui/Select'
import Button from '@/components/ui/Button'
import type { Account, CreateAccountRequest, UpdateAccountRequest, AccountType } from '@/models/models'

const accountTypeOptions = [
  { label: 'Conta Corrente', value: 'CHECKING' },
  { label: 'Poupança', value: 'SAVINGS' },
]

interface AccountFormProps {
  open: boolean
  onClose: () => void
  onSubmit: (data: CreateAccountRequest | UpdateAccountRequest) => Promise<void>
  initial?: Account | null
}

export default function AccountForm({ open, onClose, onSubmit, initial }: AccountFormProps) {
  const [name, setName] = useState('')
  const [balance, setBalance] = useState('')
  const [type, setType] = useState<AccountType>('CHECKING')
  const [isActive, setIsActive] = useState(true)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (initial) {
      setName(initial.name)
      setBalance(String(initial.balance))
      setType(initial.type)
      setIsActive(initial.isActive)
    } else {
      setName('')
      setBalance('')
      setType('CHECKING')
      setIsActive(true)
    }
  }, [initial, open])

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    setLoading(true)
    try {
      const payload = initial
        ? ({ id: initial.id, name, balance: Number(balance), type, isActive } as UpdateAccountRequest)
        : ({ name, balance: Number(balance), type, isActive } as CreateAccountRequest)
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
      title={initial ? 'Editar Conta' : 'Nova Conta'}
      footer={
        <>
          <Button variant="ghost" type="button" onClick={onClose} size="sm">Cancelar</Button>
          <Button type="submit" form="account-form" loading={loading} size="sm">
            {initial ? 'Salvar' : 'Criar'}
          </Button>
        </>
      }
    >
      <form id="account-form" onSubmit={handleSubmit} className="flex flex-col gap-4">
        <Input
          id="acc-name"
          label="Nome"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Ex: Conta Principal"
          required
        />
        <Input
          id="acc-balance"
          label="Saldo inicial"
          type="number"
          step="0.01"
          value={balance}
          onChange={(e) => setBalance(e.target.value)}
          placeholder="0,00"
          required
        />
        <Select
          id="acc-type"
          label="Tipo"
          options={accountTypeOptions}
          value={type}
          onChange={(e) => setType(e.target.value as AccountType)}
        />
        <label className="flex items-center gap-2 text-sm text-[var(--text)]">
          <input
            type="checkbox"
            checked={isActive}
            onChange={(e) => setIsActive(e.target.checked)}
            className="h-4 w-4 accent-[var(--primary)] rounded"
          />
          Conta ativa
        </label>
      </form>
    </Modal>
  )
}
