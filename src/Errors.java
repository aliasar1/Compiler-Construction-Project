public class Errors {
    int lineNumber;
    String errorType;
    String errorMessage;
    String lexeme;

    public Errors(int lineNumber, String errorType, String errorMessage, String lexeme) {
        this.lineNumber = lineNumber;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.lexeme = lexeme;
    }
}
