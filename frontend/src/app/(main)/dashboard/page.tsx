'use client'

import { useEffect, useState } from 'react'
import PageHeader from '@/components/layout/PageHeader'
import Card from '@/components/ui/Card'
import Spinner from '@/components/ui/Spinner'
import { accountService } from '@/services/account.service'
import { transactionService } from '@/services/transaction.service'
import { journalService } from '@/services/journal.service'

export default function DashboardPage() {
  const [data, setData] = useState({ accounts: 0, balance: 0, transactions: 0, journals: 0 })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      accountService.findAll(),
      transactionService.findAll(),
      journalService.findAll(),
    ]).then(([accounts, transactions, journals]) => {
      setData({
        accounts: accounts.length,
        balance: accounts.reduce((sum, a) => sum + (a.balance ?? 0), 0),
        transactions: transactions.length,
        journals: journals.length,
      })
    }).catch(() => {}).finally(() => setLoading(false))
  }, [])

  const fmt = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' })

  const stats = [
    {
      label: 'Saldo Total',
      value: loading ? '...' : fmt.format(data.balance),
      icon: (
        <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="1.5">
          <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v12m-3-2.818l.879.659c1.171.879 3.07.879 4.242 0 1.172-.879 1.172-2.303 0-3.182C13.536 12.219 12.768 12 12 12c-.725 0-1.45-.22-2.003-.659-1.106-.879-1.106-2.303 0-3.182s2.9-.879 4.006 0l.415.33M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      ),
    },
    {
      label: 'Contas',
      value: loading ? '...' : String(data.accounts),
      icon: (
        <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="1.5">
          <path strokeLinecap="round" strokeLinejoin="round" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
        </svg>
      ),
    },
    {
      label: 'Transações',
      value: loading ? '...' : String(data.transactions),
      icon: (
        <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="1.5">
          <path strokeLinecap="round" strokeLinejoin="round" d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4" />
        </svg>
      ),
    },
    {
      label: 'Diários',
      value: loading ? '...' : String(data.journals),
      icon: (
        <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="1.5">
          <path strokeLinecap="round" strokeLinejoin="round" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
        </svg>
      ),
    },
  ]

  return (
    <>
      <PageHeader title="Dashboard" />

      {loading ? (
        <div className="flex justify-center py-16"><Spinner size="lg" /></div>
      ) : (
        <div className="grid mt-6 grid-cols-2 gap-4 lg:grid-cols-4">
          {stats.map((s) => (
            <Card key={s.label}>
              <div className="flex items-start justify-between">
                <div>
                  <p className="text-xs font-medium text-[var(--muted)] uppercase tracking-wide">{s.label}</p>
                  <p className="mt-2 text-2xl font-bold text-[var(--text)]">{s.value}</p>
                </div>
                <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-[var(--primary-light)] text-[var(--primary)]">
                  {s.icon}
                </div>
              </div>
            </Card>
          ))}
        </div>
      )}
    </>
  )
}
