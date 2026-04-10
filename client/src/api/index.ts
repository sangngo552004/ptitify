const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/music-app/api';

export const getSongStreamUrl = (songId: number | string): string =>
  `${BASE_URL}/songs/${songId}/stream`;

export const getAccessToken = () => localStorage.getItem('accessToken');
export const getRefreshToken = () => localStorage.getItem('refreshToken');
export const setTokens = (access: string, refresh: string) => {
  localStorage.setItem('accessToken', access);
  localStorage.setItem('refreshToken', refresh);
};
export const clearTokens = () => {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
};

const redirectToLogin = () => {
  if (window.location.pathname !== '/login' && window.location.pathname !== '/register') {
    window.location.href = '/login';
  }
};

async function fetchWithAuth(endpoint: string, options: RequestInit = {}) {
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string>),
  };

  const token = getAccessToken();
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  let response = await fetch(`${BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (response.status === 401) {
    const refreshToken = getRefreshToken();
    if (refreshToken) {
      try {
        const refreshRes = await fetch(`${BASE_URL}/auth/refresh`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ refreshToken }),
        });

        if (refreshRes.ok) {
          const refreshData = await refreshRes.json();
          if (refreshData.data && refreshData.data.accessToken) {
            setTokens(refreshData.data.accessToken, refreshData.data.refreshToken || refreshToken);
            headers['Authorization'] = `Bearer ${refreshData.data.accessToken}`;
            response = await fetch(`${BASE_URL}${endpoint}`, {
              ...options,
              headers,
            });
          } else {
            clearTokens();
            redirectToLogin();
            return Promise.reject(new Error('Session expired'));
          }
        } else {
          clearTokens();
          redirectToLogin();
          return Promise.reject(new Error('Session expired'));
        }
      } catch (error) {
        clearTokens();
        redirectToLogin();
        return Promise.reject(error);
      }
    } else {
      clearTokens();
      redirectToLogin();
      return Promise.reject(new Error('Session expired'));
    }
  }

  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.message || 'An error occurred');
  }

  return data;
}

export const api = {
  auth: {
    login: async (email: string, password: string) => {
      const res = await fetch(`${BASE_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ emailOrUsername: email, password }),
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.message || 'Login failed');
      return data;
    },
    register: async (email: string, password: string, username: string, fullName: string) => {
      const res = await fetch(`${BASE_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password, username, fullName }),
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.message || 'Registration failed');
      return data;
    },
    logout: async () => {
      const refreshToken = getRefreshToken();
      if (refreshToken) {
        try {
          await fetch(`${BASE_URL}/auth/logout`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken }),
          });
        } catch (error) {
          console.error('Logout error', error);
        }
      }
      clearTokens();
    }
  },

  users: {
    getProfile: () => fetchWithAuth('/users/me'),
    updateProfile: (data: { fullName?: string, newPassword?: string }) => 
      fetchWithAuth('/users/me', {
        method: 'PUT',
        body: JSON.stringify(data),
      }),
  },

  genres: {
    getAll: () => fetchWithAuth('/genres'),
    getSongs: (id: string, limit = 20, cursor?: string) => {
      let url = `/genres/${id}/songs?limit=${limit}`;
      if (cursor) url += `&cursor=${cursor}`;
      return fetchWithAuth(url);
    }
  },

  artists: {
    getAll: () => fetchWithAuth('/artists'),
    getSongs: (id: string, limit = 20, cursor?: string) => {
      let url = `/artists/${id}/songs?limit=${limit}`;
      if (cursor) url += `&cursor=${cursor}`;
      return fetchWithAuth(url);
    }
  },

  favorites: {
    toggle: (songId: string) => fetchWithAuth(`/users/favorites/${songId}`, { method: 'POST' }),
    getAll: (limit = 20, cursor?: string) => {
      let url = `/users/favorites?limit=${limit}`;
      if (cursor) url += `&cursor=${cursor}`;
      return fetchWithAuth(url);
    }
  },

  search: {
    global: (query: string, limit = 10) => fetchWithAuth(`/search?q=${encodeURIComponent(query)}&limit=${limit}`)
  }
};
