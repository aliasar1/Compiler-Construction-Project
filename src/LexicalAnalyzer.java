import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Objects;

public class LexicalAnalyzer {

//    private final String identifierRegex = "^[a-zA-Z][a-zA-Z|\\d]*";
//    Pattern p = Pattern.compile(identifierRegex);

    BufferedReader reader;


    int startAttribute = 0;
    char character;
    int count = 0;
    int lineNumber = 1;

    private final ArrayList<String> tokenName = new ArrayList<String>();
    public final ArrayList<Token> tokens = new ArrayList<>();


    private final ArrayList<Token> symbolTable = new ArrayList<>();

    public LexicalAnalyzer(String sourceCode) throws IOException {
        initializeReserveKeywords();
        Reader code = new StringReader(sourceCode);
        reader = new BufferedReader(code);
        character = readNextCharacter();
        generateTokens();
    }

    private void initializeReserveKeywords() {
        symbolTable.add(new Token(String.valueOf(startAttribute++), "int" ,"INT", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "char" ,"CHAR", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "String" ,"STRING", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "if" ,"IF", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "else" ,"ELSE", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "do" ,"DO", "-", "-", lineNumber));
        symbolTable.add(new Token(String.valueOf(startAttribute++), "while" ,"WHILE", "-", "-", lineNumber));
    }

    public void printTable() {
        System.out.println("\t\t\t\t\t\t\tSYMBOL TABLE");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Attribute Value\t\tToken Name\t\t\tType\t\tValue");
        System.out.println("-------------------------------------------------------------------------");
        Formatter fmt = new Formatter();
        for (int i = 0; i < symbolTable.size(); i++) {
            fmt.format("%6s  %16s %16s %16s\n",symbolTable.get(i).attributeValue , symbolTable.get(i).tokenName, symbolTable.get(i).type, symbolTable.get(i).value);
        }
        System.out.println(fmt);
        System.out.println("-----------------------------------------------------------------------");
    }
    public void printLexTable() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\tTOKENS");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("\t\tLexemes\t\t\t\tToken Name\t\t\tAttribute Value");
        System.out.println("-----------------------------------------------------------------------");
        Formatter fmt = new Formatter();
        for (int i = 0; i < tokens.size(); i++) {
            fmt.format("%14s  %20s  %14s\n",tokens.get(i).lexeme , tokens.get(i).tokenName, tokens.get(i).attributeValue);
        }
        System.out.println(fmt);
        System.out.println("-----------------------------------------------------------------------");
    }

    public void generateTokens() {
        Token token = readNextToken();
        while (token != null) {
            if (Objects.equals(token.tokenName, "ID") || Objects.equals(token.tokenName, "IV") || Objects.equals(token.tokenName, "SL")) {
                if (checkTokenName(token.tokenName) || checkValue(token.value)) {
                    symbolTable.add(token);
                }
            }
            String a = getExistingAttribute(token);
            if(a != null){
                token.attributeValue = a;
                tokens.add(token);
            }
            else
                tokens.add(token);
            token = readNextToken();
        }
    }

    private String getExistingAttribute(Token token){
        for (int i=0; i<symbolTable.size(); i++){
            if ((Objects.equals(symbolTable.get(i).tokenName, token.tokenName)) && (Objects.equals(symbolTable.get(i).value, token.value))){
                return symbolTable.get(i).attributeValue;
            }
        }
        return null;
    }

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
                    } else if (character == ' ' || character == '\t' ||
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
                    return new Token(String.valueOf(startAttribute), ">=","ROP", "GE", "-", lineNumber);
                }
                case 3 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "<","ROP", "LT", "-", lineNumber);
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
                    return new Token(String.valueOf(startAttribute),"==" ,"ROP", "EQ", "-", lineNumber);
                }
                case 6 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute),"<>" ,"ROP", "NE", "-", lineNumber);
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
                    return new Token(String.valueOf(startAttribute), "<=","ROP", "LE", "-", lineNumber);
                }
                case 9 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), ">","ROP", "GT", "-", lineNumber);
                }
                case 10 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "+","AOP", "AD", "-", lineNumber);
                }
                case 11 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute),"-" ,"AOP", "SB", "-", lineNumber);
                }
                case 12 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute),"*" ,"AOP", "ML", "-", lineNumber);
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
                            System.out.println(character);
                            if (character == '*' && (readNextCharacter() == '/')){
                                break;
                            }
                            else {
                                character = readNextCharacter();
                            }
                        }
                        character = readNextCharacter();
                        while (character != '\n')
                            character = readNextCharacter();
                        character = readNextCharacter();
                        state = 0;
//                        while (character != '*' && (character = readNextCharacter()) != '/') {
//                            character = readNextCharacter();
//                            System.out.println(character);
//                        }
//                        do {
//                            character = readNextCharacter();
//                        } while (character != '\n');
//                        {
//                            character = readNextCharacter();
//                        }
                    } else {
                        return new Token(String.valueOf(startAttribute), "/","AOP", "DV", "-", lineNumber);
                    }
                }
                case 14 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "=","OOP", "AS", "-", lineNumber);
                }
                case 15 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "(","OOP", "OP", "-", lineNumber);
                }
                case 16 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), ")","OOP", "CP", "-", lineNumber);
                }
                case 17 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "{","OOP", "OB", "-", lineNumber);
                }
                case 18 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), "}","OOP", "CB", "-", lineNumber);
                }
                case 19 -> {
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute), ";","OOP", "TR", "-", lineNumber);
                }
                case 20 -> {
                    character = readNextCharacter();
                    if (character == 'n') {
                        state = 21;
                    } else if (character == 'f') {
                        state = 39;
                    } else {
                        return checkIdentifiers("i");
                    }
                }
                case 21 -> {
                    character = readNextCharacter();
                    if (character == 't') {
                        state = 22;
                    } else {
                        return checkIdentifiers("in");
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
                    character = readNextCharacter();
                    return new Token(String.valueOf(startAttribute),"String","STRING", "-", "", lineNumber);
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
                                return new Token(String.valueOf(startAttribute++), String.valueOf(word),"IV", String.valueOf(word), "-", lineNumber);
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
                    return new Token(String.valueOf(startAttribute++), String.valueOf(word), String.valueOf(word), "SL","-" , lineNumber);
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
                    return new Token(String.valueOf(startAttribute++),String.valueOf(word), "ID", String.valueOf(word), "-", lineNumber);
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
