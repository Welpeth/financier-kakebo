import PageHeader from '@/components/layout/PageHeader'
import TransactionList from '@/features/transactions/components/TransactionList'

export default function TransactionsPage() {
  return (
    <>
      <PageHeader title="Transações" />
      <TransactionList />
    </>
  )
}
