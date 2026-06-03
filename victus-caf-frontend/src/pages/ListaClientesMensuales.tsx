import { useEffect, useState } from 'react';
import { obtenerClientesMensuales, ClienteMensual } from '../services/clienteService';

export default function ListaClientesMensuales() {
    const [clientes, setClientes] = useState<ClienteMensual[]>([]);
    const [cargando, setCargando] = useState(true);

    useEffect(() => {
        obtenerClientesMensuales().then((data) => {
            setClientes(data);
            setCargando(false);
        });
    }, []);

    if (cargando) return <div>Cargando clientes...</div>;

    return (
        <div>
            <h2>Clientes Particulares Mensuales</h2>
    <table border={1} cellPadding={8} style={{ width: '100%', borderCollapse: 'collapse' }}>
    <thead>
        <tr>
            <th>Documento</th>
    <th>Nombre</th>
    <th>Correo</th>
    <th>Estado</th>
    </tr>
    </thead>
    <tbody>
    {clientes.map((cliente) => (
            <tr key={cliente.id}>
                <td>{cliente.numeroDocumento}</td>
                <td>{cliente.nombreCompleto}</td>
                <td>{cliente.correoElectronico}</td>
                <td>{cliente.estadoMembresia}</td>
                </tr>
        ))}
    </tbody>
    </table>
    </div>
);
}