package logic;

import GUI.ChartPanel;
import GUI.Main;
import model.ResourceManager;

import java.util.Collections;
import java.util.List;

//Consider this class as an extension of Collection class.
public class BubbleSort {

    public static void sort(List<Integer> list) {
        for(int i = 0; i < list.size(); i++) {
            for(int j = list.size()-1; j > 0; j--) {
                if(list.get(j) < list.get(j-1)) {
                    Collections.swap(list,j, j-1);
                    ChartPanel.getInstance().swapBarGraphs(j, j-1);
                    try {
                        System.out.println("Waiting");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        System.out.println("The loop is over");
    }

}
