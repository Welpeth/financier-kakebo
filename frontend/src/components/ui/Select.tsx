import type { SelectHTMLAttributes } from 'react'

interface SelectOption {
  label: string
  value: string | number
}

interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label?: string
  options: SelectOption[]
  placeholder?: string
  error?: string
  disabled?: boolean
}

export default function Select({ label, options, placeholder, error, disabled, className = '', id, ...props }: SelectProps) {
  return (
    <div className="flex flex-col gap-1">
      {label && (
        <label htmlFor={id} className="text-sm font-medium text-[var(--text)]">
          {label}
        </label>
      )}
      <select
        id={id}
        className={`w-full rounded-lg border px-3 py-2 text-sm outline-none transition-colors
          border-[var(--border)] bg-[var(--card)] text-[var(--text)]
          focus:border-[var(--primary)] focus:ring-2 focus:ring-[var(--primary-light)]
          disabled:opacity-50 ${error ? 'border-[var(--danger)]' : ''} ${className}`}
        disabled={disabled}
        {...props}
      >
        {placeholder && <option value="">{placeholder}</option>}
        {options.map((opt) => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>
      {error && <p className="text-xs text-[var(--danger)]">{error}</p>}
    </div>
  )
}
