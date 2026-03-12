import React from 'react';
import { Play } from 'lucide-react';
import { playlists, albums, songs } from '../data/mockData';
import { usePlayerStore } from '../store/usePlayerStore';

export const Home = () => {
  const { playSong } = usePlayerStore();

  return (
    <div className="p-8">
      {/* Welcome Section */}
      <section className="mb-10">
        <h2 className="text-4xl font-black text-white mb-2 tracking-tight">Welcome Back</h2>
        <p className="text-gray-400">Your daily blend of coffee and melodies is ready.</p>
      </section>

      {/* Featured Playlists Grid */}
      <section className="mb-12">
        <h3 className="text-xl font-bold mb-6 flex items-center gap-2 text-white">
          <span className="text-primary">✨</span>
          Featured Playlists
        </h3>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4">
          {playlists.map((playlist) => (
            <div
              key={playlist.id}
              className="group relative overflow-hidden rounded-xl aspect-square bg-surface flex items-end p-4 transition-transform hover:-translate-y-1 cursor-pointer"
            >
              <div
                className="absolute inset-0 bg-cover bg-center transition-transform group-hover:scale-110"
                style={{
                  backgroundImage: `linear-gradient(to top, rgba(43, 43, 43, 0.9), transparent), url(${playlist.coverImage})`,
                }}
              />
              <div className="relative z-10">
                <p className="text-white font-bold">{playlist.title}</p>
                <p className="text-primary text-xs font-medium truncate">{playlist.description}</p>
              </div>
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  // Play first song of playlist
                  const firstSong = songs.find((s) => s.id === playlist.songs[0]);
                  if (firstSong) {
                    const queue = playlist.songs.map((id) => songs.find((s) => s.id === id)!).filter(Boolean);
                    playSong(firstSong, queue);
                  }
                }}
                className="absolute bottom-4 right-4 size-10 rounded-full bg-primary text-background opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center shadow-lg hover:scale-105"
              >
                <Play className="size-5 fill-current ml-1" />
              </button>
            </div>
          ))}
        </div>
      </section>

      {/* Recommended for You */}
      <section className="mb-12">
        <div className="flex items-center justify-between mb-6">
          <h3 className="text-xl font-bold text-white">Recommended for You</h3>
          <button className="text-sm font-bold text-primary hover:underline uppercase tracking-widest">
            See All
          </button>
        </div>
        <div className="flex gap-6 overflow-x-auto pb-4 custom-scrollbar">
          {albums.map((album) => (
            <div key={album.id} className="flex-shrink-0 w-44 group cursor-pointer">
              <div className="relative mb-3 aspect-square rounded-lg overflow-hidden bg-surface shadow-xl">
                <div
                  className="absolute inset-0 bg-cover bg-center group-hover:scale-105 transition-transform"
                  style={{ backgroundImage: `url(${album.coverImage})` }}
                />
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    const albumSongs = songs.filter((s) => s.albumId === album.id);
                    if (albumSongs.length > 0) {
                      playSong(albumSongs[0], albumSongs);
                    }
                  }}
                  className="absolute bottom-2 right-2 size-10 rounded-full bg-primary text-background opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center shadow-lg hover:scale-105"
                >
                  <Play className="size-5 fill-current ml-1" />
                </button>
              </div>
              <p className="font-bold text-white truncate">{album.title}</p>
              <p className="text-sm text-gray-400 truncate">{album.artist}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Recently Played */}
      <section>
        <h3 className="text-xl font-bold mb-6 text-white">Recently Played</h3>
        <div className="flex flex-col bg-surface/30 rounded-2xl overflow-hidden divide-y divide-primary/5">
          {songs.slice(0, 3).map((song) => (
            <div
              key={song.id}
              onClick={() => playSong(song, songs)}
              className="flex items-center justify-between p-3 hover:bg-primary/5 group cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-4">
                <div
                  className="size-12 rounded bg-surface bg-cover bg-center"
                  style={{ backgroundImage: `url(${song.coverImage})` }}
                />
                <div>
                  <p className="font-bold text-white group-hover:text-primary transition-colors">{song.title}</p>
                  <p className="text-xs text-gray-400">{song.artist}</p>
                </div>
              </div>
              <div className="flex items-center gap-8">
                <p className="text-sm text-gray-400">
                  {Math.floor(song.duration / 60)}:{(song.duration % 60).toString().padStart(2, '0')}
                </p>
                <div className="flex gap-4 opacity-0 group-hover:opacity-100 transition-opacity">
                  <button className="text-gray-400 hover:text-primary">
                    <svg className="size-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                    </svg>
                  </button>
                  <button className="text-gray-400 hover:text-primary">
                    <svg className="size-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};
