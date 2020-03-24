package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceManager {
    private static ResourceManager resourceManager;
    List<Integer> dataset;
    private ResourceManager() {
        dataset = new ArrayList<>();
        readFromFile("res/output_10.txt");
    }

    public static ResourceManager getInstance() {
        if (resourceManager == null) {
            resourceManager = new ResourceManager();
        }
        return resourceManager;
    }

    public void readFromFile(String path) {
        dataset.clear();
        File file = new File(path);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                dataset.add(sc.nextInt());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getDataset() { return dataset; }
    public int getDatasetSize() { return dataset.size();}

    //For Debugging
    public void printDataset() {
        dataset.forEach(dat -> System.out.print(dat + " "));
        System.out.println(" ");
    }
}
