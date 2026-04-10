import React, { useEffect, useState } from 'react';
import { Play, ChevronLeft, ChevronRight } from 'lucide-react';
import { usePlayerStore, Song, ApiGenre, ApiArtist } from '../store/usePlayerStore';
import { api } from '../api';

export const Discover = () => {
  const { playSong } = usePlayerStore();
  const [topArtist, setTopArtist] = useState<ApiArtist | null>(null);
  const [topSongs, setTopSongs] = useState<Song[]>([]);
  const [newReleases, setNewReleases] = useState<Song[]>([]);
  const [genres, setGenres] = useState<ApiGenre[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [artistsRes, genresRes] = await Promise.all([
          api.artists.getAll(),
          api.genres.getAll()
        ]);
        
        const fetchedArtists = artistsRes.data || [];
        const fetchedGenres = genresRes.data || [];
        
        if (fetchedArtists.length > 0) {
          setTopArtist(fetchedArtists[0]);
          const artistSongsRes = await api.artists.getSongs(fetchedArtists[0].id.toString(), 5);
          setTopSongs(artistSongsRes.data?.items || artistSongsRes.data || []);
        }

        if (fetchedGenres.length > 0) {
          setGenres(fetchedGenres);
          const newReleaseRes = await api.genres.getSongs(fetchedGenres[0].id.toString(), 5);
          setNewReleases(newReleaseRes.data?.items || newReleaseRes.data || []);
        }
      } catch (error) {
        console.error("Failed to load discover data", error);
      } finally {
        setIsLoading(false);
      }
    };
    loadData();
  }, []);

  const handlePlayArtist = async (artist: ApiArtist) => {
    try {
      const songsRes = await api.artists.getSongs(artist.id.toString(), 20);
      const artistSongs = songsRes.data?.items || songsRes.data || [];
      if (artistSongs.length > 0) {
        playSong(artistSongs[0], artistSongs);
      }
    } catch (e) {
      console.error(e);
    }
  };

  if (isLoading) {
    return <div className="p-8 text-white">Loading...</div>;
  }

  return (
    <div className="p-8 pb-32">
      {/* Featured Banner */}
      {topArtist && (
        <section className="mb-10 relative group rounded-xl overflow-hidden h-80">
          <div className="absolute inset-0 bg-gradient-to-r from-background/90 via-background/40 to-transparent z-10" />
          <img
            src={topArtist.banner || 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819'}
            alt={topArtist.name}
            className="absolute inset-0 w-full h-full object-cover"
          />
          <div className="relative z-20 h-full flex flex-col justify-center px-12 max-w-2xl">
            <span className="inline-block px-3 py-1 bg-primary text-background text-xs font-bold rounded-full mb-4 w-max">
              ARTIST OF THE MONTH
            </span>
            <h2 className="text-5xl font-bold text-white mb-2 leading-tight">{topArtist.name}</h2>
            <p className="text-gray-200 text-lg mb-6 truncate">
              Discover their top tracking hits and newest releases.
            </p>
            <div className="flex gap-4">
              <button
                onClick={() => handlePlayArtist(topArtist)}
                className="bg-primary hover:bg-primary/90 text-background px-8 py-3 rounded-full font-bold flex items-center gap-2 transition-transform hover:scale-105"
              >
                <Play className="size-5 fill-current" /> Listen Now
              </button>
              <button className="bg-white/10 backdrop-blur-md border border-white/20 hover:bg-white/20 text-white px-8 py-3 rounded-full font-bold transition-transform hover:scale-105">
                View Artist
              </button>
            </div>
          </div>
        </section>
      )}

      {/* Top Charts Grid */}
      <section className="mb-12">
        <div className="flex items-center justify-between mb-6">
          <h3 className="text-2xl font-bold text-white">Top Charts ({topArtist?.name})</h3>
          <button className="text-primary text-sm font-semibold hover:underline">View All</button>
        </div>
        <div className="bg-primary/5 rounded-xl border border-primary/10 overflow-hidden">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="text-gray-500 text-xs uppercase border-b border-primary/10">
                <th className="px-6 py-4 font-semibold w-12 text-center">#</th>
                <th className="px-6 py-4 font-semibold">Track</th>
                <th className="px-6 py-4 font-semibold text-right">Duration</th>
                <th className="px-6 py-4 font-semibold w-16"></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-primary/5">
              {topSongs.slice(0, 3).map((song, index) => (
                <tr
                  key={song.id}
                  onClick={() => playSong(song, topSongs)}
                  className="hover:bg-primary/10 transition-colors group cursor-pointer"
                >
                  <td className="px-6 py-4 text-sm text-gray-500 text-center">{index + 1}</td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-4">
                      <div className="size-12 rounded-lg bg-surface overflow-hidden shrink-0 shadow-md">
                        <img src={song.artist?.avatar || song.genre?.image || 'https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17'} alt={song.title} className="w-full h-full object-cover" />
                      </div>
                      <div>
                        <p className="font-bold text-sm text-white group-hover:text-primary transition-colors">
                          {song.title}
                        </p>
                        <p className="text-xs text-gray-500">{song.artist?.name || 'Unknown Artist'}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-sm text-gray-500 text-right">
                    {Math.floor((song.duration || 0) / 60)}:{((song.duration || 0) % 60).toString().padStart(2, '0')}
                  </td>
                  <td className="px-6 py-4 text-right">
                    <button className="text-gray-400 hover:text-primary opacity-0 group-hover:opacity-100 transition-opacity">
                      <svg className="size-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z" />
                      </svg>
                    </button>
                  </td>
                </tr>
              ))}
              {topSongs.length === 0 && (
                <tr>
                  <td colSpan={4} className="px-6 py-4 text-gray-400 text-center text-sm">No tracks available.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      {/* New Releases */}
      <section className="mb-12">
        <div className="flex items-center justify-between mb-6">
          <h3 className="text-2xl font-bold text-white">New Releases ({genres[0]?.name})</h3>
          <div className="flex gap-2">
            <button className="size-10 rounded-full bg-primary/10 border border-primary/20 flex items-center justify-center text-primary hover:bg-primary/20 transition-colors">
              <ChevronLeft className="size-5" />
            </button>
            <button className="size-10 rounded-full bg-primary/10 border border-primary/20 flex items-center justify-center text-primary hover:bg-primary/20 transition-colors">
              <ChevronRight className="size-5" />
            </button>
          </div>
        </div>
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-6">
          {newReleases.map((song) => (
            <div
              key={song.id}
              onClick={() => playSong(song, newReleases)}
              className="bg-white/5 p-4 rounded-xl border border-transparent hover:border-primary/20 hover:bg-primary/5 transition-all group cursor-pointer"
            >
              <div className="relative mb-4">
                <img
                  src={song.artist?.avatar || song.genre?.image || 'https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17'}
                  alt={song.title}
                  className="w-full aspect-square object-cover rounded-lg shadow-lg"
                />
                <button className="absolute bottom-2 right-2 size-10 bg-primary rounded-full flex items-center justify-center text-background opacity-0 group-hover:opacity-100 shadow-xl translate-y-2 group-hover:translate-y-0 transition-all">
                  <Play className="size-5 fill-current ml-1" />
                </button>
              </div>
              <p className="font-bold text-sm text-white truncate group-hover:text-primary transition-colors">
                {song.title}
              </p>
              <p className="text-xs text-gray-500 truncate">{song.artist?.name || 'Unknown Artist'}</p>
            </div>
          ))}
          {newReleases.length === 0 && (
            <div className="col-span-full p-4 text-gray-400 text-sm">No new releases available.</div>
          )}
        </div>
      </section>

      {/* Browse Genres */}
      <section className="mb-20">
        <h3 className="text-2xl font-bold mb-6 text-white">Browse Genres</h3>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {genres.map((genre) => (
            <div
              key={genre.id}
              className="h-32 rounded-xl bg-gradient-to-br from-slate-700 to-slate-900 p-5 relative overflow-hidden group cursor-pointer transition-transform hover:-translate-y-1"
            >
              <span className="text-xl font-bold text-white relative z-10">{genre.name}</span>
              <img
                src={genre.image || 'https://images.unsplash.com/photo-1518609878373-06d740f60d8b'}
                alt={genre.name}
                className="absolute -right-4 -bottom-4 w-24 h-24 object-cover rounded-full opacity-20 rotate-12 group-hover:rotate-0 transition-transform mix-blend-overlay"
              />
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};
