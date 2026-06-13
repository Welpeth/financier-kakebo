import type { ReactNode } from 'react'

interface TableProps { children: ReactNode; className?: string }
interface ThProps { children: ReactNode; className?: string }
interface TdProps { children: ReactNode; className?: string; colSpan?: number }
interface TrProps { children: ReactNode; className?: string; onClick?: () => void }

function TableRoot({ children, className = '' }: TableProps) {
  return (
    <div className={`w-full overflow-hidden rounded-xl border border-[var(--border)] bg-[var(--card)] ${className}`}>
      <table className="w-full text-sm">
        {children}
      </table>
    </div>
  )
}

function Head({ children }: { children: ReactNode }) {
  return (
    <thead>
      <tr className="bg-[var(--primary)]">
        {children}
      </tr>
    </thead>
  )
}

function Th({ children, className = '' }: ThProps) {
  return (
    <th className={`px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-white first:rounded-tl-xl last:rounded-tr-xl ${className}`}>
      {children}
    </th>
  )
}

function Body({ children }: { children: ReactNode }) {
  return <tbody className="divide-y divide-[var(--border)]">{children}</tbody>
}

function Tr({ children, className = '', onClick }: TrProps) {
  return (
    <tr
      className={`transition-colors hover:bg-[var(--surface)] ${onClick ? 'cursor-pointer' : ''} ${className}`}
      onClick={onClick}
    >
      {children}
    </tr>
  )
}

function Td({ children, className = '', colSpan }: TdProps) {
  return (
    <td className={`px-4 py-3 text-[var(--text)] ${className}`} colSpan={colSpan}>
      {children}
    </td>
  )
}

const Table = Object.assign(TableRoot, { Head, Th, Body, Tr, Td })
export default Table
