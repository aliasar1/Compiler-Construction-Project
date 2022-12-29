import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Formatter;

public class MainForm extends JFrame {
    public void createGUI() {
        // Set the frame properties
        // Get the available screen bounds, which will exclude the taskbar
        Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        // Set the minimum size of the frame to the available screen bounds
        setMinimumSize(new Dimension(screenBounds.width, screenBounds.height));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setTitle("Compiler Construction");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a panel to hold the scroll panes and labels
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create the first label and scroll pane
        JLabel label1 = new JLabel("INPUT CODE");
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea inputTextArea = new JTextArea();
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane();
        inputScrollPane.setViewportView(inputTextArea);
        inputScrollPane.setPreferredSize(new Dimension(100, 100));

        // Add the label and scroll pane to the panel
        panel.add(label1);
        panel.add(inputScrollPane);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Create the second label and scroll pane
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

        // Create a panel to hold the buttons in a horizontal line
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));


        // Create the buttons
        JButton button1 = new JButton("Lexical Analyzer");
        JButton button2 = new JButton("Syntax Analyzer");
        JButton button3 = new JButton("Exit");
        button1.add(Box.createRigidArea(new Dimension(200, 20)));
        button2.add(Box.createRigidArea(new Dimension(200, 20)));
        button3.add(Box.createRigidArea(new Dimension(50, 20)));
        // Add the buttons to the button panel
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        // Add the button panel to the main panel
        panel.add(buttonPanel);

        // Add the label and scroll pane to the panel
        panel.add(label2);
        panel.add(outputScrollPane);

        // Add the panel to the frame
        add(panel);

        // Display the frame
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
                sa.la.tokens.remove(sa.EOF_TOKEN);
                sa.la.symbolTable.remove(sa.EOF_TOKEN);
                Formatter f1 = sa.la.printLexTable();
                Formatter f2 = sa.la.printTable();
                String show = h1 + f1.toString() + line + h2 + f2.toString() + line;
                boolean f = sa.recognizeSyntax();
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