export interface Song {
  id: string;
  title: string;
  artist: string;
  artistId: string;
  album: string;
  albumId: string;
  coverImage: string;
  audioUrl: string;
  duration: number; // in seconds
}

export interface Artist {
  id: string;
  name: string;
  avatar: string;
  banner: string;
  followers: number;
}

export interface Album {
  id: string;
  title: string;
  artist: string;
  artistId: string;
  coverImage: string;
  releaseYear: number;
}

export interface Playlist {
  id: string;
  title: string;
  description: string;
  coverImage: string;
  songs: string[]; // array of song IDs
}

export interface Genre {
  id: string;
  name: string;
  image: string;
  color: string;
}

// Free public domain audio samples for playback
const audio1 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
const audio2 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3";
const audio3 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3";
const audio4 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3";

export const artists: Artist[] = [
  {
    id: "a1",
    name: "HIEUTHUHAI",
    avatar: "https://images.unsplash.com/photo-1618085220188-b4f210d22703?auto=format&fit=crop&q=80&w=200&h=200",
    banner: "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&q=80&w=1200&h=400",
    followers: 3500000,
  },
  {
    id: "a2",
    name: "MCK",
    avatar: "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?auto=format&fit=crop&q=80&w=200&h=200",
    banner: "https://images.unsplash.com/photo-1493225457124-a1a2a5f5f9af?auto=format&fit=crop&q=80&w=1200&h=400",
    followers: 2800000,
  },
  {
    id: "a3",
    name: "Wxrdie",
    avatar: "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&q=80&w=200&h=200",
    banner: "https://images.unsplash.com/photo-1459749411175-04bf5292ceea?auto=format&fit=crop&q=80&w=1200&h=400",
    followers: 1200000,
  },
  {
    id: "a4",
    name: "tlinh",
    avatar: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=200&h=200",
    banner: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&q=80&w=1200&h=400",
    followers: 2100000,
  }
];

export const albums: Album[] = [
  {
    id: "al1",
    title: "Ai Cũng Phải Bắt Đầu Từ Đâu Đó",
    artist: "HIEUTHUHAI",
    artistId: "a1",
    coverImage: "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?auto=format&fit=crop&q=80&w=400&h=400",
    releaseYear: 2023,
  },
  {
    id: "al2",
    title: "99%",
    artist: "MCK",
    artistId: "a2",
    coverImage: "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&q=80&w=400&h=400",
    releaseYear: 2023,
  },
  {
    id: "al3",
    title: "Ái",
    artist: "tlinh",
    artistId: "a4",
    coverImage: "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&q=80&w=400&h=400",
    releaseYear: 2023,
  },
];

export const songs: Song[] = [
  {
    id: "s1",
    title: "Ngủ Một Mình",
    artist: "HIEUTHUHAI",
    artistId: "a1",
    album: "Ai Cũng Phải Bắt Đầu Từ Đâu Đó",
    albumId: "al1",
    coverImage: albums[0].coverImage,
    audioUrl: audio1,
    duration: 210,
  },
  {
    id: "s2",
    title: "Không Thể Say",
    artist: "HIEUTHUHAI",
    artistId: "a1",
    album: "Ai Cũng Phải Bắt Đầu Từ Đâu Đó",
    albumId: "al1",
    coverImage: albums[0].coverImage,
    audioUrl: audio2,
    duration: 195,
  },
  {
    id: "s3",
    title: "Chìm Sâu",
    artist: "MCK",
    artistId: "a2",
    album: "99%",
    albumId: "al2",
    coverImage: albums[1].coverImage,
    audioUrl: audio3,
    duration: 185,
  },
  {
    id: "s4",
    title: "Tại Vì Sao",
    artist: "MCK",
    artistId: "a2",
    album: "99%",
    albumId: "al2",
    coverImage: albums[1].coverImage,
    audioUrl: audio4,
    duration: 205,
  },
  {
    id: "s5",
    title: "Harder",
    artist: "Wxrdie",
    artistId: "a3",
    album: "Single",
    albumId: "single",
    coverImage: "https://images.unsplash.com/photo-1493225457124-a1a2a5f5f9af?auto=format&fit=crop&q=80&w=400&h=400",
    audioUrl: audio1,
    duration: 175,
  },
  {
    id: "s6",
    title: "Thích Quá Rùi Nà",
    artist: "tlinh",
    artistId: "a4",
    album: "Ái",
    albumId: "al3",
    coverImage: albums[2].coverImage,
    audioUrl: audio2,
    duration: 190,
  },
];

export const playlists: Playlist[] = [
  {
    id: "p1",
    title: "Rap Việt Top Hits",
    description: "Những bản Rap Việt hot nhất hiện nay. HIEUTHUHAI, MCK, Wxrdie và nhiều hơn nữa.",
    coverImage: "https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&q=80&w=400&h=400",
    songs: ["s1", "s3", "s5", "s6"],
  },
  {
    id: "p2",
    title: "Chill Cùng MCK",
    description: "Những giai điệu suy và chill nhất từ MCK.",
    coverImage: "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&q=80&w=400&h=400",
    songs: ["s3", "s4"],
  },
  {
    id: "p3",
    title: "V-Pop Rising",
    description: "Nhạc Việt thế hệ mới.",
    coverImage: "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&q=80&w=400&h=400",
    songs: ["s2", "s6", "s1", "s4"],
  },
];

export const genres: Genre[] = [
  { id: "g1", name: "Viet Rap", image: "https://images.unsplash.com/photo-1511192336575-5a79af67a629?auto=format&fit=crop&q=80&w=400&h=400", color: "from-amber-800 to-primary" },
  { id: "g2", name: "V-Pop", image: "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?auto=format&fit=crop&q=80&w=400&h=400", color: "from-slate-700 to-slate-900" },
  { id: "g3", name: "Indie VN", image: "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?auto=format&fit=crop&q=80&w=400&h=400", color: "from-orange-800 to-orange-400" },
  { id: "g4", name: "Chillout", image: "https://images.unsplash.com/photo-1459749411175-04bf5292ceea?auto=format&fit=crop&q=80&w=400&h=400", color: "from-emerald-800 to-emerald-400" },
];

