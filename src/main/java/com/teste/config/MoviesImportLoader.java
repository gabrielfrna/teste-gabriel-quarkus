package com.teste.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teste.model.Movie;
import com.teste.service.MovieService;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MoviesImportLoader {

    private static final Logger logger = LoggerFactory.getLogger(MoviesImportLoader.class);

    @Inject
    MovieService movieService;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {

        try (InputStream resourceStream = getClass().getResourceAsStream("/Movielist.csv")) {
            if (resourceStream == null) {
                logger.error("Arquivo Movielist.csv não encontrado.");
                return;
            }

            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(resourceStream, StandardCharsets.UTF_8))) {

                String line = bufferedReader.readLine();
                int numLinha = 1;
                while ((line = bufferedReader.readLine()) != null) {
                    numLinha++;
                    String[] parts = line.split(";");

                    if (parts.length < 4) {
                        logger.warn("Linha {} ignorada, formato inválido: {}", numLinha, line);
                        continue;
                    }

                    try {
                        Movie movie = new Movie();
                        movie.setMovieYear(Integer.parseInt(parts[0].trim()));
                        movie.setTitle(parts[1].trim());
                        movie.setStudios(parts[2].trim());
                        movie.setProducers(parts[3].trim());

                        String winner = parts.length > 4 ? parts[4].trim() : "";
                        movie.setWinner("yes".equalsIgnoreCase(winner));

                        movieService.addMovie(movie);
                    } catch (Exception e) {
                        logger.error("Erro ao processar linha {}: {}. Linha ignorada.", numLinha, line, e);
                    }
                }

            } catch (IOException e) {
                logger.error("Erro na leitura do arquivo", e);
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar o arquivo Movielist.csv", e);
        }
    }
}
