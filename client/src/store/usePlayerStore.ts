import { create } from 'zustand';
import { Song } from '../data/mockData';

interface PlayerState {
  currentSong: Song | null;
  isPlaying: boolean;
  queue: Song[];
  currentIndex: number;
  volume: number;
  progress: number;
  duration: number;
  playSong: (song: Song, queue?: Song[]) => void;
  pause: () => void;
  resume: () => void;
  setVolume: (volume: number) => void;
  setProgress: (progress: number) => void;
  setDuration: (duration: number) => void;
  nextSong: () => void;
  prevSong: () => void;
  setQueue: (queue: Song[]) => void;
}

export const usePlayerStore = create<PlayerState>((set, get) => ({
  currentSong: null,
  isPlaying: false,
  queue: [],
  currentIndex: -1,
  volume: 0.5,
  progress: 0,
  duration: 0,

  playSong: (song, queue) => {
    const currentQueue = queue || get().queue;
    const index = currentQueue.findIndex((s) => s.id === song.id);
    
    set({
      currentSong: song,
      isPlaying: true,
      queue: currentQueue.length > 0 ? currentQueue : [song],
      currentIndex: index !== -1 ? index : 0,
      progress: 0,
    });
  },

  pause: () => set({ isPlaying: false }),
  
  resume: () => {
    if (get().currentSong) {
      set({ isPlaying: true });
    }
  },

  setVolume: (volume) => set({ volume }),
  
  setProgress: (progress) => set({ progress }),
  
  setDuration: (duration) => set({ duration }),

  nextSong: () => {
    const { queue, currentIndex } = get();
    if (queue.length > 0 && currentIndex < queue.length - 1) {
      const nextIndex = currentIndex + 1;
      set({
        currentSong: queue[nextIndex],
        currentIndex: nextIndex,
        isPlaying: true,
        progress: 0,
      });
    }
  },

  prevSong: () => {
    const { queue, currentIndex } = get();
    if (queue.length > 0 && currentIndex > 0) {
      const prevIndex = currentIndex - 1;
      set({
        currentSong: queue[prevIndex],
        currentIndex: prevIndex,
        isPlaying: true,
        progress: 0,
      });
    }
  },

  setQueue: (queue) => set({ queue }),
}));
