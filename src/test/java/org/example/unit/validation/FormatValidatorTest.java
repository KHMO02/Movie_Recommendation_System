package org.example.unit.validation;

import org.example.exception.ValidationException;
import org.example.validation.FormatValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FormatValidatorTest
{
    @Test
    void matchingLetters() throws ValidationException
    {
        assertDoesNotThrow(() ->
                FormatValidator.validateMovieIdMatchesTitle("TLO123", "The Lord Of"));
    }

    @Test
    void mismatchedLength() {
        assertThrows(ValidationException.class,
                () -> FormatValidator.validateMovieIdMatchesTitle("TL123", "The Lord Of"));
    }

    @Test
    void mismatchedLetters() {
        assertThrows(ValidationException.class,
                () -> FormatValidator.validateMovieIdMatchesTitle("TRO123", "The Lord Of"));
    }

    @Test
    void idWithExactlyThreeDigits() {
        assertDoesNotThrow(() ->
                FormatValidator.validateMovieId("TLO123")
        );
    }

    @Test
    void idWithLessThanThreeDigits() {
        assertThrows(ValidationException.class, () ->
                FormatValidator.validateMovieId("TLO12")
        );
    }

    @Test
    void idWithMoreThanThreeDigits() {
        assertThrows(ValidationException.class, () ->
                FormatValidator.validateMovieId("TLO1234")
        );
    }

    @Test
    void digitsNotAtEnd() {
        assertThrows(ValidationException.class, () ->
                FormatValidator.validateMovieId("TL1O23")
        );
    }

    @Test
    void nonDigitInDigitsSection() {
        assertThrows(ValidationException.class, () ->
                FormatValidator.validateMovieId("TLO123A")
        );
    }


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
    void TitleWithHyphen() {
        assertDoesNotThrow(() ->
                FormatValidator.validateMovieTitle("Spider-Man")
        );
    }

    @Test
    void TitleAllCapitalized() {
        assertThrows(ValidationException.class,
                () -> FormatValidator.validateMovieTitle("MEMENTO"));
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
