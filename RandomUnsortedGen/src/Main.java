import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Argument passed: " + args.length);
        if(args.length < 3) {
            System.out.println("The program needs 3 parameter: (total number to select) (min number to start) (max number to end)");
            System.exit(0);
        } else {
            int selection = Integer.parseInt(args[0]);
            int min = Integer.parseInt(args[1]);
            int max = Integer.parseInt(args[2]);
            if(selection > (max-min)) {
                System.out.println("The number of unique number between max and min number should be greater then the number that you want to select.");
                System.exit(0);
            }
            List<Integer> uniqueSelection = new ArrayList<>();
            while(uniqueSelection.size() < selection) {
                int rndSelection = (int)((Math.random() * (max-min)) + min);
                if(uniqueSelection.indexOf(rndSelection) == -1) {
                    uniqueSelection.add(rndSelection);
                }
            }
            try {
                FileWriter fw =  new FileWriter("pickup/output.txt");
                for(int i = 0; i < uniqueSelection.size(); i++) {
                    String line = Integer.toString(uniqueSelection.get(i));
                    if(i < uniqueSelection.size()-1) {
                        line += "\n";
                    }
                    fw.write(line);
                }
                fw.close();
                System.out.println("File Successfully Created. Check pickup/output.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Program Finished.");
        }
    }
}
