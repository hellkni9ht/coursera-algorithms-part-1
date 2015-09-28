import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Bazna on 9/28/2015.
 */
public class Deque<Item> implements Iterable<Item> {
    private int size = 0;               // number of elements on queue
    private Node<Item> first = null;    // beginning of queue
    private Node<Item> last = null;     // end of queue

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    // construct an empty deque
    public Deque() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        // check input
        if (item == null)
            throw new NullPointerException();

        Node<Item> oldFirst = first;

        // add the new item
        first = new Node<Item>();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if (!isEmpty())
            oldFirst.prev = first;
        else
            last = first;

        // increase number of items on queue
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        // check input
        if (item == null)
            throw new NullPointerException();

        Node<Item> oldLast = last;

        // add the new item
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (!isEmpty())
            oldLast.next = last;
        else
            first = last;

        // increase number of items on queue
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        // throw an exception if the client attempts to remove an item from an empty deque
        if (size == 0)
            throw new NoSuchElementException();

        // store item value
        Item item = first.item;

        // remove first
        Node<Item> oldFirstNext = first.next;
        first.next = null;
        first = oldFirstNext;

        // decrease number of items on queue
        size--;

        if (!isEmpty())
            first.prev = null;
        else
            last = null;

        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        // throw an exception if the client attempts to remove an item from an empty deque
        if (size == 0)
            throw new NoSuchElementException();

        // store item value
        Item item = last.item;

        // remove first
        Node<Item> oldPrev = last.prev;
        last.prev = null;
        last = oldPrev;

        // decrease number of items on queue
        size--;

        if (!isEmpty())
            last.next = null;
        else
            first = null;

        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null; }
        public void remove()      { throw new UnsupportedOperationException(); }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing
    public static void main(String[] args) {
        Deque<String> q = new Deque<String>();

        boolean addFirst = false;
        boolean removeFirst = true;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals(":af:"))
                addFirst = true;
            else if (item.equals(":al:"))
                addFirst = false;
            else if (item.equals(":rf:"))
                removeFirst = true;
            else if (item.equals(":rl:"))
                removeFirst = false;
            else if (item.equals(":break:"))
                break;
            else if (!item.equals("-")) {
                if (addFirst)
                    q.addFirst(item);
                else
                    q.addLast(item);
            }
            else if (!q.isEmpty()) {
                StdOut.println((removeFirst ? q.removeFirst() : q.removeLast()) + " was removed");
            }

            for (String str : q)
                StdOut.print(str + " ");
            StdOut.println();
        }

        for (String item : q)
            StdOut.print(item + " ");
        StdOut.println();

        StdOut.println("(" + q.size() + " left on queue)");
    }
}

