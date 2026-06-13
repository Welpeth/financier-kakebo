import PageHeader from '@/components/layout/PageHeader'
import AccountList from '@/features/accounts/components/AccountList'

export default function AccountsPage() {
  return (
    <>
      <PageHeader title="Contas" />
      <AccountList />
    </>
  )
}
