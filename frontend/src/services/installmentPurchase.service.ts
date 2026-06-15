import api from '@/lib/api'
import type { InstallmentPurchase } from '@/models/models'

export const installmentPurchaseService = {
  async findByTransaction(transactionId: string): Promise<InstallmentPurchase[]> {
    const { data } = await api.get<InstallmentPurchase[]>(`/installment-purchase/by-transaction/${transactionId}`)
    return data
  },
}
