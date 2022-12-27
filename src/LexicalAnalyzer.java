import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Objects;

public class LexicalAnalyzer {

    int startAttribute = 0;
    char character;
    int lineNumber = 1;
    boolean flag = true;

    BufferedReader reader;
    public final ArrayList<Token> tokens = new ArrayList<>();
    private final ArrayList<Token> symbolTable = new ArrayList<>();
    private final  ArrayList<Errors> errorsList = new ArrayList<>();

    public LexicalAnalyzer(String sourceCode) {
        initializeReserveKeywords();
        Reader code = new StringReader(sourceCode);
        reader = new BufferedReader(code);
        character = readNextCharacter();
        generateTokens();
    }

    private void initializeReserveKeywords() {
        symbolTable.add(new Token(String.valueOf(startAttribute++), "int" ,"INT", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "char" ,"CHAR", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "string" ,"STRING", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "if" ,"IF", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "else" ,"ELSE", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "do" ,"DO", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "while" ,"WHILE", "-", "-", lineNumber));
    }

    public Formatter printTable() {
        Formatter fmt = new Formatter();
        for (Token token : symbolTable) {
            fmt.format("%16s  %50s %50s %60s\n", token.attributeValue, token.tokenName, token.type, token.value);
        }
        return fmt;
    }
    public Formatter printLexTable() {
        Formatter fmt = new Formatter();
        for (Token token : tokens) {
            fmt.format("%14s  %50s  %50s  %60s\n", token.lexeme, token.tokenName, token.attributeValue, " on line " + token.lineNumber);
        }
        return fmt;
    }

    public Formatter errorTable() {
        Formatter fmt = new Formatter();
        for (Errors error : errorsList) {
            fmt.format("%14s  %50s  %60s  %40s\n", error.lexeme, error.errorType, error.errorMessage, " on line " + error.lineNumber);
        }
        System.out.println(fmt);
        return fmt;
    }

    public void generateTokens() {
        Token token = readNextToken();
        while (token != null) {
            if (Objects.equals(token.tokenName, "ID") || Objects.equals(token.tokenName, "IV") || Objects.equals(token.tokenName, "SL")) {
                if (checkTokenName(token.tokenName) || checkValue(token.value)) {
                    startAttribute++;
                    symbolTable.add(token);
                }
            }
            String a = getExistingAttribute(token);
            if(a != null){
                token.attributeValue = a;
                tokens.add(token);
            }
            else{
                tokens.add(token);
            }
            token = readNextToken();
            if (token == null && flag){
                token = readNextToken();
            }
        }
    }

    private String getExistingAttribute(Token token){
        for (Token value : symbolTable) {
            if ((Objects.equals(value.tokenName, token.tokenName)) && (Objects.equals(value.value, token.value))) {
                return value.attributeValue;
            }
        }
        return null;
    }

