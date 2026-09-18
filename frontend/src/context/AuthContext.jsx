import { createContext, useContext, useMemo, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem('lab-user');
    return storedUser ? JSON.parse(storedUser) : null;
  });

  const [token, setToken] = useState(() => localStorage.getItem('lab-token'));

  const login = (authData) => {
    const userData = {
      email: authData.email,
      rol: authData.rol,
    };

    setUser(userData);
    setToken(authData.token);
    localStorage.setItem('lab-user', JSON.stringify(userData));
    localStorage.setItem('lab-token', authData.token);
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('lab-user');
    localStorage.removeItem('lab-token');
  };

  const value = useMemo(
    () => ({
      user,
      token,
      isAuthenticated: Boolean(token),
      login,
      logout,
    }),
    [user, token],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }

  return context;
}
