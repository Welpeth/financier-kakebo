interface CardProps {
  children: React.ReactNode
  className?: string
  padding?: boolean
}

export default function Card({ children, className = '', padding = true }: CardProps) {
  return (
    <div className={`rounded-xl bg-[var(--card)] border border-[var(--border)] shadow-sm ${padding ? 'p-6' : ''} ${className}`}>
      {children}
    </div>
  )
}
