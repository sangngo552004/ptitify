import { create } from 'zustand';

interface User {
  name: string;
  email: string;
  avatar: string;
}

interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  login: (email: string) => void;
  register: (name: string, email: string) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,
  login: (email) => set({
    user: { 
      name: email.split('@')[0], 
      email, 
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=100&h=100' 
    },
    isAuthenticated: true
  }),
  register: (name, email) => set({
    user: { 
      name, 
      email, 
      avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=100&h=100' 
    },
    isAuthenticated: true
  }),
  logout: () => set({ user: null, isAuthenticated: false }),
}));
