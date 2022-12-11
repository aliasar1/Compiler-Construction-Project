import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

    private ArrayList<String> tokenName = new ArrayList<String>();

    private ArrayList<Token> symbolTable = new ArrayList<>();

    public  LexicalAnalyzer(String sourceCode){
        initializeReserveKeywords();
        this.sourceCode = sourceCode;
        Reader code = new StringReader(sourceCode);
        reader = new BufferedReader(code);
        character = readNextCharacter();
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

    public List<Token> generateTokens() throws IOException {
        Token token = readNextToken();
        while (token != null) {
            symbolTable.add(token);
            token = readNextToken();
        }
        return symbolTable;
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
                    if (character == ' ' || character == '\n' || character == '\t' ||
                            character == '\f' || character == '\b' || character == '\r') {
                        character = readNextCharacter();
                        continue;
                    } else if (character == '/') {
                        character = readNextCharacter();
                        if (character == '/'){
                            character = readNextCharacter();
                            while (character != '\n') {
                                character = readNextCharacter();
                            }
                            character = readNextCharacter();
                            count++;
                        } else if (character == '*') {
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
                        }
                    }
                    else{
                        // character = readNextCharacter();
//                        Parser.add("/");
                    }
                }
                case 1:
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

}
