package com.sangngo552004.musicapp.controller;

import com.sangngo552004.musicapp.dto.response.ApiResponse;
import com.sangngo552004.musicapp.dto.response.SearchResponse;
import com.sangngo552004.musicapp.service.SearchService;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchController {

    @Inject
    private SearchService searchService;

    @GET
    public Response search(
            @QueryParam("q") String query,
            @DefaultValue("10") @QueryParam("limit") int limit
    ) {
        SearchResponse response = searchService.search(query, limit);
        return Response.ok(ApiResponse.of(Response.Status.OK.getStatusCode(), "Search completed successfully", response)).build();
    }
}
