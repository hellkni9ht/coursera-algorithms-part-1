import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Bazna on 9/28/2015.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;        // queue elements
    private int size = 0;        // number of elements on queue
    static private int seed = 0; // extra seed

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[])new Object[2];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the queue
    public int size() {
        return size;
    }

    private void resize(int max) {
        assert max >= size;
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < size; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    // add the item
    public void enqueue(Item item) {
        // check input
        if (item == null)
            throw new NullPointerException();

        // double size of array if necessary and recopy to front of array
        if (size == queue.length)
            resize(2 * queue.length);

        // add item
        queue[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        // throw an exception if the client attempts to remove an item from an empty deque
        if (size == 0)
            throw new NoSuchElementException();

        int randomItemIndex = StdRandom.uniform(0, size);
        Item item = queue[randomItemIndex];
        queue[randomItemIndex] = queue[size - 1];
        queue[size - 1] = null;

        size--;

        // shrink size of array if necessary
        if (size > 0 && size == queue.length / 4)
            resize(queue.length / 2);

        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        // throw an exception if the client attempts to remove an item from an empty deque
        if (size == 0)
            throw new NoSuchElementException();

        int randomItemIndex = StdRandom.uniform(size);
        Item item = queue[randomItemIndex];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomArrayIterator<Item>();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomArrayIterator<Item> implements Iterator<Item> {
        // random order
        int[] order;

        // the current order index
        int current = 0;

        public RandomArrayIterator() {
            order = new int[size];
            for (int i = 0; i < order.length; i++) {
                order[i] = i;
            }

            // each iterator maintains its own random order
            StdRandom.setSeed(System.currentTimeMillis() + seed++);
            StdRandom.shuffle(order);
        }

        public boolean hasNext()  {
            return current < order.length;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return (Item)queue[order[current++]];
        }
    }

    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        for (int i = 0; i < 3; i++)
            queue.enqueue(i);

        for (int item : queue)
            StdOut.print(item + " ");
        StdOut.println();

        queue.dequeue();
        queue.dequeue();

        StdOut.println("After two dequeue calls");
        for (int item : queue)
            StdOut.print(item + " ");
        StdOut.println();

        StdOut.println("queue.sample() = " + queue.sample());
        StdOut.println("queue.sample() = " + queue.sample());

        StdOut.println("The order items in lines should be different!");
        for (int item : queue)
            StdOut.print(item + " ");
        StdOut.println();

        for (int item : queue)
            StdOut.print(item + " ");
        StdOut.println();

        queue.dequeue();

        for (int i = 0; i < 1024; i++) {
            queue.enqueue(i);
            if (i != queue.dequeue())
                StdOut.println("Failed!");
        }

        StdOut.println("Probably everything is good:)");
    }
}
