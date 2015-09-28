import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.function.Function;

/**
 * Created by Bazna on 9/28/2015.
 */
public class Subset {
    public static void main(String[] args) {
        int k = -1;

        // extract command-line argument
        if (args.length >= 1) {

            Function<String, Integer> parseArgument = (String argument) -> {
                try {
                    return Integer.parseInt(argument);
                } catch (NumberFormatException e) {
                    System.err.println("Argument" + argument + " must be an integer.");
                    System.exit(1);
                    // java interpreter is happy
                    return -1;
                }
            };

            k = parseArgument.apply(args[0]);
        }

        if (k < 0) {
            System.err.println("K is invalid!");
            return;
        }

        RandomizedQueue<String> queue = new RandomizedQueue<String>();

        // read strings from input
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
//            if (item.equals("-1"))
//                break;
            queue.enqueue(item);
        }

        if (k > queue.size()) {
            System.err.println("K is grater than number of eneterd strings!");
            return;
        }

        // print out strings in randomized order
        Iterator<String> item = queue.iterator();
        for (int i = 0; i < k; i++) {
            StdOut.println(item.next());
        }
    }
}
