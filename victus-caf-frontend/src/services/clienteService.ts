import api from './api';

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

function unwrap<T>(res: { data: ApiResponse<T> }): T {
  return res.data.data;
}

export function getApiErrorMessage(err: unknown, fallback: string): string {
  if (typeof err === 'object' && err !== null && 'response' in err) {
    const data = (err as { response?: { data?: { message?: string } } }).response?.data;
    if (data?.message) return data.message;
  }
  return fallback;
}

export interface ClienteBase {
  id: number;
  tipoDeDocumento: string;
  numeroDeDocumento: number;
  nombreCompleto: string;
  fechaDeNacimiento?: string;
  telefono: number;
  correoElectronico: string;
  estado?: boolean;
  tipoDeCliente?: string;
  estadoMembresia?: string;
  estadoContrato?: string;
}

export interface ClienteMensualDTO {
  tipoDeDocumento: string;
  numeroDeDocumento: number;
  nombreCompleto: string;
  fechaDeNacimiento: string;
  telefono: number;
  correoElectronico: string;
  contrasena: string;
  tipoMembresia: 'MENSUAL' | 'TRIMESTRAL' | 'SEMESTRAL' | 'ANUAL';
}

export interface ClienteDiarioDTO {
  tipoDeDocumento: string;
  numeroDeDocumento: number;
  nombreCompleto: string;
  fechaDeNacimiento: string;
  telefono: number;
  correoElectronico: string;
  contrasena: string;
}

export interface BeneficiarioEpsDTO {
  tipoDeDocumento: string;
  numeroDeDocumento: number;
  nombreCompleto: string;
  fechaDeNacimiento: string;
  telefono: number;
  correoElectronico: string;
  contrasena: string;
  tieneEntrenadorPermanente?: boolean;
  fechaFin: string;
  sesionesAutorizadas: number;
  medicoRemitente: string;
  entidadEps: string;
  diagnostico: string;
  zonaCuerpoTratar: string;
}

export interface ActualizarClienteDTO {
  nombreCompleto?: string;
  fechaDeNacimiento?: string;
  telefono?: number;
  correoElectronico?: string;
  contrasena?: string;
  tipoMembresia?: 'MENSUAL' | 'TRIMESTRAL' | 'SEMESTRAL' | 'ANUAL';
}

export const crearClienteMensual = (data: ClienteMensualDTO) =>
  api.post<ApiResponse<ClienteBase>>('/clientes/mensual', data).then(unwrap);

export const crearClienteDiario = (data: ClienteDiarioDTO) =>
  api.post<ApiResponse<ClienteBase>>('/clientes/diario', data).then(unwrap);

export const crearBeneficiarioEps = (data: BeneficiarioEpsDTO) =>
  api.post<ApiResponse<ClienteBase>>('/clientes/eps', data).then(unwrap);

export const listarMensualesActivos = () =>
  api.get<ApiResponse<ClienteBase[]>>('/clientes/mensuales/activos').then(unwrap);

export const listarMensualesTodos = () =>
  api.get<ApiResponse<ClienteBase[]>>('/clientes/mensuales/todos').then(unwrap);

export const listarDiariosActivos = () =>
  api.get<ApiResponse<ClienteBase[]>>('/clientes/diarios/activos').then(unwrap);

export const listarDiariosTodos = () =>
  api.get<ApiResponse<ClienteBase[]>>('/clientes/diarios/todos').then(unwrap);

export const listarEpsActivos = () =>
  api.get<ApiResponse<ClienteBase[]>>('/clientes/eps/activos').then(unwrap);

export const listarEpsTodos = () =>
  api.get<ApiResponse<ClienteBase[]>>('/clientes/eps/todos').then(unwrap);

export const buscarClienteMensual = (numeroDocumento: number) =>
  api.get<ApiResponse<ClienteBase>>(`/clientes/mensual/${numeroDocumento}`).then(unwrap);

export const buscarClienteDiario = (numeroDocumento: number) =>
  api.get<ApiResponse<ClienteBase>>(`/clientes/diario/${numeroDocumento}`).then(unwrap);

export const buscarBeneficiarioEps = (numeroDocumento: number) =>
  api.get<ApiResponse<ClienteBase>>(`/clientes/eps/${numeroDocumento}`).then(unwrap);

export const actualizarClienteMensual = (numeroDocumento: number, data: ActualizarClienteDTO) =>
  api.put<ApiResponse<ClienteBase>>(`/clientes/mensual/${numeroDocumento}`, data).then(unwrap);

export const actualizarClienteDiario = (numeroDocumento: number, data: ActualizarClienteDTO) =>
  api.put<ApiResponse<ClienteBase>>(`/clientes/diario/${numeroDocumento}`, data).then(unwrap);

export const actualizarBeneficiarioEps = (numeroDocumento: number, data: ActualizarClienteDTO) =>
  api.put<ApiResponse<ClienteBase>>(`/clientes/eps/${numeroDocumento}`, data).then(unwrap);

export const desactivarClienteMensual = (numeroDocumento: number) =>
  api.delete<ApiResponse<void>>(`/clientes/mensual/${numeroDocumento}`);

export const desactivarClienteDiario = (numeroDocumento: number) =>
  api.delete<ApiResponse<void>>(`/clientes/diario/${numeroDocumento}`);

export const desactivarBeneficiarioEps = (numeroDocumento: number) =>
  api.delete<ApiResponse<void>>(`/clientes/eps/${numeroDocumento}`);
