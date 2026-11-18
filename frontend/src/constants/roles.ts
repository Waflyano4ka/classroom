export const ROLES = {
  CREATOR: 'CREATOR',
  TEACHER: 'TEACHER',
  STUDENT: 'STUDENT'
} as const

export function isCreator(role?: string | null): boolean {
  return role === ROLES.CREATOR
}


