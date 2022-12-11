import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        //me frame = new MyFrame();
        String sc = "int val1 = 21; // to store a value\n" +
                "int val2;\n" +
                "int total;\n" +
                "string purpose = “Calculation”; /* to store \n" +
                "calculation */\n" +
                "if ( val1 > 10 ) {\n" +
                " val2 = 5;\n" +
                "}\n" +
                "else {\n" +
                " val2 = 10;\n" +
                "}\n" +
                "total = (val1 + val2) * val1;\n";

        String test = "// hello\n";
        LexicalAnalyzer lex = new LexicalAnalyzer(test);
        try {
            List<Token> ls = lex.generateTokens();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}