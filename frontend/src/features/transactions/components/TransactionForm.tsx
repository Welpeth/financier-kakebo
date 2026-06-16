'use client'

import { useState, useEffect } from 'react'
import Modal from '@/components/ui/Modal'
import Input from '@/components/ui/Input'
import Select from '@/components/ui/Select'
import Button from '@/components/ui/Button'
import { accountCardService } from '@/services/accountCard.service'
import type {
  Transaction, CreateTransactionRequest, UpdateTransactionRequest,
  TransactionType, SubscriptionFrequency, InstallmentType, Account, AccountCard, Category,
  AvailableLimitResponse, Journal,
} from '@/models/models'

const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

const typeOptions = [
  { label: 'Dinheiro', value: 'CASH' },
  { label: 'Débito', value: 'DEBIT' },
  { label: 'Crédito', value: 'CREDIT' },
  { label: 'PIX', value: 'PIX' },
]

const installmentTypeOptions = [
  { label: 'Price — parcelas fixas', value: 'PRICE' },
  { label: 'SAC — parcelas decrescentes', value: 'SAC' },
]

const frequencyOptions = [
  { label: 'Diário', value: 'DAILY' },
  { label: 'Semanal', value: 'WEEKLY' },
  { label: 'Mensal', value: 'MONTHLY' },
  { label: 'Trimestral', value: 'QUARTERLY' },
  { label: 'Anual', value: 'YEARLY' },
]

function filterCards(cards: AccountCard[], type: TransactionType) {
  if (type === 'CREDIT') return cards.filter((c) => c.type === 'CREDIT')
  if (type === 'DEBIT') return cards.filter((c) => c.type === 'DEBIT')
  return cards
}

interface TransactionFormProps {
  open: boolean
  onClose: () => void
  onSubmit: (data: CreateTransactionRequest | UpdateTransactionRequest) => Promise<void>
  initial?: Transaction | null
  accounts: Account[]
  cards: AccountCard[]
  categories: Category[]
  journals: Journal[]
}

