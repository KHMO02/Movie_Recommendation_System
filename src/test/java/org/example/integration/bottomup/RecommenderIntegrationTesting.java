package org.example.integration.bottomup;

import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.Recommender;
import org.example.recommendation.UserRecommendations;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class RecommenderIntegrationTesting{
    @Test
    public void recommenderIntegrationSingleUserTest() throws ValidationException{
        List<Movie> movies = List.of(new Movie("Iron Man", "IM001", List.of("Action", "Sci-Fi")),
                new Movie("Superman", "S001", List.of("Action")),
                new Movie("Dark", "D001", List.of("Horror"))
        );
        Recommender recommender = new Recommender(movies);
        User one = new User("Fathy Alaa", "123456789", List.of("IM001"));
        List<Movie> actual = recommender.recommendForUser(one);
        assertEquals(1, actual.size());
        assertEquals("Superman", actual.get(0).getTitle());
    }

    @Test
    public void recommenderIntegrationMultipleUserTest() throws ValidationException{
        List<Movie> movies = List.of(new Movie("Iron Man", "IM001", List.of("Action", "Sci-Fi")),
                new Movie("The Dark Knight", "TDK001", List.of("Action")),
                new Movie("Dark", "D001", List.of("Horror")),
                new Movie("Prisoners", "P001", List.of("Mystery")),
                new Movie("The Invisible Guest", "TIG001", List.of("Mystery")),
                new Movie("Scream", "S001", List.of("Horror"))
        );
        Recommender recommender = new Recommender(movies);
        List<User> users = List.of(new User("Fathy Alaa", "123456789", List.of("IM001")),
                new User("Omar Tamer", "12345678m", List.of("P001")),
                new User("Adham Hossam", "12345678i", List.of("S001"))
        );

        List<UserRecommendations> actual = recommender.recommendForAllUsers(users);
        assertEquals(3, actual.size());

        assertEquals("The Dark Knight", actual.getFirst().recommendations().getFirst().getTitle());
        assertEquals("The Invisible Guest", actual.get(1).recommendations().getFirst().getTitle());
        assertEquals("Dark", actual.get(2).recommendations().getFirst().getTitle());
    }
}
