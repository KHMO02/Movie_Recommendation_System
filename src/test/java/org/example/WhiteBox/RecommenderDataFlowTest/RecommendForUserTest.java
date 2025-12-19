package org.example.WhiteBox.RecommenderDataFlowTest;

import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.Recommender;
import org.example.recommendation.UserRecommendations;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendForUserTest {

    @Test
    void recommendForUser_AllDefs() throws ValidationException {
        // DEF: likedMovieIds, likedGenres

        Movie m1 = new Movie("The Dark Knight", "TDK123", List.of("Action"));
        Movie m2 = new Movie("Inception Movie", "IM456", List.of("Action"));

        User user = new User(
                "Ahmed Hassan",
                "12345678A",
                List.of("TDK123")
        );

        Recommender recommender = new Recommender(List.of(m1, m2));
        List<Movie> result = recommender.recommendForUser(user);

        assertNotNull(result);
    }

    @Test
    void recommendForUser_AllUses() throws ValidationException {
        // USE:
        // likedMovieIds.contains()
        // likedGenres used in hasMatchingGenre()

        Movie m1 = new Movie("The Matrix", "TM123", List.of("Action"));
        Movie m2 = new Movie("John Wick", "JW456", List.of("Action"));

        User user = new User(
                "Omar Ali",
                "87654321B",
                List.of("TM123")
        );

        Recommender recommender = new Recommender(List.of(m1, m2));
        List<Movie> result = recommender.recommendForUser(user);

        assertEquals(1, result.size());
    }

    @Test
    void recommendForUser_AllDUPaths() throws ValidationException {
        // DU PATH:
        // likedMovieIds -> collectLikedGenres -> findMatchingMovies

        Movie m1 = new Movie("Star Wars", "SW123", List.of("SciFi"));
        Movie m2 = new Movie("Galaxy Quest", "GQ456", List.of("SciFi"));

        User user = new User(
                "Mohamed Adel",
                "11112222C",
                List.of("SW123")
        );

        Recommender recommender = new Recommender(List.of(m1, m2));
        List<Movie> result = recommender.recommendForUser(user);

        assertEquals("GQ456", result.get(0).getId());
    }

}
