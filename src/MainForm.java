import javax.swing.*;
import java.awt.*;
import java.util.Formatter;

public class MainForm extends JFrame {
    public void createGUI() {
        // Set the frame properties
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(screenSize));
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
          String s = inputTextArea.getText();
          LexicalAnalyzer lx = new LexicalAnalyzer(s);
          Formatter f1 = lx.printLexTable();
          Formatter f2 = lx.printTable();
          outputTextArea.setText(f1.toString() + "\n" + f2.toString());
        });
    }
}



//import javax.swing.*;
//import java.awt.*;
//import java.awt.*;
//import javax.swing.*;
//
//public class MainForm extends JFrame {
//    public MainForm() {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        setMinimumSize(new Dimension(screenSize));
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        pack();
//        setTitle("Compiler Construction");
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//
////        JLabel inputLabel = new JLabel("Input Code");
////        inputLabel.setBounds(50, 5, 20,50);
////        JLabel outputLabel = new JLabel("Output");
//
//        JTextArea inputTextArea = new JTextArea();
//        inputTextArea.setLineWrap(true);
//        inputTextArea.setWrapStyleWord(true);
//
//        // Create left scroll pane
//        JScrollPane leftScrollPane = new JScrollPane();
//        leftScrollPane.setPreferredSize(new Dimension(500, 50));
//        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        leftScrollPane.setViewportView(inputTextArea);
//
//        JTextArea outputTextArea = new JTextArea();
//        outputTextArea.setLineWrap(true);
//        outputTextArea.setWrapStyleWord(true);
//        // Create right scroll pane
//        JScrollPane rightScrollPane = new JScrollPane();
//        rightScrollPane.setPreferredSize(new Dimension(500, 50));
//        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        rightScrollPane.setViewportView(outputTextArea);
//
//        // Create button panel
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
//        JButton button1 = new JButton("Button 1");
//        JButton button2 = new JButton("Button 2");
//        JButton button3 = new JButton("Button 3");
//        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
//        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
//        button3.setAlignmentX(Component.CENTER_ALIGNMENT);
//        button1.setAlignmentY(Component.CENTER_ALIGNMENT);
//        button2.setAlignmentY(Component.CENTER_ALIGNMENT);
//        button3.setAlignmentY(Component.CENTER_ALIGNMENT);
//        buttonPanel.add(button1);
//        buttonPanel.add(button2);
//        buttonPanel.add(button3);
//
//        // Add components to main panel
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BorderLayout());
//        mainPanel.add(leftScrollPane, BorderLayout.WEST);
//        mainPanel.add(buttonPanel, BorderLayout.CENTER);
//        mainPanel.add(rightScrollPane, BorderLayout.EAST);
////        mainPanel.add(inputLabel);
////        mainPanel.add(outputLabel);
//
//        // Add main panel to frame
//        add(mainPanel);
//    }
//
//    public static void main(String[] args) {
//        MainForm frame = new MainForm();
//        frame.setVisible(true);
//    }
//}
//
//
////public class MainForm extends JFrame{
////    private JFrame mainFrame = new JFrame("Compiler Construction");
////    private JScrollPane inputCode = new JScrollPane();
////    private JScrollPane output = new JScrollPane();
////
////    public MainForm(){
////
////        inputCode.setAlignmentX(4);
////        inputCode.setAlignmentY(20);
////        inputCode.createVerticalScrollBar();
////        inputCode.createHorizontalScrollBar();
////        inputCode.setVisible(true);
////
////        mainFrame.add(inputCode);
////
////        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
////        mainFrame.setMinimumSize(new Dimension(screenSize));
////        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
////        mainFrame.pack();
////        mainFrame.setVisible(true);
////    }
////}
