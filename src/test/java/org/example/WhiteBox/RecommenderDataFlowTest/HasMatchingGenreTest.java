package org.example.WhiteBox.RecommenderDataFlowTest;

import org.example.model.Movie;
import org.example.recommendation.Recommender;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HasMatchingGenreTest {
    @Test
    void hasMatchingGenre_AllDefs() throws Exception {
        // DEF: genre loop variable

        Movie movie = new Movie("Action Blast", "AB123", List.of("Action"));
        Recommender recommender = new Recommender(List.of());

        Method method = Recommender.class
                .getDeclaredMethod("hasMatchingGenre", Movie.class, Set.class);
        method.setAccessible(true);

        boolean result =
                (boolean) method.invoke(recommender, movie, Set.of("Action"));

        assertTrue(result);
    }

    @Test
    void hasMatchingGenre_AllUses() throws Exception {
        // USE:
        // likedGenres.contains(genre)

        Movie movie = new Movie("Drama King", "DK123", List.of("Drama"));
        Recommender recommender = new Recommender(List.of());

        Method method = Recommender.class
                .getDeclaredMethod("hasMatchingGenre", Movie.class, Set.class);
        method.setAccessible(true);

        boolean result =
                (boolean) method.invoke(recommender, movie, Set.of("Drama"));

        assertTrue(result);
    }
    @Test
    void hasMatchingGenre_AllDUPaths() throws Exception {
        // DU PATH:
        // genre -> contains() -> return true

        Movie movie = new Movie("Horror Night", "HN456", List.of("Horror"));
        Recommender recommender = new Recommender(List.of());

        Method method = Recommender.class
                .getDeclaredMethod("hasMatchingGenre", Movie.class, Set.class);
        method.setAccessible(true);

        boolean result =
                (boolean) method.invoke(recommender, movie, Set.of("Horror"));

        assertTrue(result);
    }

}