export default function TransactionForm({ open, onClose, onSubmit, initial, accounts, cards, categories, journals }: TransactionFormProps) {
  const [description, setDescription] = useState('')
  const [amount, setAmount] = useState('')
  const [fee, setFee] = useState('0')
  const [installment, setInstallment] = useState('1')
  const [type, setType] = useState<TransactionType>('CASH')
  const [accountId, setAccountId] = useState('')
  const [cardId, setCardId] = useState('')
  const [cardError, setCardError] = useState('')
  const [categoryId, setCategoryId] = useState('')
  const [categoryError, setCategoryError] = useState('')
  const [dueDate, setDueDate] = useState('')
  const [isSubscription, setIsSubscription] = useState(false)
  const [frequency, setFrequency] = useState<SubscriptionFrequency>('MONTHLY')
  const [installmentType, setInstallmentType] = useState<InstallmentType>('SAC')
  const [availableLimit, setAvailableLimit] = useState<AvailableLimitResponse | null>(null)
  const [journalId, setJournalId] = useState('')
  const [loading, setLoading] = useState(false)

  const accountOptions = accounts.length === 0
    ? [{ label: 'Nenhuma encontrada', value: '' }]
    : accounts.map((a) => ({ label: a.name, value: a.id }))
  const filteredCards = filterCards(cards, type)
  const cardOptions = filteredCards.length === 0
    ? [{ label: 'Nenhum encontrado', value: '' }]
    : type === 'CREDIT'
      ? [{ label: 'Selecione um cartão', value: '' }, ...filteredCards.map((c) => ({ label: c.name, value: c.id }))]
      : [{ label: 'Nenhum', value: '' }, ...filteredCards.map((c) => ({ label: c.name, value: c.id }))]
  const categoryOptions = categories.length === 0
    ? [{ label: 'Nenhuma encontrada', value: '' }]
    : categories.map((c) => ({ label: c.name, value: c.id }))
  const isCredit = type === 'CREDIT'
  const cardDisabled = type === 'PIX' || type === 'CASH'
  const categoryDisabled = categories.length === 0

  useEffect(() => {
    setCategoryError('')
    setCardError('')
    setAvailableLimit(null)
    if (initial) {
      setDescription(initial.description)
      setAmount(String(initial.amount))
      setFee(String(initial.fee ?? 0))
      setInstallment(String(initial.installment ?? 1))
      setInstallmentType(initial.installmentType ?? 'SAC')
      setType(initial.type)
      setAccountId(initial.account?.id ?? '')
      setCardId(initial.accountCard?.id ?? '')
      setCategoryId(initial.category?.id ?? '')
      setIsSubscription(initial.subscription ?? false)
      setDueDate('')
      setFrequency('MONTHLY')
    } else {
      setDescription('')
      setAmount('')
      setFee('0')
      setInstallment('1')
      setType('CASH')
      setAccountId(accounts[0]?.id ?? '')
      setCardId('')
      setCategoryId(categories[0]?.id ?? '')
      setIsSubscription(false)
      setDueDate(new Date().toISOString().slice(0, 10))
      setFrequency('MONTHLY')
      setJournalId('')
    }
  }, [initial, open, accounts, categories])

  useEffect(() => {
    if (isSubscription) {
      setFee('0')
    }
  }, [isSubscription])

  useEffect(() => {
    if (!isCredit || !cardId) {
      setAvailableLimit(null)
      return
    }
    accountCardService.getAvailableLimit(cardId)
      .then(setAvailableLimit)
      .catch(() => setAvailableLimit(null))
  }, [isCredit, cardId])

  const handleTypeChange = (newType: TransactionType) => {
    setType(newType)
    setCardId('')
    setAvailableLimit(null)
    setCardError('')
    if (newType !== 'CREDIT') setIsSubscription(false)
  }

  const handleCardChange = (id: string) => {
    setCardId(id)
    setCardError('')
  }

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    setCategoryError('')
    setCardError('')

    if (!categoryId) {
      setCategoryError('Preencha o campo obrigatório')
      return
    }
    if (isCredit && !cardId) {
      setCardError('Selecione um cartão de crédito')
      return
    }

    setLoading(true)
    try {
      const account = accounts.find((a) => a.id === accountId)!
      const accountCard = (cards.find((c) => c.id === cardId) ?? null) as AccountCard
      const category = categories.find((c) => c.id === categoryId)!

      const base = { description, amount: Number(amount), fee: Number(fee), type, account, accountCard, category, installmentType }

      const journal = journals.find((j) => j.id === journalId) ?? undefined

      const payload = initial
        ? ({ id: initial.id, ...base, installment: Number(installment) } as UpdateTransactionRequest)
        : ({
            ...base,
            installment: isCredit && !isSubscription ? Number(installment) : 1,
            ...(isCredit && { dueDate: dueDate as unknown as Date }),
            ...(isCredit && isSubscription && { frequency }),
            ...(journal && { journal }),
          } as CreateTransactionRequest)

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
            label="Juros (% a.m.)"
            type="number"
            step="0.01"
            min="0"
            value={fee}
            onChange={(e) => setFee(e.target.value)}
            placeholder="0,00"
            disabled={isSubscription}
          />
        </div>

        <Select
          id="tx-type"
          label="Tipo"
          options={typeOptions}
          value={type}
          onChange={(e) => handleTypeChange(e.target.value as TransactionType)}
        />

        {isCredit && !initial && (
          <>
            <Input
              id="tx-due"
              label="Data de vencimento"
              type="date"
              value={dueDate}
              onChange={(e) => setDueDate(e.target.value)}
              required
            />
            <label className="flex cursor-pointer gap-3 rounded-lg py-2.5">
              <input
                type="checkbox"
                checked={isSubscription}
                onChange={(e) => setIsSubscription(e.target.checked)}
                className="h-4 w-4 accent-[var(--primary)]"
              />
              <span className="text-sm font-medium text-[var(--text)]">Assinatura</span>
            </label>
            {isSubscription ? (
              <Select
                id="tx-freq"
                label="Frequência"
                options={frequencyOptions}
                value={frequency}
                onChange={(e) => setFrequency(e.target.value as SubscriptionFrequency)}
              />
            ) : (
              <>
                <Input
                  id="tx-inst"
                  label="Parcelas"
                  type="number"
                  min="1"
                  value={installment}
                  onChange={(e) => setInstallment(e.target.value)}
                />
                <Select
                  id="tx-installment-type"
                  label="Tipo de parcelamento"
                  options={installmentTypeOptions}
                  value={installmentType}
                  onChange={(e) => setInstallmentType(e.target.value as InstallmentType)}
                />
              </>
            )}
          </>
        )}

        {!isCredit && initial && (
          <Input
            id="tx-inst"
            label="Parcelas"
            type="number"
            min="1"
            disabled
            value={installment}
            onChange={(e) => setInstallment(e.target.value)}
          />
        )}

        <Select
          id="tx-account"
          label="Conta"
          options={accountOptions}
          value={accountId}
          onChange={(e) => setAccountId(e.target.value)}
        />
        <div className="flex flex-col gap-1">
          <Select
            id="tx-card"
            label={`Cartão${isCredit ? ' (obrigatório)' : ' (opcional)'}${type === 'CREDIT' ? ' — somente Crédito' : type === 'DEBIT' ? ' — somente Débito' : ''}`}
            disabled={cardDisabled}
            options={cardOptions}
            value={cardId}
            onChange={(e) => handleCardChange(e.target.value)}
            error={cardError}
          />
          {isCredit && availableLimit && (
            <p className={`text-xs font-medium ${availableLimit.availableLimit <= 0 ? 'text-red-500' : 'text-[var(--primary)]'}`}>
              Limite disponível: {fmt.format(availableLimit.availableLimit)}
              {availableLimit.availableLimit <= 0 && ' — Limite atingido'}
            </p>
          )}
        </div>
        <Select
          id="tx-category"
          label="Categoria"
          disabled={categoryDisabled}
          options={categoryOptions}
          value={categoryId}
          onChange={(e) => { setCategoryId(e.target.value); setCategoryError('') }}
          error={categoryError}
        />
        {!initial && (
          <Select
            id="tx-journal"
            label="Diário (opcional)"
            options={[
              { label: 'Nenhum', value: '' },
              ...journals.map((j) => ({ label: j.name, value: j.id })),
            ]}
            value={journalId}
            onChange={(e) => setJournalId(e.target.value)}
          />
        )}
      </form>
    </Modal>
  )
}
