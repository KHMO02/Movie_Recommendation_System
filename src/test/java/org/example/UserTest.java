package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

class UserTest {
    private User user;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        List<String> genre = new ArrayList<>();
        genre.add("Drama");
        new Movie("The Shawshank Redemption","TSR001",genre);
        genre.add("Crime");
        new Movie("The Godfather","TG002",genre);
        genre.add("Action");
        new Movie("The Dark Knight","TDK003",genre);
    }
    @Test
    void getRecommendations() {
        List<String> movies=new ArrayList<>();
        movies.add("TSR001");
        movies.add("TDK003");
        user=new User("Hassan Ali","12345678X",movies);
        List<Movie> recommendations=user.getRecommendations();

        Vector<String> expected=new Vector<>();
        expected.add("The Godfather");
        for(int i=0;i<recommendations.size();i++){
            assertEquals(recommendations.get(i).getTitle(),expected.get(i));
        }
    }
    @Test
    void getRecommendations2() {
        List<String> movies=new ArrayList<>();
        movies.add("TG002");
        user=new User("Ali Mohamed","87654321W",movies);
        List<Movie> recommendations=user.getRecommendations();

        Vector<String> expected=new Vector<>();
        expected.add("The Shawshank Redemption");
        expected.add("The Dark Knight");
        for(int i=0;i<recommendations.size();i++){
            assertEquals(recommendations.get(i).getTitle(),expected.get(i));
        }
    }
    @Test
    void getRecommendations3() {
        List<String> movies=new ArrayList<>();
        movies.add("TDK003");
        user=new User("John Shoukry","87654341W",movies);
        List<Movie> recommendations=user.getRecommendations();

        Vector<String> expected=new Vector<>();
        expected.add("The Shawshank Redemption");
        expected.add("The Godfather");
        for(int i=0;i<recommendations.size();i++){
            assertEquals(recommendations.get(i).getTitle(),expected.get(i));
        }
    }
}