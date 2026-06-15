import api from '@/lib/api'
import type { Installment } from '@/models/models'

export const installmentService = {
  async findByPurchase(installmentPurchaseId: string): Promise<Installment[]> {
    const { data } = await api.get<Installment[]>(`/installment/by-purchase/${installmentPurchaseId}`)
    return data
  },

  async findByCard(cardId: string): Promise<Installment[]> {
    const { data } = await api.get<Installment[]>(`/installment/by-card/${cardId}`)
    return data
  },

  async pay(id: string): Promise<void> {
    await api.patch(`/installment/${id}/pay`)
  },
}
