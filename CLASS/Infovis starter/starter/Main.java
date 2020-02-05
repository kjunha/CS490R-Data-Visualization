package starter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;


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
		JMenu fileMenu = new JMenu("picture");
		JMenuItem left = new JMenuItem("Move Left");
		JMenuItem right = new JMenuItem("Move Right");
		JMenu subMenu = new JMenu("Submenu");
		JMenuItem item2 = new JMenuItem("Item 2");
		
		//setup action listeners
		left.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Just clicked menu item 1");			
			}			
		});
		right.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		item2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Just clicked menu item 2");			
			}			
		});
		
		//now hook them all together
		menuBar.add(left);
		menuBar.add(right);
		
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
