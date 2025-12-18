package org.example.integration.bottomup;

import org.example.exception.ValidationException;
import org.example.io.loader.MovieLoader;
import org.example.io.loader.UserLoader;
import org.example.io.loader.component.FileReader;
import org.example.io.loader.component.IdValidator;
import org.example.io.loader.component.ParsedEntity;
import org.example.io.loader.component.TwoLineParser;
import org.example.model.Movie;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class EntityLoaderIntegrationTesting {
    @Test
    public void testValidUsers() throws ValidationException, IOException{
        String userInput = "./src/test/resources/integration/bottomup/LoaderFiles/sample.txt\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());

        FileReader fileReader = new FileReader(new Scanner(bais));
        TwoLineParser parser = new TwoLineParser();
        IdValidator idValidator = new IdValidator();

        List<ParsedEntity> expected = List.of(
                new ParsedEntity("Omar Tamer", "123456789", List.of("MML001", "SM001", "FG001")),
                new ParsedEntity("Ali Tamer", "12345678m", List.of("LL001", "SV001", "V001")),
                new ParsedEntity("Nada Ali", "15467892b", List.of("LL002", "LM001", "TT002"))
        );

        UserLoader loader = new UserLoader(fileReader, parser, idValidator);

        List<User> actual = loader.load();

        assertEquals(3, actual.size());

        assertEquals(expected.get(0).getName(), actual.get(0).getUserName());
        assertEquals(expected.get(0).getId(), actual.get(0).getUserId());
        assertEquals(expected.get(0).getAttributes(), actual.get(0).getMoviesId());

        assertEquals(expected.get(1).getName(), actual.get(1).getUserName());
        assertEquals(expected.get(1).getId(), actual.get(1).getUserId());
        assertEquals(expected.get(1).getAttributes(), actual.get(1).getMoviesId());

        assertEquals(expected.get(2).getName(), actual.get(2).getUserName());
        assertEquals(expected.get(2).getId(), actual.get(2).getUserId());
        assertEquals(expected.get(2).getAttributes(), actual.get(2).getMoviesId());
    }

    @Test
    public void testDuplicateIdUsers() throws ValidationException, IOException{
        String userInput = "./src/test/resources/integration/bottomup/LoaderFiles/InvalidSample.txt\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());

        FileReader fileReader = new FileReader(new Scanner(bais));
        TwoLineParser parser = new TwoLineParser();
        IdValidator idValidator = new IdValidator();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        System.setOut(out);
        UserLoader loader = new UserLoader(fileReader, parser, idValidator);

        assertThrows(ValidationException.class, loader::load);
    }
    
    @Test
    public void testValidMoviesName() throws ValidationException, IOException{
        String userInput = "./src/test/resources/integration/bottomup/LoaderFiles/Movies/Movies.txt\n";
        InputStream bais = new ByteArrayInputStream(userInput.getBytes());

        FileReader fileReader = new FileReader(new Scanner(bais));
        TwoLineParser parser = new TwoLineParser();
        IdValidator idValidator = new IdValidator();


        MovieLoader loader = new MovieLoader(fileReader, parser, idValidator);
        List<Movie> actual = loader.load();
        assertEquals(3, actual.size());

        assertEquals("Ne Zha", actual.get(0).getTitle());
        assertEquals("NZ001", actual.get(0).getId());
        assertEquals(List.of("Action", "Adventure", "Comedy"), actual.get(0).getGenre());

        assertEquals("The Wild Robot", actual.get(1).getTitle());
        assertEquals("TWR001", actual.get(1).getId());
        assertEquals(List.of("Animation", "Drama", "Family"), actual.get(1).getGenre());

        assertEquals("The Little Prince", actual.get(2).getTitle());
        assertEquals("TLP001", actual.get(2).getId());
        assertEquals(List.of("Animation", "Family", "Adventure"), actual.get(2).getGenre());
    }

    @Test
    public void testDuplicateMoviesIds() throws ValidationException, IOException{
        String userInput = "./src/test/resources/integration/bottomup/LoaderFiles/Movies/duplicateIdMovies.txt\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());

        FileReader fileReader = new FileReader(new Scanner(bais));
        TwoLineParser parser = new TwoLineParser();
        IdValidator idValidator = new IdValidator();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);
        System.setOut(out);
        MovieLoader loader = new MovieLoader(fileReader, parser, idValidator);

        assertThrows(ValidationException.class, loader::load);
    }
}
