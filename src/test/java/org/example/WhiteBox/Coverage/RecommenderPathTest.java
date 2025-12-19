package org.example.WhiteBoxTesting;

import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.Recommender;
import org.example.recommendation.UserRecommendations;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecommenderPathTest {

    /* ================= CFG 1: Users loop ================= */

    // Path 1: No users found
    @Test
    void noUsersFound_path() {
        Recommender recommender = new Recommender(List.of());

        List<UserRecommendations> result =
                recommender.recommendForAllUsers(List.of());

        assertTrue(result.isEmpty());
    }

    // Path 2: At least one user exists
    @Test
    void userExists_path() throws ValidationException {
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        Movie m2 = new Movie("Avatar", "A002", List.of("Action"));

        User user = new User("Khaled", "12345678A", List.of("M001"));

        Recommender recommender = new Recommender(List.of(m1, m2));

        List<UserRecommendations> result =
                recommender.recommendForAllUsers(List.of(user));

        assertEquals(1, result.size());
    }

    /* ================= CFG 2: Movie processing ================= */

    // Path 1: No movies in movies list
    @Test
    void noMovies_path() throws ValidationException {
        User user = new User("Khaled", "12345678A", List.of("X001"));

        Recommender recommender = new Recommender(List.of());

        assertTrue(recommender.recommendForUser(user).isEmpty());
    }

    // Path 2: User likes at least one movie
    @Test
    void likedMovie_path() throws ValidationException {
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        Movie m2 = new Movie("Avatar", "A002", List.of("Action"));

        User user = new User("Khaled", "12345678A", List.of("M001"));

        Recommender recommender = new Recommender(List.of(m1, m2));

        List<Movie> result = recommender.recommendForUser(user);

        assertTrue(result.contains(m2));
    }

    // Path 3: Movie not liked & genre not matched
    @Test
    void noLikeNoGenreMatch_path() throws ValidationException {
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        Movie m2 = new Movie("Titanic", "T002", List.of("Drama"));

        User user = new User("Khaled", "12345678A", List.of("M001"));

        Recommender recommender = new Recommender(List.of(m1, m2));

        List<Movie> result = recommender.recommendForUser(user);

        assertTrue(result.isEmpty());
    }

    // Path 4: Movie not liked but genre matched
    @Test
    void genreMatched_path() throws ValidationException {
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        Movie m2 = new Movie("Avatar", "A002", List.of("Action"));

        User user = new User("Khaled", "12345678A", List.of("M001"));

        Recommender recommender = new Recommender(List.of(m1, m2));

        List<Movie> result = recommender.recommendForUser(user);

        assertEquals(1, result.size());
        assertTrue(result.contains(m2));
    }

    /* ================= CFG 3: Genre matching ================= */

    // Path 1: Movie has no genres
    @Test
    void noGenres_path() throws ValidationException {
        Movie m1 = new Movie("Matrix", "M001", List.of());

        User user = new User("Khaled", "12345678A", List.of("M001"));

        Recommender recommender = new Recommender(List.of(m1));

        assertTrue(recommender.recommendForUser(user).isEmpty());
    }

    // Path 2: Genres exist but none matched
    @Test
    void genreNotMatched_path() throws ValidationException {
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        Movie m2 = new Movie("Titanic", "T002", List.of("Drama"));

        User user = new User("Khaled", "12345678A", List.of("M001"));

        Recommender recommender = new Recommender(List.of(m1));

        assertTrue(recommender.recommendForUser(user).isEmpty());
    }

    // Path 3: Genre matched
    @Test
    void genreMatchedLoop_path() throws ValidationException {
        Movie m1 = new Movie("Matrix", "M001", List.of("Action"));
        Movie m2 = new Movie("Avatar", "A002", List.of("Action"));

        User user = new User("Khaled", "12345678A", List.of("M001"));

        Recommender recommender = new Recommender(List.of(m1, m2));

        assertTrue(recommender.recommendForUser(user).contains(m2));
    }
}
