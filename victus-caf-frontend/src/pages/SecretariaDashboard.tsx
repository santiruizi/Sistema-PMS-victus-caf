export default function SecretariaDashboard() {
  const logout = () => {
    localStorage.clear();
    window.location.href = '/login';
  };
  return (
    <div>
      <h1>Panel de Secretaria</h1>
      <button onClick={logout}>Cerrar sesión</button>
      <p>Aquí podrás registrar ingresos, pagos, etc.</p>
    </div>
  );
}