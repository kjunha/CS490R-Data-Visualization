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
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");

        JMenu view =  new JMenu("View");
        JMenu colorScheme = new JMenu("Color Scheme");
        JMenuItem byExtension = new JMenuItem("Extension");
        JMenuItem byFileAge = new JMenuItem("File Age");
        JMenuItem random = new JMenuItem("Random");
        JMenuItem none = new JMenuItem("None");

        open.addActionListener(e -> {contents.chooseAFile();});
        byExtension.addActionListener(e -> {contents.setColorSchemeType(ColorSchemeType.BY_EXTENSION);});
        byFileAge.addActionListener(e -> {contents.setColorSchemeType(ColorSchemeType.BY_FILE_AGE);});
        random.addActionListener(e -> {contents.setColorSchemeType(ColorSchemeType.RANDOM);});
        none.addActionListener(e -> {contents.setColorSchemeType(ColorSchemeType.NONE);});

        menuBar.add(file).add(open);
        menuBar.add(view).add(colorScheme);
        colorScheme.add(byExtension); colorScheme.add(byFileAge); colorScheme.add(random); colorScheme.add(none);
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
