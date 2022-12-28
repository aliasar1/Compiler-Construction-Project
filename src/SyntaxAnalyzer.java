import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

class SyntaxAnalyzer{
    ArrayList<Token> tokenList;
    int i = 0;
    Stack<String> parseTree = new Stack<>();
    String str;
    public String error;
    String[] productionRules = {"E -> E + T", "E -> T", "T -> T * F", "T -> F", "F -> ( E )", "F -> id"};
    public String[][] slrTable = {
            {"s5", "", "", "s4", "", "", "1", "2", "3"},
            {"", "s6", "", "", "", "accept", "", "", ""},
            {"", "r2", "s7", "", "r2", "r2", "", "", ""},
            {"", "r4", "r4", "", "r4", "r4", "", "", ""},
            {"s5", "", "", "s4", "", "", "8", "2", "3"},
            {"", "r6", "r6", "", "r6", "r6", "", "", ""},
            {"s5", "", "", "s4", "", "", "", "9", "3"},
            {"s5", "", "", "s4", "", "", "", "", "10"},
            {"", "s6", "", "", "s11", "", "", "", ""},
            {"", "r1", "s7", "", "r1", "r1", "", "", ""},
            {"", "r3", "r3", "", "r3", "r3", "", "", ""},
            {"", "r5", "r5", "", "r5", "r5", "", "", ""}
    };
    Token EOF_TOKEN = new Token("-", "$", "EOF", "$", "-", Integer.MAX_VALUE);

    public SyntaxAnalyzer(String sourceCode) {
        LexicalAnalyzer la = new LexicalAnalyzer(sourceCode);
        tokenList = la.tokens;
        tokenList.add(EOF_TOKEN);
        parseTree.push("0");
        str = tokenList.get(i).lexeme;
    }

    public boolean recognizeSyntax(){
        while (true){
            String s = check(parseTree.peek(), str);
            if (Objects.equals(s, "NULL")){
                error = "Provided code is not part of the grammar.";
                return false;
            }
            if (Objects.equals(s, "")) {
                error = "Error found at line " + tokenList.get(i - 1).lineNumber;
                return false;
            }
            String findResultOp = String.valueOf(s.charAt(0));
            String findResultID = s.substring(1);

            if (s.equals("NULL")) {
                return false;
            }

            if (findResultOp.equals("s")){
                shift(findResultID, String.valueOf(tokenList.get(i).lexeme));
                i++;
                str = tokenList.get(i).lexeme;
            }
            else if (findResultOp.equals("r")){
                reduce(findResultID);
            }
            else return findResultOp.equals("a");
        }
    }

    private boolean isIdentifier(String s){
        for (Token token : tokenList) {
            if (Objects.equals(token.lexeme, s)) {
                if (Objects.equals(token.tokenName, "ID"))
                    return true;
            }
        }
        return false;
    }

    private void shift(String TextID, String ShiftText){
        int ID = Integer.parseInt(TextID);
        parseTree.push(ShiftText);
        parseTree.push(String.valueOf(ID));
    }

    private void reduce(String TextID){
        int ID = Integer.parseInt(TextID);
        for (int i = 0; i < (getBodyLength(productionRules[ID-1])) * 2; i++) {
            parseTree.pop();
        }
        parseTree.push(getHead(productionRules[ID-1]));
        String num = check(stackSecondTop(), parseTree.peek());
        parseTree.push(num);
    }

    private String getHead(String rule){
        String[] parts = rule.split(" -> ");
        return parts[0];
    }

    private int getBodyLength(String prod){
        String[] parts = prod.split(" -> ");
        if (parts.length < 2) {
            return 0;
        }
        String[] body = parts[1].split(" ");
        return body.length;
    }

    private String stackSecondTop(){
        String temp = parseTree.pop();
        String secondTop = parseTree.peek();
        parseTree.push(temp);
        return secondTop;
    }

    private String check(String Top, String secondTop) {
        if (isIdentifier(secondTop))
            secondTop = "ID";
        int col;
        int ID = Integer.parseInt(Top);
        switch (secondTop) {
            case "ID":
                col = 0;
                break;
            case "+":
                col = 1;
                break;
            case "*":
                col = 2;
                break;
            case "(":
                col = 3;
                break;
            case ")":
                col = 4;
                break;
            case "$":
                col = 5;
                break;
            case "E":
                col = 6;
                break;
            case "T":
                col = 7;
                break;
            case "F":
                col = 8;
                break;
            default:
                return "NULL";
        }
        return slrTable[ID][col];
    }
}