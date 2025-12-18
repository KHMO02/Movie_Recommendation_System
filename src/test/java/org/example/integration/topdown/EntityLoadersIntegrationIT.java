package org.example.integration.topdown;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;
import org.example.io.loader.MovieLoader;
import org.example.io.loader.UserLoader;
import org.example.io.loader.component.FileReader;
import org.example.io.loader.component.IdValidator;
import org.example.io.loader.component.TwoLineParser;
import org.example.model.Movie;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class EntityLoadersIntegrationIT
{

    private Path resourcePath(String relative)
    {
        try
        {
            return Path.of(Objects.requireNonNull(getClass().getClassLoader().getResource(relative)).toURI());
        }
        catch (URISyntaxException | NullPointerException e)
        {
            throw new AssertionError("Test resource not found: " + relative, e);
        }
    }

    /**
     * FileReader variant that ignores the prompt and always reads a fixed test file.
     */
    private FileReader fileReaderFor(String resourceRelativePath)
    {
        Path path = resourcePath(resourceRelativePath);
        return new FileReader()
        {
            @Override
            public List<String> readLines(String ignoredPrompt) throws IOException
            {
                return Files.readAllLines(path);
            }
        };
    }

    private MovieLoader movieLoaderFor(String resourceRelativePath)
    {
        FileReader    fileReader  = fileReaderFor(resourceRelativePath);
        TwoLineParser parser      = new TwoLineParser();
        IdValidator   idValidator = new IdValidator();

        return new MovieLoader(fileReader, parser, idValidator);
    }

    private UserLoader userLoaderFor(String resourceRelativePath)
    {
        FileReader    fileReader  = fileReaderFor(resourceRelativePath);
        TwoLineParser parser      = new TwoLineParser();
        IdValidator   idValidator = new IdValidator();

        return new UserLoader(fileReader, parser, idValidator);
    }

    // ---------- GOOD FILES ----------

    @Test
    void movieLoader_loadsMoviesFromValidFile() throws IOException, ValidationException
    {
        MovieLoader movieLoader = movieLoaderFor("integration/topdown/valid movies.txt");

        List<Movie> movies = movieLoader.load();

        assertFalse(movies.isEmpty());
        Movie first = movies.getFirst();
        assertNotNull(first.getId());
        assertNotNull(first.getTitle());
        assertFalse(first.getGenre().isEmpty());
    }

    @Test
    void userLoader_loadsUsersFromValidFile() throws IOException, ValidationException
    {
        UserLoader userLoader = userLoaderFor("integration/topdown/valid users.txt");

        List<User> users = userLoader.load();

        assertFalse(users.isEmpty());
        User first = users.getFirst();
        assertNotNull(first.getUserId());
        assertNotNull(first.getUserName());
        assertFalse(first.getMoviesId().isEmpty());
    }

    // ---------- DUPLICATE ID FILES ----------

    @Test
    void movieLoader_throwsValidationException_forDuplicatedIds() throws IOException
    {
        MovieLoader movieLoader = movieLoaderFor("integration/topdown/movies with dublicated id.txt");
        ValidationException ex = assertThrows(ValidationException.class, movieLoader::load);
        assertEquals(ErrorMessage.MOVIE_ID_NOT_UNIQUE, ex.getErrorMessage());
    }

    @Test
    void userLoader_throwsValidationException_forDuplicatedIds() throws IOException
    {
        UserLoader userLoader = userLoaderFor("integration/topdown/movies with dublicated id.txt");
        ValidationException ex = assertThrows(ValidationException.class, userLoader::load);
        assertEquals(ErrorMessage.USER_ID_NOT_UNIQUE, ex.getErrorMessage());
    }
}
