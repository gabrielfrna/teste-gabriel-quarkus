package com.teste.controller;

import org.junit.jupiter.api.Test;

import com.teste.model.Movie;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MovieControllerTest {

    @Test
    public void testAddMovie() {
        Movie movie = new Movie();
        movie.setMovieYear(1999);
        movie.setTitle("Teste Filme");
        movie.setStudios("Teste Estúdio");
        movie.setProducers("Teste Produtores");
        movie.setWinner(true);

        given()
                .contentType(ContentType.JSON)
                .body(movie)
                .when()
                .post("/api/movie")
                .then()
                .statusCode(201)
                .body("movieYear", is(1999))
                .body("title", is("Teste Filme"))
                .body("studios", is("Teste Estúdio"))
                .body("producers", is("Teste Produtores"))
                .body("winner", is(true));
    }

    @Test
    public void testGetMovieById() {
        Movie movie = new Movie();
        movie.setMovieYear(2025);
        movie.setTitle("Teste de Filme Um");
        movie.setStudios("Estúdio Teste Um");
        movie.setProducers("Produtores do Teste Um");
        movie.setWinner(false);

        Long id = given()
                .contentType(ContentType.JSON)
                .body(movie)
                .when()
                .post("/api/movie")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
                .when()
                .get("/api/movie/" + id)
                .then()
                .statusCode(200)
                .body("movieYear", is(2025))
                .body("title", is("Teste de Filme Um"))
                .body("studios", is("Estúdio Teste Um"))
                .body("producers", is("Produtores do Teste Um"))
                .body("winner", is(false));
    }

    @Test
    public void testUpdateMovie() {
        Movie movie = new Movie();
        movie.setMovieYear(2021);
        movie.setTitle("Filme anterior");
        movie.setStudios("Estúdio anterior");
        movie.setProducers("Produtores anteriores");
        movie.setWinner(false);

        Movie created = given()
                .contentType(ContentType.JSON)
                .body(movie)
                .when()
                .post("/api/movie")
                .then()
                .statusCode(201)
                .extract()
                .as(Movie.class);

        created.setTitle("Filme novo");
        created.setWinner(true);

        given()
                .contentType(ContentType.JSON)
                .body(created)
                .when()
                .put("/api/movie/" + created.getId())
                .then()
                .statusCode(200)
                .body("title", is("Filme novo"))
                .body("winner", is(true));
    }

    @Test
    public void testDeleteMovie() {
        Movie movie = new Movie();
        movie.setMovieYear(2024);
        movie.setTitle("Teste de Filme Deletado");
        movie.setStudios("Estúdio Teste Deletado");
        movie.setProducers("Produtores do Teste Deletado");
        movie.setWinner(true);

        Long id = given()
                .contentType(ContentType.JSON)
                .body(movie)
                .when()
                .post("/api/movie")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        given()
                .when()
                .delete("/api/movie/" + id)
                .then()
                .statusCode(200)
                .body(is("Filme removido com sucesso"));

        given()
                .when()
                .get("/api/movie/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetNonexistentMovie() {
        given()
                .when()
                .get("/api/movie/999999")
                .then()
                .statusCode(404)
                .body(is("Filme não encontrado"));
    }

    @Test
    public void testGetAwardIntervals() {
        given()
                .when()
                .get("/api/movie/award-intervals")
                .then()
                .statusCode(200)
                .body("min[0].producer", is("Joel Silver"))
                .body("min[0].interval", is(1))
                .body("min[0].previousWin", is(1990))
                .body("min[0].followingWin", is(1991))
                .body("max[0].producer", is("Matthew Vaughn"))
                .body("max[0].interval", is(13))
                .body("max[0].previousWin", is(2002))
                .body("max[0].followingWin", is(2015));
    }

}
