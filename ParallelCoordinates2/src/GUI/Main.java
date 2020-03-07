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
        setTitle("Simple GUI by Dexter");
        setVisible(true);
    }

    public JMenuBar setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu tables = new JMenu("Table Selection");
        JMenuItem students_2012 = new JMenuItem("Student Roster - 2012");
        JMenuItem students_2019 = new JMenuItem("Student Roster - 2019");
        JMenuItem marathon = new JMenuItem("Marathon");
        JMenu tools = new JMenu("Tools");
        JMenuItem resetScope = new JMenuItem("Reset Scope");

        //menu action listener
        students_2012.addActionListener(e -> {contents.updateTable("roster");});
        students_2019.addActionListener(e -> {contents.updateTable("roster2019");});
        marathon.addActionListener(e -> {contents.updateTable("marathon");});
        resetScope.addActionListener(e -> {contents.resetScope();});


        //menu bar buildup
        menuBar.add(tables).add(students_2012);
        tables.add(students_2019);
        tables.add(marathon);
        menuBar.add(tools).add(resetScope);
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
