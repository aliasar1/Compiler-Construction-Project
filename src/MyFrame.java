import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame(){

        JLabel sourceLabel = new JLabel();
        sourceLabel.setText("Source Code");
        sourceLabel.setForeground(Color.black);
        sourceLabel.setVisible(true);
        sourceLabel.setBounds(5, 50,20,20);

        this.add(sourceLabel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Get Users Screen Size
        // Setup our MainFrame
        this.setMinimumSize(new Dimension(screenSize));
        this.setTitle("Compiler Construction");
        this.setSize(800,600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
}
