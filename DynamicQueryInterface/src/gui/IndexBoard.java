package gui;

import javax.swing.*;
import java.awt.*;

public class IndexBoard extends JPanel {
    private static IndexBoard indexBoard;
    private JTable indexTable;
    private IndexBoard() {
        String[] column = {"ID", "Name"};
        Object[][] data = {{"1", "one"}, {"2", "two"}};
        indexTable = new JTable(data, column);
        this.add(new JScrollPane(indexTable));
    }
    public static IndexBoard getInstance() {
        if(indexBoard == null) {
            indexBoard = new IndexBoard();
        }
        return indexBoard;
    }

    @Override
    public void paintComponent(Graphics g) {
        double w = getWidth();
        double h = getHeight();
        g.setColor(new Color(255,245,255));
        g.fillRect(0, 0, (int)w, (int)h);
        g.drawString("Index Board Panel", 10, 10);
    }
}
