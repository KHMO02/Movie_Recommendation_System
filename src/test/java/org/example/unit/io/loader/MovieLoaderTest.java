package org.example.unit.io.loader;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;
import org.example.io.loader.MovieLoader;
import org.example.io.loader.component.FileReader;
import org.example.io.loader.component.IdValidator;
import org.example.io.loader.component.ParsedEntity;
import org.example.io.loader.component.TwoLineParser;
import org.example.model.Movie;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieLoaderTest {

    @Test
    void loadMovies_validInput_createsMoviesFromParsedEntities() throws IOException, ValidationException {
        FileReader fileReader = mock(FileReader.class);
        TwoLineParser parser = mock(TwoLineParser.class);
        IdValidator idValidator = mock(IdValidator.class);

        List<String> lines = List.of("ignored");
        List<ParsedEntity> parsed = List.of(
                new ParsedEntity("Ne Zha", "NZ001", List.of("Action", "Adventure", "Comedy")),
                new ParsedEntity("The Wild Robot", "TWR001", List.of("Animation", "Drama", "Family")),
                new ParsedEntity("The Little Prince", "TLP001", List.of("Animation", "Family", "Adventure"))
        );

        when(fileReader.readLines(anyString())).thenReturn(lines);
        when(parser.parse(lines, ErrorMessage.FILE_MOVIES_FORMAT)).thenReturn(parsed);

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

        verify(fileReader).readLines(startsWith("Enter movies"));
        verify(parser).parse(lines, ErrorMessage.FILE_MOVIES_FORMAT);
        verify(idValidator).validateAllUnique(parsed, ErrorMessage.MOVIE_ID_NOT_UNIQUE);
    }

    @Test
    void loadMovies_duplicateId_propagatesValidationException() throws IOException, ValidationException {
        FileReader fileReader = mock(FileReader.class);
        TwoLineParser parser = mock(TwoLineParser.class);
        IdValidator idValidator = mock(IdValidator.class);

        List<String> lines = List.of("ignored");
        List<ParsedEntity> parsed = List.of(
                new ParsedEntity("Movie Master", "MM001", List.of("Action")),
                new ParsedEntity("Other Movie", "MM001", List.of("Drama"))
        );

        when(fileReader.readLines(anyString())).thenReturn(lines);
        when(parser.parse(lines, ErrorMessage.FILE_MOVIES_FORMAT)).thenReturn(parsed);
        doThrow(new ValidationException(ErrorMessage.MOVIE_ID_NOT_UNIQUE, "Movie Master"))
                .when(idValidator).validateAllUnique(parsed, ErrorMessage.MOVIE_ID_NOT_UNIQUE);

        MovieLoader loader = new MovieLoader(fileReader, parser, idValidator);

        ValidationException ex = assertThrows(
                ValidationException.class,
                loader::load
        );

        assertEquals(
                ErrorMessage.MOVIE_ID_NOT_UNIQUE.format("Movie Master"),
                ex.getMessage()
        );
    }
}
