import javax.swing.*;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private JButton lexicalAnalysisButton;
    private JButton exitButton;
    private JButton syntaxAnalysisButton;
    private JTextPane inputTextPane;
    private JTextPane outputTextPane;
    private JLabel inputLabel;
    private JLabel outputLabel;
    private JScrollPane inputPane;
    private JScrollPane outputPane;

    public MainFrame() {
        setContentPane(mainPanel);
        setTitle("Compiler");
        setSize(1366,768);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        inputPane.setBounds(400,200,400,200);
        outputPane.setBounds(400,200,400,200);
        mainPanel.setBounds(500,250,500,250);
//        setResizable(false);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
