import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private final String identifierRegex = "^[a-zA-Z][a-zA-Z|\\d]*";
    Pattern p = Pattern.compile(identifierRegex);

    BufferedReader reader;


    int startAttribute = 0;
    char character;
    int count = 0;
    int lineNumber = 1;
    char s;

    private final ArrayList<String> tokenName = new ArrayList<String>();
    public final ArrayList<Token> tokens = new ArrayList<>();


    private final ArrayList<Token> symbolTable = new ArrayList<>();

    public LexicalAnalyzer(String sourceCode) throws IOException {
        initializeReserveKeywords();
        //  int, char, string, if, else, do, while
        Reader code = new StringReader(sourceCode);
        reader = new BufferedReader(code);
        character = readNextCharacter();
        generateTokens();
    }

    private void initializeReserveKeywords() {
<<<<<<< Updated upstream
        tokenName.add("INT");
        tokenName.add("CHAR");
        tokenName.add("STRING");
        tokenName.add("IF");
        tokenName.add("ELSE");
        tokenName.add("DO");
        tokenName.add("WHILE");
        for (int i = 0; i < tokenName.size(); i++) {
            Token token = new Token(startAttribute, tokenName.get(i), "-", "-", lineNumber);
            symbolTable.add(token);
            startAttribute++;
        }
=======
        symbolTable.add(new Token(String.valueOf(startAttribute++), "int", "INT", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "char", "CHAR", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "String", "STRING", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "if", "IF", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "else", "ELSE", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "do", "DO", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "while", "WHILE", "-", "-", lineNumber));
>>>>>>> Stashed changes
    }

    public void printTable() {
        for (int i = 0; i < symbolTable.size(); i++) {
            System.out.println(symbolTable.get(i).toString());
        }
    }

    public void printLexTable() {
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(tokens.get(i).toString());
        }
    }

    public void generateTokens() {
        Token token = readNextToken();
        while (token != null) {
            if (Objects.equals(token.tokenName, "ID") || Objects.equals(token.tokenName, "IV") || Objects.equals(token.tokenName, "SL")) {
                if (checkTokenName(token.tokenName) || checkValue(token.value)) {

                    symbolTable.add(token);
                }
            }
<<<<<<< Updated upstream
            character = readNextCharacter();
            tokens.add(token);
            // TODO
            // check for duplicate IDS and int value
            // meaningful errors triggers
=======
            String a = getExistingAttribute(token);
            if (a != null) {
                token.attributeValue = a;
                tokens.add(token);
            } else {
                tokens.add(token);
            }
>>>>>>> Stashed changes
            token = readNextToken();
        }
    }

<<<<<<< Updated upstream
=======
    private String getExistingAttribute(Token token) {
        for (Token value : symbolTable) {
            if ((Objects.equals(value.tokenName, token.tokenName)) && (Objects.equals(value.value, token.value))) {
                return value.attributeValue;
            }
        }
        return null;
    }

