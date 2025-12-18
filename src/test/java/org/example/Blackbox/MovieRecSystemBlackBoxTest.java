package org.example.Blackbox;

import org.example.app.MovieRecSystem;
import org.example.exception.ValidationException;
import org.example.io.loader.MovieLoader;
import org.example.io.loader.UserLoader;
import org.example.io.loader.component.FileReader;
import org.example.io.loader.component.IdValidator;
import org.example.io.loader.component.TwoLineParser;
import org.example.io.output.OutputWriter;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Tests for Movie Recommendation System
 * 
 * These tests verify the system behavior based on inputs and outputs
 * without knowledge of internal implementation details.
 * 
 * Test Categories:
 * 1. Valid Input Tests - Normal operation scenarios
 * 2. Boundary Value Tests - Edge cases and limits
 * 3. Invalid Input Tests - Error handling scenarios
 * 4. Equivalence Partitioning Tests - Representative test cases
 */
@DisplayName("Movie Recommendation System - Black Box Tests")
class MovieRecSystemBlackBoxTest {

    private static final String TEST_RESOURCES = "src/test/resources/blackbox/";
    private static final String OUTPUT_FILE = "recommendations.txt";

    @BeforeAll
    static void setupTestResources() throws IOException {
        Files.createDirectories(Path.of(TEST_RESOURCES));
    }

    @AfterEach
    void cleanup() {
        // Clean up output file after each test
        try {
            Files.deleteIfExists(Path.of(OUTPUT_FILE));
        } catch (IOException ignored) {}
    }

    // ==================== VALID INPUT TESTS ====================

    @Nested
    @DisplayName("Valid Input Tests")
    class ValidInputTests {

        @Test
        @DisplayName("TC01: Single user with single liked movie gets recommendations")
        void singleUserSingleMovieGetsRecommendations() throws Exception {
            // Setup test files
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    Inception, I456
                    Action, Thriller
                    Titanic, T789
                    Romance, Drama
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc01_movies.txt", moviesContent, "tc01_users.txt", usersContent);

            // Execute
            MovieRecSystem system = createSystem("tc01_movies.txt", "tc01_users.txt");
            system.run();

            // Verify output file exists and contains recommendations
            String output = Files.readString(Path.of(OUTPUT_FILE));
            assertTrue(output.contains("Alice Smith"));
            assertTrue(output.contains("Inception")); // Same genre (Action)
            assertFalse(output.contains("The Matrix")); // Already liked
            assertFalse(output.contains("Titanic")); // Different genre
        }

        @Test
        @DisplayName("TC02: Multiple users get personalized recommendations")
        void multipleUsersGetPersonalizedRecommendations() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    Inception, I456
                    Action, Thriller
                    Titanic, T789
                    Romance, Drama
                    The Notebook, TN321
                    Romance, Drama
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    Bob Johnson, 87654321B
                    T789
                    """;

            createTestFiles("tc02_movies.txt", moviesContent, "tc02_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc02_movies.txt", "tc02_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            
            // Alice should get Action/Sci-Fi recommendations
            assertTrue(output.contains("Alice Smith"));
            // Bob should get Romance/Drama recommendations
            assertTrue(output.contains("Bob Johnson"));
        }

        @Test
        @DisplayName("TC03: User with multiple liked movies gets combined genre recommendations")
        void userWithMultipleLikedMoviesGetsCombinedRecommendations() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    Titanic, T456
                    Romance, Drama
                    Inception, I789
                    Action, Thriller
                    The Notebook, TN321
                    Romance, Comedy
                    Avatar, A654
                    Sci-Fi, Adventure
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123, T456
                    """;

            createTestFiles("tc03_movies.txt", moviesContent, "tc03_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc03_movies.txt", "tc03_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            
            // Should get Action, Sci-Fi, Romance, Drama recommendations
            assertTrue(output.contains("Inception") || output.contains("The Notebook") || output.contains("Avatar"));
        }



