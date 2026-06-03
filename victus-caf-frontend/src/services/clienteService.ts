import api from './api';

export interface ClienteMensual {
    id: number;
    numeroDocumento: number;
    nombreCompleto: string;
    correoElectronico: string;
    estadoMembresia: 'ACTIVO' | 'VENCIDO' | 'BLOQUEADO';
}

// Simulación: mientras el backend no funciona, usamos datos falsos
const clientesMock: ClienteMensual[] = [
    { id: 1, numeroDocumento: 12345678, nombreCompleto: 'Juan Pérez', correoElectronico: 'juan@mail.com', estadoMembresia: 'ACTIVO' },
    { id: 2, numeroDocumento: 87654321, nombreCompleto: 'María Gómez', correoElectronico: 'maria@mail.com', estadoMembresia: 'VENCIDO' },
];

export const obtenerClientesMensuales = async (): Promise<ClienteMensual[]> => {
    // TODO: cuando el backend funcione, descomentar:
    // const response = await api.get('/clientes/mensual/todos');
    // return response.data.data;
    return clientesMock; // simulación
};

export const registrarClienteMensual = async (cliente: Omit<ClienteMensual, 'id'>) => {
    // TODO: llamada real al backend
    console.log('Registrar cliente:', cliente);
    return { ...cliente, id: Math.random() };
};