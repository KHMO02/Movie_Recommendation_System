package org.example;

import org.example.exception.ValidationException;
import org.example.model.Movie;
import org.example.model.User;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommenderTest {

    private Recommender recommender;
    private final String OUTPUT_FILE = "recommendations.txt";

    @BeforeEach
    void setUp() {
        recommender = new Recommender();
    }

    @AfterEach
    void tearDown() {
        File f = new File(OUTPUT_FILE);
        if (f.exists()) {
            f.delete();
        }
    }

    private String readFileContent() throws IOException {
        return Files.readString(Path.of(OUTPUT_FILE));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Movie mockMovie(String title, String id, List<String> genres) {
        Movie m = mock(Movie.class);
        when(m.getTitle()).thenReturn(title);
        when(m.getId()).thenReturn(id);
        when(m.getGenre()).thenReturn(genres);
        return m;
    }

    private User mockUser(String name, String id, List<String> likedIds) {
        User u = mock(User.class);
        when(u.getUserName()).thenReturn(name);
        when(u.getUserId()).thenReturn(id);
        when(u.getMoviesId()).thenReturn(likedIds);
        return u;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void testRecommendForUser_LikedGenreRecommendations() {
        // Req.: If user likes a movie from genre X then recommend all other movies in that genre
        Movie m1 = mockMovie("The Batman", "B001", List.of("Action"));
        Movie m2 = mockMovie("Superman", "SUP002", List.of("Action", "Comedy"));
        Movie m3 = mockMovie("Funny Movie", "FUN003", List.of("Comedy"));

        List<Movie> all = List.of(m1, m2, m3);

        User u = mockUser("John", "U1", List.of("B001"));

        List<Movie> rec = recommender.recommendForUser(u, all);

        assertEquals(1, rec.size());
        assertEquals("Superman", rec.get(0).getTitle());
    }

    @Test
    void testRecommendForUser_NoRecommendation() {
        // Req.: Do not recommend the movie the user has already liked
        Movie m1 = mockMovie("The Batman", "B001", List.of("Action"));
        Movie m2 = mockMovie("Superman", "SUP002", List.of("Drama"));
        Movie m3 = mockMovie("Funny Movie", "FUN003", List.of("Comedy"));

        List<Movie> allMovies = Arrays.asList(m1, m2, m3);
        User u = mockUser("Neo", "U1", List.of("B001"));

        List<Movie> recommendations = recommender.recommendForUser(u, allMovies);

        assertTrue(recommendations.isEmpty(), "Should not recommend a movie the user already liked");
    }

    @Test
    void testRecommendForUser_MultiGenreMatch() {
        // Req.: matching movie of multiple genres with others

        Movie m1 = mockMovie("Scream", "SCR001", List.of("Horror", "Thriller"));
        Movie m2 = mockMovie("Se7en", "SEV001", List.of("Thriller", "Crime"));
        Movie m3 = mockMovie("The Batman", "B001", List.of("Horror"));
        Movie m4 = mockMovie("Superman", "SUP002", List.of("Thriller"));
        Movie m5 = mockMovie("Funny Movie", "FUN003", List.of("Comedy"));

        List<Movie> allMovies = Arrays.asList(m1, m2, m3, m4, m5);
        User u = mockUser("Ghostface", "U1", List.of("SCR001"));

        List<Movie> recommendations = recommender.recommendForUser(u, allMovies);

        assertEquals(3, recommendations.size());
        //make sure of multiple genre matching between Scream and Se7en
        assertEquals("Se7en", recommendations.get(0).getTitle());
        assertEquals("The Batman", recommendations.get(1).getTitle());
        assertEquals("Superman", recommendations.get(2).getTitle());
    }

    @Test
    void testRecommendForUser_SameGenresOnce() {
        // Req.: matching movie of multiple genres with others
        Movie m1 = mockMovie("Scream", "SCR001", Arrays.asList("Horror", "Thriller"));
        Movie m2 = mockMovie("Se7en", "SEV001", Arrays.asList("Horror", "Thriller"));

        List<Movie> allMovies = Arrays.asList(m1, m2);
        User user = mockUser("Ghostface", "USER001", Arrays.asList("SCR001"));

        List<Movie> recommendations = recommender.recommendForUser(user, allMovies);

        assertEquals(1, recommendations.size());
        //make sure of multiple genre matching between Scream and Se7en
        assertEquals("Se7en", recommendations.get(0).getTitle());
    }

    @Test
    void testRecommendForUser_NoLikes() throws ValidationException {
        Movie m1 = mockMovie("Movie A", "MOV001", Arrays.asList("Drama"));
        List<Movie> allMovies = Collections.singletonList(m1);

        // User likes nothing
        User user = mockUser("Bored", "USER001", new ArrayList<>());

        List<Movie> recommendations = recommender.recommendForUser(user, allMovies);

        assertTrue(recommendations.isEmpty(), "If user likes nothing, recommendations should be empty");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    void testWriteRecommendations_FileCreationAndFormat() throws IOException {
        //Req.: file creation and its format
        Movie m1 = mockMovie("Iron Man", "IRO001", Arrays.asList("Action"));
        Movie m2 = mockMovie("Hulk", "HUL001", Arrays.asList("Action"));
        Movie m3 = mockMovie("Thor", "THO001", Arrays.asList("Action"));
        List<Movie> allMovies = Arrays.asList(m1, m2, m3);

        User u1 = mockUser("Tony Stark", "USER00001", Arrays.asList("IRO001"));

        recommender.writeRecommendations(List.of(u1), allMovies);

        // Verify that the file does exist
        File file = new File(OUTPUT_FILE);
        assertTrue(file.exists(), "recommendations.txt should be created");

        String content = readFileContent();
        String expectedLine1 = "Tony Stark, USER00001"; // Name, ID

        //Make sure of file contents
        assertTrue(content.contains(expectedLine1), "File should contain user header");
        assertTrue(content.contains("Hulk"), "File should contain recommended movie Hulk");
        assertTrue(content.contains("Thor"), "File should contain recommended movie Thor");
        // Check Comma separation in the file according to what processed first
        assertTrue(content.contains("Hulk, Thor") || content.contains("Thor, Hulk"), "Movies should be comma separated");
    }

    @Test
    void testWriteRecommendations_MultipleUsers() throws IOException {
        //Req.: multiple users recommendations
        Movie m1 = mockMovie("Iron Man", "IRO001", Arrays.asList("Action"));
        Movie m2 = mockMovie("Hulk", "HUL001", Arrays.asList("Action"));
        Movie m3 = mockMovie("Notebook", "NOT001", Arrays.asList("Drama"));

        List<Movie> allMovies = Arrays.asList(m1, m2, m3);

        User u1 = mockUser("User One", "ID1", Arrays.asList("IRO001"));
        User u2 = mockUser("User Two", "ID2", Arrays.asList("NOT001"));

        recommender.writeRecommendations(Arrays.asList(u1, u2), allMovies);

        String content = readFileContent();
        // Check formatting implies new lines between users
        // Note: The logic in Recommender writes a newline only if (u < users.size() - 1)
        assertTrue(content.contains("User One, ID1"), "User 1 header missing");
        assertTrue(content.contains("User Two, ID2"), "User 2 header missing");
    }

    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////

    @Test
    void testWriteErrorMsg() throws IOException {
        String errorMsg = "ERROR: User Name {  } is wrong";

        recommender.writeErrorMsg(errorMsg);

        File file = new File(OUTPUT_FILE);
        assertTrue(file.exists());

        String content = readFileContent();
        assertEquals(errorMsg, content, "File should contain exactly the error message");
    }
}