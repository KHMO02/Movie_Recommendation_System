package org.example.Blackbox;

import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Tests for Movie and User Input Validation
 * 
 * These tests focus on input validation using equivalence partitioning
 * and boundary value analysis techniques.
 */
@DisplayName("Input Validation - Black Box Tests")
class InputValidationBlackBoxTest {

    // ==================== MOVIE TITLE VALIDATION TESTS ====================

    @Nested
    @DisplayName("Movie Title Validation")
    class MovieTitleValidation {

        @ParameterizedTest(name = "Valid title: {0}")
        @DisplayName("TC-MT-01: Valid movie titles should be accepted")
        @ValueSource(strings = {
            "Inception",                    // Single word
            "The Matrix",                   // Two words
            "The Lord Of Rings",            // Multiple words
            "A",                            // Minimum single letter
            "Ab",                           // Two letters
            "The Incredible Hulk"           // Four words
        })
        void validMovieTitles(String title) {
            assertDoesNotThrow(() -> new Movie(title, generateId(title), List.of("Action")));
        }

        @ParameterizedTest(name = "Invalid title: {0}")
        @DisplayName("TC-MT-02: Invalid movie titles should be rejected")
        @ValueSource(strings = {
            "the matrix",                   // Starts with lowercase
            "The matrix",                   // Second word lowercase
            "THE MATRIX",                   // All uppercase
            "the Matrix",                   // First word lowercase
            "The  Matrix",                  // Double space
            " The Matrix",                  // Leading space
            "The Matrix ",                  // Trailing space
            "The123Matrix",                 // Contains numbers
            "The@Matrix",                   // Contains special char
            ""                              // Empty string
        })
        void invalidMovieTitles(String title) {
            assertThrows(ValidationException.class, 
                () -> new Movie(title, "TM123", List.of("Action")));
        }

        @Test
        @DisplayName("TC-MT-03: Null movie title should be rejected")
        void nullMovieTitle() {
            assertThrows(ValidationException.class, 
                () -> new Movie(null, "TM123", List.of("Action")));
        }
    }

    // ==================== MOVIE ID VALIDATION TESTS ====================

    @Nested
    @DisplayName("Movie ID Validation")
    class MovieIdValidation {

        @ParameterizedTest(name = "Valid ID: {0} for title: {1}")
        @DisplayName("TC-MID-01: Valid movie IDs should be accepted")
        @CsvSource({
            "TM123, The Matrix",
            "I456, Inception",
            "TLOR789, The Lord Of Rings",
            "A123, Avatar"
        })
        void validMovieIds(String id, String title) {
            assertDoesNotThrow(() -> new Movie(title, id, List.of("Action")));
        }

        @ParameterizedTest(name = "Invalid ID: {0}")
        @DisplayName("TC-MID-02: Invalid movie IDs should be rejected")
        @ValueSource(strings = {
            "tm123",                        // Lowercase letters
            "TM12",                         // Only 2 digits
            "TM1234",                       // 4 digits
            "123TM",                        // Numbers before letters
            "TM",                           // No digits
            "123",                          // No letters
            "T M123",                       // Space in ID
            "TM-123",                       // Hyphen in ID
            ""                              // Empty string
        })
        void invalidMovieIds(String id) {
            assertThrows(ValidationException.class, 
                () -> new Movie("The Matrix", id, List.of("Action")));
        }

        @Test
        @DisplayName("TC-MID-03: Null movie ID should be rejected")
        void nullMovieId() {
            assertThrows(ValidationException.class, 
                () -> new Movie("The Matrix", null, List.of("Action")));
        }

        @ParameterizedTest(name = "ID {0} should not match title {1}")
        @DisplayName("TC-MID-04: Movie ID must match title initials")
        @CsvSource({
            "AB123, The Matrix",            // Wrong letters
            "T123, The Matrix",             // Missing letter
            "TMX123, The Matrix",           // Extra letter
            "MT123, The Matrix"             // Wrong order
        })
        void movieIdMustMatchTitle(String id, String title) {
            assertThrows(ValidationException.class, 
                () -> new Movie(title, id, List.of("Action")));
        }
    }

    // ==================== USER NAME VALIDATION TESTS ====================

    @Nested
    @DisplayName("User Name Validation")
    class UserNameValidation {