>>>>>>> Stashed changes
    private boolean checkValue(String value) {
        for (int i = 0; i < symbolTable.size(); i++) {
            if (Objects.equals(symbolTable.get(i).value, value)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkTokenName(String tokenName) {
        for (int i = 0; i < symbolTable.size(); i++) {
            if (Objects.equals(symbolTable.get(i).tokenName, tokenName)) {
//                System.out.print("error at line :"+Objects.toString(tokens.get(4)));
                return false;
            }
        }
        return true;
    }

    public Token readNextToken() {
        int state = 0;
        while (true) {
            if (character == '$') {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                    } else if (character == ' ' || character == '\n' || character == '\t' ||
                            character == '\f' || character == '\b' || character == '\r') {
                        character = readNextCharacter();
                    } else if (isAlphabet(character)) {
                        state = 59;
                    } else if (isDigit(character)) {
                        state = 60;
                    } else {
                        fail();
                    }
                }
                case 1 -> {
                    if (character == '>') {
                        state = 6;
                    } else if (character == '=') {
                        state = 2;
                    } else {
                        state = 3;
                    }
                }
                case 2 -> {
<<<<<<< Updated upstream
                    return new Token(startAttribute, "ROP", "GE", "-", lineNumber);
                }
                case 3 -> {
                    return new Token(startAttribute, "ROP", "LT", "-", lineNumber);
=======
                    character = readNextCharacter();
                    return new Token("GE", ">=", "ROP", "GE", "-", lineNumber);
                }
                case 3 -> {
                    character = readNextCharacter();
                    return new Token("LT", "<", "ROP", "LT", "-", lineNumber);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
                    return new Token(startAttribute, "ROP", "EQ", "-", lineNumber);
                }
                case 6 -> {
                    return new Token(startAttribute, "ROP", "NE", "-", lineNumber);
=======
                    character = readNextCharacter();
                    return new Token("EQ", "==", "ROP", "EQ", "-", lineNumber);
                }
                case 6 -> {
                    character = readNextCharacter();
                    return new Token("NE", "<>", "ROP", "NE", "-", lineNumber);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
                    return new Token(startAttribute, "ROP", "LE", "-", lineNumber);
                }
                case 9 -> {
                    return new Token(startAttribute, "ROP", "GT", "-", lineNumber);
                }
                case 10 -> {
                    return new Token(startAttribute, "AOP", "AD", "-", lineNumber);
                }
                case 11 -> {
                    return new Token(startAttribute, "AOP", "SB", "-", lineNumber);
                }
                case 12 -> {
                    return new Token(startAttribute, "AOP", "ML", "-", lineNumber);
=======
                    character = readNextCharacter();
                    return new Token("LE", "<=", "ROP", "LE", "-", lineNumber);
                }
                case 9 -> {
                    character = readNextCharacter();
                    return new Token("GT", ">", "ROP", "GT", "-", lineNumber);
                }
                case 10 -> {
                    character = readNextCharacter();
                    return new Token("AD", "+", "AOP", "AD", "-", lineNumber);
                }
                case 11 -> {
                    character = readNextCharacter();
                    return new Token("SB", "-", "AOP", "SB", "-", lineNumber);
                }
                case 12 -> {
                    character = readNextCharacter();
                    return new Token("ML", "*", "AOP", "ML", "-", lineNumber);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
                        while (true) {
                            if (character == '*' && (readNextCharacter() == '/')) {
                                break;
                            } else {
                                character = readNextCharacter();
                            }
                            if (character == '\n')
                                lineNumber++;
                        }
>>>>>>> Stashed changes
                        character = readNextCharacter();
                        while (character != '*' && (character = readNextCharacter()) != '/') {
                            character = readNextCharacter();
                        }
                        do {
                            character = readNextCharacter();
                        } while (character != '\n');
                        {
                            character = readNextCharacter();
                        }
                        count++;
                    } else {
<<<<<<< Updated upstream
                        return new Token(startAttribute, "AOP", "DV", "-", lineNumber);
                    }
                }
                case 14 -> {
                    return new Token(startAttribute, "OOP", "EQ", "-", lineNumber);
                }
                case 15 -> {
                    return new Token(startAttribute, "OOP", "OP", "-", lineNumber);
                }
                case 16 -> {
                    return new Token(startAttribute, "OOP", "CP", "-", lineNumber);
                }
                case 17 -> {
                    return new Token(startAttribute, "OOP", "OB", "-", lineNumber);
                }
                case 18 -> {
                    return new Token(startAttribute, "OOP", "CB", "-", lineNumber);
                }
                case 19 -> {
                    character = readNextCharacter();
                    return new Token(startAttribute, "OOP", "TR", "-", lineNumber);
=======
                        return new Token("DV", "/", "AOP", "DV", "-", lineNumber);
                    }
                }
                case 14 -> {
                    character = readNextCharacter();
                    return new Token("AS", "=", "OOP", "AS", "-", lineNumber);
                }
                case 15 -> {
                    character = readNextCharacter();
                    return new Token("OP", "(", "OOP", "OP", "-", lineNumber);
                }
                case 16 -> {
                    character = readNextCharacter();
                    return new Token("CP", ")", "OOP", "CP", "-", lineNumber);
                }
                case 17 -> {
                    character = readNextCharacter();
                    return new Token("OB", "{", "OOP", "OB", "-", lineNumber);
                }
                case 18 -> {
                    character = readNextCharacter();
                    return new Token("CB", "}", "OOP", "CB", "-", lineNumber);
                }
                case 19 -> {
                    character = readNextCharacter();
                    return new Token("TR", ";", "OOP", "TR", "-", lineNumber);
>>>>>>> Stashed changes
                }
                case 20 -> {
                    character = readNextCharacter();
                    if (character == 'n') {
                        state = 21;
                    } else if (character == 'f') {
                        state = 39;
                    } else {

                      //  return new Token("Token does not belong to the language", " " + character, " ", "Error in Line: " +lineNumber);
                        return new Token("Error","Error","not recognized token"," "," ",lineNumber);

                    }
                }
                case 21 -> {
                    character = readNextCharacter();
                    if (character == 't') {
                        state = 22;
                    } else {
                        return new Token("Error","Error","not recognized token"," "," ",lineNumber);
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
<<<<<<< Updated upstream
                    return new Token(startAttribute, "INT", "-", "-", lineNumber);
=======
                    character = readNextCharacter();
                    Token token = new Token(String.valueOf(startAttribute), "int", "INT", "-", "-", lineNumber);
//                    if(token.){
//
//                    }
                    return token;
>>>>>>> Stashed changes
                }
                case 24 -> {
                    return checkIdentifiers("int" + character);
                }
                case 25 -> {
                    character = readNextCharacter();
                    if (character == 'h') {
                        state = 26;
                    } else {
                        return checkIdentifiers("c");
                    }
                }
                case 26 -> {
                    character = readNextCharacter();
                    if (character == 'a') {
                        state = 27;
                    } else {
                        return checkIdentifiers("ch");
                    }
                }
                case 27 -> {
                    character = readNextCharacter();
                    if (character == 'r') {
                        state = 28;
                    } else {
                        return checkIdentifiers("cha");
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
<<<<<<< Updated upstream
                    return new Token(startAttribute, "CHAR", "", "", lineNumber);
=======
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "char", "CHAR", "-", "", lineNumber);
>>>>>>> Stashed changes
                }
                case 30 -> {
                    return checkIdentifiers("char" + character);
                }
                case 31 -> {
                    character = readNextCharacter();
                    if (character == 't') {
                        state = 32;
                    } else {
                        return checkIdentifiers("s");
                    }
                }
                case 32 -> {
                    character = readNextCharacter();
                    if (character == 'r') {
                        state = 33;
                    } else
                        return checkIdentifiers("st");
                }
                case 33 -> {
                    character = readNextCharacter();
                    if (character == 'i') {
                        state = 34;
                    } else
                        return checkIdentifiers("str");
                }
                case 34 -> {
                    character = readNextCharacter();
                    if (character == 'n') {
                        state = 35;
                    } else {
                        return checkIdentifiers("stri");
                    }
                }
                case 35 -> {
                    character = readNextCharacter();
                    if (character == 'g') {
                        state = 36;
                    } else {
                        return checkIdentifiers("strin");
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
<<<<<<< Updated upstream
                    return new Token(startAttribute, "STRING", "", "", lineNumber);
=======
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "String", "STRING", "-", "", lineNumber);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
                    return new Token(startAttribute, "IF", "-", "-", lineNumber);
=======
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "if", "IF", "-", "-", lineNumber);
>>>>>>> Stashed changes
                }
                case 41 -> {
                    return checkIdentifiers("if" + character);
                }
                case 42 -> {
                    character = readNextCharacter();
                    if (character == 'l')
                        state = 43;
                    else
                        return checkIdentifiers("e");
                }
                case 43 -> {
                    character = readNextCharacter();
                    if (character == 's')
                        state = 44;
                    else
                        return checkIdentifiers("el");
                }
                case 44 -> {
                    character = readNextCharacter();
                    if (character == 'e')
                        state = 45;
                    else
                        return checkIdentifiers("els");
                }
                case 45 -> {
                    character = readNextCharacter();
                    if (character == ' ')
                        state = 46;
                    else
                        state = 47;
                }
                case 46 -> {
                    return new Token(startAttribute, "ELSE", "", "", lineNumber);
                }
                case 47 -> {
                    return checkIdentifiers("else" + character);
                }
                case 48 -> {
                    character = readNextCharacter();
                    if (character == 'O') {
                        state = 49;
                    } else {
                        return checkIdentifiers("D");
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
<<<<<<< Updated upstream
                    return new Token(startAttribute, "DO", "-", "", lineNumber);
=======
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "do", "DO", "", "", lineNumber);
>>>>>>> Stashed changes
                }
                case 51 -> {
                    return checkIdentifiers("do" + character);
                }
                case 52 -> {
                    character = readNextCharacter();
                    if (character == 'h') {
                        state = 53;
                    } else {
                        return checkIdentifiers("w");
                    }
                }
                case 53 -> {
                    character = readNextCharacter();
                    if (character == 'i') {
                        state = 54;
                    } else {
                        return checkIdentifiers("wh");
                    }
                }
                case 54 -> {
                    character = readNextCharacter();
                    if (character == 'l') {
                        state = 55;
                    } else {
                        return checkIdentifiers("whi");
                    }
                }
                case 55 -> {
                    character = readNextCharacter();
                    if (character == 'e') {
                        state = 56;
                    } else {
                        return checkIdentifiers("whil");
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
<<<<<<< Updated upstream
                    return new Token(startAttribute, "WHILE", "", "-", lineNumber);
=======
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "while", "WHILE", "-", "-", lineNumber);
>>>>>>> Stashed changes
                }
                case 58 -> {
                    return checkIdentifiers("while" + character);
                }
                case 59 -> {
                    return checkIdentifiers(String.valueOf(character));
                }
                case 60 -> {
<<<<<<< Updated upstream
                    if (isDigit(character)) {
                        StringBuilder word = new StringBuilder(String.valueOf(character));
                        while (true) {
                            character = readNextCharacter();
                            if (isDigit(character)) {
                                word.append(character);
                            } else {
                                if (checkTokenName(String.valueOf(word))) {
                                    return new Token(startAttribute, "IV", String.valueOf(word), "-", lineNumber);
                                }
                                break;
=======
                    StringBuilder word = new StringBuilder(String.valueOf(character));
                    while (true) {
                        character = readNextCharacter();
                        if (isDigit(character)) {
                            word.append(character);
                        } else {
                            if (checkTokenName(String.valueOf(word))&&readNextCharacter()==' ') {
                                return new Token(String.valueOf(startAttribute), String.valueOf(word), "IV", String.valueOf(word), "-", lineNumber);
>>>>>>> Stashed changes
                            }
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
<<<<<<< Updated upstream
                    return new Token(startAttribute++, "SL", String.valueOf(word), "-", lineNumber);
=======
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), String.valueOf(word), "SL", String.valueOf(word), "-", lineNumber);
                }
                case 62 ->{
                    //error case
>>>>>>> Stashed changes
                }
                default -> fail();
            }
        }

    }

    char readNextCharacter() {
        try {
            return (char) reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (char) -1;
    }

    private Token checkIdentifiers(String s) {
        StringBuilder word = new StringBuilder(s);
        while (true) {
            character = readNextCharacter();
            if (isAlphabet(character) || isDigit(character)) {
                word.append(character);
            } else {
                if (checkTokenName(String.valueOf(word))) {
<<<<<<< Updated upstream
                    return new Token(startAttribute++, "ID", String.valueOf(word), "-", lineNumber);
=======
                    return new Token(String.valueOf(startAttribute), String.valueOf(word), "ID", String.valueOf(word), "-", lineNumber);
>>>>>>> Stashed changes
                }
                break;
            }
        }
        return null;
    }

    private void fail() {

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
