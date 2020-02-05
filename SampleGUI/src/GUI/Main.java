package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Main extends JFrame {
    private Vis mainPanel;

    public Main() {

        JMenuBar mb = setupMenu();
        setJMenuBar(mb);

        mainPanel = new Vis();
        setContentPane(mainPanel);

        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simple GUI by Dexter");
        setVisible(true);
    }

    private JMenuBar setupMenu() {
        //instantiate menubar, menus, and menu options
        JMenuBar menuBar = new JMenuBar();
        menuBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        JMenu oriMenu = new JMenu("Orientation");
        JMenuItem left = new JMenuItem("Move Left");
        JMenuItem right = new JMenuItem("Move Right");
        JMenu background = new JMenu("BackGround");
        JMenu color = new JMenu("Color Set");
        JMenuItem red = new JMenuItem("Red");
        JMenuItem green = new JMenuItem("Green");
        JMenuItem white = new JMenuItem("White");
        JMenu showBox = new JMenu("Show Box");
        JCheckBoxMenuItem visiable = new JCheckBoxMenuItem("Visiable", false);

        //setup action listeners
        left.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Item move left");
                mainPanel.moveLeft();
            }
        });
        right.addActionListener(e -> {
            System.out.println("Item move right");
            mainPanel.moveRight();

        });
        red.addActionListener(e -> {
            System.out.println("Background Set Red");
            mainPanel.setColor(Color.RED);
        });
        green.addActionListener(e -> {
            System.out.println("Background Set Green");
            mainPanel.setColor(Color.GREEN);
        });
        white.addActionListener(e -> {
            System.out.println("Background Set White");
            mainPanel.setColor(Color.WHITE);
        });
        visiable.addItemListener(e -> mainPanel.setShowbox(visiable.getState()));

        //now hook them all together
        oriMenu.add(left);
        oriMenu.add(right);
        color.add(red);
        color.add(green);
        color.add(white);
        background.add(color);
        showBox.add(visiable);
        menuBar.add(oriMenu);
        menuBar.add(background);
        menuBar.add(showBox);

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
