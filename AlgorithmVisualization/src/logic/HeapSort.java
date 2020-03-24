package logic;

import java.util.Collections;
import java.util.List;

public class HeapSort {
    private static List<Integer> mylist;
    private static int heapSize;

    public static List<Integer> sort(List<Integer> list) {
        mylist = list;
        buildMaxHeap();
        for(int i = mylist.size()-1; i > 0; i--) {
            Collections.swap(mylist, 0, i);
            heapSize--;
            maxHipify(0);
        }
        return mylist;
    }

    public static void maxHipify(int i) {
        int left = i * 2;
        int right = left + 1;
        int largest = (left < heapSize && mylist.get(left) > mylist.get(i))?left:i;
        if(right < heapSize && mylist.get(right) > mylist.get(largest)) {
            largest = right;
        }
        if(largest != i) {
            Collections.swap(mylist, i, largest);
            maxHipify(largest);
        }
    }

    public static void buildMaxHeap() {
        heapSize = mylist.size();
        for(int i = (int)(heapSize/2); i >= 0; i--) {
            maxHipify(i);
        }
    }
}