    private boolean checkValue(String value) {
        for (Token token : symbolTable) {
            if (Objects.equals(token.value, value)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkTokenName(String tokenName) {
        for (Token token : symbolTable) {
            if (Objects.equals(token.tokenName, tokenName)) {
                return false;
            }
        }
        return true;
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
                flag = false;
                return null;
            }
            if (character == '\n') {
                character = readNextCharacter();
                lineNumber++;
            }
            switch (state) {
                case 0 -> {
                    if (character == 'd') {
                        state = 48;
                    } else if (character == 'c') {
                        state = 25;
                    } else if (character == '<') {
                        state = 1;
                    } else if (character == 'i') {
                        state = 20;
                    } else if (character == '=') {
                        state = 4;
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
                    } else if (character == '"') {
                        state = 61;
                    } else if (character == ' ' || character == '\t' ||
                            character == '\f' || character == '\b' || character == '\r') {
                        character = readNextCharacter();
                    } else if (isAlphabet(character)) {
                        state = 59;
                    } else if (isDigit(character)) {
                        state = 60;
                    }
                    else {
                        callErrorRoutine(true);
                    }
                }
                case 1 -> {
                    character = readNextCharacter();
                    if (character == '>') {
                        state = 6;
                    } else if (character == '=') {
                        state = 2;
                    } else {
                        state = 3;
                    }
                }
                case 2 -> {
                    character = readNextCharacter();
                    return new Token("GE", ">=","ROP", "GE", "-", lineNumber);
                }
                case 3 -> {
                    character = readNextCharacter();
                    return new Token("LT", "<","ROP", "LT", "-", lineNumber);
                }
                case 4 -> {
                    character = readNextCharacter();
                    if (character == '=') {
                        state = 5;
                    } else {
                        state = 14;
                    }
                }
                case 5 -> {
                    character = readNextCharacter();
                    return new Token("EQ","==" ,"ROP", "EQ", "-", lineNumber);
                }
                case 6 -> {
                    character = readNextCharacter();
                    return new Token("NE","<>" ,"ROP", "NE", "-", lineNumber);
                }
                case 7 -> {
                    character = readNextCharacter();
                    if (character == '=') {
                        state = 8;
                    } else {
                        state = 9;
                    }
                }
                case 8 -> {
                    character = readNextCharacter();
                    return new Token("LE", "<=","ROP", "LE", "-", lineNumber);
                }
                case 9 -> {
                    character = readNextCharacter();
                    return new Token("GT", ">","ROP", "GT", "-", lineNumber);
                }
                case 10 -> {
                    character = readNextCharacter();
                    return new Token("AD", "+","AOP", "AD", "-", lineNumber);
                }
                case 11 -> {
                    character = readNextCharacter();
                    return new Token("SB","-" ,"AOP", "SB", "-", lineNumber);
                }
                case 12 -> {
                    character = readNextCharacter();
                    return new Token("ML","*" ,"AOP", "ML", "-", lineNumber);
                }
                case 13 -> {
                    character = readNextCharacter();
                    if (character == '/') {
                        character = readNextCharacter();
                        while (character != '\n') {
                            character = readNextCharacter();
                        }
                        character = readNextCharacter();
                        lineNumber++;
                        state = 0;
                    } else if (character == '*') {
                        while (true){
                            if (character == '*' && (readNextCharacter() == '/')){
                                break;
                            }
                            else {
                                character = readNextCharacter();
                            }
                            if (character == '\n')
                                lineNumber++;
                        }
                        character = readNextCharacter();
                        while (character != '\n')
                            character = readNextCharacter();
                        character = readNextCharacter();
                        lineNumber++;
                        state = 0;
                    } else {
                        return new Token("DV", "/","AOP", "DV", "-", lineNumber);
                    }
                }
                case 14 -> {
                    character = readNextCharacter();
                    return new Token("AS", "=","OOP", "AS", "-", lineNumber);
                }
                case 15 -> {
                    character = readNextCharacter();
                    return new Token("OP", "(","OOP", "OP", "-", lineNumber);
                }
                case 16 -> {
                    character = readNextCharacter();
                    return new Token("CP", ")","OOP", "CP", "-", lineNumber);
                }
                case 17 -> {
                    character = readNextCharacter();
                    return new Token("OB", "{","OOP", "OB", "-", lineNumber);
                }
                case 18 -> {
                    character = readNextCharacter();
                    return new Token("CB", "}","OOP", "CB", "-", lineNumber);
                }
                case 19 -> {
                    character = readNextCharacter();
                    return new Token("TR", ";","OOP", "TR", "-", lineNumber);

                }
                case 20 -> {
                    character = readNextCharacter();
                    if (character == 'n') {
                        state = 21;
                    } else if (character == 'f') {
                        state = 39;
                    } else {
                        return checkIdentifiers("i"+character);
                    }
                }
                case 21 -> {
                    character = readNextCharacter();
                    if (character == 't') {
                        state = 22;
                    } else {
                        return checkIdentifiers("in"+character);
                    }
                }
                case 22 -> {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 23;
                    } else {
                        state = 24;
                    }
                }
                case 23 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute),"int" ,"INT", "-", "-", lineNumber);
                }
                case 24 -> {
                    return checkIdentifiers("int" + character);
                }
                case 25 -> {
                    character = readNextCharacter();
                    if (character == 'h') {
                        state = 26;
                    } else {
                        return checkIdentifiers("c"+character);
                    }
                }
                case 26 -> {
                    character = readNextCharacter();
                    if (character == 'a') {
                        state = 27;
                    } else {
                        return checkIdentifiers("ch"+character);
                    }
                }
                case 27 -> {
                    character = readNextCharacter();
                    if (character == 'r') {
                        state = 28;
                    } else {
                        return checkIdentifiers("cha"+character);
                    }
                }
                case 28 -> {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 29;
                    } else {
                        state = 30;
                    }
                }
                case 29 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "char","CHAR", "-", "", lineNumber);
                }
                case 30 -> {
                    character = readNextCharacter();
                    return checkIdentifiers("char" + character);
                }
                case 31 -> {
                    character = readNextCharacter();
                    if (character == 't') {
                        state = 32;
                    } else {
                        return checkIdentifiers("s"+character);
                    }
                }
                case 32 -> {
                    character = readNextCharacter();
                    if (character == 'r') {
                        state = 33;
                    } else
                        return checkIdentifiers("st"+character);
                }
                case 33 -> {
                    character = readNextCharacter();
                    if (character == 'i') {
                        state = 34;
                    } else
                        return checkIdentifiers("str"+character);
                }
                case 34 -> {
                    character = readNextCharacter();
                    if (character == 'n') {
                        state = 35;
                    } else {
                        return checkIdentifiers("stri"+character);
                    }
                }
                case 35 -> {
                    character = readNextCharacter();
                    if (character == 'g') {
                        state = 36;
                    } else {
                        return checkIdentifiers("strin"+character);
                    }
                }
                case 36 -> {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 37;
                    } else {
                        state = 38;
                    }
                }
                case 37 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute),"string","STRING", "-", "", lineNumber);
                }
                case 38 -> {
                    return checkIdentifiers("string" + character);
                }
                case 39 -> {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 40;
                    } else
                        state = 41;
                }
                case 40 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "if","IF", "-", "-", lineNumber);
                }
                case 41 -> {
                    return checkIdentifiers("if" + character);
                }
                case 42 -> {
                    character = readNextCharacter();
                    if (character == 'l')
                        state = 43;
                    else
                        return checkIdentifiers("e"+character);
                }
                case 43 -> {
                    character = readNextCharacter();
                    if (character == 's')
                        state = 44;
                    else
                        return checkIdentifiers("el"+character);
                }
                case 44 -> {
                    character = readNextCharacter();
                    if (character == 'e')
                        state = 45;
                    else
                        return checkIdentifiers("els"+character);
                }
                case 45 -> {
                    character = readNextCharacter();
                    if (character == ' ')
                        state = 46;
                    else
                        state = 47;
                }
                case 46 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "else", "ELSE", "-", "", lineNumber);
                }
                case 47 -> {
                    return checkIdentifiers("else" + character);
                }
                case 48 -> {
                    character = readNextCharacter();
                    if (character == 'O') {
                        state = 49;
                    } else {
                        return checkIdentifiers("D"+character);
                    }
                }
                case 49 -> {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 50;
                    } else {
                        state = 51;
                    }
                }
                case 50 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "do","DO", "", "", lineNumber);
                }
                case 51 -> {
                    return checkIdentifiers("do" + character);
                }
                case 52 -> {
                    character = readNextCharacter();
                    if (character == 'h') {
                        state = 53;
                    } else {
                        return checkIdentifiers("w"+character);
                    }
                }
                case 53 -> {
                    character = readNextCharacter();
                    if (character == 'i') {
                        state = 54;
                    } else {
                        return checkIdentifiers("wh"+character);
                    }
                }
                case 54 -> {
                    character = readNextCharacter();
                    if (character == 'l') {
                        state = 55;
                    } else {
                        return checkIdentifiers("whi"+character);
                    }
                }
                case 55 -> {
                    character = readNextCharacter();
                    if (character == 'e') {
                        state = 56;
                    } else {
                        return checkIdentifiers("whil"+character);
                    }
                }
                case 56 -> {
                    character = readNextCharacter();
                    if (character == ' ') {
                        state = 57;
                    } else {
                        state = 58;
                    }
                }
                case 57 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "while","WHILE", "-", "-", lineNumber);
                }
                case 58 -> {
                    return checkIdentifiers("while" + character);
                }
                case 59 -> {
                    return checkIdentifiers(String.valueOf(character));
                }
                case 60 -> {
                    StringBuilder word = new StringBuilder(String.valueOf(character));
                    while (true) {
                        character = readNextCharacter();
                        if (isDigit(character)) {
                            word.append(character);
                        } else {
                            if (checkTokenName(String.valueOf(word))) {
                                return new Token(String.valueOf(startAttribute), String.valueOf(word),"IV", String.valueOf(word), "-", lineNumber);
                            }
                            break;
                        }
                    }
                }
                case 61 -> {
                    character = readNextCharacter();
                    StringBuilder word = new StringBuilder();
                    while (character != '"') {
                        word.append(character);
                        character = readNextCharacter();
                    }
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), String.valueOf(word), "SL",String.valueOf(word),"-" , lineNumber);
                }
                default -> callErrorRoutine(false);
            }
        }
    }

    private void callErrorRoutine(boolean isInvalidLexeme){
        StringBuilder str = new StringBuilder();
        if (isInvalidLexeme) {
            str.append(character);
            while (true) {
                character = readNextCharacter();
                if (character == ' ' || character == '\n' || character == ';' || character == (char) -1){
                    errorsList.add(new Errors(lineNumber, "Invalid Lexeme", "Message: Invalid Lexeme Found.", String.valueOf(str)));
                    return;
                }
                str.append(character);
            }
        }
    }

    private char readNextCharacter() {
        try {
            return (char) reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (char) -1;
    }

    public Token checkIdentifiers(String s) {
        StringBuilder word = new StringBuilder(s);
        while (true) {
            character = readNextCharacter();
            if (isAlphabet(character) || isDigit(character)) {
                word.append(character);
            }
            else if (!Character.isLetterOrDigit(character) && !(character == ' ' || character == '\n' || character == ';' || character == (char) -1)){
                word.append(character);
                while (true){
                    character = readNextCharacter();
                    if (character == ' ' || character == '\n' || character == ';' || character == (char) -1){
                        errorsList.add(new Errors(lineNumber, "Invalid Identifier", "Message: Invalid Identifier Found.", String.valueOf(word)));
                        flag = true;
                        return null;
                    }
                    word.append(character);
                }
            }
            else {
                if (checkTokenName(String.valueOf(word))) {
                    return new Token(String.valueOf(startAttribute),String.valueOf(word), "ID", String.valueOf(word), "-", lineNumber);
                }
                break;
            }
        }
        return null;
    }

    boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    boolean isAlphabet(char c) {
        if (c >= 'a' && c <= 'z')
            return true;
        return c >= 'A' && c <= 'Z';
    }

}
