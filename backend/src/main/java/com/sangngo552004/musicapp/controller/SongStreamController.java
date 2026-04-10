package com.sangngo552004.musicapp.controller;

import com.sangngo552004.musicapp.entity.Song;
import com.sangngo552004.musicapp.exception.ResourceNotFoundException;
import com.sangngo552004.musicapp.repository.SongRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;

@Path("/songs")
public class SongStreamController {

    private static final long DEFAULT_CHUNK_SIZE = 1024L * 1024L;

    @Inject
    private SongRepository songRepository;

    @GET
    @Path("/{id}/stream")
    public Response streamSong(
            @PathParam("id") Long id,
            @HeaderParam("Range") String rangeHeader) {
        Song song = songRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song was not found"));

        java.nio.file.Path filePath = resolveMusicPath(song.getFileUrl());

        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Audio file not found on disk: " + filePath)
                    .build();
        }

        try {
            long fileSize = Files.size(filePath);
            String mimeType = resolveMimeType(song.getFileUrl());

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                return buildPartialResponse(filePath, rangeHeader, fileSize, mimeType);
            }

            return buildFullResponse(filePath, fileSize, mimeType);

        } catch (IOException ex) {
            return Response.serverError()
                    .entity("Failed to read audio file: " + ex.getMessage())
                    .build();
        }
    }

    private Response buildFullResponse(java.nio.file.Path filePath, long fileSize, String mimeType) {
        StreamingOutput stream = output -> {
            try (InputStream in = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
            }
        };

        return Response.ok(stream)
                .header("Content-Type", mimeType)
                .header("Content-Length", fileSize)
                .header("Accept-Ranges", "bytes")
                .build();
    }

    private Response buildPartialResponse(
            java.nio.file.Path filePath, String rangeHeader, long fileSize, String mimeType) throws IOException {
        String rangeValue = rangeHeader.substring("bytes=".length());
        String[] parts = rangeValue.split("-", 2);

        long start;
        long end;
        try {
            start = Long.parseLong(parts[0].trim());
        } catch (NumberFormatException e) {
            start = 0;
        }

        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            try {
                end = Long.parseLong(parts[1].trim());
            } catch (NumberFormatException e) {
                end = Math.min(start + DEFAULT_CHUNK_SIZE - 1, fileSize - 1);
            }
        } else {
            end = Math.min(start + DEFAULT_CHUNK_SIZE - 1, fileSize - 1);
        }

        if (end >= fileSize)
            end = fileSize - 1;

        if (start > end || start < 0 || start >= fileSize) {
            return Response.status(416) // 416 Range Not Satisfiable
                    .header("Content-Range", "bytes */" + fileSize)
                    .build();
        }

        long contentLength = end - start + 1;
        final long finalStart = start;
        final long finalEnd = end;

        StreamingOutput stream = output -> {
            try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
                raf.seek(finalStart);
                long remaining = finalEnd - finalStart + 1;
                byte[] buffer = new byte[8192];
                while (remaining > 0) {
                    int toRead = (int) Math.min(buffer.length, remaining);
                    int bytesRead = raf.read(buffer, 0, toRead);
                    if (bytesRead == -1)
                        break;
                    output.write(buffer, 0, bytesRead);
                    remaining -= bytesRead;
                }
            }
        };

        return Response.status(206) // 206 Partial Content
                .header("Content-Type", mimeType)
                .header("Content-Length", contentLength)
                .header("Content-Range", "bytes " + start + "-" + end + "/" + fileSize)
                .header("Accept-Ranges", "bytes")
                .entity(stream)
                .build();
    }

    private java.nio.file.Path resolveMusicPath(String fileUrl) {
        String dir = System.getenv("MUSIC_DIR");
        if (dir == null || dir.isBlank()) {
            dir = System.getProperty("MUSIC_DIR");
        }
        if (dir == null || dir.isBlank()) {
            dir = "muzik";
        }
        return java.nio.file.Paths.get(dir, fileUrl);
    }

    private String resolveMimeType(String filename) {
        if (filename == null)
            return "audio/mpeg";
        String lower = filename.toLowerCase();
        if (lower.endsWith(".mp3"))
            return "audio/mpeg";
        if (lower.endsWith(".ogg"))
            return "audio/ogg";
        if (lower.endsWith(".wav"))
            return "audio/wav";
        if (lower.endsWith(".flac"))
            return "audio/flac";
        if (lower.endsWith(".aac"))
            return "audio/aac";
        if (lower.endsWith(".m4a"))
            return "audio/mp4";
        if (lower.endsWith(".opus"))
            return "audio/ogg; codecs=opus";
        return "audio/mpeg";
    }
}
