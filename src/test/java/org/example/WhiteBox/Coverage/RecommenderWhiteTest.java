package org.example.WhiteBoxTesting;

import java.util.ArrayList;
import java.util.List;

import org.example.recommendation.*;
import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecommenderWhiteTest {

    @Test
    void CoverageTest() throws ValidationException {

        // Arrange
        Movie m1 = new Movie(
                "Transporter",
                "T001",
                List.of("Action")
        );

        Movie m2 = new Movie(
                "Hulk",
                "H002",
                List.of("Action")
        );

        Movie m3 = new Movie(
                "Scent Of A Woman",
                "SOAW003",
                List.of("Drama")
        );

        Recommender recommender = new Recommender(List.of(m1, m2, m3));

        List<User> users = new ArrayList<>();

        User user = new User(
                "Khaled",
                "12345678A",
                List.of("T001")
        );

        users.add(user);

        // Act
        List<UserRecommendations> result = recommender.recommendForAllUsers(users);

        UserRecommendations userRec = result.get(0);
        List<Movie> recommendedMovies = userRec.recommendations();
        // Assert
        assertEquals(1, recommendedMovies.size());
        assertTrue(recommendedMovies.contains(m2));
        assertFalse(recommendedMovies.contains(m1));
        assertFalse(recommendedMovies.contains(m3));

    }
}
