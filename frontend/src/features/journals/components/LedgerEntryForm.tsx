'use client'

import { useState, useEffect } from 'react'
import Modal from '@/components/ui/Modal'
import Input from '@/components/ui/Input'
import Select from '@/components/ui/Select'
import Button from '@/components/ui/Button'
import type { LedgerEntry, CreateLedgerEntryRequest, UpdateLedgerEntryRequest, Transaction, Journal } from '@/models/models'

interface LedgerEntryFormProps {
  open: boolean
  onClose: () => void
  onSubmit: (data: CreateLedgerEntryRequest | UpdateLedgerEntryRequest) => Promise<void>
  initial?: LedgerEntry | null
  journal: Journal
  transactions: Transaction[]
}

export default function LedgerEntryForm({ open, onClose, onSubmit, initial, journal, transactions }: LedgerEntryFormProps) {
  const [name, setName] = useState('')
  const [finalDate, setFinalDate] = useState('')
  const [transactionId, setTransactionId] = useState('')
  const [transactionError, setTransactionError] = useState('')
  const [loading, setLoading] = useState(false)

  const txOptions = transactions.map((t) => ({
    label: `${t.description} — R$ ${Number(t.amount).toFixed(2)}`,
    value: t.id,
  }))

  useEffect(() => {
    setTransactionError('')
    if (initial) {
      setName(initial.name)
      setFinalDate(initial.finalDate ? new Date(initial.finalDate).toISOString().slice(0, 10) : '')
      setTransactionId(initial.transaction?.id ?? '')
    } else {
      setName('')
      setFinalDate(new Date().toISOString().slice(0, 10))
      setTransactionId(transactions[0]?.id ?? '')
    }
  }, [initial, open, transactions])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setTransactionError('')

    if (!initial && !transactionId) {
      setTransactionError('Preencha o campo obrigatório')
      return
    }

    setLoading(true)
    try {
      const transaction = transactions.find((t) => t.id === transactionId)!
      const payload = initial
        ? ({ id: initial.id, name, finalDate: new Date(finalDate) } as UpdateLedgerEntryRequest)
        : ({ name, finalDate: new Date(finalDate), journal, transaction } as CreateLedgerEntryRequest)
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
      title={initial ? 'Editar Lançamento' : 'Novo Lançamento'}
      footer={
        <>
          <Button variant="ghost" type="button" onClick={onClose} size="sm">Cancelar</Button>
          <Button type="submit" form="entry-form" loading={loading} size="sm">
            {initial ? 'Salvar' : 'Criar'}
          </Button>
        </>
      }
    >
      <form id="entry-form" onSubmit={handleSubmit} className="flex flex-col gap-4">
        <Input
          id="entry-name"
          label="Nome do lançamento"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Ex: Compra no supermercado"
          required
        />
        <Input
          id="entry-date"
          label="Data final"
          type="date"
          value={finalDate}
          onChange={(e) => setFinalDate(e.target.value)}
          required
        />
        {!initial && (
          transactions.length === 0 ? (
            <div className="flex flex-col gap-1">
              <span className="text-sm font-medium text-[var(--text)]">Transação vinculada</span>
              <p className="text-sm text-[var(--muted)] rounded-lg border border-[var(--border)] px-3 py-2">
                Nenhuma transação encontrada
              </p>
              {transactionError && (
                <p className="text-xs text-[var(--danger)]">{transactionError}</p>
              )}
            </div>
          ) : (
            <Select
              id="entry-tx"
              label="Transação vinculada"
              options={txOptions}
              value={transactionId}
              onChange={(e) => { setTransactionId(e.target.value); setTransactionError('') }}
              error={transactionError}
            />
          )
        )}
      </form>
    </Modal>
  )
}
