package org.example.unit.recommendation;

import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.Recommender;
import org.example.recommendation.UserRecommendations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommenderTest
{

    private Recommender recommender;

    @BeforeEach
    void setUp() throws ValidationException
    {
        Movie m1 = new Movie("The Batman", "TB001", List.of("Action"));
        Movie m2 = new Movie("Superman Returns", "SR001", List.of("Action", "Comedy"));
        Movie m3 = new Movie("Funny Movie", "FM001", List.of("Comedy"));

        List<Movie> allMovies = List.of(m1, m2, m3);
        recommender = new Recommender(allMovies);
    }

    @Test
    void recommendForUser_likedGenreRecommendations() throws ValidationException
    {
        User u = new User("John Wick", "123456789", List.of("TB001"));

        List<Movie> rec = recommender.recommendForUser(u);

        assertEquals(1, rec.size());
        assertEquals("Superman Returns", rec.getFirst().getTitle());
    }

    @Test
    void recommendForUser_noRecommendationWhenNoGenreMatch() throws ValidationException
    {
        User u = new User("Neo", "987654321", List.of("NON_EXISTENT"));

        List<Movie> rec = recommender.recommendForUser(u);

        assertTrue(rec.isEmpty());
    }

    @Test
    void recommendForUser_multiGenreMatch() throws ValidationException
    {
        Movie m1 = new Movie("Scream", "SCR001", List.of("Horror", "Thriller"));
        Movie m2 = new Movie("Se7en", "SEV001", List.of("Thriller", "Crime"));
        Movie m3 = new Movie("The Nun", "NUN001", List.of("Horror"));
        Movie m4 = new Movie("Superman Returns", "SR001", List.of("Thriller"));
        Movie m5 = new Movie("Funny Movie", "FM001", List.of("Comedy"));

        List<Movie> movies = Arrays.asList(m1, m2, m3, m4, m5);
        Recommender localRec = new Recommender(movies);

        User u = new User("Ghostface", "123456780", List.of("SCR001"));

        List<Movie> recommendations = localRec.recommendForUser(u);

        assertEquals(3, recommendations.size());
        assertEquals("Se7en", recommendations.get(0).getTitle());
        assertEquals("The Nun", recommendations.get(1).getTitle());
        assertEquals("Superman Returns", recommendations.get(2).getTitle());
    }

    @Test
    void recommendForUser_sameGenresOnce() throws ValidationException
    {
        Movie m1 = new Movie("Scream", "SCR001", Arrays.asList("Horror", "Thriller"));
        Movie m2 = new Movie("Se7en", "SEV001", Arrays.asList("Horror", "Thriller"));

        List<Movie> movies = Arrays.asList(m1, m2);
        Recommender localRec = new Recommender(movies);

        User user = new User("Ghostface", "123456781", Collections.singletonList("SCR001"));

        List<Movie> recommendations = localRec.recommendForUser(user);

        assertEquals(1, recommendations.size());
        assertEquals("Se7en", recommendations.getFirst().getTitle());
    }

    @Test
    void recommendForUser_noLikes() throws ValidationException
    {
        Movie m1 = new Movie("Movie A", "MOV001", Collections.singletonList("Drama"));
        List<Movie> movies = Collections.singletonList(m1);
        Recommender localRec = new Recommender(movies);

        User user = new User("Bored User", "123456782", Collections.emptyList());

        List<Movie> recommendations = localRec.recommendForUser(user);

        assertTrue(recommendations.isEmpty());
    }

    @Test
    void recommendForAllUsers_buildsUserRecommendationsPerUser() throws ValidationException
    {
        Movie action = new Movie("Iron Man", "IRM001", List.of("Action"));
        Movie hulk = new Movie("Hulk", "HUL001", List.of("Action"));
        Movie drama = new Movie("Notebook", "NTB001", List.of("Drama"));

        List<Movie> movies = Arrays.asList(action, hulk, drama);
        Recommender localRec = new Recommender(movies);

        User u1 = new User("User One", "123456783", List.of("IRM001"));
        User u2 = new User("User Two", "123456784", List.of("NTB001"));

        List<UserRecommendations> allRecs = localRec.recommendForAllUsers(List.of(u1, u2));

        assertEquals(2, allRecs.size());
        assertEquals("User One", allRecs.get(0).user().getUserName());
        assertEquals("User Two", allRecs.get(1).user().getUserName());

        assertEquals(1, allRecs.get(0).recommendations().size());
        assertEquals("Hulk", allRecs.get(0).recommendations().getFirst().getTitle());

        assertTrue(allRecs.get(1).recommendations().isEmpty());
    }

}
