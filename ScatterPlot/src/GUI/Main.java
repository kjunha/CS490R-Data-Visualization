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
        JMenu query = new JMenu("Query");
        JMenuItem creditAttempted_CreditPassed = new JMenuItem("Credit Attempted - Credit Passed");
        JMenuItem creditAttempted_GPA = new JMenuItem("Credit Attempted - GPA");
        JMenuItem creditPassed_GPA = new JMenuItem("Credit Passed - GPA");
        JMenuItem age_GPA = new JMenuItem("AGE - GPA");
        JMenuItem age_currentCredit = new JMenuItem("Age - Current Credit");
//        JMenu chartType = new JMenu("Chart Type");
//        JCheckBoxMenuItem barChart = new JCheckBoxMenuItem("Bar Chart", true);
//        JCheckBoxMenuItem lineChart = new JCheckBoxMenuItem("Line Chart", false);

        JMenu view = new JMenu("View");
//        JMenu ymetric = new JMenu("Y-Metric");
//        JMenuItem bytotal = new JMenuItem("Total Based");
//        JMenuItem bygap = new JMenuItem("Gap Revealed");
        JMenu xy_scale = new JMenu("XY-Scale");
        JMenuItem y1 = new JMenuItem("1");
        JMenuItem y5 = new JMenuItem("5");
        JMenuItem y8 = new JMenuItem("8");
        JMenuItem y10 = new JMenuItem("10");
        JMenuItem y16 = new JMenuItem("16");
        JMenuItem y30 = new JMenuItem("30");
//        JMenu color = new JMenu("Color");
//        JMenuItem red = new JMenuItem("RED");
//        JMenuItem blue = new JMenuItem("BLUE");
//        JMenuItem green = new JMenuItem("GREEN");
        JCheckBoxMenuItem exactValue = new JCheckBoxMenuItem("Show Exact Values", false);
        JCheckBoxMenuItem revealGender = new JCheckBoxMenuItem("Show Gender Information", false);
        JMenuItem reset = new JMenuItem("Reset Zoom");

        JMenu order = new JMenu("Order");
//        JMenuItem ascending = new JMenuItem("Ascending");
//        JMenuItem decending = new JMenuItem("Decending");
        JMenuItem none = new JMenuItem("Default");

        //menu action listener
        creditAttempted_CreditPassed.addActionListener(e -> {contents.getCreditsAttempted_CreditsPassed();});
        creditAttempted_GPA.addActionListener(e -> {contents.getCreditsAttempted_GPA();});
        creditPassed_GPA.addActionListener(e -> {contents.getCreditsPassed_GPA();});
        age_GPA.addActionListener(e -> {contents.getAge_GPA();});
        age_currentCredit.addActionListener(e -> {contents.getAge_CurrentCredits();});
//        barChart.addItemListener(e -> {contents.showBarChart(barChart.getState());});
//        lineChart.addItemListener(e -> {contents.showLineChart(lineChart.getState());});
//        bytotal.addActionListener(e -> {contents.setTotalBased(true);});
//        bygap.addActionListener(e -> {contents.setTotalBased(false);});
        y1.addActionListener(e -> {contents.setScaleCount(1);});
        y5.addActionListener(e -> {contents.setScaleCount(5);});
        y8.addActionListener(e -> {contents.setScaleCount(8);});
        y10.addActionListener(e -> {contents.setScaleCount(10);});
        y16.addActionListener(e -> {contents.setScaleCount(16);});
        y30.addActionListener(e -> {contents.setScaleCount(30);});
        exactValue.addItemListener(e -> {contents.showExactValue(exactValue.getState());});
        revealGender.addItemListener(e -> {contents.showGenderInfo(revealGender.getState());});
        reset.addActionListener(e -> {contents.resetToOriginal();});
//        ascending.addActionListener(e -> {contents.setOrderBy(OrderMode.ASCD);});
//        decending.addActionListener(e -> {contents.setOrderBy(OrderMode.DESC);});


        //menubar buildup
        menuBar.add(query).add(creditAttempted_CreditPassed);
        query.add(creditAttempted_GPA);
        query.add(creditPassed_GPA);
        query.add(age_GPA);
        query.add(age_currentCredit);

//        menuBar.add(chartType).add(barChart);
//        chartType.add(lineChart);

//        menuBar.add(view).add(ymetric).add(bytotal);
        menuBar.add(view).add(xy_scale).add(y1);
        view.add(exactValue);
        view.add(revealGender);
        view.add(reset);
        xy_scale.add(y5);xy_scale.add(y8);xy_scale.add(y10);xy_scale.add(y16);xy_scale.add(y30);
//        ymetric.add(bygap);
//        menuBar.add(order).add(ascending);
//        order.add(decending);
        order.add(none);
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
