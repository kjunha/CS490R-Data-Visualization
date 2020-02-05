package GUI;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private ChartPanel contents;
    private YScaleToolbar yScaleToolbar;

    public Main() {
        JMenuBar mb = setupMenu();
        setJMenuBar(mb);
        contents = ChartPanel.getInstance();
        yScaleToolbar = YScaleToolbar.getInstance();
        setLayout(new BorderLayout());
        add(yScaleToolbar, BorderLayout.NORTH);
        add(contents, BorderLayout.CENTER);

        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simple GUI by Dexter");
        setVisible(true);
    }

    public JMenuBar setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu query = new JMenu("Query");
        JMenuItem countByCountry = new JMenuItem("Count By Home Country");
        JMenuItem countByMajor = new JMenuItem("Count By Major");
        JMenuItem countByYear = new JMenuItem("Count By Graduation Year");
        JMenuItem countByGPA = new JMenuItem("Count By GPA");
        JMenuItem averageGPAByMajor = new JMenuItem("Average GPA by Major");
        JMenuItem averageCreditByGraduation = new JMenuItem("Average Credit by Year");
        JMenu chartType = new JMenu("Chart Type");
        JCheckBoxMenuItem barChart = new JCheckBoxMenuItem("Bar Chart", true);
        JCheckBoxMenuItem lineChart = new JCheckBoxMenuItem("Line Chart", false);

        JMenu view = new JMenu("View");
        JMenu ymetric = new JMenu("Y-Metric");
        JMenuItem bytotal = new JMenuItem("Total Based");
        JMenuItem bygap = new JMenuItem("Gap Revealed");
        JMenu yscale = new JMenu("Y-Scale");
        JMenuItem y1 = new JMenuItem("1");
        JMenuItem y5 = new JMenuItem("5");
        JMenuItem y8 = new JMenuItem("8");
        JMenuItem y10 = new JMenuItem("10");
        JMenuItem y16 = new JMenuItem("16");
        JMenuItem y30 = new JMenuItem("30");
        JMenu color = new JMenu("Color");
        JMenuItem red = new JMenuItem("RED");
        JMenuItem blue = new JMenuItem("BLUE");
        JMenuItem green = new JMenuItem("GREEN");
        JCheckBoxMenuItem exactValue = new JCheckBoxMenuItem("Show Exact Values", false);

        JMenu order = new JMenu("Order");
        JMenuItem ascending = new JMenuItem("Ascending");
        JMenuItem decending = new JMenuItem("Decending");
        JMenuItem none = new JMenuItem("Default");

        //menu action listener
        countByCountry.addActionListener(e -> {contents.getStudentByHomeCountry();});
        countByMajor.addActionListener(e -> {contents.getStudentByMajor();});
        countByYear.addActionListener(e -> {contents.getStudentByGraduation();});
        countByGPA.addActionListener(e -> {contents.getStudentsByGPA();});
        averageGPAByMajor.addActionListener(e -> {contents.getAverageGPAByMajor();});
        averageCreditByGraduation.addActionListener(e -> {contents.getAverageCreditByGraduation();});
        barChart.addItemListener(e -> {contents.showBarChart(barChart.getState());});
        lineChart.addItemListener(e -> {contents.showLineChart(lineChart.getState());});
        bytotal.addActionListener(e -> {contents.setTotalBased(true);});
        bygap.addActionListener(e -> {contents.setTotalBased(false);});
        y1.addActionListener(e -> {contents.setyPoints(1);});
        y5.addActionListener(e -> {contents.setyPoints(5);});
        y8.addActionListener(e -> {contents.setyPoints(8);});
        y10.addActionListener(e -> {contents.setyPoints(10);});
        y16.addActionListener(e -> {contents.setyPoints(16);});
        y30.addActionListener(e -> {contents.setyPoints(30);});
        exactValue.addItemListener(e -> {contents.showExactValue(exactValue.getState());});
        ascending.addActionListener(e -> {contents.setOrderBy(OrderMode.ASCD);});
        decending.addActionListener(e -> {contents.setOrderBy(OrderMode.DESC);});
        none.addActionListener(e -> {contents.setOrderBy(OrderMode.NORM);});


        //menubar buildup
        menuBar.add(query).add(countByCountry);
        query.add(countByMajor);
        query.add(countByYear);
        query.add(countByGPA);
        query.add(averageGPAByMajor);
        query.add(averageCreditByGraduation);

        menuBar.add(chartType).add(barChart);
        chartType.add(lineChart);

        menuBar.add(view).add(ymetric).add(bytotal);
        view.add(yscale).add(y1);
        view.add(exactValue);
        ymetric.add(bygap);
        yscale.add(y5);yscale.add(y8);yscale.add(y10);yscale.add(y16);yscale.add(y30);
        menuBar.add(order).add(ascending);
        order.add(decending);
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