        @Test
        @DisplayName("TC05: User ID with 9 digits format")
        void userIdWith9Digits() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    John Doe, 123456789
                    TM123
                    """;

            createTestFiles("tc05_movies.txt", moviesContent, "tc05_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc05_movies.txt", "tc05_users.txt");
            
            assertDoesNotThrow(() -> system.run());
        }

        @Test
        @DisplayName("TC06: User ID with 8 digits and letter format")
        void userIdWith8DigitsAndLetter() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    John Doe, 12345678Z
                    TM123
                    """;

            createTestFiles("tc06_movies.txt", moviesContent, "tc06_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc06_movies.txt", "tc06_users.txt");
            
            assertDoesNotThrow(() -> system.run());
        }
    }

    // ==================== BOUNDARY VALUE TESTS ====================

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @Test
        @DisplayName("TC07: Single movie in database")
        void singleMovieInDatabase() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc07_movies.txt", moviesContent, "tc07_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc07_movies.txt", "tc07_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            assertTrue(output.contains("Alice Smith"));
            // No recommendations possible - only one movie
        }

        @Test
        @DisplayName("TC08: Single user in database")
        void singleUserInDatabase() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    Inception, I456
                    Action, Thriller
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc08_movies.txt", moviesContent, "tc08_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc08_movies.txt", "tc08_users.txt");
            
            assertDoesNotThrow(() -> system.run());
        }

        @Test
        @DisplayName("TC09: Movie with single genre")
        void movieWithSingleGenre() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action
                    Inception, I456
                    Action
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc09_movies.txt", moviesContent, "tc09_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc09_movies.txt", "tc09_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            assertTrue(output.contains("Inception"));
        }

        @Test
        @DisplayName("TC10: Movie with multiple genres")
        void movieWithMultipleGenres() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi, Thriller, Adventure
                    Inception, I456
                    Action, Mystery
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc10_movies.txt", moviesContent, "tc10_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc10_movies.txt", "tc10_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            assertTrue(output.contains("Inception"));
        }

        @Test
        @DisplayName("TC11: Minimum valid movie ID (2 letters + 3 digits)")
        void minimumValidMovieId() throws Exception {
            String moviesContent = """
                    Go Fast, GF123
                    Action
                    """;
            String usersContent = """
                    John Doe, 123456789
                    GF123
                    """;

            createTestFiles("tc11_movies.txt", moviesContent, "tc11_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc11_movies.txt", "tc11_users.txt");
            
            assertDoesNotThrow(() -> system.run());
        }

        @Test
        @DisplayName("TC12: Single word movie title")
        void singleWordMovieTitle() throws Exception {
            String moviesContent = """
                    Inception, I123
                    Action, Sci-Fi
                    Avatar, A456
                    Sci-Fi, Adventure
                    """;
            String usersContent = """
                    John Doe, 123456789
                    I123
                    """;

            createTestFiles("tc12_movies.txt", moviesContent, "tc12_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc12_movies.txt", "tc12_users.txt");
            
            assertDoesNotThrow(() -> system.run());
        }

        @Test
        @DisplayName("TC13: Single word user name")
        void singleWordUserName() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice, 12345678A
                    TM123
                    """;

            createTestFiles("tc13_movies.txt", moviesContent, "tc13_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc13_movies.txt", "tc13_users.txt");
            
            assertDoesNotThrow(() -> system.run());
        }
    }

    // ==================== INVALID INPUT TESTS ====================

    @Nested
    @DisplayName("Invalid Input Tests - Error Handling")
    class InvalidInputTests {

        @Test
        @DisplayName("TC14: Invalid movie title - starts with lowercase")
        void invalidMovieTitleStartsWithLowercase() throws Exception {
            String moviesContent = """
                    the Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc14_movies.txt", moviesContent, "tc14_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc14_movies.txt", "tc14_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }

        @Test
        @DisplayName("TC15: Invalid movie ID - wrong format (lowercase)")
        void invalidMovieIdLowercase() throws Exception {
            String moviesContent = """
                    The Matrix, tm123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    tm123
                    """;

            createTestFiles("tc15_movies.txt", moviesContent, "tc15_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc15_movies.txt", "tc15_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }

        @Test
        @DisplayName("TC16: Invalid movie ID - does not match title initials")
        void invalidMovieIdDoesNotMatchTitle() throws Exception {
            String moviesContent = """
                    The Matrix, AB123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    AB123
                    """;

            createTestFiles("tc16_movies.txt", moviesContent, "tc16_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc16_movies.txt", "tc16_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }

        @Test
        @DisplayName("TC17: Invalid user name - contains numbers")
        void invalidUserNameWithNumbers() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice123, 12345678A
                    TM123
                    """;

            createTestFiles("tc17_movies.txt", moviesContent, "tc17_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc17_movies.txt", "tc17_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }

        @Test
        @DisplayName("TC18: Invalid user ID - wrong length")
        void invalidUserIdWrongLength() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice Smith, 1234567
                    TM123
                    """;

            createTestFiles("tc18_movies.txt", moviesContent, "tc18_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc18_movies.txt", "tc18_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }

        @Test
        @DisplayName("TC19: Duplicate movie IDs")
        void duplicateMovieIds() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    The Mask, TM123
                    Comedy
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc19_movies.txt", moviesContent, "tc19_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc19_movies.txt", "tc19_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }

        @Test
        @DisplayName("TC20: Duplicate user IDs")
        void duplicateUserIds() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    Bob Johnson, 12345678A
                    TM123
                    """;

            createTestFiles("tc20_movies.txt", moviesContent, "tc20_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc20_movies.txt", "tc20_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }

        @Test
        @DisplayName("TC21: Empty movie title")
        void emptyMovieTitle() throws Exception {
            String moviesContent = """
                    , TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc21_movies.txt", moviesContent, "tc21_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc21_movies.txt", "tc21_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }

        @Test
        @DisplayName("TC22: Empty user name")
        void emptyUserName() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    , 12345678A
                    TM123
                    """;

            createTestFiles("tc22_movies.txt", moviesContent, "tc22_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc22_movies.txt", "tc22_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }

        @Test
        @DisplayName("TC23: Movie ID with wrong number of digits")
        void movieIdWithWrongDigits() throws Exception {
            String moviesContent = """
                    The Matrix, TM12
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM12
                    """;

            createTestFiles("tc23_movies.txt", moviesContent, "tc23_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc23_movies.txt", "tc23_users.txt");
            
            assertThrows(ValidationException.class, () -> system.run());
        }
    }

    // ==================== EQUIVALENCE PARTITIONING TESTS ====================

    @Nested
    @DisplayName("Equivalence Partitioning Tests")
    class EquivalencePartitioningTests {

        @Test
        @DisplayName("TC24: User with no matching genre recommendations")
        void userWithNoMatchingGenreRecommendations() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    Titanic, T456
                    Romance, Drama
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc24_movies.txt", moviesContent, "tc24_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc24_movies.txt", "tc24_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            assertTrue(output.contains("Alice Smith"));
            assertFalse(output.contains("Titanic")); // Different genre
        }

        @Test
        @DisplayName("TC25: User who has watched all movies")
        void userWhoWatchedAllMovies() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    Titanic, T456
                    Romance, Drama
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123, T456
                    """;

            createTestFiles("tc25_movies.txt", moviesContent, "tc25_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc25_movies.txt", "tc25_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            assertTrue(output.contains("Alice Smith"));
            // No recommendations - user watched all movies
        }

        @Test
        @DisplayName("TC26: All movies share same genre")
        void allMoviesShareSameGenre() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action
                    Die Hard, DH456
                    Action
                    Terminator, T789
                    Action
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc26_movies.txt", moviesContent, "tc26_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc26_movies.txt", "tc26_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            assertTrue(output.contains("Die Hard"));
            assertTrue(output.contains("Terminator"));
        }

        @Test
        @DisplayName("TC27: No common genres between movies")
        void noCommonGenresBetweenMovies() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action
                    Titanic, T456
                    Romance
                    Comedy Movie, CM789
                    Comedy
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc27_movies.txt", moviesContent, "tc27_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc27_movies.txt", "tc27_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            assertFalse(output.contains("Titanic"));
            assertFalse(output.contains("Comedy Movie"));
        }
    }

    // ==================== SPECIAL CHARACTER TESTS ====================

    @Nested
    @DisplayName("Special Character Tests")
    class SpecialCharacterTests {


        @Test
        @DisplayName("TC29: User name with multiple spaces between words")
        void userNameMultipleWords() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    """;
            String usersContent = """
                    John William Doe, 123456789
                    TM123
                    """;

            createTestFiles("tc29_movies.txt", moviesContent, "tc29_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc29_movies.txt", "tc29_users.txt");
            
            assertDoesNotThrow(() -> system.run());
        }
    }

    // ==================== OUTPUT FORMAT TESTS ====================

    @Nested
    @DisplayName("Output Format Tests")
    class OutputFormatTests {

        @Test
        @DisplayName("TC30: Output file contains user info and recommendations")
        void outputFileFormat() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    Inception, I456
                    Action, Thriller
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    """;

            createTestFiles("tc30_movies.txt", moviesContent, "tc30_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc30_movies.txt", "tc30_users.txt");
            system.run();

            // Verify output file exists
            assertTrue(Files.exists(Path.of(OUTPUT_FILE)));
            
            String output = Files.readString(Path.of(OUTPUT_FILE));
            // Verify output contains user identifier
            assertTrue(output.contains("Alice Smith"));
            assertTrue(output.contains("12345678A"));
        }

        @Test
        @DisplayName("TC31: Multiple users output separation")
        void multipleUsersOutputSeparation() throws Exception {
            String moviesContent = """
                    The Matrix, TM123
                    Action, Sci-Fi
                    Inception, I456
                    Action, Thriller
                    """;
            String usersContent = """
                    Alice Smith, 12345678A
                    TM123
                    Bob Johnson, 87654321B
                    I456
                    """;

            createTestFiles("tc31_movies.txt", moviesContent, "tc31_users.txt", usersContent);

            MovieRecSystem system = createSystem("tc31_movies.txt", "tc31_users.txt");
            system.run();

            String output = Files.readString(Path.of(OUTPUT_FILE));
            // Both users should be in output
            assertTrue(output.contains("Alice Smith"));
            assertTrue(output.contains("Bob Johnson"));
        }
    }

    // ==================== HELPER METHODS ====================

    private void createTestFiles(String moviesFileName, String moviesContent,
                                  String usersFileName, String usersContent) throws IOException {
        Files.writeString(Path.of(TEST_RESOURCES + moviesFileName), moviesContent);
        Files.writeString(Path.of(TEST_RESOURCES + usersFileName), usersContent);
    }

    private MovieRecSystem createSystem(String moviesFile, String usersFile) {
        String moviesPath = TEST_RESOURCES + moviesFile;
        String usersPath = TEST_RESOURCES + usersFile;

        // Create loaders with predefined file paths (simulating user input)
        MovieLoader movieLoader = new MovieLoader(
                new TestFileReader(moviesPath),
                new TwoLineParser(),
                new IdValidator()
        );

        UserLoader userLoader = new UserLoader(
                new TestFileReader(usersPath),
                new TwoLineParser(),
                new IdValidator()
        );

        OutputWriter writer = new OutputWriter();

        return new MovieRecSystem(movieLoader, userLoader, writer);
    }

    /**
     * Test helper class that provides predefined file path instead of reading from console
     */
    private static class TestFileReader extends FileReader {
        private final String filePath;

        public TestFileReader(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public List<String> readLines(String prompt) throws IOException {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.isBlank()) {
                        lines.add(line.trim());
                    }
                }
            }
            return lines;
        }
    }
}
