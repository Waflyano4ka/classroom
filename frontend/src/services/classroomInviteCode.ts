import api from '@/services/api'
import { API_PATHS } from "@/constants/apiPaths.ts";

export const getInviteCode = async (): Promise<string> => {
  const { data } = await api.get<{ inviteCode: string }>(API_PATHS.CLASSROOMS.INVITE_CODE)
  return data.inviteCode
}
