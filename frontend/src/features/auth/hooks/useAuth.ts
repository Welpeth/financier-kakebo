'use client'

import { useState, useCallback } from 'react'
import { useRouter } from 'next/navigation'
import { authService } from '@/services/auth.service'

export function useAuth() {
  const router = useRouter()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const login = useCallback(async (email: string, password: string) => {
    setLoading(true)
    setError(null)
    try {
      await authService.login({ email, password })
      router.push('/dashboard')
    } catch {
      setError('E-mail ou senha inválidos.')
    } finally {
      setLoading(false)
    }
  }, [router])

  const logout = useCallback(() => {
    authService.logout()
    router.push('/login')
  }, [router])

  return { login, logout, loading, error, isAuthenticated: authService.isAuthenticated() }
}
