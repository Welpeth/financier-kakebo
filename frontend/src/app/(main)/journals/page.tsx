import PageHeader from '@/components/layout/PageHeader'
import JournalList from '@/features/journals/components/JournalList'

export default function JournalsPage() {
  return (
    <>
      <PageHeader title="Diários" />
      <JournalList />
    </>
  )
}
