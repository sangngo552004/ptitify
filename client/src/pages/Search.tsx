import React from 'react';
import { Play, X } from 'lucide-react';
import { genres, albums } from '../data/mockData';

export const Search = () => {
  return (
    <div className="p-8">
      {/* Recent Searches */}
      <section className="mb-10">
        <h2 className="text-xl font-bold mb-4 text-white">Recent Searches</h2>
        <div className="flex flex-wrap gap-2">
          {['Lofi Morning Coffee', 'Jazz Classics', 'Bossa Nova Sundays', 'Acoustic Hits'].map((term) => (
            <div
              key={term}
              className="group flex items-center gap-2 px-4 py-2 bg-surface/50 hover:bg-primary/20 rounded-full cursor-pointer transition-colors border border-transparent hover:border-primary/30"
            >
              <span className="text-sm font-medium text-white">{term}</span>
              <X className="size-4 text-gray-400 group-hover:text-primary" />
            </div>
          ))}
        </div>
      </section>

      {/* Browse All Grid */}
      <section>
        <h2 className="text-xl font-bold mb-6 text-white">Browse All</h2>
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
          {genres.map((genre) => (
            <div
              key={genre.id}
              className="aspect-square relative rounded-xl overflow-hidden group cursor-pointer bg-surface"
            >
              <div className={`absolute inset-0 bg-gradient-to-br ${genre.color} opacity-40 z-0`} />
              <img
                src={genre.image}
                alt={genre.name}
                className="absolute inset-0 w-full h-full object-cover mix-blend-overlay opacity-60 group-hover:scale-110 transition-transform duration-500"
              />
              <div className="relative z-10 p-4 h-full flex flex-col justify-between">
                <h3 className="text-lg font-bold text-white">{genre.name}</h3>
                <div className="flex justify-end translate-y-4 opacity-0 group-hover:translate-y-0 group-hover:opacity-100 transition-all">
                  <div className="size-10 bg-primary rounded-full flex items-center justify-center shadow-lg">
                    <Play className="size-5 fill-current text-background ml-1" />
                  </div>
                </div>
              </div>
            </div>
          ))}
          {/* Add some extra cards to fill the grid based on albums */}
          {albums.map((album) => (
            <div
              key={album.id}
              className="aspect-square relative rounded-xl overflow-hidden group cursor-pointer bg-surface"
            >
              <div className="absolute inset-0 bg-gradient-to-br from-primary/40 to-black/60 z-0" />
              <img
                src={album.coverImage}
                alt={album.title}
                className="absolute inset-0 w-full h-full object-cover mix-blend-overlay opacity-60 group-hover:scale-110 transition-transform duration-500"
              />
              <div className="relative z-10 p-4 h-full flex flex-col justify-between">
                <h3 className="text-lg font-bold text-white">{album.title}</h3>
                <div className="flex justify-end translate-y-4 opacity-0 group-hover:translate-y-0 group-hover:opacity-100 transition-all">
                  <div className="size-10 bg-primary rounded-full flex items-center justify-center shadow-lg">
                    <Play className="size-5 fill-current text-background ml-1" />
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};
