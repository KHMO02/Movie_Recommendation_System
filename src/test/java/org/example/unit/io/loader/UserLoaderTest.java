package org.example.unit.io.loader;

import org.example.exception.ValidationException;
import org.example.exception.ValidationException.ErrorMessage;
import org.example.io.loader.UserLoader;
import org.example.io.loader.component.FileReader;
import org.example.io.loader.component.IdValidator;
import org.example.io.loader.component.ParsedEntity;
import org.example.io.loader.component.TwoLineParser;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserLoaderTest {

    @Test
    void loadUsers_validInput_createsUsersFromParsedEntities() throws IOException, ValidationException {
        FileReader fileReader = mock(FileReader.class);
        TwoLineParser parser = mock(TwoLineParser.class);
        IdValidator idValidator = mock(IdValidator.class);

        List<String> lines = List.of("ignored");
        List<ParsedEntity> parsed = List.of(
                new ParsedEntity("Omar Tamer", "123456789", List.of("MML001", "SM001", "FG001")),
                new ParsedEntity("Ali Tamer", "12345678m", List.of("LL001", "SV001", "V001")),
                new ParsedEntity("Nada Ali", "15467892b", List.of("LL002", "LM001", "TT002"))
        );

        when(fileReader.readLines(anyString())).thenReturn(lines);
        when(parser.parse(lines, ErrorMessage.FILE_USERS_FORMAT)).thenReturn(parsed);

        UserLoader loader = new UserLoader(fileReader, parser, idValidator);

        List<User> actual = loader.load();

        assertEquals(3, actual.size());

        assertEquals("Omar Tamer", actual.get(0).getUserName());
        assertEquals("123456789", actual.get(0).getUserId());
        assertEquals(List.of("MML001", "SM001", "FG001"), actual.get(0).getMoviesId());

        assertEquals("Ali Tamer", actual.get(1).getUserName());
        assertEquals("12345678m", actual.get(1).getUserId());
        assertEquals(List.of("LL001", "SV001", "V001"), actual.get(1).getMoviesId());

        assertEquals("Nada Ali", actual.get(2).getUserName());
        assertEquals("15467892b", actual.get(2).getUserId());
        assertEquals(List.of("LL002", "LM001", "TT002"), actual.get(2).getMoviesId());

        verify(fileReader).readLines(startsWith("Enter users"));
        verify(parser).parse(lines, ErrorMessage.FILE_USERS_FORMAT);
        verify(idValidator).validateAllUnique(parsed, ErrorMessage.USER_ID_NOT_UNIQUE);
    }

    @Test
    void loadUsers_duplicateId_propagatesValidationException() throws IOException, ValidationException {
        FileReader fileReader = mock(FileReader.class);
        TwoLineParser parser = mock(TwoLineParser.class);
        IdValidator idValidator = mock(IdValidator.class);

        List<String> lines = List.of("ignored");
        List<ParsedEntity> parsed = List.of(
                new ParsedEntity("Ali Tamer", "12345678m", List.of("LL001", "SV001", "V001")),
                new ParsedEntity("Ali Copy", "12345678m", List.of("XX001", "YY001", "ZZ001"))
        );

        when(fileReader.readLines(anyString())).thenReturn(lines);
        when(parser.parse(lines, ErrorMessage.FILE_USERS_FORMAT)).thenReturn(parsed);
        doThrow(new ValidationException(ErrorMessage.USER_ID_NOT_UNIQUE, "Ali Tamer"))
                .when(idValidator).validateAllUnique(parsed, ErrorMessage.USER_ID_NOT_UNIQUE);

        UserLoader loader = new UserLoader(fileReader, parser, idValidator);

        ValidationException ex = assertThrows(
                ValidationException.class,
                loader::load
        );

        assertEquals(
                ErrorMessage.USER_ID_NOT_UNIQUE.format("Ali Tamer"),
                ex.getMessage()
        );
    }
}
