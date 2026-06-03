import api from './api';

export interface UsuarioSistema {
  idUsuarioSistema: number;
  tipoDeDocumento: string;
  numeroDeDocumento: number;
  nombreCompleto: string;
  telefono: number;
  correoElectronico: string;
  rol: 'ADMINISTRADOR' | 'ENTRENADOR' | 'SECRETARIA';
  estado: boolean;
  contrasena?: string;
}

export const listarUsuarios = () => api.get<UsuarioSistema[]>('/usuarios-sistema');
export const crearUsuario = (data: Omit<UsuarioSistema, 'idUsuarioSistema'>) =>
  api.post<UsuarioSistema>('/usuarios-sistema', data);
export const actualizarUsuario = (id: number, data: Partial<UsuarioSistema>) =>
  api.put<UsuarioSistema>(`/usuarios-sistema/${id}`, data);
export const desactivarUsuario = (id: number) => api.delete(`/usuarios-sistema/${id}`);