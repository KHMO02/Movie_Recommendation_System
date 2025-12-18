package org.example.unit.model;

import org.example.exception.ValidationException;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    // ------------------ VALID CASE ------------------
    @Test
    public void testValidUser() throws ValidationException {
        User user = new User(
                "Germine Chawki",
                "12345678A",
                Arrays.asList("M123", "M567")
        );

        assertEquals("Germine Chawki", user.getUserName());
        assertEquals("12345678A", user.getUserId());
        assertEquals(Arrays.asList("M123", "M567"), user.getMoviesId());
    }

    // ------------------ NAME TESTS ------------------

    @Test
    public void testUserNameStartsWithSpace() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User(" Germine", "123456789", List.of("M1"))
        );
        assertEquals(
                ValidationException.ErrorMessage.USER_NAME.format(" Germine"),
                e.getMessage()
        );
    }

    @Test
    public void testUserNameContainsNumbers() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User("Germine2 Chawki", "123456789", List.of("M1"))
        );
        assertEquals(
                ValidationException.ErrorMessage.USER_NAME.format("Germine2 Chawki"),
                e.getMessage()
        );
    }

    @Test
    public void testUserNameContainsSymbols() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User("Germine@Chawki", "123456789", List.of("M1"))
        );
        assertEquals(
                ValidationException.ErrorMessage.USER_NAME.format("Germine@Chawki"),
                e.getMessage()
        );
    }

    @Test
    public void testUserNameEmpty() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User("", "123456789", List.of("M1"))
        );
        assertEquals(
                ValidationException.ErrorMessage.USER_NAME.format(""),
                e.getMessage()
        );
    }

    // ------------------ USER ID TESTS ------------------

    @Test
    public void testUserIdTooShort() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User("Germine Chawki", "12345", List.of("M1"))
        );
        assertEquals(
                ValidationException.ErrorMessage.USER_ID.format("12345"),
                e.getMessage()
        );
    }

    @Test
    public void testUserIdTooLong() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User("Germine Chawki", "1234567890", List.of("M1"))
        );
        assertEquals(
                ValidationException.ErrorMessage.USER_ID.format("1234567890"),
                e.getMessage()
        );
    }

    @Test
    public void testUserIdContainsSymbols() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User("Germine Chawki", "12345#789", List.of("M1"))
        );
        assertEquals(
                ValidationException.ErrorMessage.USER_ID.format("12345#789"),
                e.getMessage()
        );
    }

    @Test
    public void testUserIdDoesNotStartWithNumber() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User("Germine Chawki", "A23456789", List.of("M1"))
        );
        assertEquals(
                ValidationException.ErrorMessage.USER_ID.format("A23456789"),
                e.getMessage()
        );
    }

    @Test
    public void testUserIdEndsWithMoreThanOneLetter() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User("Germine Chawki", "1234567AB", List.of("M1"))
        );
        assertEquals(
                ValidationException.ErrorMessage.USER_ID.format("1234567AB"),
                e.getMessage()
        );
    }

    // ------------------ MOVIES LIST TESTS ------------------

    @Test
    public void testMoviesListContainsEmpty() {
        ValidationException e = assertThrows(
                ValidationException.class,
                () -> new User("Germine Chawki", "123456789", List.of(""))
        );
        assertEquals(
                ValidationException.ErrorMessage.MOVIE_ID_LETTERS.format(""),
                e.getMessage()
        );
    }
}
