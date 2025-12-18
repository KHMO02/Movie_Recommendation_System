/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package org.example.unit.validation;

import org.junit.jupiter.api.Test;
import org.example.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

public class IdMatchesTitleTest {

    @Test
    void matchingLetters() throws ValidationException {
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
}
