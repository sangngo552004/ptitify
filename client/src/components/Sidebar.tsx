import React from 'react';
import { NavLink } from 'react-router-dom';
import { Home, Compass, Search, Library, PlusSquare, Heart, Coffee } from 'lucide-react';
import clsx from 'clsx';
import { useAuthStore } from '../store/useAuthStore';

export const Sidebar = () => {
  const { user } = useAuthStore();
  
  const navItems = [
    { icon: Home, label: 'Home', path: '/' },
    { icon: Compass, label: 'Discover', path: '/discover' },
    { icon: Search, label: 'Search', path: '/search' },
    { icon: Library, label: 'Library', path: '/library' },
  ];

  return (
    <aside className="w-64 flex-shrink-0 border-r border-primary/10 bg-background flex flex-col justify-between p-6 hidden md:flex">
      <div className="flex flex-col gap-8">
        <div className="flex items-center gap-3">
          <div className="size-10 rounded-full bg-primary flex items-center justify-center text-background">
            <Coffee className="size-6" />
          </div>
          <div className="flex flex-col">
            <h1 className="text-white text-base font-bold leading-none">{user?.name || 'Coffee Lover'}</h1>
            <p className="text-primary text-xs font-medium uppercase tracking-wider">Premium</p>
          </div>
        </div>

        <nav className="flex flex-col gap-2">
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                clsx(
                  'flex items-center gap-3 px-3 py-2.5 rounded-xl font-semibold transition-colors',
                  isActive
                    ? 'bg-primary text-background'
                    : 'text-gray-400 hover:bg-primary/10 hover:text-primary'
                )
              }
            >
              <item.icon className="size-5" />
              <span>{item.label}</span>
            </NavLink>
          ))}
        </nav>

        <div className="flex flex-col gap-4">
          <h3 className="px-3 text-xs font-bold uppercase tracking-widest text-gray-500">Your Playlists</h3>
          <div className="flex flex-col gap-1">
            <button className="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 hover:text-primary transition-colors">
              <PlusSquare className="size-5" />
              <span className="text-sm font-medium">Create Playlist</span>
            </button>
            <button className="flex items-center gap-3 px-3 py-2 rounded-lg text-gray-400 hover:text-primary transition-colors">
              <Heart className="size-5" />
              <span className="text-sm font-medium">Liked Songs</span>
            </button>
          </div>
        </div>
      </div>

      <div className="bg-primary/10 rounded-xl p-4 flex flex-col gap-3">
        <p className="text-sm font-medium text-gray-300">Upgrade to unlock spatial audio and offline listening.</p>
        <button className="w-full py-2 bg-primary text-background text-sm font-bold rounded-lg hover:brightness-110 transition-all">
          Go Pro
        </button>
      </div>
    </aside>
  );
};
