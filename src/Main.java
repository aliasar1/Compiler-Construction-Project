import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
<<<<<<< Updated upstream
    public static void main(String[] args) throws IOException {
        // MyFrame frame = new MyFrame();
//        String sc = """
//                int val1 = 21; // to store a value
//                int val2;
//                int total;
//                string purpose = "Calculation";
//                if ( val1 > 10 ) {
//                 val2 = 5;
//                }
//                else {
//                 val2 = 10;
//                }
//                total = (val1 + val2) )* val1;
//                """;
        String sc = "total = (val1 + val2) * val1;" ;

  sc = sc+'$';
=======
    public static void main(String[] args) {
         MainFrame frame = new MainFrame();
//
//        frame.setVisible(true);
//        Toolkit tk=Toolkit.getDefaultToolkit(); //Initializing the Toolkit class.
//        Dimension size = tk.getScreenSize(); //Get the Screen resolution of our device.
//        frame.setSize(size.width,size.height);
//        frame.setSize(800,800);


        String sc = """
                inT 2val1 = 21; // to store a value
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
        sc = sc+'$';
>>>>>>> Stashed changes
        LexicalAnalyzer lex = new LexicalAnalyzer(sc);
        lex.printTable();
        System.out.println("------------------------");
        lex.printLexTable();
    }


}