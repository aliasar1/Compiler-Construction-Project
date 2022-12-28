public class Token {

    String attributeValue;
    String lexeme;
    String tokenName;
    String type;
    String value;
    int lineNumber;

    public Token(String attributeValue, String lexeme, String tokenName, String value, String type, int line){
        this.attributeValue = attributeValue;
        this.lexeme = lexeme;
        this.tokenName = tokenName;
        this.type = type;
        this.value = value;
        this.lineNumber = line;
    }
}
