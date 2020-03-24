package GUI;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private ChartPanel contents;

    public Main() {
        JMenuBar mb = setupMenu();
        setJMenuBar(mb);
        setLayout(new BorderLayout());
        contents = ChartPanel.getInstance();
        add(AnimationSpeedToolbar.getInstance(), BorderLayout.NORTH);
        add(ChartPanel.getInstance(), BorderLayout.CENTER);
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simple GUI by Dexter");
        setVisible(true);
    }

    public JMenuBar setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu animation = new JMenu("Animation");
        JMenuItem bubbleSort = new JMenuItem("Bubble Sort");
        //Add action Listener
        bubbleSort.addActionListener(e -> {contents.doBubbleSort();});

        //Build Menubar
        menuBar.add(file);
        menuBar.add(animation).add(bubbleSort);
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
