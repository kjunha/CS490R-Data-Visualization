package GUI;

import javax.swing.*;
import java.text.DecimalFormat;

public class AnimationSpeedToolbar extends JToolBar {
    private static AnimationSpeedToolbar animationSpeedToolbar;
    private double currentHigh;
    private double currentLow;
    private JPanel panel;
    private ChartPanel chartPanel;
    private DecimalFormat df;
    private JSpinner minSetter;
    private JSpinner maxSetter;
    private AnimationSpeedToolbar() {
        currentLow = 0.0;
        currentHigh = 0.0;
        chartPanel = ChartPanel.getInstance();
        panel = new JPanel();
        panel.add(new JLabel("min: "));
        panel.add(minSetter = new JSpinner());
        panel.add(new JLabel("Max: "));
        panel.add(maxSetter = new JSpinner());
        add(panel);
    }

    public static AnimationSpeedToolbar getInstance() {
        if(animationSpeedToolbar == null) {
            animationSpeedToolbar = new AnimationSpeedToolbar();
        }
        return animationSpeedToolbar;
    }
}
