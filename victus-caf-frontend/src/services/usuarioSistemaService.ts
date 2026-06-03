import api from './api';

// Esta interfaz debe ser exportada
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

export type UsuarioSistemaInput = {
  tipoDeDocumento: string;
  numeroDeDocumento: number;
  nombreCompleto: string;
  telefono: number;
  correoElectronico: string;
  rol: UsuarioSistema['rol'];
  contrasena?: string;
};

export const listarUsuarios = () => api.get<UsuarioSistema[]>('/usuarios-sistema');
export const buscarUsuarioPorDocumento = (documento: number) =>
  api.get<UsuarioSistema>('/usuarios-sistema/buscar', { params: { documento } });
export const crearUsuario = (data: UsuarioSistemaInput & { contrasena: string }) =>
  api.post<UsuarioSistema>('/usuarios-sistema', data);
export const actualizarUsuario = (id: number, data: UsuarioSistemaInput) =>
  api.put<UsuarioSistema>(`/usuarios-sistema/${id}`, data);
export const desactivarUsuario = (id: number) => api.delete(`/usuarios-sistema/${id}`);
export const reactivarUsuario = (id: number) =>
  api.patch<UsuarioSistema>(`/usuarios-sistema/${id}/reactivar`);