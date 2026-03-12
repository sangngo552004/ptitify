import React, { useEffect, useRef } from 'react';
import { usePlayerStore } from '../store/usePlayerStore';
import { Play, Pause, SkipBack, SkipForward, Shuffle, Repeat, Volume2, Heart, ListMusic, Maximize2 } from 'lucide-react';

export const PlayerBar = () => {
  const {
    currentSong,
    isPlaying,
    volume,
    progress,
    duration,
    playSong,
    pause,
    resume,
    setVolume,
    setProgress,
    setDuration,
    nextSong,
    prevSong,
  } = usePlayerStore();

  const audioRef = useRef<HTMLAudioElement>(null);

  useEffect(() => {
    if (audioRef.current) {
      if (isPlaying) {
        audioRef.current.play().catch(() => pause());
      } else {
        audioRef.current.pause();
      }
    }
  }, [isPlaying, currentSong, pause]);

  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = volume;
    }
  }, [volume]);

  const handleTimeUpdate = () => {
    if (audioRef.current) {
      setProgress(audioRef.current.currentTime);
    }
  };

  const handleLoadedMetadata = () => {
    if (audioRef.current) {
      setDuration(audioRef.current.duration);
    }
  };

  const handleSeek = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newTime = Number(e.target.value);
    if (audioRef.current) {
      audioRef.current.currentTime = newTime;
      setProgress(newTime);
    }
  };

  const formatTime = (time: number) => {
    if (isNaN(time)) return '0:00';
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  };

  if (!currentSong) return null;

  return (
    <footer className="fixed bottom-0 left-0 right-0 h-24 bg-background/95 border-t border-primary/10 backdrop-blur-xl px-6 flex items-center justify-between z-50">
      <audio
        ref={audioRef}
        src={currentSong.audioUrl}
        onTimeUpdate={handleTimeUpdate}
        onLoadedMetadata={handleLoadedMetadata}
        onEnded={nextSong}
      />

      {/* Current Track Info */}
      <div className="flex items-center gap-4 w-1/3">
        <div className="size-14 rounded-lg bg-surface overflow-hidden shadow-lg border border-primary/20 shrink-0">
          <img src={currentSong.coverImage} alt={currentSong.title} className="w-full h-full object-cover" />
        </div>
        <div className="hidden sm:flex flex-col min-w-0">
          <p className="text-sm font-bold text-white truncate">{currentSong.title}</p>
          <p className="text-xs text-primary font-medium truncate">{currentSong.artist}</p>
        </div>
        <button className="ml-2 text-primary hover:scale-110 transition-transform hidden sm:block">
          <Heart className="size-5" />
        </button>
      </div>

      {/* Player Controls */}
      <div className="flex flex-col items-center gap-2 w-1/3 max-w-xl">
        <div className="flex items-center gap-6">
          <button className="text-gray-500 hover:text-primary transition-colors">
            <Shuffle className="size-5" />
          </button>
          <button onClick={prevSong} className="text-gray-300 hover:text-white transition-colors">
            <SkipBack className="size-6" />
          </button>
          <button
            onClick={isPlaying ? pause : resume}
            className="size-10 rounded-full bg-primary text-background flex items-center justify-center hover:scale-105 transition-transform"
          >
            {isPlaying ? <Pause className="size-5 fill-current" /> : <Play className="size-5 fill-current ml-1" />}
          </button>
          <button onClick={nextSong} className="text-gray-300 hover:text-white transition-colors">
            <SkipForward className="size-6" />
          </button>
          <button className="text-gray-500 hover:text-primary transition-colors">
            <Repeat className="size-5" />
          </button>
        </div>
        <div className="flex items-center gap-3 w-full">
          <span className="text-[10px] text-gray-500 font-medium w-8 text-right">{formatTime(progress)}</span>
          <div className="flex-1 relative flex items-center group">
            <input
              type="range"
              min={0}
              max={duration || 100}
              value={progress}
              onChange={handleSeek}
              className="absolute w-full h-1 opacity-0 cursor-pointer z-10"
            />
            <div className="w-full h-1 bg-surface rounded-full overflow-hidden relative">
              <div
                className="absolute top-0 left-0 h-full bg-primary rounded-full"
                style={{ width: `${(progress / (duration || 1)) * 100}%` }}
              />
            </div>
            <div
              className="absolute size-3 bg-white rounded-full shadow-md opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none"
              style={{ left: `calc(${(progress / (duration || 1)) * 100}% - 6px)` }}
            />
          </div>
          <span className="text-[10px] text-gray-500 font-medium w-8">{formatTime(duration)}</span>
        </div>
      </div>

      {/* Volume & Extra Controls */}
      <div className="flex items-center justify-end gap-4 w-1/3">
        <button className="text-gray-500 hover:text-primary transition-colors hidden md:block">
          <ListMusic className="size-5" />
        </button>
        <div className="flex items-center gap-2 group w-32">
          <Volume2 className="size-5 text-gray-500 group-hover:text-primary" />
          <div className="flex-1 relative flex items-center">
            <input
              type="range"
              min={0}
              max={1}
              step={0.01}
              value={volume}
              onChange={(e) => setVolume(Number(e.target.value))}
              className="absolute w-full h-1 opacity-0 cursor-pointer z-10"
            />
            <div className="w-full h-1 bg-surface rounded-full overflow-hidden relative">
              <div
                className="absolute top-0 left-0 h-full bg-primary rounded-full"
                style={{ width: `${volume * 100}%` }}
              />
            </div>
          </div>
        </div>
        <button className="text-gray-500 hover:text-primary transition-colors hidden md:block">
          <Maximize2 className="size-5" />
        </button>
      </div>
    </footer>
  );
};
