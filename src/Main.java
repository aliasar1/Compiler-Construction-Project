public class Main {
    public static void main(String[] args) {
//         MyFrame frame = new MyFrame();
        String sc = """
                int val1 = 21; // to store a value
                int val2;
                int total;
                string purpose = "Calculation"; /* to store
                calculation */
                if ( val1 > 10 ) {
                 val2 = 5;
                }
                else {
                 val2 = 10;
                }
                total = (val1 + val2) * val1;
                """;
//        String sc = """
////                int val2;
//                int total;
//                """;
        LexicalAnalyzer lex = new LexicalAnalyzer(sc);
        lex.printLexTable();
        lex.printTable();
        lex.errorTable();
    }
}