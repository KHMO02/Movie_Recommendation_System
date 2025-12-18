/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package org.example.unit.model;

import org.junit.jupiter.api.Test;
import org.example.exception.ValidationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieTest
{

    @Test
    void validMovie() {
        assertDoesNotThrow(() -> new Movie("The Lord Of", "TLO123", List.of("Action")));
    }

    @Test
    void invalidTitle() {
        assertThrows(ValidationException.class,
                () -> new Movie("the lord", "TLO123", List.of("Action")));
    }

    @Test
    void invalidId() {
        assertThrows(ValidationException.class,
                () -> new Movie("The Lord Of", "TLO9999", List.of("Action")));
    }

    @Test
    void idDoesNotMatchTitle() {
        assertThrows(ValidationException.class,
                () -> new Movie("The Lord Of", "XYZ123", List.of("Action")));
    }
}

