import React, { useEffect, useState } from 'react';
import { Play } from 'lucide-react';
import { usePlayerStore, Song, ApiGenre, ApiArtist } from '../store/usePlayerStore';
import { api } from '../api';

export const Home = () => {
    const { playSong } = usePlayerStore();
    const [genres, setGenres] = useState<ApiGenre[]>([]);
    const [artists, setArtists] = useState<ApiArtist[]>([]);
    const [recentSongs, setRecentSongs] = useState<Song[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const loadData = async () => {
            try {
                const [genresRes, artistsRes] = await Promise.all([
                    api.genres.getAll(),
                    api.artists.getAll()
                ]);
                const fetchedGenres = genresRes.data || [];
                setGenres(fetchedGenres);
                setArtists(artistsRes.data || []);

                if (fetchedGenres.length > 0) {
                    const songsRes = await api.genres.getSongs(fetchedGenres[0].id.toString(), 5);
                    setRecentSongs(songsRes.data?.items || songsRes.data || []);
                }
            } catch (error) {
                console.error("Failed to load home data", error);
            } finally {
                setIsLoading(false);
            }
        };
        loadData();
    }, []);

    const handlePlayGenre = async (genre: ApiGenre) => {
        try {
            const songsRes = await api.genres.getSongs(genre.id.toString(), 20);
            const genreSongs = songsRes.data?.items || songsRes.data || [];
            if (genreSongs.length > 0) {
                playSong(genreSongs[0], genreSongs);
            }
        } catch (e) {
            console.error(e);
        }
    };

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
            {/* Welcome Section */}
            <section className="mb-10">
                <h2 className="text-4xl font-black text-white mb-2 tracking-tight">Welcome Back</h2>
                <p className="text-gray-400">Your daily blend of coffee and melodies is ready.</p>
            </section>

            {/* Featured Genres Grid */}
            <section className="mb-12">
                <h3 className="text-xl font-bold mb-6 flex items-center gap-2 text-white">
                    <span className="text-primary">✨</span>
                    Featured Genres
                </h3>
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-4">
                    {genres.slice(0, 6).map((genre) => (
                        <div
                            key={genre.id}
                            className="group relative overflow-hidden rounded-xl aspect-square bg-surface flex items-end p-4 transition-transform hover:-translate-y-1 cursor-pointer"
                        >
                            <div
                                className="absolute inset-0 bg-cover bg-center transition-transform group-hover:scale-110"
                                style={{
                                    backgroundImage: `linear-gradient(to top, rgba(43, 43, 43, 0.9), transparent), url(${genre.image || 'https://images.unsplash.com/photo-1511192336575-5a79af67a629'})`,
                                }}
                            />
                            <div className="relative z-10">
                                <p className="text-white font-bold">{genre.name}</p>
                            </div>
                            <button
                                onClick={(e) => {
                                    e.stopPropagation();
                                    handlePlayGenre(genre);
                                }}
                                className="absolute bottom-4 right-4 size-10 rounded-full bg-primary text-background opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center shadow-lg hover:scale-105"
                            >
                                <Play className="size-5 fill-current ml-1" />
                            </button>
                        </div>
                    ))}
                </div>
            </section>

            {/* Recommended Artists */}
            <section className="mb-12">
                <div className="flex items-center justify-between mb-6">
                    <h3 className="text-xl font-bold text-white">Recommended Artists</h3>
                    <button className="text-sm font-bold text-primary hover:underline uppercase tracking-widest">
                        See All
                    </button>
                </div>
                <div className="flex gap-6 overflow-x-auto pb-4 custom-scrollbar">
                    {artists.map((artist) => (
                        <div key={artist.id} className="flex-shrink-0 w-44 group cursor-pointer" onClick={() => handlePlayArtist(artist)}>
                            <div className="relative mb-3 aspect-square rounded-full overflow-hidden bg-surface shadow-xl">
                                <div
                                    className="absolute inset-0 bg-cover bg-center group-hover:scale-105 transition-transform"
                                    style={{ backgroundImage: `url(${artist.avatarUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb'})` }}
                                />
                                <button
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        handlePlayArtist(artist);
                                    }}
                                    className="absolute bottom-2 right-2 size-10 rounded-full bg-primary text-background opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center shadow-lg hover:scale-105"
                                >
                                    <Play className="size-5 fill-current ml-1" />
                                </button>
                            </div>
                            <p className="font-bold text-white text-center truncate">{artist.name}</p>
                        </div>
                    ))}
                </div>
            </section>

            {/* Suggested Tracks */}
            <section>
                <h3 className="text-xl font-bold mb-6 text-white">Suggested Tracks</h3>
                <div className="flex flex-col bg-surface/30 rounded-2xl overflow-hidden divide-y divide-primary/5">
                    {recentSongs.map((song) => (
                        <div
                            key={song.id}
                            onClick={() => playSong(song, recentSongs)}
                            className="flex items-center justify-between p-3 hover:bg-primary/5 group cursor-pointer transition-colors"
                        >
                            <div className="flex items-center gap-4">
                                <div
                                    className="size-12 rounded bg-surface bg-cover bg-center"
                                    style={{ backgroundImage: `url(${song.artist?.avatar || song.genre?.image || 'https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17'})` }}
                                />
                                <div>
                                    <p className="font-bold text-white group-hover:text-primary transition-colors">{song.title}</p>
                                    <p className="text-xs text-gray-400">{song.artist?.name || 'Unknown Artist'}</p>
                                </div>
                            </div>
                            <div className="flex items-center gap-8">
                                <p className="text-sm text-gray-400">
                                    {Math.floor((song.duration || 0) / 60)}:{((song.duration || 0) % 60).toString().padStart(2, '0')}
                                </p>
                                <div className="flex gap-4 opacity-0 group-hover:opacity-100 transition-opacity">
                                    <button className="text-gray-400 hover:text-primary">
                                        <svg className="size-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                    {recentSongs.length === 0 && (
                        <div className="p-4 text-gray-400 text-sm">No suggested tracks available.</div>
                    )}
                </div>
            </section>
        </div>
    );
};
