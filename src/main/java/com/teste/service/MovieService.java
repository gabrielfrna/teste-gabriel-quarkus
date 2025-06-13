package com.teste.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.teste.model.Movie;
import com.teste.repository.MovieRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MovieService {

    @Inject
    MovieRepository movieRepository;

    public List<Movie> findAllMovies() {
        return movieRepository.findAll().list();
    }

    public Movie findMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public void addMovie(Movie movie) {
        movieRepository.persist(movie);
    }

    public boolean deleteMovie(Long id) {
        return movieRepository.deleteById(id);
    }

    public Map<String, List<Map<String, Object>>> getAwardIntervals() {
        List<Movie> winners = movieRepository.list("winner", true);

        Map<String, List<Integer>> producerWins = new HashMap<>();

        for (Movie movie : winners) {
            String[] producers = movie.getProducers().split("(,| and )");

            for (String rawProducer : producers) {
                String producer = normalizeProducer(rawProducer);
                producerWins.computeIfAbsent(producer, k -> new ArrayList<>()).add(movie.getMovieYear());
            }
        }

        List<Map<String, Object>> intervalList = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
            List<Integer> years = entry.getValue();
            if (years.size() < 2) continue;

            Collections.sort(years);

            for (int i = 1; i < years.size(); i++) {
                int previousWin = years.get(i - 1);
                int followingWin = years.get(i);
                int interval = followingWin - previousWin;

                Map<String, Object> result = new HashMap<>();
                result.put("producer", entry.getKey());
                result.put("interval", interval);
                result.put("previousWin", previousWin);
                result.put("followingWin", followingWin);

                intervalList.add(result);
            }
        }

        int minInterval = intervalList.stream().mapToInt(m -> (int) m.get("interval")).min().orElse(0);
        int maxInterval = intervalList.stream().mapToInt(m -> (int) m.get("interval")).max().orElse(0);

        List<Map<String, Object>> minList = intervalList.stream()
                .filter(m -> (int) m.get("interval") == minInterval)
                .collect(Collectors.toList());

        List<Map<String, Object>> maxList = intervalList.stream()
                .filter(m -> (int) m.get("interval") == maxInterval)
                .collect(Collectors.toList());

        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("min", minList);
        result.put("max", maxList);

        return result;
    }

    private String normalizeProducer(String producer) {
        return producer.trim().replaceAll("\\s+", " ");
    }
}
