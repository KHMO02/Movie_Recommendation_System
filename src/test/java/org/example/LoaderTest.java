package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoaderTest {
    private static List<Movie> movies;
    private static List<User> users;
    private static Loader loader;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        movies = new ArrayList<>();
        users = new ArrayList<>();
        loader = new Loader();
    }

    @Test
    void testLoadMovies() {
        String filename = "./src/test/java/org/example/movies.txt";
        ByteArrayInputStream bais = new ByteArrayInputStream(filename.getBytes());
        System.setIn(bais);
        try{
            Movie ne_zha = new Movie("Ne Zha", "NZ001",
                    new ArrayList<>(Arrays.asList("Action", "Adventure", "Comedy")));
            Movie wild_robot = new Movie("The Wild Robot", "TWR001",
                    new ArrayList<>(Arrays.asList("Animation", "Drama", "Family")));
            Movie spider_man = new Movie("The Little Prince", "TLP001",
                    new ArrayList<>(Arrays.asList("Animation", "Family", "Adventure")));
            ArrayList<Movie> expected = new ArrayList<>(Arrays.asList(ne_zha, wild_robot, spider_man));
            List<Movie> actual =loader.loadMovies();
            assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
            assertEquals(expected.get(1).getTitle(), actual.get(1).getTitle());
            assertEquals(expected.get(2).getTitle(), actual.get(2).getTitle());
            assertEquals(expected.get(0).getId(), actual.get(0).getId());
            assertEquals(expected.get(1).getId(), actual.get(1).getId());
            assertEquals(expected.get(2).getId(), actual.get(2).getId());
            assertEquals(expected.get(0).getGenre(), actual.get(0).getGenre());
            assertEquals(expected.get(1).getGenre(), actual.get(1).getGenre());
            assertEquals(expected.get(2).getGenre(), actual.get(2).getGenre());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testLoadMovies2() {
        String filename = "./src/test/java/org/example/movies2.txt";
        ByteArrayInputStream bais = new ByteArrayInputStream(filename.getBytes());
        System.setIn(bais);
        try{
            Movie movie = new Movie("Punch Drunk Love", "PDL001",
                    new ArrayList<>(Arrays.asList("Romance", "Drama")));
            Movie movie2 = new Movie("Forrest Gump", "FG001",
                    new ArrayList<>(Arrays.asList("Adventure","Romance", "Drama")));
            Movie movie3 = new Movie("Memento", "M001",
                    new ArrayList<>(Arrays.asList("Thriller", "Mystery", "Investigation")));
            ArrayList<Movie> expected = new ArrayList<>(Arrays.asList(movie, movie2, movie3));
            List<Movie> actual =loader.loadMovies();
            assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
            assertEquals(expected.get(1).getTitle(), actual.get(1).getTitle());
            assertEquals(expected.get(2).getTitle(), actual.get(2).getTitle());
            assertEquals(expected.get(0).getId(), actual.get(0).getId());
            assertEquals(expected.get(1).getId(), actual.get(1).getId());
            assertEquals(expected.get(2).getId(), actual.get(2).getId());
            assertEquals(expected.get(0).getGenre(), actual.get(0).getGenre());
            assertEquals(expected.get(1).getGenre(), actual.get(1).getGenre());
            assertEquals(expected.get(2).getGenre(), actual.get(2).getGenre());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testLoadUsers() {
        String filename = "./src/test/java/org/example/users.txt";
        ByteArrayInputStream bais = new ByteArrayInputStream(filename.getBytes());
        System.setIn(bais);
        try{
            User user1 = new User("Omar Tamer", "123456789",
                    new ArrayList<>(Arrays.asList("MML001", "SM001", "FG001")));
            User user2 = new User("Ali Tamer", "12345678m",
                    new ArrayList<>(Arrays.asList("LL001", "SV001", "V001")));
            User user3 = new User("Nada Ali", "15467892b",
                    new ArrayList<>(Arrays.asList("LL002", "LM001", "TT002")));

            ArrayList<User> expected = new ArrayList<>(Arrays.asList(user1, user2, user3));
            List<User> actual =loader.loadUsers();
            assertEquals(expected.get(0).getUserName(), actual.get(0).getUserName());
            assertEquals(expected.get(1).getUserName(), actual.get(1).getUserName());
            assertEquals(expected.get(2).getUserName(), actual.get(2).getUserName());
            assertEquals(expected.get(0).getUserId(), actual.get(0).getUserId());
            assertEquals(expected.get(1).getUserId(), actual.get(1).getUserId());
            assertEquals(expected.get(2).getUserId(), actual.get(2).getUserId());
            assertEquals(expected.get(0).getMoviesId(), actual.get(0).getMoviesId());
            assertEquals(expected.get(1).getMoviesId(), actual.get(1).getMoviesId());
            assertEquals(expected.get(2).getMoviesId(), actual.get(2).getMoviesId());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testMovieIdUnique(){
        String filename = "./src/test/java/org/example/movies with dublicated id.txt";
        ByteArrayInputStream bais = new ByteArrayInputStream(filename.getBytes());
        System.setIn(bais);
        try{
            List<Movie> actual =loader.loadMovies();
            fail("expected exception");
        }
        catch(Exception e){
            assertEquals(e.getMessage(), "ERROR: Movie Master's Id is not unique");
        }
    }

    @Test
    void testUserIdUnique(){
        String filename = "./src/test/java/org/example/users with dublicated id.txt";
        ByteArrayInputStream bais = new ByteArrayInputStream(filename.getBytes());
        System.setIn(bais);
        try{
            List<User> actual =loader.loadUsers();
            fail("expected exception");
        }
        catch(Exception e){
            assertEquals(e.getMessage(), "ERROR: User Ali Tamer's Id is not unique");
        }
    }
}
