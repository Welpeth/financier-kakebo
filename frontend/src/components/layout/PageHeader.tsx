import type { ReactNode } from 'react'

interface PageHeaderProps {
  title: string
  action?: ReactNode
}

export default function PageHeader({ title, action }: PageHeaderProps) {
  return (
    <div className="flex items-center justify-between">
      <span className="inline-flex items-center rounded-full bg-[var(--primary)] px-4 py-1.5 text-sm font-semibold text-white">
        {title}
      </span>
      {action && <div>{action}</div>}
    </div>
  )
}
