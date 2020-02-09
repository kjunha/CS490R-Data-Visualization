package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Vis extends JPanel {
    private int xpos;
    private Color background;
    private boolean showbox;
    private Point boxPoint;
    private Timer timer;
    private double dir;


    public Vis() {
        super();
        xpos = 10;
        background = Color.WHITE;
        showbox = false;
        boxPoint = new Point(500,500);
        timer = new Timer(50, e -> {
            if(dir < 0.5) {
                boxPoint.setLocation(boxPoint.getX() + 5, boxPoint.getY() + 5);
            } else {
                boxPoint.setLocation(boxPoint.getX() - 5, boxPoint.getY() - 5);
            }
            repaint();
        });
    }

    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/helloworld.png"));
        } catch (IOException e) {
            System.out.println("Image is not found in src");
        }

        //draw blank background
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());

        //render visualization
        g.setColor(Color.BLACK);
        g.drawString("Your content here", 10, 20);
        g.drawImage(image, xpos, 30, 300, 300, null);

        if(showbox) {
            g.setColor(Color.BLUE);
            g.fillRect(boxPoint.x, boxPoint.y, 50,50);
        }
    }
    //functions for the action listeners
    public void moveLeft() {
        xpos -= 10;
        repaint();
    }

    public void moveRight() {
        xpos += 10;
        repaint();
    }

    public void setColor(Color c) {
        this.background = c;
        repaint();
    }

    public void setShowbox(boolean b) {
        this.showbox = b;
        if (showbox) {
            boxPoint.setLocation(700.0*Math.random(), 500*Math.random());
            dir = Math.random();
            timer.start();
        } else {
            timer.stop();
        }
        repaint();
    }
}
