package org.example;

import org.junit.jupiter.api.BeforeAll;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class UserCheckTest {
    private static List<String> movies;
    @BeforeAll
    static void setUp() {
        movies = new ArrayList<>();
        movies.add("TSR001");
        movies.add("TDK003");
        movies.add("TG002");
    }

    @Test
    void userNameCheck1() {
        User user = new User("Ahmed Hany","12345678X",movies);
        assertTrue(Recommender.userNameCheck(user.getUserName()));
    }
    @Test
    void userNameCheck2() {
        User user = new User(" Ahmed Hany","12345678X",movies);
        assertFalse(Recommender.userNameCheck(user.getUserName()));
    }
    @Test
    void userNameCheck3() {
        User user = new User("Ahmed Hany1","12345678X",movies);
        assertFalse(Recommender.userNameCheck(user.getUserName()));
    }


    @Test
    void userIdCheck1() {
        User user = new User("Ahmed Hany","12345678X",movies);
        assertTrue(Recommender.userIdCheck(user.getUserId()));
    }
    @Test
    void userIdCheck2() {//not 9 characters
        User user = new User("Ahmed Hany","1234567X",movies);
        assertFalse(Recommender.userIdCheck(user.getUserId()));
    }
    @Test
    void userIdCheck3() {//not unique
        User user = new User("John Shoukry","12345678X",movies);
        assertFalse(Recommender.userIdCheck(user.getUserId()));
    }
    @Test
    void userIdCheck4() {//more than 2 characters
        User user = new User("Ahmed Hany","1234567XX",movies);
        assertFalse(Recommender.userIdCheck(user.getUserId()));
    }
    @Test
    void userIdCheck5() {//special character
        User user = new User("Ahmed Hany","12345678@",movies);
        assertFalse(Recommender.userIdCheck(user.getUserId()));
    }

}