package GUI;

import javax.swing.*;

public class Main extends JFrame {
    private Contents contents;

    public Main(){
        JMenuBar mb = setupMenu();
        setJMenuBar(mb);

        contents = new Contents();
        setContentPane(contents);

        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simple GUI by Dexter");
        setVisible(true);

    }
    //Set up a menu bar and actions.
    public JMenuBar setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        //File Menu Tree
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        //Query Menu Tree
        JMenu query = new JMenu("Query");
        JMenu gender = new JMenu("Gender");
        JMenuItem noGender = new JMenuItem("None");
        JMenuItem male = new JMenuItem("gender = Male");
        JMenuItem female = new JMenuItem("gender = Female");
        JMenu age = new JMenu("Age");
        JMenuItem noAge = new JMenuItem("None");
        JMenuItem less25 = new JMenuItem("age < 25");
        JMenuItem btw39 = new JMenuItem("25 <= age <= 39 ");
        JMenuItem great40 = new JMenuItem("40 <= age");
        JMenu time = new JMenu("Time");
        JMenuItem noTime = new JMenuItem("None");
        JMenuItem less4 = new JMenuItem("time < 4");
        JMenuItem btw5 = new JMenuItem("4 <= time <= 5");
        JMenuItem great5 = new JMenuItem("time > 5");
        JMenuItem clear = new JMenuItem("Clear");
        //Action Listeners
        noGender.addActionListener(e->{contents.queryGender(null);});
        male.addActionListener(e->{contents.queryGender("M");});
        female.addActionListener(e->{contents.queryGender("F");});
        noAge.addActionListener(e->{contents.queryAge(0,0);});
        less25.addActionListener(e->{contents.queryAge(0,24);});
        btw39.addActionListener(e->{contents.queryAge(25,39);});
        great40.addActionListener(e->{contents.queryAge(40,81);});
        noTime.addActionListener(e->{contents.queryTime(0,0);});
        less4.addActionListener(e->{contents.queryTime(0,4);});
        btw5.addActionListener(e->{contents.queryTime(4,5);});
        great5.addActionListener(e->contents.queryTime(5,9));
        clear.addActionListener(e->{contents.clearRoster();});


        //Assemble Menu
        file.add(open);
        gender.add(noGender);
        gender.add(male);
        gender.add(female);
        age.add(noAge);
        age.add(less25);
        age.add(btw39);
        age.add(great40);
        time.add(noTime);
        time.add(less4);
        time.add(btw5);
        time.add(great5);
        query.add(gender);
        query.add(age);
        query.add(time);
        query.add(clear);
        menuBar.add(file);
        menuBar.add(query);
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
