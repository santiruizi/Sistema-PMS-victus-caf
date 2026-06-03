import axios from 'axios';

export const SERVER_UNREACHABLE_MESSAGE =
  'No se pudo conectar con el servidor. Verifica que MariaDB y el backend estén activos (puerto 8080).';

export function isServerUnreachable(err: unknown): boolean {
  return axios.isAxiosError(err) && !err.response;
}