        @ParameterizedTest(name = "Valid name: {0}")
        @DisplayName("TC-UN-01: Valid user names should be accepted")
        @ValueSource(strings = {
            "Alice",                        // Single name
            "Alice Smith",                  // Two names
            "Alice Marie Smith",            // Three names
            "alice",                        // All lowercase
            "ALICE",                        // All uppercase
            "A",                            // Single letter
            "Alice Marie Jane Smith"        // Four names
        })
        void validUserNames(String name) {
            assertDoesNotThrow(() -> new User(name, "12345678A", List.of("TM123")));
        }

        @ParameterizedTest(name = "Invalid name: {0}")
        @DisplayName("TC-UN-02: Invalid user names should be rejected")
        @ValueSource(strings = {
            "Alice123",                     // Contains numbers
            "Alice@Smith",                  // Contains special char
            "Alice  Smith",                 // Double space
            " Alice",                       // Leading space
            "Alice ",                       // Trailing space
            ""                              // Empty string
        })
        void invalidUserNames(String name) {
            assertThrows(ValidationException.class, 
                () -> new User(name, "12345678A", List.of("TM123")));
        }

        @Test
        @DisplayName("TC-UN-03: Null user name should be rejected")
        void nullUserName() {
            assertThrows(ValidationException.class, 
                () -> new User(null, "12345678A", List.of("TM123")));
        }
    }

    // ==================== USER ID VALIDATION TESTS ====================

    @Nested
    @DisplayName("User ID Validation")
    class UserIdValidation {

        @ParameterizedTest(name = "Valid ID: {0}")
        @DisplayName("TC-UID-01: Valid user IDs should be accepted")
        @ValueSource(strings = {
            "123456789",                    // 9 digits
            "12345678A",                    // 8 digits + uppercase letter
            "12345678a",                    // 8 digits + lowercase letter
            "12345678Z",                    // 8 digits + Z
            "000000000",                    // 9 zeros
            "00000000A"                     // 8 zeros + letter
        })
        void validUserIds(String id) {
            assertDoesNotThrow(() -> new User("Alice Smith", id, List.of("TM123")));
        }

        @ParameterizedTest(name = "Invalid ID: {0}")
        @DisplayName("TC-UID-02: Invalid user IDs should be rejected")
        @ValueSource(strings = {
            "12345678",                     // Only 8 digits
            "1234567890",                   // 10 digits
            "1234567AB",                    // 7 digits + 2 letters
            "123456789A",                   // 9 digits + letter
            "1234567",                      // Only 7 digits
            "ABCDEFGHI",                    // All letters
            "12345678@",                    // 8 digits + special char
            "1234 5678",                    // Space in ID
            ""                              // Empty string
        })
        void invalidUserIds(String id) {
            assertThrows(ValidationException.class, 
                () -> new User("Alice Smith", id, List.of("TM123")));
        }

        @Test
        @DisplayName("TC-UID-03: Null user ID should be rejected")
        void nullUserId() {
            assertThrows(ValidationException.class, 
                () -> new User("Alice Smith", null, List.of("TM123")));
        }
    }

    // ==================== USER MOVIES LIST VALIDATION TESTS ====================

    @Nested
    @DisplayName("User Movies List Validation")
    class UserMoviesListValidation {

        @Test
        @DisplayName("TC-UML-01: Valid single movie ID list should be accepted")
        void validSingleMovieIdList() {
            assertDoesNotThrow(() -> new User("Alice Smith", "12345678A", List.of("TM123")));
        }

        @Test
        @DisplayName("TC-UML-02: Valid multiple movie IDs list should be accepted")
        void validMultipleMovieIdsList() {
            assertDoesNotThrow(() -> 
                new User("Alice Smith", "12345678A", List.of("TM123", "I456", "A789")));
        }

        @Test
        @DisplayName("TC-UML-03: Empty movie list should be rejected")
        void emptyMovieList() {
            assertThrows(ValidationException.class, 
                () -> new User("Alice Smith", "12345678A", List.of()));
        }

        @Test
        @DisplayName("TC-UML-04: Null movie list should be rejected")
        void nullMovieList() {
            assertThrows(ValidationException.class, 
                () -> new User("Alice Smith", "12345678A", null));
        }

        @Test
        @DisplayName("TC-UML-05: List with invalid movie ID should be rejected")
        void listWithInvalidMovieId() {
            assertThrows(ValidationException.class, 
                () -> new User("Alice Smith", "12345678A", List.of("TM123", "invalid", "A789")));
        }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Generate a valid movie ID based on title initials
     */
    private String generateId(String title) {
        StringBuilder initials = new StringBuilder();
        for (String word : title.split("[\\s-]+")) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }
        return initials.toString().toUpperCase() + "123";
    }
}
