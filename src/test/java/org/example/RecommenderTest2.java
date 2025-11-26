package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

class RecommenderTest2 {

    @Test
    void movieIdNumbersCheck1() {
        List<String> genre = new ArrayList<>();
        genre.add("Drama");
        Movie movie = new Movie("The Shawshank Redemption","TSR001",genre);
        assertTrue(Recommender.movieIdNumbersCheck(movie.getId()));
    }
    @Test
    void movieIdNumbersCheck2() { //unique check
        List<String> genre = new ArrayList<>();
        genre.add("Drama");
        genre.add("Crime");
        Movie movie2 = new Movie("The Godfather","TG001",genre);
        Assertions.assertFalse(Recommender.movieIdNumbersCheck(movie2.getId()));
    }
    @Test
    void movieIdNumbersCheck3() { //no of characters
        List<String> genre = new ArrayList<>();
        genre.add("Drama");
        genre.add("Crime");
        genre.add("Adventure");
        Movie movie = new Movie("Jumanji","J2345",genre);
        assertFalse(Recommender.movieIdNumbersCheck(movie.getId()));
    }

    @Test
    void movieIdLettersCheck1() {
        List<String> genre = new ArrayList<>();
        genre.add("Drama");
        Movie movie = new Movie("The Shawshank Redemption","TSR001",genre);
        String[] letters={movie.getTitle(), movie.getId()};
        assertTrue(Recommender.movieIdLettersCheck(letters));
    }
    @Test
    void movieIdLettersCheck2() {
        List<String> genre = new ArrayList<>();
        genre.add("Drama");
        Movie movie = new Movie("The Shawshank Redemption","tsr001",genre);
        String[] letters={movie.getTitle(), movie.getId()};
        assertFalse(Recommender.movieIdLettersCheck(letters));
    }
    @Test
    void movieIdLettersCheck3() {
        List<String> genre = new ArrayList<>();
        genre.add("Drama");
        Movie movie = new Movie("The Shawshank Redemption","TSRX01",genre);
        String[] letters={movie.getTitle(), movie.getId()};
        assertFalse(Recommender.movieIdLettersCheck(letters));
    }

    @Test
    void movieTitleCheck1() {
        List<String> genre = new ArrayList<>();
        genre.add("Drama");
        Movie movie = new Movie("The Shawshank Redemption","TSR001",genre);
        assertTrue(Recommender.movieTitleCheck(movie.getTitle()));
    }
    @Test
    void movieTitleCheck2() {
        List<String> genre = new ArrayList<>();
        genre.add("Drama");
        Movie movie = new Movie("The shawshank Redemption","tsr001",genre);
        assertFalse(Recommender.movieTitleCheck(movie.getTitle()));
    }

}