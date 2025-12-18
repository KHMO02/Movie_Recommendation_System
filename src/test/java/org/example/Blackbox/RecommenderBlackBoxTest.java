package org.example.Blackbox;

import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.example.recommendation.Recommender;
import org.example.recommendation.UserRecommendations;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Tests for Recommendation Engine
 * 
 * These tests verify the recommendation algorithm behavior based on
 * user preferences and movie genres, without testing internal implementation.
 */
@DisplayName("Recommendation Engine - Black Box Tests")
class RecommenderBlackBoxTest {

    private List<Movie> movieDatabase;

    @BeforeEach
    void setup() throws ValidationException {
        movieDatabase = new ArrayList<>();
    }

    // ==================== BASIC RECOMMENDATION TESTS ====================

    @Nested
    @DisplayName("Basic Recommendation Tests")
    class BasicRecommendationTests {

        @Test
        @DisplayName("TC-REC-01: User gets recommendations based on liked genre")
        void userGetsRecommendationsBasedOnLikedGenre() throws ValidationException {
            // Given: Movies with different genres
            Movie actionMovie1 = new Movie("The Matrix", "TM123", List.of("Action", "Sci-Fi"));
            Movie actionMovie2 = new Movie("Inception", "I456", List.of("Action", "Thriller"));
            Movie romanceMovie = new Movie("Titanic", "T789", List.of("Romance", "Drama"));
            
            movieDatabase.add(actionMovie1);
            movieDatabase.add(actionMovie2);
            movieDatabase.add(romanceMovie);
            
            // User likes action movie
            User user = new User("Alice", "12345678A", List.of("TM123"));
            
            // When: Getting recommendations
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            // Then: User should get Inception (same genre Action)
            assertTrue(recommendations.contains(actionMovie2));
            assertFalse(recommendations.contains(actionMovie1)); // Already liked
            assertFalse(recommendations.contains(romanceMovie)); // Different genre
        }

