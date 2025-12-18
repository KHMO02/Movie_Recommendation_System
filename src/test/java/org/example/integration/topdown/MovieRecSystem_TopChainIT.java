package org.example.integration.topdown;

import org.example.app.MovieRecSystem;
import org.example.exception.ValidationException;
import org.example.io.loader.MovieLoader;
import org.example.io.loader.UserLoader;
import org.example.io.output.OutputWriter;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.UserRecommendations;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class MovieRecSystem_TopChainIT
{

    @Test
    void run_passesExpectedRecommendationsToWriter() throws IOException, ValidationException
    {
        MovieLoader movieLoader = mock(MovieLoader.class);
        UserLoader userLoader   = mock(UserLoader.class);
        OutputWriter writer     = mock(OutputWriter.class);

        Movie movie1;
        Movie movie2;
        try
        {
            movie1 = new Movie("Inception Movie", "IM123", List.of("Sci-Fi", "Thriller"));
            movie2 = new Movie("Dark Knight", "DK456", List.of("Sci-Fi", "Drama"));
        }
        catch (ValidationException e)
        {
            throw new AssertionError("Invalid movie test data: " + e.getMessage(), e);
        }

        when(movieLoader.load()).thenReturn(List.of(movie1, movie2));

        User user1;
        User user2;
        try
        {
            user1 = new User("Alice Smith", "12345678A", List.of("IM123"));   // likes Inception Movie
            user2 = new User("Bob Johnson", "987654321", List.of("DK456"));   // likes Dark Knight
        }
        catch (ValidationException e)
        {
            throw new AssertionError("Invalid user test data: " + e.getMessage(), e);
        }

        when(userLoader.load()).thenReturn(List.of(user1, user2));

        // capture variable
        final List<UserRecommendations>[] captured = new List[1];

        // intercept the call to writeRecommendations and store the argument
        doAnswer(invocation ->
                 {
                     captured[0] = invocation.getArgument(0);
                     return null;
                 }).when(writer).writeRecommendations(anyList());

        MovieRecSystem app = new MovieRecSystem(movieLoader, userLoader, writer);

        // Act
        try
        {
            app.run();
        }
        catch (Exception e)
        {
            throw new AssertionError("app.run() threw unexpectedly: " + e.getMessage(), e);
        }

        // Assert on captured recommendations
        List<UserRecommendations> recs = captured[0];
        assertEquals(2, recs.size());

        UserRecommendations ur1 = recs.get(0);
        assertEquals("12345678A", ur1.user().getUserId());
        assertEquals("Alice Smith", ur1.user().getUserName());
        assertEquals(1, ur1.recommendations().size());
        assertEquals("Dark Knight", ur1.recommendations().get(0).getTitle());

        UserRecommendations ur2 = recs.get(1);
        assertEquals("987654321", ur2.user().getUserId());
        assertEquals("Bob Johnson", ur2.user().getUserName());
        assertEquals(1, ur2.recommendations().size());
        assertEquals("Inception Movie", ur2.recommendations().get(0).getTitle());

        verify(writer, never()).writeError(anyString());
    }
}
