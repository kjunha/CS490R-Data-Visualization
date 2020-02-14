package GUI;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private ChartPanel contents;

    public Main() {
        JMenuBar mb = setupMenu();
        setJMenuBar(mb);
        contents = ChartPanel.getInstance();
        setLayout(new BorderLayout());
        add(contents, BorderLayout.CENTER);

        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("File Tree Map by Dexter");
        setVisible(true);
    }

    public JMenuBar setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        return menuBar;
    }

    public static void main(String[] args) {
        //this makes the GUI adopt the look-n-feel of the windowing system (Windows/X11/Mac)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
