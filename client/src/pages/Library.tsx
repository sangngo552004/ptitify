import React, { useEffect, useState } from 'react';
import { Play, Heart } from 'lucide-react';
import { usePlayerStore, Song } from '../store/usePlayerStore';
import { api } from '../api';

export const Library = () => {
    const { playSong } = usePlayerStore();
    const [favoriteSongs, setFavoriteSongs] = useState<Song[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchFavorites = async () => {
            try {
                const res = await api.favorites.getAll(50);
                const songs: Song[] = res.data?.items || [];
                setFavoriteSongs(songs);
            } catch (error) {
                console.error("Failed to load favorites", error);
            } finally {
                setIsLoading(false);
            }
        };
        fetchFavorites();
    }, []);

    if (isLoading) {
        return <div className="p-8 text-white">Loading...</div>;
    }

    return (
        <div className="p-8 pb-32">
            <header className="flex flex-col md:flex-row items-end gap-8 mb-10">
                <div className="size-64 rounded-xl shadow-2xl bg-gradient-to-br from-purple-700 to-indigo-900 flex items-center justify-center shrink-0">
                    <Heart className="size-24 text-white fill-current" />
                </div>
                <div className="flex flex-col gap-2">
                    <span className="text-xs font-bold uppercase tracking-widest text-primary">Private Playlist</span>
                    <h2 className="text-6xl font-black tracking-tighter text-white">Liked Songs</h2>
                    <p className="text-gray-400 mt-2 text-lg">Your personal collection of favorite tracks.</p>
                    <div className="flex items-center gap-2 mt-4 text-sm font-medium">
                        <span className="text-white">{favoriteSongs.length} songs</span>
                    </div>
                    <div className="flex items-center gap-4 mt-6">
                        <button
                            onClick={() => {
                                if (favoriteSongs.length > 0) {
                                    playSong(favoriteSongs[0], favoriteSongs);
                                }
                            }}
                            disabled={favoriteSongs.length === 0}
                            className="flex items-center gap-2 px-8 py-3 bg-primary text-background rounded-full font-bold hover:scale-105 transition-transform disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            <Play className="size-5 fill-current ml-1" />
                            Play
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
                            <th className="px-4 py-4 text-right pr-12">
                                <svg className="size-4 inline-block" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                            </th>
                        </tr>
                    </thead>
                    <tbody className="text-sm">
                        {favoriteSongs.map((song, index) => {
                            if (!song) return null;
                            return (
                                <tr
                                    key={song.id}
                                    onClick={() => playSong(song, favoriteSongs)}
                                    className="group hover:bg-primary/5 transition-colors cursor-pointer"
                                >
                                    <td className="px-6 py-3 text-center text-gray-400 group-hover:text-primary">{index + 1}</td>
                                    <td className="px-4 py-3">
                                        <div className="flex items-center gap-4">
                                            <div className="size-10 rounded bg-surface overflow-hidden shrink-0">
                                                <img src={song.artist?.avatar || song.genre?.image || 'https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17'} alt={song.title} className="w-full h-full object-cover" />
                                            </div>
                                            <div>
                                                <p className="font-bold text-white group-hover:text-primary transition-colors">{song.title}</p>
                                                <p className="text-xs text-gray-400">{song.artist?.name || 'Unknown Artist'}</p>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-4 py-3 text-right pr-12 text-gray-400">
                                        {Math.floor((song.duration || 0) / 60)}:{((song.duration || 0) % 60).toString().padStart(2, '0')}
                                    </td>
                                </tr>
                            );
                        })}
                        {favoriteSongs.length === 0 && (
                            <tr>
                                <td colSpan={3} className="px-6 py-8 text-center text-gray-400">
                                    You haven't liked any songs yet. Go discover some!
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