        @Test
        @DisplayName("TC-REC-02: User who liked movie with multiple genres gets wider recommendations")
        void userWithMultipleGenresGetsWiderRecommendations() throws ValidationException {
            // Given: Movies
            Movie sciFiAction = new Movie("The Matrix", "TM123", List.of("Action", "Sci-Fi"));
            Movie actionOnly = new Movie("Die Hard", "DH456", List.of("Action"));
            Movie sciFiOnly = new Movie("Avatar", "A789", List.of("Sci-Fi"));
            Movie comedy = new Movie("Home Alone", "HA321", List.of("Comedy"));
            
            movieDatabase.add(sciFiAction);
            movieDatabase.add(actionOnly);
            movieDatabase.add(sciFiOnly);
            movieDatabase.add(comedy);
            
            // User likes Sci-Fi Action movie
            User user = new User("Bob", "87654321B", List.of("TM123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            // Should get both Action AND Sci-Fi movies
            assertTrue(recommendations.contains(actionOnly));  // Has Action
            assertTrue(recommendations.contains(sciFiOnly));   // Has Sci-Fi
            assertFalse(recommendations.contains(comedy));     // Different genre
        }

        @Test
        @DisplayName("TC-REC-03: User with multiple liked movies gets combined genre recommendations")
        void userWithMultipleLikedMoviesGetsCombinedRecommendations() throws ValidationException {
            // Given: Movies
            Movie action = new Movie("The Matrix", "TM123", List.of("Action"));
            Movie romance = new Movie("Titanic", "T456", List.of("Romance"));
            Movie actionRec = new Movie("Die Hard", "DH789", List.of("Action"));
            Movie romanceRec = new Movie("The Notebook", "TN321", List.of("Romance"));
            
            movieDatabase.add(action);
            movieDatabase.add(romance);
            movieDatabase.add(actionRec);
            movieDatabase.add(romanceRec);
            
            // User likes both Action and Romance
            User user = new User("Charlie", "123456789", List.of("TM123", "T456"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            // Should get recommendations from both genres
            assertTrue(recommendations.contains(actionRec));
            assertTrue(recommendations.contains(romanceRec));
        }
    }

    // ==================== EDGE CASE TESTS ====================

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("TC-REC-04: User who watched all movies gets no recommendations")
        void userWhoWatchedAllMoviesGetsNoRecommendations() throws ValidationException {
            Movie movie1 = new Movie("The Matrix", "TM123", List.of("Action"));
            Movie movie2 = new Movie("Titanic", "T456", List.of("Romance"));
            
            movieDatabase.add(movie1);
            movieDatabase.add(movie2);
            
            // User has watched all movies
            User user = new User("Alice", "12345678A", List.of("TM123", "T456"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            assertTrue(recommendations.isEmpty());
        }

        @Test
        @DisplayName("TC-REC-05: User with unique genre gets no recommendations")
        void userWithUniqueGenreGetsNoRecommendations() throws ValidationException {
            Movie documentary = new Movie("Planet Earth", "PE123", List.of("Documentary"));
            Movie action = new Movie("Die Hard", "DH456", List.of("Action"));
            Movie comedy = new Movie("Home Alone", "HA789", List.of("Comedy"));
            
            movieDatabase.add(documentary);
            movieDatabase.add(action);
            movieDatabase.add(comedy);
            
            // User likes Documentary (only one exists)
            User user = new User("Alice", "12345678A", List.of("PE123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            assertTrue(recommendations.isEmpty());
        }

        @Test
        @DisplayName("TC-REC-06: Empty movie database produces no recommendations")
        void emptyMovieDatabaseProducesNoRecommendations() throws ValidationException {
            // Empty movie database
            User user = new User("Alice", "12345678A", List.of("TM123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            assertTrue(recommendations.isEmpty());
        }

        @Test
        @DisplayName("TC-REC-07: Single movie in database, user likes it, no recommendations")
        void singleMovieLikedNoRecommendations() throws ValidationException {
            Movie onlyMovie = new Movie("The Matrix", "TM123", List.of("Action"));
            movieDatabase.add(onlyMovie);
            
            User user = new User("Alice", "12345678A", List.of("TM123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            assertTrue(recommendations.isEmpty());
        }
    }

    // ==================== MULTIPLE USERS TESTS ====================

    @Nested
    @DisplayName("Multiple Users Tests")
    class MultipleUsersTests {

        @Test
        @DisplayName("TC-REC-08: Each user gets personalized recommendations")
        void eachUserGetsPersonalizedRecommendations() throws ValidationException {
            Movie action = new Movie("The Matrix", "TM123", List.of("Action"));
            Movie romance = new Movie("Titanic", "T456", List.of("Romance"));
            Movie actionRec = new Movie("Die Hard", "DH789", List.of("Action"));
            Movie romanceRec = new Movie("The Notebook", "TN321", List.of("Romance"));
            
            movieDatabase.add(action);
            movieDatabase.add(romance);
            movieDatabase.add(actionRec);
            movieDatabase.add(romanceRec);
            
            User actionLover = new User("Alice", "12345678A", List.of("TM123"));
            User romanceLover = new User("Bob", "87654321B", List.of("T456"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<UserRecommendations> allRecs = recommender.recommendForAllUsers(
                List.of(actionLover, romanceLover));
            
            assertEquals(2, allRecs.size());
            
            // Alice should get action recommendation
            UserRecommendations aliceRecs = allRecs.get(0);
            assertTrue(aliceRecs.recommendations().contains(actionRec));
            assertFalse(aliceRecs.recommendations().contains(romanceRec));
            
            // Bob should get romance recommendation
            UserRecommendations bobRecs = allRecs.get(1);
            assertTrue(bobRecs.recommendations().contains(romanceRec));
            assertFalse(bobRecs.recommendations().contains(actionRec));
        }

        @Test
        @DisplayName("TC-REC-09: Empty user list produces empty recommendations")
        void emptyUserListProducesEmptyRecommendations() throws ValidationException {
            Movie movie = new Movie("The Matrix", "TM123", List.of("Action"));
            movieDatabase.add(movie);
            
            Recommender recommender = new Recommender(movieDatabase);
            List<UserRecommendations> allRecs = recommender.recommendForAllUsers(List.of());
            
            assertTrue(allRecs.isEmpty());
        }

        @Test
        @DisplayName("TC-REC-10: Users with same preferences get same recommendations")
        void usersWithSamePreferencesGetSameRecommendations() throws ValidationException {
            Movie liked = new Movie("The Matrix", "TM123", List.of("Action"));
            Movie recommendation = new Movie("Die Hard", "DH456", List.of("Action"));
            
            movieDatabase.add(liked);
            movieDatabase.add(recommendation);
            
            User user1 = new User("Alice", "12345678A", List.of("TM123"));
            User user2 = new User("Bob", "87654321B", List.of("TM123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<UserRecommendations> allRecs = recommender.recommendForAllUsers(List.of(user1, user2));
            
            assertEquals(allRecs.get(0).recommendations(), allRecs.get(1).recommendations());
        }
    }

    // ==================== GENRE MATCHING TESTS ====================

    @Nested
    @DisplayName("Genre Matching Tests")
    class GenreMatchingTests {

        @Test
        @DisplayName("TC-REC-11: Partial genre match triggers recommendation")
        void partialGenreMatchTriggersRecommendation() throws ValidationException {
            // Movie has Action, Sci-Fi
            Movie liked = new Movie("The Matrix", "TM123", List.of("Action", "Sci-Fi"));
            // Movie has Action, Comedy - shares Action
            Movie partialMatch = new Movie("Rush Hour", "RH456", List.of("Action", "Comedy"));
            
            movieDatabase.add(liked);
            movieDatabase.add(partialMatch);
            
            User user = new User("Alice", "12345678A", List.of("TM123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            assertTrue(recommendations.contains(partialMatch));
        }

        @Test
        @DisplayName("TC-REC-12: No genre overlap means no recommendation")
        void noGenreOverlapMeansNoRecommendation() throws ValidationException {
            Movie liked = new Movie("The Matrix", "TM123", List.of("Action", "Sci-Fi"));
            Movie noMatch = new Movie("Comedy Central", "CC456", List.of("Comedy", "Romance"));
            
            movieDatabase.add(liked);
            movieDatabase.add(noMatch);
            
            User user = new User("Alice", "12345678A", List.of("TM123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            assertFalse(recommendations.contains(noMatch));
        }

        @Test
        @DisplayName("TC-REC-13: Case-sensitive genre matching")
        void caseSensitiveGenreMatching() throws ValidationException {
            // Note: This tests the expected behavior - genres should match exactly
            Movie liked = new Movie("The Matrix", "TM123", List.of("Action"));
            Movie similarGenre = new Movie("Die Hard", "DH456", List.of("Action")); // Same case
            
            movieDatabase.add(liked);
            movieDatabase.add(similarGenre);
            
            User user = new User("Alice", "12345678A", List.of("TM123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<Movie> recommendations = recommender.recommendForUser(user);
            
            assertTrue(recommendations.contains(similarGenre));
        }
    }



    // ==================== ORDER AND CONSISTENCY TESTS ====================

    @Nested
    @DisplayName("Order and Consistency Tests")
    class OrderConsistencyTests {

        @Test
        @DisplayName("TC-REC-17: Recommendations order is consistent")
        void recommendationsOrderIsConsistent() throws ValidationException {
            Movie m1 = new Movie("Action One", "AO123", List.of("Action"));
            Movie m2 = new Movie("Action Two", "AT456", List.of("Action"));
            Movie m3 = new Movie("Action Three", "AT789", List.of("Action"));
            
            movieDatabase.add(m1);
            movieDatabase.add(m2);
            movieDatabase.add(m3);
            
            User user = new User("Alice", "12345678A", List.of("AO123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            
            // Run multiple times
            List<Movie> rec1 = recommender.recommendForUser(user);
            List<Movie> rec2 = recommender.recommendForUser(user);
            List<Movie> rec3 = recommender.recommendForUser(user);
            
            // Results should be consistent
            assertEquals(rec1, rec2);
            assertEquals(rec2, rec3);
        }

        @Test
        @DisplayName("TC-REC-18: User recommendations maintain user reference")
        void userRecommendationsMaintainUserReference() throws ValidationException {
            Movie movie = new Movie("The Matrix", "TM123", List.of("Action"));
            movieDatabase.add(movie);
            
            User user = new User("Alice", "12345678A", List.of("TM123"));
            
            Recommender recommender = new Recommender(movieDatabase);
            List<UserRecommendations> allRecs = recommender.recommendForAllUsers(List.of(user));
            
            assertEquals(user, allRecs.get(0).user());
        }
    }
}
