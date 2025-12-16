package org.example;

import org.example.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.example.exception.ValidationException;

import java.util.Arrays;

public class UserTest {


    // ------------------ VALID CASE ------------------
    @Test
    public void testValidUser() {
        try {
            User user = new User(
                    "Germine Chawki",        // UPDATED VALID NAME
                    "12345678A",             // valid ID
                    Arrays.asList("M123", "M567")
            );

            assertEquals("Germine Chawki", user.getUserName());
            assertEquals("12345678A", user.getUserId());
            assertEquals(Arrays.asList("M123", "M567"), user.getMoviesId());

        } catch (ValidationException e) {
            fail("No exception expected for valid input.");
        }
    }


    // ------------------ NAME TESTS ------------------

    @Test
    public void testUserNameStartsWithSpace() {
        try {
            new User(" Germine", "123456789", Arrays.asList("M1"));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: User Name  Germine is wrong", e.getMessage());
        }
    }

    @Test
    public void testUserNameContainsNumbers() {
        try {
            new User("Germine2 Chawki", "123456789", Arrays.asList("M1"));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: User Name Germine2 Chawki is wrong", e.getMessage());
        }
    }

    @Test
    public void testUserNameContainsSymbols() {
        try {
            new User("Germine@Chawki", "123456789", Arrays.asList("M1"));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: User Name Germine@Chawki is wrong", e.getMessage());
        }
    }

    @Test
    public void testUserNameEmpty() {
        try {
            new User("", "123456789", Arrays.asList("M1"));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: User Name  is wrong", e.getMessage());
        }
    }


    // ------------------ USER ID TESTS ------------------

    @Test
    public void testUserIdTooShort() {
        try {
            new User("Germine Chawki", "12345", Arrays.asList("M1"));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: User Id 12345 is wrong", e.getMessage());
        }
    }

    @Test
    public void testUserIdTooLong() {
        try {
            new User("Germine Chawki", "1234567890", Arrays.asList("M1"));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: User Id 1234567890 is wrong", e.getMessage());
        }
    }

    @Test
    public void testUserIdContainsSymbols() {
        try {
            new User("Germine Chawki", "12345#789", Arrays.asList("M1"));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: User Id 12345#789 is wrong", e.getMessage());
        }
    }

    @Test
    public void testUserIdDoesNotStartWithNumber() {
        try {
            new User("Germine Chawki", "A23456789", Arrays.asList("M1"));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: User Id A23456789 is wrong", e.getMessage());
        }
    }

    @Test
    public void testUserIdEndsWithMoreThanOneLetter() {
        try {
            new User("Germine Chawki", "1234567AB", Arrays.asList("M1"));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: User Id 1234567AB is wrong", e.getMessage());
        }
    }


    // ------------------ MOVIES LIST TESTS ------------------

    @Test
    public void testMoviesListContainsEmpty() {
        try {
            new User("Germine Chawki", "123456789", Arrays.asList(""));
            fail("Expected ValidationException");
        } catch (ValidationException e) {
            assertEquals("ERROR: Movie Id letters  are wrong", e.getMessage());
        }
    }



}
