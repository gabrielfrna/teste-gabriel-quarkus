package com.teste.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.teste.model.Movie;
import com.teste.service.MovieService;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MoviesImportLoader {

    @Inject
    MovieService movieService;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Movielist.csv"), StandardCharsets.UTF_8))) {

            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length < 4) {
                    System.out.println("Linha ignorada, formato inválido: " + line);
                    continue;
                } 

                Movie movie = new Movie();
                movie.setMovieYear(Integer.parseInt(parts[0].trim()));
                movie.setTitle(parts[1].trim());
                movie.setStudios(parts[2].trim());
                movie.setProducers(parts[3].trim());

                String winner = parts.length > 4 ? parts[4].trim() : "";
                movie.setWinner("yes".equalsIgnoreCase(winner));

                movieService.addMovie(movie);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
