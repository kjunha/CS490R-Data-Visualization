package logic;

import java.util.Collections;
import java.util.List;

public class QuickSort {

    public static List<Integer> sort(List<Integer> list, int p, int r) {
        if (p < r) {
            int q = partition(list, p, r);
            sort(list, p, q - 1);
            sort(list, q + 1, r);
        }
        return list;
    }

    public static int partition (List<Integer> list, int p, int r) {
        int x = list.get(r);
        int i = p-1;
        for(int j = p; j < r; j++) {
            if(list.get(j) <= x) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1,r);
        return i + 1;
    }
}
