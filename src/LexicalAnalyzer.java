import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Objects;

public class LexicalAnalyzer {

    private int startAttribute = 0;
    public char character;
    private int lineNumber = 1;
    private boolean flag = true;

    private final BufferedReader reader;
    public final ArrayList<Token> tokens = new ArrayList<>();
    public final ArrayList<Token> symbolTable = new ArrayList<>();
    public final  ArrayList<Errors> errorsList = new ArrayList<>();

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
        return fmt;
    }

    private void generateTokens() {
        Token token = readNextToken();
        if (token == null && flag){
            token = readNextToken();
        }
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

    private Token readNextToken() {
        int state = 0;
        boolean lineFlag = false;
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
                while (true){
                    if (character == '\n'){
                        character = readNextCharacter();
                        lineNumber++;
                    }
                    else
                        break;
                }
                if (character == '\uFFFF'){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    flag = false;
                    return null;
                }
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
                    if (character == '\uFFFF')
                        return new Token("LT", "<","ROP", "LT", "-", lineNumber);
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
                    return new Token("LE", "<=","ROP", "LE", "-", lineNumber);
                }
                case 3 -> {
                    return new Token("LT", "<","ROP", "LT", "-", lineNumber);
                }
                case 4 -> {
                    character = readNextCharacter();
                    if (character == '\uFFFF')
                        return new Token("AS", "=","OOP", "AS", "-", lineNumber);
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
                    if (character == '\uFFFF')
                        return new Token("GT", ">","ROP", "GT", "-", lineNumber);
                    if (character == '=') {
                        state = 8;
                    }
                    else if (character == ' '){
                        return new Token("GT", ">","ROP", "GT", "-", lineNumber);
                    }
                    else {
                        state = 9;
                    }
                }
                case 8 -> {
                    character = readNextCharacter();
                    return new Token("GE", ">=","ROP", "GE", "-", lineNumber);
                }
                case 9 -> {
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
                        while (character != '\n' && character != '\uFFFF') {
                            character = readNextCharacter();
                        }
                        character = readNextCharacter();
                        lineNumber++;
                        state = 0;
                    } else if (character == '*') {
                        character = readNextCharacter();
                        while (true){
                            if (character == '*' && (readNextCharacter() == '/')){
                                break;
                            }
                            else {
                                character = readNextCharacter();
                            }
                            if (character == '\uFFFF'){
                                errorsList.add(new Errors(lineNumber+1, "Multiline Comment", "Message: Multiline comment is not closed.", "Multiline comment"));
                                return null;
                            }
                            if (character == '\n')
                                lineNumber++;
                        }
                        character = readNextCharacter();
                        state = 0;
                    } else {
                        return new Token("DV", "/","AOP", "DV", "-", lineNumber);
                    }
                }
                case 14 -> {
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
                        return checkIdentifiers("i", String.valueOf(character));
                    }
                }
                case 21 -> {
                    character = readNextCharacter();
                    if (character == 't') {
                        state = 22;
                    } else {
                        return checkIdentifiers("in",String.valueOf(character));
                    }
                }
                case 22 -> {
                    character = readNextCharacter();
                    if (character == '\uFFFF'){
                        return new Token(String.valueOf(startAttribute),"int" ,"INT", "-", "-", lineNumber);
                    }
                    if (character == ' ') {
                        state = 23;
                    }
                    else if (character == '\n'){
                        character = readNextCharacter();
                        lineFlag = true;
                        state = 23;
                    }
                    else {
                        state = 24;
                    }
                }
                case 23 -> {
                    if (lineFlag){
                        return new Token(String.valueOf(startAttribute),"int" ,"INT", "-", "-", lineNumber++);
                    }
                    else
                        return new Token(String.valueOf(startAttribute),"int" ,"INT", "-", "-", lineNumber);
                }
                case 24 -> {
                    return checkIdentifiers("int",String.valueOf(character));
                }
                case 25 -> {
                    character = readNextCharacter();
                    if (character == 'h') {
                        state = 26;
                    } else {
                        return checkIdentifiers("c",String.valueOf(character));
                    }
                }
                case 26 -> {
                    character = readNextCharacter();
                    if (character == 'a') {
                        state = 27;
                    } else {
                        return checkIdentifiers("ch",String.valueOf(character));
                    }
                }
                case 27 -> {
                    character = readNextCharacter();
                    if (character == 'r') {
                        state = 28;
                    } else {
                        return checkIdentifiers("cha",String.valueOf(character));
                    }
                }
                case 28 -> {
                    character = readNextCharacter();
                    if (character == '\uFFFF'){
                        return new Token(String.valueOf(startAttribute), "char","CHAR", "-", "", lineNumber);
                    }
                    if (character == ' ') {
                        state = 29;
                    }
                    else if (character == '\n'){
                        character = readNextCharacter();
                        lineFlag = true;
                        state = 29;
                    }
                    else {
                        state = 30;
                    }
                }
                case 29 -> {
                    if (lineFlag)
                        return new Token(String.valueOf(startAttribute), "char","CHAR", "-", "", lineNumber++);
                    else
                        return new Token(String.valueOf(startAttribute), "char","CHAR", "-", "", lineNumber);
                }
                case 30 -> {
                    return checkIdentifiers("char",String.valueOf(character));
                }
                case 31 -> {
                    character = readNextCharacter();
                    if (character == 't') {
                        state = 32;
                    } else {
                        return checkIdentifiers("s",String.valueOf(character));
                    }
                }
                case 32 -> {
                    character = readNextCharacter();
                    if (character == 'r') {
                        state = 33;
                    } else
                        return checkIdentifiers("st",String.valueOf(character));
                }
                case 33 -> {
                    character = readNextCharacter();
                    if (character == 'i') {
                        state = 34;
                    } else
                        return checkIdentifiers("str",String.valueOf(character));
                }
                case 34 -> {
                    character = readNextCharacter();
                    if (character == 'n') {
                        state = 35;
                    } else {
                        return checkIdentifiers("stri",String.valueOf(character));
                    }
                }
                case 35 -> {
                    character = readNextCharacter();
                    if (character == 'g') {
                        state = 36;
                    } else {
                        return checkIdentifiers("strin",String.valueOf(character));
                    }
                }
                case 36 -> {
                    character = readNextCharacter();
                    if (character == '\uFFFF')
                        return new Token(String.valueOf(startAttribute),"string","STRING", "-", "", lineNumber);
                    if (character == ' ') {
                        state = 37;
                    }
                    else if (character == '\n'){
                        character = readNextCharacter();
                        lineFlag = true;
                        state = 37;
                    }
                    else {
                        state = 38;
                    }
                }
                case 37 -> {
                    if (lineFlag)
                        return new Token(String.valueOf(startAttribute),"string","STRING", "-", "", lineNumber++);
                    else
                        return new Token(String.valueOf(startAttribute),"string","STRING", "-", "", lineNumber);
                }
                case 38 -> {
                    return checkIdentifiers("string",String.valueOf(character));
                }
                case 39 -> {
                    character = readNextCharacter();
                    if (character == '\uFFFF')
                        return new Token(String.valueOf(startAttribute), "if","IF", "-", "-", lineNumber);
                    if (character == ' ') {
                        state = 40;
                    }
                    else if (character == '\n'){
                        character = readNextCharacter();
                        lineFlag = true;
                        state = 40;
                    }
                    else
                        state = 41;
                }
                case 40 -> {
                    if (lineFlag)
                        return new Token(String.valueOf(startAttribute), "if","IF", "-", "-", lineNumber++);
                    else
                        return new Token(String.valueOf(startAttribute), "if","IF", "-", "-", lineNumber);
                }
                case 41 -> {
                    return checkIdentifiers("if",String.valueOf(character));
                }
                case 42 -> {
                    character = readNextCharacter();
                    if (character == 'l')
                        state = 43;
                    else
                        return checkIdentifiers("e",String.valueOf(character));
                }
                case 43 -> {
                    character = readNextCharacter();
                    if (character == 's')
                        state = 44;
                    else
                        return checkIdentifiers("el",String.valueOf(character));
                }
                case 44 -> {
                    character = readNextCharacter();
                    if (character == 'e')
                        state = 45;
                    else
                        return checkIdentifiers("els",String.valueOf(character));
                }
                case 45 -> {
                    character = readNextCharacter();
                    if (character == '\uFFFF')
                        return new Token(String.valueOf(startAttribute), "else", "ELSE", "-", "", lineNumber);
                    if (character == ' ')
                        state = 46;
                    else if (character == '\n'){
                        character = readNextCharacter();
                        lineFlag = true;
                        state = 46;
                    }
                    else
                        state = 47;
                }
                case 46 -> {
                    if (lineFlag)
                        return new Token(String.valueOf(startAttribute), "else", "ELSE", "-", "", lineNumber++);
                    else
                        return new Token(String.valueOf(startAttribute), "else", "ELSE", "-", "", lineNumber);
                }
                case 47 -> {
                    return checkIdentifiers("else",String.valueOf(character));
                }
                case 48 -> {
                    character = readNextCharacter();
                    if (character == 'o') {
                        state = 49;
                    } else {
                        return checkIdentifiers("d",String.valueOf(character));
                    }
                }
                case 49 -> {
                    character = readNextCharacter();
                    if (character == '\uFFFF')
                        return new Token(String.valueOf(startAttribute), "do","DO", "", "", lineNumber);
                    if (character == ' ') {
                        state = 50;
                    }
                    else if (character == '\n'){
                        character = readNextCharacter();
                        lineFlag = true;
                        state = 50;
                    }
                    else {
                        state = 51;
                    }
                }
                case 50 -> {
                    if (lineFlag)
                        return new Token(String.valueOf(startAttribute), "do","DO", "", "", lineNumber++);
                    else
                        return new Token(String.valueOf(startAttribute), "do","DO", "", "", lineNumber);
                }
                case 51 -> {
                    return checkIdentifiers("do",String.valueOf(character));
                }
                case 52 -> {
                    character = readNextCharacter();
                    if (character == 'h') {
                        state = 53;
                    } else {
                        return checkIdentifiers("w",String.valueOf(character));
                    }
                }
                case 53 -> {
                    character = readNextCharacter();
                    if (character == 'i') {
                        state = 54;
                    } else {
                        return checkIdentifiers("wh",String.valueOf(character));
                    }
                }
                case 54 -> {
                    character = readNextCharacter();
                    if (character == 'l') {
                        state = 55;
                    } else {
                        return checkIdentifiers("whi",String.valueOf(character));
                    }
                }
                case 55 -> {
                    character = readNextCharacter();
                    if (character == 'e') {
                        state = 56;
                    } else {
                        return checkIdentifiers("whil",String.valueOf(character));
                    }
                }
                case 56 -> {
                    character = readNextCharacter();
                    if (character == '\uFFFF')
                        return new Token(String.valueOf(startAttribute), "while","WHILE", "-", "-", lineNumber);
                    if (character == ' ') {
                        state = 57;
                    }
                    else if (character == '\n'){
                        character = readNextCharacter();
                        lineFlag = true;
                        state = 57;
                    }
                    else {
                        state = 58;
                    }
                }
                case 57 -> {
                    if (lineFlag)
                        return new Token(String.valueOf(startAttribute), "while","WHILE", "-", "-", lineNumber++);
                    else
                        return new Token(String.valueOf(startAttribute), "while","WHILE", "-", "-", lineNumber);
                }
                case 58 -> {
                    return checkIdentifiers("while",String.valueOf(character));
                }
                case 59 -> {
                    return checkIdentifiers(String.valueOf(character),"");
                }
                case 60 -> {
                    Token t = checkInteger();
                    if (t != null)
                        return t;
                    else
                        state = 0;
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

    private Token checkInteger(){
        StringBuilder word = new StringBuilder(String.valueOf(character));
        while (true) {
            character = readNextCharacter();
            if (isDigit(character)) {
                word.append(character);
            }
            else if (checker(character)){
                if (checkTokenName(String.valueOf(word))) {
                    return new Token(String.valueOf(startAttribute), String.valueOf(word),"IV", String.valueOf(word), "-", lineNumber);
                }
            }
            else if (!isDigit(character) && (isAlphabet(character) || !(character == ' ' || character == '\n' || character == ';' || character == '=' || character == ')'|| character == (char) -1))){
                while (true){
                    if (character != ';')
                        word.append(character);
                    if (character == ' ' || character == '\n' || character == ';' || character == '=' || character == ')'|| character == (char) -1){
                        errorsList.add(new Errors(lineNumber, "Invalid Lexeme", "Message: Invalid Lexeme Found.", String.valueOf(word)));
                        return null;
                    }
                    character = readNextCharacter();
                    if (character == '\uFFFF' || character == '\n'){
                        errorsList.add(new Errors(lineNumber, "Invalid Lexeme", "Message: Invalid Lexeme Found.", String.valueOf(word)));
                        return null;
                    }
                }
            }
            else {
                if (checkTokenName(String.valueOf(word))) {
                    return new Token(String.valueOf(startAttribute), String.valueOf(word),"IV", String.valueOf(word), "-", lineNumber);
                }
            }
        }
    }

    private void callErrorRoutine(boolean isInvalidLexeme){
        StringBuilder str = new StringBuilder();
        if (isInvalidLexeme) {
            str.append(character);
            while (true) {
                character = readNextCharacter();
                if (character == ' ' || character == '\n' || character == ';' || character == '=' || character == ')'|| character == (char) -1){
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

    private int getAttributeValue(String str){
        return switch (str) {
            case "int" -> 0;
            case "char" -> 1;
            case "string" -> 2;
            case "if" -> 3;
            case "else" -> 4;
            case "do" -> 5;
            case "while" -> 6;
            default -> -1;
        };
    }

    private Token checkIdentifiers(String s, String newS) {
        StringBuilder word = new StringBuilder(s);
        if (Objects.equals(newS, String.valueOf(';'))){
            int a = getAttributeValue(String.valueOf(word));
            return new Token(String.valueOf(a),String.valueOf(word), s.toUpperCase(), String.valueOf(word), "-", lineNumber);

        }
        else if (!newS.isEmpty()){
            if (checker(newS.charAt(0))){
                if (checkTokenName(String.valueOf(word))) {
                    return new Token(String.valueOf(startAttribute),String.valueOf(word), "ID", String.valueOf(word), "-", lineNumber);
                }
            }
        }
        if (newS.equals(String.valueOf(' '))){
            if (checkTokenName(String.valueOf(word))) {
                return new Token(String.valueOf(startAttribute),String.valueOf(word), "ID", String.valueOf(word), "-", lineNumber);
            }
        }
        else if (newS.equals(String.valueOf('\n'))){
            if (checkTokenName(String.valueOf(word))) {
                return new Token(String.valueOf(startAttribute),String.valueOf(word), "ID", String.valueOf(word), "-", lineNumber);
            }
        }
        else if (newS.equals(String.valueOf('\uFFFF'))){
            if (checkTokenName(String.valueOf(word))) {
                return new Token(String.valueOf(startAttribute),String.valueOf(word), "ID", String.valueOf(word), "-", lineNumber);
            }
        }
        else if (!Objects.equals(newS, String.valueOf('\uFFFF'))) {
            word.append(newS);
        }
        while (true) {
            character = readNextCharacter();
            char c = 0;
            boolean invalid = false;
            if (!newS.equals("")){
                invalid = true;
                c = newS.charAt(0);
            }
            if (invalid){
                if (character == ';'){
                    errorsList.add(new Errors(lineNumber, "Invalid Identifier", "Message: Invalid Identifier Found.", String.valueOf(word)));
                    flag = true;
                    return null;
                }
                if (!isAlphabet(c) && !isDigit(c)){
                    word.append(character);
                    while (true){
                        character = readNextCharacter();
                        if (character == ' ' || character == '\n' || character == ';' || character == '=' || character == ')'|| character == (char) -1){
                            errorsList.add(new Errors(lineNumber, "Invalid Identifier", "Message: Invalid Identifier Found.", String.valueOf(word)));
                            flag = true;
                            return null;
                        }
                        word.append(character);
                    }
                }
            }
            if (isAlphabet(character) || isDigit(character)) {
                word.append(character);
            }
            else if (character == ' ' || character == '\n' || character == ';' || character == '=' || character == ')'|| character == (char) -1 || checker(character)){
                if (checkTokenName(String.valueOf(word))) {
                    return new Token(String.valueOf(startAttribute),String.valueOf(word), "ID", String.valueOf(word), "-", lineNumber);
                }
                break;
            }
            else {
                word.append(character);
                while (true){
                    character = readNextCharacter();
                    if (character == ' ' || character == '\n' || character == ';' || character == '=' || character == ')'|| character == (char) -1){
                        errorsList.add(new Errors(lineNumber, "Invalid Identifier", "Message: Invalid Identifier Found.", String.valueOf(word)));
                        flag = true;
                        return null;
                    }
                    word.append(character);
                }
            }
        }
        return null;
    }

    private boolean checker(char c){
        return c == '+' || c == '-' || c == '/' || c == '*' || c == '(' || c == ')' || c == '{' || c == '}' || c == '=' || c == '<' || c == '>';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlphabet(char c) {
        if (c >= 'a' && c <= 'z')
            return true;
        return c >= 'A' && c <= 'Z';
    }
}
