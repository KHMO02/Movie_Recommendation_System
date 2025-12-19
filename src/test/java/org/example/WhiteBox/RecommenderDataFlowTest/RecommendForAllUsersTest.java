package org.example.WhiteBox.RecommenderDataFlowTest;

import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.Recommender;
import org.example.recommendation.UserRecommendations;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendForAllUsersTest {
    @Test
    void recommendForAllUsers_AllDefs() throws ValidationException {
        // DEF: allRecommendations

        Movie movie = new Movie("Fast Furious", "FF123", List.of("Action"));

        User user = new User(
                "Ali Hassan",
                "22223333D",
                List.of("FF123")
        );

        Recommender recommender = new Recommender(List.of(movie));
        List<UserRecommendations> result =
                recommender.recommendForAllUsers(List.of(user));

        assertNotNull(result);
    }

    @Test
    void recommendForAllUsers_AllUses() throws ValidationException {
        // USE:
        // allRecommendations.add()
        // loop variable user

        Movie movie = new Movie("Mission Impossible", "MI123", List.of("Action"));

        User u1 = new User("Sara Ahmed", "33334444E", List.of("MI123"));
        User u2 = new User("Nour Adel", "44445555F", List.of("MI123"));

        Recommender recommender = new Recommender(List.of(movie));
        List<UserRecommendations> result =
                recommender.recommendForAllUsers(List.of(u1, u2));

        assertEquals(2, result.size());
    }

    @Test
    void recommendForAllUsers_AllDUPaths() throws ValidationException {
        // DU PATH:
        // allRecommendations -> add() -> return

        Movie movie = new Movie("Avatar Movie", "AM123", List.of("Fantasy"));

        User user = new User(
                "Khaled Mahmoud",
                "55556666G",
                List.of("AM123")
        );

        Recommender recommender = new Recommender(List.of(movie));
        List<UserRecommendations> result =
                recommender.recommendForAllUsers(List.of(user));

        assertEquals(1, result.size());
    }

}