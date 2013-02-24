package pk.dyplom.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame {

    public MainFrame(final String title, final Container panel, final WindowListener listener) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setTitle(title);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setContentPane(panel);
                setResizable(true);
                pack();
                setVisible(true);
                addWindowListener(listener);
            }
        });
    }
}
