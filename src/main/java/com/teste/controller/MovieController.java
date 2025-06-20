package com.teste.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teste.model.Movie;
import com.teste.service.MovieService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/api/movie")
public class MovieController {

    @Inject
    MovieService movieService;

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @GET
    public Response retrieveMovies() {
        List<Movie> movies = movieService.findAllMovies();
        return Response.ok(movies).build();
    }

    @GET
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") Long id) {
        Movie movie = movieService.findMovieById(id);
        if (movie != null) {
            return Response.ok(movie).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Filme não encontrado").build();
        }
    }

    @POST
    @Transactional
    public Response addMovie(Movie movie) {
        movieService.addMovie(movie);
        return Response.status(Response.Status.CREATED).entity(movie).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateMovie(@PathParam("id") Long id, Movie movie) {
        Movie existingMovie = movieService.findMovieById(id);

        if (existingMovie == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Filme não encontrado").build();
        }

        existingMovie.setMovieYear(movie.getMovieYear());
        existingMovie.setTitle(movie.getTitle());
        existingMovie.setStudios(movie.getStudios());
        existingMovie.setProducers(movie.getProducers());
        existingMovie.setWinner(movie.getWinner());

        return Response.ok(movie).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteMovie(@PathParam("id") Long id) {
        boolean deleted = movieService.deleteMovie(id);
        if (deleted) {
            return Response.ok("Filme removido com sucesso").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Filme não encontrado").build();
        }
    }

    @GET
    @Path("/award-intervals")
    public Response getAwardIntervals() {
        Map<String, List<Map<String, Object>>> result = movieService.getAwardIntervals();
        return Response.ok(result).build();
    }

}
