export interface CurrentUser {
  id: string
  username: string
  email: string
  lastName: string | null
  firstName: string | null
  middleName: string | null
  accountStatusName: string
  createdAt: string
}
