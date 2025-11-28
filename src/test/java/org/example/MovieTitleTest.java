package org.example;

import org.junit.jupiter.api.Test;
import org.example.FormatValidator;
import org.example.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTitleTest {


    @Test
    void validTitle() {
        assertDoesNotThrow(() -> 
            FormatValidator.validateMovieTitle("The Lord Of The Rings")
        );
    }

    @Test
    void oneLetterWord() {
        assertDoesNotThrow(() -> 
            FormatValidator.validateMovieTitle("A")
        );
    }


    @Test
    void singleWordCapitalized() {
        assertDoesNotThrow(() ->
            FormatValidator.validateMovieTitle("Up")
        );
    }


    
    
    
    @Test
    void nullTitle() {
        assertThrows(ValidationException.class,
                () -> FormatValidator.validateMovieTitle(null));
    }

    @Test
    void emptyTitle() {
        assertThrows(ValidationException.class,
                () -> FormatValidator.validateMovieTitle(""));
    }

    @Test
    void lowercaseWords() {
        assertThrows(ValidationException.class,
                () -> FormatValidator.validateMovieTitle("the lord"));
    }

    @Test
    void leadingSpace() {
        assertThrows(ValidationException.class,
                () -> FormatValidator.validateMovieTitle(" The Lord"));
    }

    @Test
    void wordContainingNumbers() {
        assertThrows(ValidationException.class,
                () -> FormatValidator.validateMovieTitle("Lord2"));
    }

    @Test
    void wordContainingSymbols() {
        assertThrows(ValidationException.class,
                () -> FormatValidator.validateMovieTitle("Lord-"));
    }
}
