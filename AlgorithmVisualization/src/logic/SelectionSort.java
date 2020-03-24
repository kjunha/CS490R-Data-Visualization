package logic;

import java.util.Collections;
import java.util.List;

public class SelectionSort {

    public static List<Integer> sort(List<Integer> list) {
        for(int i = 0; i < list.size()-1; i++) {
            int min = i;
            for(int j = i+1; j < list.size(); j++) {
                if(list.get(j) < list.get(min)) {
                    min = j;
                }
            }
            if(min != i) {
                Collections.swap(list, min, i);
            }
        }
        return list;
    }
}
