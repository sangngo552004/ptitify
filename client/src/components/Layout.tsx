import React from 'react';
import { Outlet } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { PlayerBar } from './PlayerBar';
import { Search, Bell, Settings, LogOut } from 'lucide-react';
import { useAuthStore } from '../store/useAuthStore';

export const Layout = () => {
  const { user, logout } = useAuthStore();

  return (
    <div className="flex h-screen w-full bg-background text-light-accent overflow-hidden font-sans">
      <Sidebar />
      <main className="flex-1 flex flex-col overflow-hidden relative">
        <header className="h-16 flex items-center justify-between px-8 bg-background/50 backdrop-blur-md border-b border-primary/5 shrink-0 z-20">
          <div className="flex items-center gap-4 w-96">
            <div className="relative w-full">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 size-5" />
              <input
                type="text"
                placeholder="Search artists, songs, or podcasts"
                className="w-full bg-surface/50 border-none rounded-full py-2 pl-10 pr-4 text-sm focus:ring-2 focus:ring-primary outline-none text-white placeholder-gray-500"
              />
            </div>
          </div>
          <div className="flex items-center gap-4">
            <button className="text-gray-400 hover:text-primary transition-colors">
              <Bell className="size-5" />
            </button>
            <button className="text-gray-400 hover:text-primary transition-colors">
              <Settings className="size-5" />
            </button>
            <div className="flex items-center gap-3 pl-4 border-l border-primary/20">
              <div className="flex-col items-end hidden sm:flex">
                <span className="text-sm font-bold text-white">{user?.name}</span>
                <span className="text-xs text-primary">Premium</span>
              </div>
              <div className="size-8 rounded-full bg-primary/20 border border-primary/40 overflow-hidden">
                <img
                  src={user?.avatar}
                  alt="User"
                  className="w-full h-full object-cover"
                />
              </div>
              <button onClick={logout} className="text-gray-400 hover:text-red-400 transition-colors ml-2" title="Logout">
                <LogOut className="size-5" />
              </button>
            </div>
          </div>
        </header>
        <div className="flex-1 overflow-y-auto custom-scrollbar pb-32">
          <Outlet />
        </div>
      </main>
      <PlayerBar />
    </div>
  );
};
