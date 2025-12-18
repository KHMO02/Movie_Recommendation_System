package org.example.unit.validation;

import org.example.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieIdNumbersTest {

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


}
