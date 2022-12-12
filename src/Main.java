import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // MyFrame frame = new MyFrame();
        String sc = """
                int val1 = 21; // to store a value
                int val2;
                int total;
                string purpose = "Calculation";
                if ( val1 > 10 ) {
                 val2 = 5;
                }
                else {
                 val2 = 10;
                }
                total = (val1 + val2) * val1;
                """;
        sc = sc+'$';
        LexicalAnalyzer lex = new LexicalAnalyzer(sc);
        lex.printTable();
    }
}