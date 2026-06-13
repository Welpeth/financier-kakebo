interface BadgeProps {
  children: React.ReactNode
  variant?: 'success' | 'danger' | 'warning' | 'neutral' | 'primary'
  className?: string
}

const variants = {
  primary: 'bg-[var(--primary)] text-white',
  success: 'bg-[var(--success-light)] text-[var(--success)]',
  danger: 'bg-[var(--danger-light)] text-[var(--danger)]',
  warning: 'bg-[var(--warning-light)] text-[var(--warning)]',
  neutral: 'bg-[var(--surface)] text-[var(--muted)] border border-[var(--border)]',
}

export default function Badge({ children, variant = 'neutral', className = '' }: BadgeProps) {
  return (
    <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${variants[variant]} ${className}`}>
      {children}
    </span>
  )
}
