'use client'

import { useState, useEffect } from 'react'
import Modal from '@/components/ui/Modal'
import Input from '@/components/ui/Input'
import Select from '@/components/ui/Select'
import Button from '@/components/ui/Button'
import type {
  Transaction, CreateTransactionRequest, UpdateTransactionRequest,
  TransactionType, Account, AccountCard, Category
} from '@/models/models'

const typeOptions = [
  { label: 'Dinheiro', value: 'CASH' },
  { label: 'Débito', value: 'DEBIT' },
  { label: 'Crédito', value: 'CREDIT' },
  { label: 'PIX', value: 'PIX' },
]

function filterCards(cards: AccountCard[], type: TransactionType) {
  if (type === 'CREDIT') return cards.filter((c) => c.type === 'CREDIT')
  if (type === 'DEBIT') return cards.filter((c) => c.type === 'DEBIT')
  return cards
}

function isDisabledCardOptions(type: TransactionType) {
  if (type === 'PIX' || type === 'CASH') {
    return true
  }
  else {
    return false
  }
}

function isDisabledInstallment(type: TransactionType) {
  if (type !== 'CREDIT') {
    return true
  }
  else {
    return false
  }
}

interface TransactionFormProps {
  open: boolean
  onClose: () => void
  onSubmit: (data: CreateTransactionRequest | UpdateTransactionRequest) => Promise<void>
  initial?: Transaction | null
  accounts: Account[]
  cards: AccountCard[]
  categories: Category[]
}

export default function TransactionForm({ open, onClose, onSubmit, initial, accounts, cards, categories }: TransactionFormProps) {
  const [description, setDescription] = useState('')
  const [amount, setAmount] = useState('')
  const [fee, setFee] = useState('0')
  const [installment, setInstallment] = useState('1')
  const [type, setType] = useState<TransactionType>('CASH')
  const [accountId, setAccountId] = useState('')
  const [cardId, setCardId] = useState('')
  const [categoryId, setCategoryId] = useState('')
  const [categoryError, setCategoryError] = useState('')
  const [loading, setLoading] = useState(false)

  const accountOptions = accounts.map((a) => ({ label: a.name, value: a.id }))
  const filteredCards = filterCards(cards, type)
  const cardOptions = [{ label: 'Nenhum', value: '' }, ...filteredCards.map((c) => ({ label: c.name, value: c.id }))]
  const categoryOptions = categories.map((c) => ({ label: c.name, value: c.id }))

  useEffect(() => {
    setCategoryError('')
    if (initial) {
      setDescription(initial.description)
      setAmount(String(initial.amount))
      setFee(String(initial.fee ?? 0))
      setInstallment(String(initial.installment))
      setType(initial.type)
      setAccountId(initial.account?.id ?? '')
      setCardId(initial.accountCard?.id ?? '')
      setCategoryId(initial.category?.id ?? '')
    } else {
      setDescription('')
      setAmount('')
      setFee('0')
      setInstallment('1')
      setType('CASH')
      setAccountId(accounts[0]?.id ?? '')
      setCardId('')
      setCategoryId(categories[0]?.id ?? '')
    }
  }, [initial, open, accounts, categories])

  const handleTypeChange = (newType: TransactionType) => {
    setType(newType)
    setCardId('')
  }

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    setCategoryError('')

    if (!categoryId) {
      setCategoryError('Preencha o campo obrigatório')
      return
    }

    setLoading(true)
    try {
      const account = accounts.find((a) => a.id === accountId)!
      const accountCard = cards.find((c) => c.id === cardId) ?? null
      const category = categories.find((c) => c.id === categoryId)!
      const base = {
        description,
        amount: Number(amount),
        fee: Number(fee),
        installment: Number(installment),
        type,
        account,
        accountCard: accountCard as AccountCard,
        category,
      }
      const payload = initial
        ? ({ id: initial.id, ...base } as UpdateTransactionRequest)
        : (base as CreateTransactionRequest)
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
      title={initial ? 'Editar Transação' : 'Nova Transação'}
      footer={
        <>
          <Button variant="ghost" type="button" onClick={onClose} size="sm">Cancelar</Button>
          <Button type="submit" form="tx-form" loading={loading} size="sm">
            {initial ? 'Salvar' : 'Criar'}
          </Button>
        </>
      }
    >
      <form id="tx-form" onSubmit={handleSubmit} className="flex flex-col gap-4">
        <Input
          id="tx-desc"
          label="Descrição"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Ex: Supermercado"
          required
        />
        <div className="grid grid-cols-2 gap-3">
          <Input
            id="tx-amount"
            label="Valor (R$)"
            type="number"
            step="0.01"
            min="1"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            placeholder="0,00"
            required
          />
          <Input
            id="tx-fee"
            label="Taxa (R$)"
            type="number"
            step="0.01"
            min="0"
            value={fee}
            onChange={(e) => setFee(e.target.value)}
            placeholder="0,00"
          />
        </div>
        <Input
          id="tx-inst"
          label="Parcelas"
          type="number"
          min="1"
          disabled={isDisabledInstallment(type)}
          value={installment}
          onChange={(e) => setInstallment(e.target.value)}
        />
        <Select
          id="tx-type"
          label="Tipo"
          options={typeOptions}
          value={type}
          onChange={(e) => handleTypeChange(e.target.value as TransactionType)}
        />
        <Select
          id="tx-account"
          label="Conta"
          options={accountOptions}
          value={accountId}
          onChange={(e) => setAccountId(e.target.value)}
        />
        <Select
          id="tx-card"
          label={`Cartão (opcional)${type === 'CREDIT' ? ' — somente Crédito' : type === 'DEBIT' ? ' — somente Débito' : ''}`}
          disabled={isDisabledCardOptions(type)}
          options={cardOptions}
          value={cardId}
          onChange={(e) => setCardId(e.target.value)}
        />
        {categories.length === 0 ? (
          <div className="flex flex-col gap-1">
            <span className="text-sm font-medium text-[var(--text)]">Categoria</span>
            <p className="text-sm text-[var(--muted)] rounded-lg border border-[var(--border)] px-3 py-2">
              Nenhuma categoria encontrada
            </p>
            {categoryError && <p className="text-xs text-[var(--danger)]">{categoryError}</p>}
          </div>
        ) : (
          <Select
            id="tx-category"
            label="Categoria"
            options={categoryOptions}
            value={categoryId}
            onChange={(e) => { setCategoryId(e.target.value); setCategoryError('') }}
            error={categoryError}
          />
        )}
      </form>
    </Modal>
  )
}
