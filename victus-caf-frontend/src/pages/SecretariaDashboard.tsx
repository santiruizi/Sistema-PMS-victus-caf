import Layout from '../components/Layout';

export default function SecretariaDashboard() {
  return (
    <Layout title="Panel de Secretaria">
      <div className="bg-white p-8 rounded-lg shadow-sm border border-gray-200">
        <h2 className="text-xl font-semibold mb-4 text-gray-800">Bienvenida</h2>
        <p className="text-gray-600">Aquí podrás registrar ingresos, pagos y gestionar a los clientes particulares y de EPS.</p>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
          <div className="bg-blue-50 p-6 rounded-lg border border-blue-100">
            <h3 className="font-bold text-blue-800 mb-2">Ingresos Rápidos</h3>
            <p className="text-sm text-blue-600">Registra la entrada de los usuarios al CAF.</p>
          </div>
          <div className="bg-green-50 p-6 rounded-lg border border-green-100">
            <h3 className="font-bold text-green-800 mb-2">Clientes Nuevos</h3>
            <p className="text-sm text-green-600">Inscribe nuevos clientes al sistema.</p>
          </div>
          <div className="bg-purple-50 p-6 rounded-lg border border-purple-100">
            <h3 className="font-bold text-purple-800 mb-2">Pagos Mensuales</h3>
            <p className="text-sm text-purple-600">Registra renovaciones y pagos de membresías.</p>
          </div>
        </div>
      </div>
    </Layout>
  );
}