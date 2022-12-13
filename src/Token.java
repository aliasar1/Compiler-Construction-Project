public class Token {

    int attributeValue;
    String tokenName;
    String type;
    String value;
    int lineNumber;

    public Token(int attributeValue, String tokenName, String value, String type, int line){
        this.attributeValue = attributeValue;
        this.tokenName = tokenName;
        this.type = type;
        this.value = value;
        this.lineNumber = line;
    }

    @Override
    public String toString() {
        return this.attributeValue + "  " + this.tokenName + "  " + this.value + "  " + this.type + " at line " + this.lineNumber;
    }
}
