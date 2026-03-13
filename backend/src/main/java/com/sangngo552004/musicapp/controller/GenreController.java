package com.sangngo552004.musicapp.controller;

import com.sangngo552004.musicapp.dto.response.ApiResponse;
import com.sangngo552004.musicapp.dto.response.CursorResult;
import com.sangngo552004.musicapp.dto.response.GenreResponse;
import com.sangngo552004.musicapp.dto.response.SongResponse;
import com.sangngo552004.musicapp.service.MusicCatalogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/genres")
@Produces(MediaType.APPLICATION_JSON)
public class GenreController {

    @Inject
    private MusicCatalogService musicCatalogService;

    @GET
    public Response getGenres() {
        List<GenreResponse> response = musicCatalogService.getGenres();
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Genres fetched successfully", response)).build();
    }

    @GET
    @Path("/{genreId}/songs")
    public Response getSongsByGenre(
            @PathParam("genreId") Long genreId,
            @QueryParam("cursor") Long cursor,
            @DefaultValue("20") @QueryParam("limit") Integer limit
    ) {
        CursorResult<SongResponse> response = musicCatalogService.getSongsByGenre(genreId, cursor, limit);
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Songs fetched successfully", response)).build();
    }
}
