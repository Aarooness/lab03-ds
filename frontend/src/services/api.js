const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export async function apiFetch(path, options = {}) {
  const token = localStorage.getItem('lab-token');
  const headers = { ...(options.headers || {}) };

  if (!(options.body instanceof FormData) && !headers['Content-Type']) {
    headers['Content-Type'] = 'application/json';
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers,
  });

  const text = await response.text();
  const payload = text ? JSON.parse(text) : null;

  if (!response.ok) {
    const message = payload?.message || payload?.error || 'Ocurrió un error en la solicitud';
    throw new Error(message);
  }

  return payload;
}
