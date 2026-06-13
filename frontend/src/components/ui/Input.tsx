import type { InputHTMLAttributes } from 'react'

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string
  error?: string
  disabled?: boolean
}

export default function Input({ label, error, disabled, className = '', id, ...props }: InputProps) {
  return (
    <div className="flex flex-col gap-1">
      {label && (
        <label htmlFor={id} className="text-sm font-medium text-[var(--text)]">
          {label}
        </label>
      )}
      <input
        id={id}
        className={`w-full rounded-lg border px-3 py-2 text-sm outline-none transition-colors
          border-[var(--border)] bg-[var(--card)] text-[var(--text)] placeholder:text-[var(--muted)]
          focus:border-[var(--primary)] focus:ring-2 focus:ring-[var(--primary-light)]
          disabled:opacity-50 ${error ? 'border-[var(--danger)]' : ''} ${className}`}
          disabled={disabled}
        {...props}
      />
      {error && <p className="text-xs text-[var(--danger)]">{error}</p>}
    </div>
  )
}
