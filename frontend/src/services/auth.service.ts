import api from '@/lib/api'

const TOKEN_KEY = 'kakebo:token'

interface LoginCredentials {
  email: string
  password: string
}

interface LoginResponse {
  token: string
}

export const authService = {
  async login(credentials: LoginCredentials): Promise<LoginResponse> {
    const { data } = await api.post<LoginResponse>('/auth/login', credentials)
    localStorage.setItem(TOKEN_KEY, data.token)
    return data
  },

  async register(credentials: LoginCredentials): Promise<void> {
    await api.post('/auth/register', credentials)
  },

  logout(): void {
    localStorage.removeItem(TOKEN_KEY)
  },

  getToken(): string | null {
    if (typeof window === 'undefined') return null
    return localStorage.getItem(TOKEN_KEY)
  },

  isAuthenticated(): boolean {
    return !!this.getToken()
  },
}
