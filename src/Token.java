public class Token {

    public String attributeValue;
    public String lexeme;
    public String tokenName;
    public String type;
    public String value;
    public int lineNumber;

    public Token(String attributeValue, String lexeme, String tokenName, String value, String type, int line){
        this.attributeValue = attributeValue;
        this.lexeme = lexeme;
        this.tokenName = tokenName;
        this.type = type;
        this.value = value;
        this.lineNumber = line;
    }
}
