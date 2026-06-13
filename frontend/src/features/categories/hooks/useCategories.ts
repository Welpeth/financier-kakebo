'use client'

import { useState, useEffect, useCallback } from 'react'
import { categoryService } from '@/services/category.service'
import type { Category, CreateCategoryRequest, UpdateCategoryRequest } from '@/models/models'

export function useCategories() {
  const [categories, setCategories] = useState<Category[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetch = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await categoryService.findAll()
      setCategories(data)
    } catch {
      setError('Erro ao carregar categorias.')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetch() }, [fetch])

  const create = useCallback(async (payload: CreateCategoryRequest) => {
    await categoryService.create(payload)
    await fetch()
  }, [fetch])

  const update = useCallback(async (payload: UpdateCategoryRequest) => {
    await categoryService.update(payload)
    await fetch()
  }, [fetch])

  const remove = useCallback(async (id: string) => {
    await categoryService.remove(id)
    await fetch()
  }, [fetch])

  return { categories, loading, error, create, update, remove }
}
