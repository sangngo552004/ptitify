import { create } from 'zustand';
import { api, setTokens, clearTokens } from '../api';

export interface User {
  id: string;
  email: string;
  username: string;
  fullName: string;
  avatarUrl?: string;
}

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, password: string, username: string, fullName: string) => Promise<void>;
  logout: () => Promise<void>;
  initAuth: () => Promise<void>;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,
  isLoading: true,
  login: async (email, password) => {
    const response = await api.auth.login(email, password);
    setTokens(response.data.accessToken, response.data.refreshToken);
    set({
      user: response.data.user,
      isAuthenticated: true
    });
  },
  register: async (email, password, username, fullName) => {
    const response = await api.auth.register(email, password, username, fullName);
  },
  logout: async () => {
    await api.auth.logout();
    set({ user: null, isAuthenticated: false });
  },
  initAuth: async () => {
    try {
      set({ isLoading: true });
      const response = await api.users.getProfile();
      set({
        user: response.data,
        isAuthenticated: true,
        isLoading: false
      });
    } catch (err) {
      clearTokens();
      set({
        user: null,
        isAuthenticated: false,
        isLoading: false
      });
    }
  }
}));
