import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Play } from 'lucide-react';
import { api } from '../api';
import { usePlayerStore, Song, ApiArtist } from '../store/usePlayerStore';

export const Search = () => {
    const [searchParams] = useSearchParams();
    const query = searchParams.get('q') || '';
    const { playSong } = usePlayerStore();

    const [results, setResults] = useState<{ songs: Song[], artists: ApiArtist[] }>({ songs: [], artists: [] });
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        if (!query.trim()) {
            setResults({ songs: [], artists: [] });
            return;
        }

        const fetchResults = async () => {
            setIsLoading(true);
            try {
                const res = await api.search.global(query);
                setResults({
                    songs: res.data?.songs || [],
                    artists: res.data?.artists || []
                });
            } catch (err) {
                console.error("Search failed:", err);
            } finally {
                setIsLoading(false);
            }
        };

        const debounceId = setTimeout(fetchResults, 300);
        return () => clearTimeout(debounceId);
    }, [query]);

    const handlePlayArtist = async (artist: ApiArtist) => {
        try {
            const songsRes = await api.artists.getSongs(artist.id.toString(), 20);
            const artistSongs: Song[] = songsRes.data?.items || [];
            if (artistSongs.length > 0) {
                playSong(artistSongs[0], artistSongs);
            }
        } catch (e) {
            console.error(e);
        }
    };

    return (
        <div className="p-8 pb-32">
            {!query.trim() ? (
                <section className="flex flex-col items-center justify-center pt-20">
                    <h2 className="text-2xl font-bold text-white mb-2">Search for music</h2>
                    <p className="text-gray-400">Type in the search bar to find artists and songs.</p>
                </section>
            ) : (
                <>
                    <h2 className="text-2xl font-bold mb-6 text-white">Top Results for "{query}"</h2>

                    {isLoading ? (
                        <div className="text-gray-400">Searching...</div>
                    ) : (
                        <div className="flex flex-col gap-10">

                            {/* Artists Results */}
                            {results.artists.length > 0 && (
                                <section>
                                    <h3 className="text-xl font-bold mb-4 flex items-center gap-2 text-white">Artists</h3>
                                    <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 xl:grid-cols-6 gap-6">
                                        {results.artists.map((artist) => (
                                            <div
                                                key={artist.id}
                                                onClick={() => handlePlayArtist(artist)}
                                                className="bg-white/5 p-4 rounded-xl border border-transparent hover:border-primary/20 hover:bg-primary/5 transition-all group cursor-pointer flex flex-col items-center"
                                            >
                                                <div className="relative mb-4 w-full aspect-square rounded-full overflow-hidden shadow-lg">
                                                    <img
                                                        src={artist.avatar || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb'}
                                                        alt={artist.name}
                                                        className="w-full h-full object-cover"
                                                    />
                                                    <div className="absolute inset-0 bg-black/20 group-hover:bg-transparent transition-colors" />
                                                    <button className="absolute bottom-2 right-2 size-10 bg-primary rounded-full flex items-center justify-center text-background opacity-0 group-hover:opacity-100 shadow-xl translate-y-2 group-hover:translate-y-0 transition-all">
                                                        <Play className="size-5 fill-current ml-1" />
                                                    </button>
                                                </div>
                                                <p className="font-bold text-sm text-white text-center truncate w-full group-hover:text-primary transition-colors">
                                                    {artist.name}
                                                </p>
                                                <p className="text-xs text-gray-500 mt-1">Artist</p>
                                            </div>
                                        ))}
                                    </div>
                                </section>
                            )}

                            {/* Songs Results */}
                            {results.songs.length > 0 && (
                                <section>
                                    <h3 className="text-xl font-bold mb-4 flex items-center gap-2 text-white">Songs</h3>
                                    <div className="flex flex-col bg-surface/30 rounded-2xl overflow-hidden divide-y divide-primary/5">
                                        {results.songs.map((song) => (
                                            <div
                                                key={song.id}
                                                onClick={() => playSong(song, results.songs)}
                                                className="flex items-center justify-between p-3 hover:bg-primary/5 group cursor-pointer transition-colors"
                                            >
                                                <div className="flex items-center gap-4">
                                                    <div
                                                        className="size-12 rounded bg-surface bg-cover bg-center shrink-0"
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
                                                        <button className="text-gray-400 hover:text-primary">
                                                            <Play className="size-5 fill-current ml-1" />
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </section>
                            )}

                            {/* No Results */}
                            {results.songs.length === 0 && results.artists.length === 0 && (
                                <div className="text-gray-400 pt-8 text-center">
                                    No results found for "{query}". Try searching for something else.
                                </div>
                            )}
                        </div>
                    )}
                </>
            )}
        </div>
    );
};
