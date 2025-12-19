package org.example.WhiteBox.RecommenderDataFlowTest;

import org.example.model.Movie;
import org.example.recommendation.Recommender;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FindMatchingMoviesTest {
    @Test
    void findMatchingMovies_AllDefs() throws Exception {
        // DEF: recommendations

        Movie movie = new Movie("Comedy Show", "CS123", List.of("Comedy"));
        Recommender recommender = new Recommender(List.of(movie));

        Method method = Recommender.class
                .getDeclaredMethod("findMatchingMovies", Set.class, Set.class);
        method.setAccessible(true);

        List<Movie> result =
                (List<Movie>) method.invoke(
                        recommender,
                        Set.of(),
                        Set.of("Comedy")
                );

        assertNotNull(result);
    }

    @Test
    void findMatchingMovies_AllUses() throws Exception {
        // USE:
        // likedMovieIds.contains()
        // recommendations.add()

        Movie m1 = new Movie("Fun Time", "FT123", List.of("Comedy"));
        Movie m2 = new Movie("Laugh Hard", "LH456", List.of("Comedy"));

        Recommender recommender = new Recommender(List.of(m1, m2));

        Method method = Recommender.class
                .getDeclaredMethod("findMatchingMovies", Set.class, Set.class);
        method.setAccessible(true);

        List<Movie> result =
                (List<Movie>) method.invoke(
                        recommender,
                        Set.of("FT123"),
                        Set.of("Comedy")
                );

        assertEquals(1, result.size());
    }

    @Test
    void findMatchingMovies_AllDUPaths() throws Exception {
        // DU PATH:
        // recommendations -> add() -> return

        Movie movie = new Movie("Romantic Story", "RS123", List.of("Drama"));
        Recommender recommender = new Recommender(List.of(movie));

        Method method = Recommender.class
                .getDeclaredMethod("findMatchingMovies", Set.class, Set.class);
        method.setAccessible(true);

        List<Movie> result =
                (List<Movie>) method.invoke(
                        recommender,
                        Set.of(),
                        Set.of("Drama")
                );

        assertEquals("RS123", result.get(0).getId());
    }

}
