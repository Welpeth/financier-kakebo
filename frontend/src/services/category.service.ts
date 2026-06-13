import api from '@/lib/api'
import type { Category, CreateCategoryRequest, UpdateCategoryRequest } from '@/models/models'

export const categoryService = {
  async findAll(): Promise<Category[]> {
    const { data } = await api.get<Category[]>('/category/list')
    return data
  },

  async findById(id: string): Promise<Category> {
    const { data } = await api.get<Category>(`/category/${id}`)
    return data
  },

  async create(payload: CreateCategoryRequest): Promise<Category> {
    const { data } = await api.post<Category>('/category', payload)
    return data
  },

  async update(payload: UpdateCategoryRequest): Promise<void> {
    await api.patch('/category', payload)
  },

  async remove(id: string): Promise<void> {
    await api.delete(`/category/${id}`)
  },
}
