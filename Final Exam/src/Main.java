import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String[] strings = {"herb", "pear", "bone", "pink", "plum", "leaf", "dusk", "puce", "bole",
                "mist", "ruby", "mint", "palm", "sand", "flax", "cyan", "kobi", "lust", "navy", "rose", "iris", "buff", "silk",
                "gold"};

        // selection
//        Selection.sort(strings, (o1, o2) -> { return ((String)o1).compareTo((String)o2); });

        // insertion
      //  Insertion.sort(strings);

        // heap
      //  Quick.sort(strings);

//        Merge.sort(strings);

        Heap.sort(strings);

        // quick

    }
}
