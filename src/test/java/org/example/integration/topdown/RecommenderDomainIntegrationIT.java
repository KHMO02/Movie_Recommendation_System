package org.example.integration.topdown;

import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.Recommender;
import org.example.recommendation.UserRecommendations;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecommenderDomainIntegrationIT
{

    @Test
    void recommendForAllUsers_respectsLikedGenresAndExcludesLikedMovies() throws ValidationException
    {
        // Movies:
        // - Same genre as user1's liked movie (Sci-Fi)
        // - Different genre only (Comedy)
        // - Mixed genres
        Movie m1 = new Movie("Space Adventure", "SA001", List.of("Sci-Fi"));          // will be liked by user1
        List<UserRecommendations> allRecs = getUserRecommendations(m1);

        // Assert size: one entry per user
        assertEquals(2, allRecs.size());

        // user1
        UserRecommendations ur1 = allRecs.getFirst();
        assertEquals("123456789", ur1.user().getUserId());

        // user1 should be recommended DS002 only
        assertEquals(1, ur1.recommendations().size());
        assertEquals("Deep Space", ur1.recommendations().getFirst().getTitle());

        // user2
        UserRecommendations ur2 = allRecs.get(1);
        assertEquals("12345678A", ur2.user().getUserId());
        // user2 has no other Action movies; liked movie AH004 must be excluded
        assertEquals(0, ur2.recommendations().size());
    }

    private static List<UserRecommendations> getUserRecommendations(Movie m1) throws ValidationException
    {
        Movie m2 = new Movie("Deep Space", "DS002", List.of("Sci-Fi", "Drama"));  // same genre as m1
        Movie m3 = new Movie("Funny Times", "FT003", List.of("Comedy"));          // no Sci-Fi
        Movie m4 = new Movie("Action Hero", "AH004", List.of("Action"));          // for user2

        List<Movie> allMovies = List.of(m1, m2, m3, m4);

        // Users:
        // user1 likes SA001 (Sci-Fi) -> should get DS002, but not SA001 itself and not Comedy-only movie
        // user2 likes AH004 (Action) -> no other Action movies -> gets no recommendations
        User user1 = new User("Omar Tamer", "123456789", List.of("SA001"));
        User user2 = new User("Ali Tamer", "12345678A", List.of("AH004"));

        List<User> users = List.of(user1, user2);

        Recommender recommender = new Recommender(allMovies);

        // Act
        return recommender.recommendForAllUsers(users);
    }
}
