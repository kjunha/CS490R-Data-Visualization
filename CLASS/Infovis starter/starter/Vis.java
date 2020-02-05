package starter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Vis extends JPanel {
	private int x = 10;
	
	
	public Vis() {
		super();
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
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//render visualization
		g.setColor(Color.BLACK);
		g.drawString("Your content here", 10, 20);
		g.drawImage(image, 10, 30, 300, 300, null);
	}
	
	public void moveLeft() {
		x -= 10;
	}
	
	public void moveRight() {
		x += 10;
	}

}
