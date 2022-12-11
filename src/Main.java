import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        //me frame = new MyFrame();
        String sc = "int a = 3;\n";


        LexicalAnalyzer lex = new LexicalAnalyzer(sc);
        lex.printTable();
    }
}