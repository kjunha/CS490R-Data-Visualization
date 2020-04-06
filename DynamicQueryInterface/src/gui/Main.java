package gui;

import helper.CalenderToolBar;
import helper.DetailViewMode;
import helper.TableStructure;
import model.WebAPIManager;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Main extends JFrame {
    public Main() {
        JMenuBar mb = setupMenu();
        setJMenuBar(mb);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(layout);
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        add(CalenderToolBar.getInstance(), c);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0.4;
        add(DetailChart.getInstance(), c);
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.6;
        add(OverallChart.getInstance(), c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 2;
        c.weightx = 0.7;
        add(DetailTable.getInstance(), c);
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simple GUI by Dexter");
        setVisible(true);
    }

    public JMenuBar setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu view = new JMenu("View");
        JMenu detailView = new JMenu("Detail View");
        JMenuItem singleView = new JMenuItem("Toggle Single Detail View");
        JMenuItem dualView = new JMenuItem("Toggle Dual Detail View");

        singleView.addActionListener(e -> {DetailChart.getInstance().setMode(DetailViewMode.SINGLE);});
        dualView.addActionListener(e -> {DetailChart.getInstance().setMode(DetailViewMode.DUAL);});

        view.add(detailView).add(singleView);
        detailView.add(dualView);
        menuBar.add(view);
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
