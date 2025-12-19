package org.example.unit.app;

import org.example.app.MovieRecSystem;
import org.example.exception.ValidationException;
import org.example.io.loader.MovieLoader;
import org.example.io.loader.UserLoader;
import org.example.io.output.OutputWriter;
import org.example.model.Movie;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class MovieRecSystemTest {
    @Test
    public void MovieRecSystemTest() throws ValidationException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        System.setOut(out);
        MovieLoader movieLoader = mock(MovieLoader.class);
        UserLoader userLoader = mock(UserLoader.class);
        OutputWriter writer = mock(OutputWriter.class);
        MovieRecSystem movieRecSystem = new MovieRecSystem(movieLoader, userLoader, writer);
        List<Movie> movies = List.of(new Movie("Superman", "S001", List.of("Action"))
                , new Movie("The Dark Knight", "TDK002", List.of("Action")),
                new Movie("Rango", "R003", List.of("Animation")),
                new Movie("Soul", "S003", List.of("Animation"))
        );

        List<User> users = List.of(new User("Ali", "123456789", List.of("S001")),
                new User("Bob", "12345678e", List.of("R003"))
        );

        when(movieLoader.load()).thenReturn(movies);
        when(userLoader.load()).thenReturn(users);
        doAnswer(invocationOnMock -> {
            System.out.println("Success");
            return null;
        }).when(writer).writeRecommendations(anyList());
        movieRecSystem.run();

        assertTrue(baos.toString().contains("Success"));

        verify(movieLoader).load();
        verify(userLoader).load();
        verify(writer).writeRecommendations(anyList());
    }
}
