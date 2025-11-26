package org.example.exceptions;

public class ValidationException extends Exception {

    public enum ErrorMessage
    {
        MOVIE_TITLE("ERROR: Movie Title %s is wrong"),
        MOVIE_ID_LETTERS("ERROR: Movie Id letters %s are wrong"),
        MOVIE_ID_NUMBERS("ERROR: Movie Id numbers %s aren’t unique"),
        USER_NAME("ERROR: User Name %s is wrong"),
        USER_ID("ERROR: User Id %s is wrong"),
        FILE_MOVIES_FORMAT("ERROR: Movies file format is wrong"),
        FILE_USERS_FORMAT("ERROR: Users file format is wrong");

        private final String template;

        ErrorMessage(String template) {
            this.template = template;
        }

        public String format(Object... args) {
            return String.format(template, args);
        }
    }

    private final ErrorMessage errorMessage;

    public ValidationException(ErrorMessage errorMessage, Object... args) {
        super(errorMessage.format(args));
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
