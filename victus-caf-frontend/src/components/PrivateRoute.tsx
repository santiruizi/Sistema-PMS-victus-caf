import { Navigate } from 'react-router-dom';

interface PrivateRouteProps {
  children: JSX.Element;
  allowedRoles: string[];
}

export const PrivateRoute = ({ children, allowedRoles }: PrivateRouteProps) => {
  const userStr = localStorage.getItem('user');
  if (!userStr) {
    return <Navigate to="/login" />;
  }
  const user = JSON.parse(userStr);
  if (!allowedRoles.includes(user.rol)) {
    return <Navigate to="/login" />;
  }
  return children;
};