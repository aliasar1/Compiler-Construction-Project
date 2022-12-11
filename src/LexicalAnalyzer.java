import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    //  int, char, string, if, else, do, while
    private String sourceCode;
    private String identifierRegex = "^[a-zA-Z][a-zA-Z|\\d]*";
    Pattern p = Pattern.compile(identifierRegex);

    BufferedReader reader;


    int startAttribute = 0;
    char character;
    int count = 0;
    int lineNumber = 1;
    int counter = 6;

    private ArrayList<String> tokenName = new ArrayList<String>();

    private ArrayList<Token> symbolTable = new ArrayList<>();

    public  LexicalAnalyzer(String sourceCode){
        initializeReserveKeywords();
        this.sourceCode = sourceCode;
        Reader code = new StringReader(sourceCode);
        reader = new BufferedReader(code);
        character = readNextCharacter();
        generateTokens();
    }

    private void initializeReserveKeywords(){
        tokenName.add("INT");
        tokenName.add("CHAR");
        tokenName.add("STRING");
        tokenName.add("IF");
        tokenName.add("ELSE");
        tokenName.add("DO");
        tokenName.add("WHILE");
        for (int i = 0; i<tokenName.size(); i++){
            Token token = new Token(startAttribute, tokenName.get(i), "-", "-", lineNumber);
            symbolTable.add(token);
            startAttribute++;
        }
    }

    private int getLineNumber(){
        if(character == '\n')
            return lineNumber++;
        return  lineNumber;
    }

    public void printTable(){
        for (int i = 0; i<symbolTable.size(); i++){
            System.out.println(symbolTable.get(i).toString());
        }
    }

    public void generateTokens() throws IOException {
        Token token = readNextToken();
        while (token != null) {
            symbolTable.add(token);
            token = readNextToken();
        }
    }

    public Token readNextToken() {
        int state = 0;
        while (true) {
            if (character == (char) -1) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            if (character == '\n') {
                count++;
            }
            switch (state) {
                case 0: {
                    if (character == 'd') {
                        state = 48;
                    } else if (character == 'c') {
                        state = 25;
                    } else if (character == '<') {
                        state = 1;
                    } else if (character == 'i') {
                        state = 20;
                    } else if (character == '=') {
                        character = readNextCharacter();
                        if (character == '=') {
                            state = 5;
                        } else {
                            state = 14;
                        }
                    } else if (character == 's') {
                        state = 31;
                    } else if (character == 'e') {
                        state = 42;
                    } else if (character == 'w') {
                        state = 52;
                    } else if (character == '>') {
                        state = 7;
                    } else if (character == '+') {
                        state = 10;
                    } else if (character == '-') {
                        state = 11;
                    } else if (character == '*') {
                        state = 12;
                    } else if (character == '/') {
                        state = 13;
                    } else if (character == '(') {
                        state = 15;
                    } else if (character == ')') {
                        state = 16;
                    } else if (character == '{') {
                        state = 17;
                    } else if (character == '}') {
                        state = 18;
                    } else if (character == ';') {
                        state = 19;
                    } else if (character == ' ') {
                        character = readNextCharacter();
                    } else if (isAlphabet(character)) {
                        state = 59;
                    } else {
                        fail();
                    }
                    break;
                }
                case 1: {
                    if (character == '>') {
                        state = 6;
                    } else if (character == '=') {
                        state = 2;
                    } else {
                        state = 3;
                    }
                    break;
                }
                case 2: {
                    Token token = new Token(startAttribute, "ROP", "GE", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 3: {
                    Token token = new Token(startAttribute, "ROP", "LT", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 4: {
                    if (character == '=') {
                        state = 5;
                    }
                    break;
                }
                case 5: {
                    Token token = new Token(startAttribute, "ROP", "EQ", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 6: {
                    Token token = new Token(startAttribute, "ROP", "NE", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 7: {
                    character = readNextCharacter();
                    if (character == '=') {
                        state = 8;
                    } else {
                        state = 9;
                    }
                    break;
                }
                case 8: {
                    Token token = new Token(startAttribute, "ROP", "LE", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 9: {
                    Token token = new Token(startAttribute, "ROP", "GT", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 10: {
                    Token token = new Token(startAttribute, "AOP", "AD", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 11: {
                    Token token = new Token(startAttribute, "AOP", "SB", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 12: {
                    Token token = new Token(startAttribute, "AOP", "ML", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 13: {
                    Token token = new Token(startAttribute, "AOP", "DV", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 14: {
                    Token token = new Token(startAttribute, "OOP", "EQ", "-", lineNumber);
                    startAttribute++;
                    return  token;
                }
                case 15: {
                    Token token = new Token(startAttribute, "OOP", "OP", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 16: {
                    Token token = new Token(startAttribute, "OOP", "CP", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 17: {
                    Token token = new Token(startAttribute, "OOP", "OB", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 18: {
                    Token token = new Token(startAttribute, "OOP", "CB", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 19: {
                    Token token = new Token(startAttribute, "OOP", "TR", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 20: {
                    character = readNextCharacter();
                    if (character == 'n') {
                        state = 21;
                    } else if (character == 'f') {
                        state = 39;
                    } else {
                        checkIdentifiers("i");
                    }
                    break;
                }
                case 21:{
                    character = readNextCharacter();
                    if (character == 't') {
                        state = 22;
                    } else {
                        checkIdentifiers("in");
                    }
                    break;
                }
                case 22: {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 23;
                    } else {
                        state = 24;
                    }
                    break;
                }
                case 23: {
                    Token token = new Token(startAttribute, "INT", "-", "-", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 24: {
                    checkIdentifiers("int" + character);
                    break;
                }
                case 25: {
                    character = readNextCharacter();
                    if (character == 'h') {
                        state = 26;
                    } else {
                        checkIdentifiers("c");
                    }
                    break;
                }
                case 26: {
                    character = readNextCharacter();
                    if (character == 'a') {
                        state = 27;
                    } else {
                        checkIdentifiers("ch");
                    }
                    break;
                }
                case 27: {
                    character = readNextCharacter();
                    if (character == 'r') {
                        state = 28;
                    } else {
                        checkIdentifiers("cha");
                    }
                    break;
                }
                case 28: {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 29;
                    } else {
                        state = 30;
                    }
                    break;
                }
                case 29: {
                    Token token = new Token(startAttribute, "CHAR", "", "", lineNumber);
                    startAttribute++;
                    return token;
                }

                case 30: {
                    checkIdentifiers("char" + character);
                    break;
                }
                case 31: {
                    character = readNextCharacter();
                    if (character == 't') {
                        state = 32;
                    } else {
                        checkIdentifiers("s");
                    }
                    break;
                }
                case 32: {
                    character = readNextCharacter();
                    if (character == 'r') {
                        state = 33;
                    } else
                        checkIdentifiers("st");
                    break;
                }
                case 33: {
                    character = readNextCharacter();
                    if (character == 'i') {
                        state = 34;
                    } else
                        checkIdentifiers("str");
                    break;
                }
                case 34: {
                    character = readNextCharacter();
                    if (character == 'n') {
                        state = 35;
                    } else {
                        checkIdentifiers("stri");
                    }
                    break;
                }
                case 35: {
                    character = readNextCharacter();
                    if (character == 'g') {
                        state = 36;
                    } else {
                        checkIdentifiers("strin");
                    }
                    break;
                }
                case 36:{
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 37;
                    } else {
                        state = 38;
                    }
                    break;
                }
                case 37: {
                    Token token = new Token(startAttribute, "STRING", "", "", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 38: {
                    checkIdentifiers("string" + character);
                    break;
                }
                case 39: {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 40;
                    } else
                        state = 41;
                    break;
                }
                case 40: {
                    Token token = new Token(startAttribute, "IF", "", "", lineNumber);
                    startAttribute++;
                    return token;
                }
                case 41: {
                    checkIdentifiers("if" + character);
                    break;
                }
                case 42: {
                    character = readNextCharacter();
                    if (character == 'l')
                        state = 43;
                    else
                        checkIdentifiers("e");
                    break;
                }
                case 43: {
                    character = readNextCharacter();
                    if (character == 's')
                        state = 44;
                    else
                        checkIdentifiers("el");
                    break;
                }
                case 44: {
                    character = readNextCharacter();
                    if (character == 'e')
                        state = 45;
                    else
                        checkIdentifiers("els");
                    break;
                }
                case 45: {
                    character = readNextCharacter();
                    if (character == ' ')
                        state = 46;
                    else
                        state = 47;
                    break;
                }
                case 46: {
                    Token token = new Token(startAttribute++, "ELSE", "", "", lineNumber);
                    return token;
                }
                case 47: {
                    checkIdentifiers("else" + character);
                    break;
                }
                case 48: {
                    character = readNextCharacter();
                    if (character == 'O') {
                        state = 49;
                    } else {
                        checkIdentifiers("D");
                    }
                    break;
                }
                case 49: {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 50;
                    } else {
                        state = 51;
                    }
                    break;
                }
                case 50: {
                    Token token = new Token(startAttribute++, "DO", "-", "", lineNumber);
                    return token;
                }
                case 51: {
                    checkIdentifiers("do" + character);
                    break;
                }
                case 52: {
                    character = readNextCharacter();
                    if (character == 'h') {
                        state = 53;
                    } else {
                        checkIdentifiers("w");
                    }
                    break;
                }
                case 53: {
                    character = readNextCharacter();
                    if (character == 'i') {
                        state = 54;
                    } else {
                        checkIdentifiers("wh");
                    }
                    break;
                }

                case 54: {
                    character = readNextCharacter();
                    if (character == 'l') {
                        state = 55;
                    } else {
                        checkIdentifiers("whi");
                    }
                    break;
                }
                case 55: {
                    character = readNextCharacter();
                    if (character == 'e') {
                        state = 56;
                    } else {
                        checkIdentifiers("whil");
                    }
                    break;
                }
                case 56: {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 57;
                    } else {
                        state = 58;
                    }
                    break;
                }
                case 57: {
                    Token token = new Token(startAttribute++, "WHILE", "", "-", lineNumber);
                    return token;
                }
                case 58: {
                    checkIdentifiers("while" + character);
                    break;
                }
                case 59: {
                    checkIdentifiers(String.valueOf(character));
                    break;
                }
                case 60: {
                    if (isDigit(character)) {
                        StringBuilder word = new StringBuilder(String.valueOf(character));
                        while (true) {
                            character = readNextCharacter();
                            if (isDigit(character)) {
                                word.append(character);
                            } else {
                                if (!checkValue(String.valueOf(word))) {
                                    Token token = new Token(startAttribute++, "IV", "-", String.valueOf(word), lineNumber);
                                    symbolTable.add(token);
                                    return token;
                                }
                                break;
                            }
                        }
                    }
                }
                default:
                    fail();
                    break;
            }
        }

    }

    char readNextCharacter() {
        try {
            return (char) reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLineNumber();
        return (char) -1;
    }

    private Token checkIdentifiers(String s) {
        StringBuilder word = new StringBuilder(s);
        while (true) {
            character = readNextCharacter();
            if (isAlphabet(character) || isDigit(character)) {
                word.append(character);
            } else {
                if (!checkValue(String.valueOf(word))) {
                    Token token = new Token(startAttribute, "ID", "-", String.valueOf(word), lineNumber);
                    symbolTable.add(token);
                    return token;
                }
                break;
            }
        }
        return null;
    }

    private void fail() {

    }

    private boolean checkValue(String value) {
        for (int i = 0; i < symbolTable.size(); i++) {
            if (Objects.equals(symbolTable.get(i).value, value)) {
                return true;
            }
        }
        return false;
    }

    boolean isDigit(char c) {       //Check if current character is a Digit or not
        if (c >= '0' && c <= '9' || c == '.')
            return true;

        return false;
    }

    boolean isAlphabet(char c) {       //Check if current character is a Alphabet or not
        if (c >= 'a' && c <= 'z')
            return true;
        if (c >= 'A' && c <= 'Z')
            return true;

        return false;
    }

}
