package org.example.system;

import org.example.app.MovieRecSystem;
import org.example.exception.ValidationException;
import org.example.io.loader.MovieLoader;
import org.example.io.loader.UserLoader;
import org.example.io.loader.component.FileReader;
import org.example.io.loader.component.IdValidator;
import org.example.io.loader.component.TwoLineParser;
import org.example.io.output.OutputWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MovieRecSystemSystemTest
{

    private static final Path OUTPUT_PATH = Path.of("recommendations.txt");

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
     * Build resource path under src/test/resources/system/,
     * allowing numbered prefixes like "1- " + fileName.
     */
    private String systemResource(String prefix, String fileName)
    {
        if (prefix == null || prefix.isBlank())
        {
            return "system/" + fileName;
        }
        return "system/" + prefix + "- " + fileName;
    }

    private void copySystemInput(String resourceRelative, String targetFileName) throws IOException
    {
        Files.copy(resourcePath(resourceRelative), Path.of(targetFileName),
                   java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Create a MovieRecSystem wired with loaders that use a shared test scanner
     * providing "movies.txt" then "users.txt" when FileReader.readLines is called.
     */
    private MovieRecSystem newSystemUnderTest()
    {
        Scanner scanner = new Scanner("movies.txt\nusers.txt\n");

        FileReader movieFileReader = new FileReader(scanner);
        FileReader userFileReader  = new FileReader(scanner);

        TwoLineParser parser      = new TwoLineParser();
        IdValidator   idValidator = new IdValidator();

        MovieLoader movieLoader = new MovieLoader(movieFileReader, parser, idValidator);
        UserLoader  userLoader  = new UserLoader(userFileReader, parser, idValidator);

        OutputWriter writer = new OutputWriter();

        return new MovieRecSystem(movieLoader, userLoader, writer);
    }

    private void assertOutputEqualsResource(String expectedResourcePath) throws IOException
    {
        assertTrue(Files.exists(OUTPUT_PATH), "recommendations.txt should be created");
        List<String> actual   = Files.readAllLines(OUTPUT_PATH);
        List<String> expected = Files.readAllLines(resourcePath(expectedResourcePath));
        assertEquals(expected, actual);
    }

    private void runExpectingSuccess(MovieRecSystem app)
    {
        try
        {
            app.run();
        }
        catch (ValidationException | IOException e)
        {
            fail("run() threw unexpectedly: " + e.getMessage());
        }
    }

    private void runWithErrorAndAssertOutput(
            String moviesResource,
            String usersResource,
            String expectedOutputResource,
            String failureMessage
                                            ) throws IOException
    {
        copySystemInput(moviesResource, "movies.txt");
        copySystemInput(usersResource, "users.txt");

        MovieRecSystem app = newSystemUnderTest();

        ValidationException ex = assertThrows(
                ValidationException.class,
                () ->
                {
                    try
                    {
                        app.run();
                    }
                    catch (IOException e)
                    {
                        fail("Unexpected IO error: " + e.getMessage());
                    }
                },
                failureMessage
                                             );

        new OutputWriter().writeError(ex.getMessage());

        assertOutputEqualsResource(expectedOutputResource);
    }

    @AfterEach
    void cleanUp() throws IOException
    {
        Files.deleteIfExists(OUTPUT_PATH);
        Files.deleteIfExists(Path.of("movies.txt"));
        Files.deleteIfExists(Path.of("users.txt"));
    }

    @Test
    void run_withValidData_producesExpectedRecommendationsFile() throws IOException
    {
        copySystemInput(systemResource("1", "movies_valid.txt"), "movies.txt");
        copySystemInput(systemResource("1", "users_valid.txt"), "users.txt");

        MovieRecSystem app = newSystemUnderTest();

        runExpectingSuccess(app);

        assertOutputEqualsResource(systemResource("1", "expected_recommendations_valid.txt"));
    }

    @Test
    void run_withDuplicatedMovieIds_writesErrorFile() throws IOException
    {
        runWithErrorAndAssertOutput(
                systemResource("2", "movies_dup_movie_ids.txt"),
                systemResource("2", "users_valid_for_dup_movie_ids.txt"),
                systemResource("2", "expected_recommendations_dup_movie_ids.txt"),
                "Expected ValidationException due to duplicate movie IDs"
                                   );
    }

    @Test
    void run_withDuplicatedUserIds_writesErrorFile() throws IOException
    {
        runWithErrorAndAssertOutput(
                systemResource("3", "movies_valid_for_dup_user_ids.txt"),
                systemResource("3", "users_dup_user_ids.txt"),
                systemResource("3", "expected_recommendations_dup_user_ids.txt"),
                "Expected ValidationException due to duplicate user IDs"
                                   );
    }

    @Test
    void run_withBadFormatMovies_writesErrorFile() throws IOException
    {
        runWithErrorAndAssertOutput(
                systemResource("4", "movies_bad_format.txt"),
                systemResource("4", "users_valid_for_bad_movies.txt"),
                systemResource("4", "expected_recommendations_bad_movies.txt"),
                "Expected ValidationException due to bad movie file format"
                                   );
    }

    @Test
    void run_withBadFormatUsers_writesErrorFile() throws IOException
    {
        runWithErrorAndAssertOutput(
                systemResource("5", "movies_valid_for_bad_users.txt"),
                systemResource("5", "users_bad_format.txt"),
                systemResource("5", "expected_recommendations_bad_users.txt"),
                "Expected ValidationException due to bad user file format"
                                   );
    }
}
