package org.example.integration.bottomup;

import org.example.app.MovieRecSystem;
import org.example.exception.ValidationException;
import org.example.io.loader.MovieLoader;
import org.example.io.loader.UserLoader;
import org.example.io.loader.component.FileReader;
import org.example.io.loader.component.IdValidator;
import org.example.io.loader.component.TwoLineParser;
import org.example.io.output.OutputWriter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MovieRecSystemIntegrationTesting {
    @Test
    public void MovieRec() throws ValidationException, IOException {
        String MoviePath = "src/test/resources/integration/bottomup/MovieRecSystemFiles/Movies.txt";
        InputStream inputStream = new ByteArrayInputStream(MoviePath.getBytes());
        MovieLoader movieLoader = new MovieLoader(
                new FileReader(new Scanner(new ByteArrayInputStream(MoviePath.getBytes())))
                , new TwoLineParser(), new IdValidator());
        String UserPath = "src/test/resources/integration/bottomup/MovieRecSystemFiles/Users.txt";
        UserLoader userLoader = new UserLoader(
                new FileReader(new Scanner(new ByteArrayInputStream(UserPath.getBytes()))),
                new TwoLineParser(), new IdValidator());

        OutputWriter writer = new OutputWriter();
        MovieRecSystem app = new MovieRecSystem(movieLoader, userLoader, writer);
        app.run();
        Path output = Path.of("recommendations.txt");
        List<String> actual = Files.readAllLines(output);
        assertEquals(6, actual.size());
        //assert names
        assertTrue(actual.get(0).contains("Ali Tamer"));
        assertTrue(actual.get(2).contains("Nada Fathy"));
        assertTrue(actual.get(4).contains("Noor Ali"));
        //assert Ids
        assertTrue(actual.get(0).contains("123456789"));
        assertTrue(actual.get(2).contains("12345678k"));
        assertTrue(actual.get(4).contains("12345678o"));
        //assert Movie title
        assertTrue(actual.get(1).contains("John Wick"));
        assertTrue(actual.get(3).contains("The Invisible Guest"));
        assertTrue(actual.get(5).contains("The Wild Robot"));
        Files.deleteIfExists(output);
    }
}
