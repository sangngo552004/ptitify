import React from 'react';
import { Play } from 'lucide-react';
import { playlists, songs } from '../data/mockData';
import { usePlayerStore } from '../store/usePlayerStore';

export const Library = () => {
  const { playSong } = usePlayerStore();

  return (
    <div className="p-8">
      <header className="flex flex-col md:flex-row items-end gap-8 mb-10">
        <div className="size-64 rounded-xl shadow-2xl bg-surface overflow-hidden shrink-0">
          <img
            src={playlists[0].coverImage}
            alt={playlists[0].title}
            className="w-full h-full object-cover"
          />
        </div>
        <div className="flex flex-col gap-2">
          <span className="text-xs font-bold uppercase tracking-widest text-primary">Public Playlist</span>
          <h2 className="text-6xl font-black tracking-tighter text-white">{playlists[0].title}</h2>
          <p className="text-gray-400 mt-2 text-lg">{playlists[0].description}</p>
          <div className="flex items-center gap-2 mt-4 text-sm font-medium">
            <span className="font-bold text-white">Coffee Beats</span>
            <span className="text-gray-400">• {playlists[0].songs.length} songs, 2 hr 45 min</span>
          </div>
          <div className="flex items-center gap-4 mt-6">
            <button
              onClick={() => {
                const firstSong = songs.find((s) => s.id === playlists[0].songs[0]);
                if (firstSong) {
                  const queue = playlists[0].songs.map((id) => songs.find((s) => s.id === id)!).filter(Boolean);
                  playSong(firstSong, queue);
                }
              }}
              className="flex items-center gap-2 px-8 py-3 bg-primary text-background rounded-full font-bold hover:scale-105 transition-transform"
            >
              <Play className="size-5 fill-current ml-1" />
              Play
            </button>
            <button className="size-12 rounded-full border border-primary/20 flex items-center justify-center hover:bg-primary/10 transition-colors text-primary">
              <svg className="size-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
              </svg>
            </button>
            <button className="size-12 rounded-full flex items-center justify-center text-gray-400 hover:text-white transition-colors">
              <svg className="size-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z" />
              </svg>
            </button>
          </div>
        </div>
      </header>

      <div className="bg-background/20 rounded-xl overflow-hidden backdrop-blur-sm">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="border-b border-primary/10 text-gray-400 text-xs uppercase tracking-widest font-bold">
              <th className="px-6 py-4 w-12 text-center">#</th>
              <th className="px-4 py-4">Title</th>
              <th className="px-4 py-4 hidden md:table-cell">Album</th>
              <th className="px-4 py-4 text-right pr-12">
                <svg className="size-4 inline-block" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </th>
            </tr>
          </thead>
          <tbody className="text-sm">
            {playlists[0].songs.map((songId, index) => {
              const song = songs.find((s) => s.id === songId);
              if (!song) return null;
              return (
                <tr
                  key={song.id}
                  onClick={() => {
                    const queue = playlists[0].songs.map((id) => songs.find((s) => s.id === id)!).filter(Boolean);
                    playSong(song, queue);
                  }}
                  className="group hover:bg-primary/5 transition-colors cursor-pointer"
                >
                  <td className="px-6 py-3 text-center text-gray-400 group-hover:text-primary">{index + 1}</td>
                  <td className="px-4 py-3">
                    <div className="flex items-center gap-4">
                      <div className="size-10 rounded bg-surface overflow-hidden shrink-0">
                        <img src={song.coverImage} alt={song.title} className="w-full h-full object-cover" />
                      </div>
                      <div>
                        <p className="font-bold text-white group-hover:text-primary transition-colors">{song.title}</p>
                        <p className="text-xs text-gray-400">{song.artist}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-4 py-3 text-gray-400 hidden md:table-cell">{song.album}</td>
                  <td className="px-4 py-3 text-right pr-12 text-gray-400">
                    {Math.floor(song.duration / 60)}:{(song.duration % 60).toString().padStart(2, '0')}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};
