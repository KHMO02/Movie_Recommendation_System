package org.example.WhiteBox.RecommenderDataFlowTest;

import org.example.model.Movie;
import org.example.recommendation.Recommender;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CollectLikedGenresTest {
    @Test
    void collectLikedGenres_AllDefs() throws Exception {
        // DEF: genres

        Movie movie = new Movie("Horror Night", "HN123", List.of("Horror"));
        Recommender recommender = new Recommender(List.of(movie));

        Method method = Recommender.class
                .getDeclaredMethod("collectLikedGenres", Set.class);
        method.setAccessible(true);

        Set<String> result =
                (Set<String>) method.invoke(recommender, Set.of("HN123"));

        assertNotNull(result);
    }

    @Test
    void collectLikedGenres_AllUses() throws Exception {
        // USE:
        // likedMovieIds.contains()
        // genres.addAll()

        Movie m1 = new Movie("Action Hero", "AH123", List.of("Action"));
        Movie m2 = new Movie("Drama Life", "DL456", List.of("Drama"));

        Recommender recommender = new Recommender(List.of(m1, m2));

        Method method = Recommender.class
                .getDeclaredMethod("collectLikedGenres", Set.class);
        method.setAccessible(true);

        Set<String> result =
                (Set<String>) method.invoke(recommender, Set.of("AH123", "DL456"));

        assertEquals(2, result.size());
    }
    @Test
    void collectLikedGenres_AllDUPaths() throws Exception {
        // DU PATH:
        // genres -> addAll() -> return

        Movie movie = new Movie("Scary House", "SH123", List.of("Horror"));
        Recommender recommender = new Recommender(List.of(movie));

        Method method = Recommender.class
                .getDeclaredMethod("collectLikedGenres", Set.class);
        method.setAccessible(true);

        Set<String> result =
                (Set<String>) method.invoke(recommender, Set.of("SH123"));

        assertTrue(result.contains("Horror"));
    }

}