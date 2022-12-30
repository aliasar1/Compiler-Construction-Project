import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Formatter;

public class MainForm extends JFrame {
    public void createGUI() {

        Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setMinimumSize(new Dimension(screenBounds.width, screenBounds.height));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setTitle("Compiler Construction");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label1 = new JLabel("INPUT CODE");
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane();
        inputScrollPane.setViewportView(inputTextArea);
        inputScrollPane.setPreferredSize(new Dimension(100, 100));

        panel.add(label1);
        panel.add(inputScrollPane);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label2 = new JLabel("OUTPUT RESULT");
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.add(Box.createRigidArea(new Dimension(10, 40)));

        JTextArea outputTextArea = new JTextArea();
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane();
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setViewportView(outputTextArea);
        outputScrollPane.setPreferredSize(new Dimension(100, 100));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton button1 = new JButton("Lexical Analyzer");
        JButton button2 = new JButton("Syntax Analyzer");
        JButton button3 = new JButton("Exit");
        button1.add(Box.createRigidArea(new Dimension(200, 20)));
        button2.add(Box.createRigidArea(new Dimension(200, 20)));
        button3.add(Box.createRigidArea(new Dimension(50, 20)));
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        panel.add(buttonPanel);

        panel.add(label2);
        panel.add(outputScrollPane);

        add(panel);

        setVisible(true);

        button1.addActionListener(e -> {
            outputTextArea.setText("");
            try {
                System.setErr(new PrintStream("error.log"));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            String line = "------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            String h2 = line+"\t\t\tSYMBOL TABLE\n"+line+"   Attribute Value\t\tToken Name\t\tType\t\tValue\n"+line;
            String h1 = line+"\t\t\tTOKENS\n"+line+"      Lexeme\t\tToken Name\t\tAttribute Value\t\tLine Number\n"+line;
            String h3 = line+"\t\t\tErrors\n"+line+"      Lexeme\t\tError Type\t\tError Description\t\tLine Number\n"+line;
            String s = inputTextArea.getText();
            if (s.isBlank() || s.isEmpty()){
                outputTextArea.setText("Please provide an input source code.");
            }
            else {
                LexicalAnalyzer lx = new LexicalAnalyzer(s);
                Formatter f1 = lx.printLexTable();
                String output = h1 + f1.toString() + line;
                Formatter f2 = lx.printTable();
                Formatter f3 = lx.errorTable();
                outputTextArea.setText(output + h2 + f2.toString() + line + h3 + f3.toString() + line);
            }
        });

        button2.addActionListener(e -> {
            outputTextArea.setText("");
            try {
                System.setErr(new PrintStream("error.log"));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            String line = "------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
            String h2 = line+"\t\t\tSYMBOL TABLE\n"+line+"   Attribute Value\t\tToken Name\t\tType\t\tValue\n"+line;
            String h1 = line+"\t\t\tTOKENS\n"+line+"      Lexeme\t\tToken Name\t\tAttribute Value\t\tLine Number\n"+line;
            String s = inputTextArea.getText();
            if (s.isBlank() || s.isEmpty()){
                outputTextArea.setText("Please provide an input source code.");
            }
            else {
                SyntaxAnalyzer sa = new SyntaxAnalyzer(s);
                boolean f = sa.recognizeSyntax();
                sa.la.tokens.remove(sa.EOF_TOKEN);
                sa.la.symbolTable.remove(sa.EOF_TOKEN);
                Formatter f1 = sa.la.printLexTable();
                Formatter f2 = sa.la.printTable();
                String show = h1 + f1.toString() + line + h2 + f2.toString() + line;
                if (f)
                    outputTextArea.setText(show + "\nCompiled Successfully!");
                else{
                    outputTextArea.setText(show + sa.error + "\nCompilation failed!");
                }
            }
        });

        button3.addActionListener(e -> System.exit(0));
    }
}