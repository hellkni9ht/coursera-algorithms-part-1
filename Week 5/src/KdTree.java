import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

/**
 * Created by Bazna on 11/7/2015.
 */
public class KdTree {
    private static class Node {
        private Point2D point;  // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        private int level;      // the level of tree
        private int size;       // the number of nodes in the subtree

        public Node(final int level, final Point2D point, final RectHV rect) {
            this.level = level;
            this.point = point;
            this.rect = rect;
            this.size = 1;
        }

        public int compare(final Point2D pointToCompare) {
            if (level % 2 == 0) {
                return Double.compare(pointToCompare.x(), point.x());
            } else {
                return Double.compare(pointToCompare.y(), point.y());
            }
        }
    }

    private Node root;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }
    // number of points in the set
    public int size() {
        return size(root);
    }

    public int size(Node node) {
        return node == null ? 0 : node.size;
    }

    private boolean isXLevel(int level) {
        return level % 2 == 0;
    }

    private void ensureInput(Object object) {
        if (object == null)
            throw new java.lang.NullPointerException();
    }

    private Node put(final Node node, final Point2D pointToInsert, final int level, final RectHV rect) {
        if (node == null) {
            return new Node(level, pointToInsert, rect);
        }

        int compareResult = node.compare(pointToInsert);

        RectHV lbRect = null;
        RectHV rtRect = null;
        if (node.lb == null && compareResult <= 0) {
            if (isXLevel(level)) {
                lbRect = new RectHV(node.rect.xmin(), node.point.x(), node.rect.ymin(), node.rect.ymax());
            } else {
                lbRect = new RectHV(node.rect.xmin(), node.rect.xmax(), node.rect.ymin(), node.point.y());
            }
        }
        else if (compareResult >= 0) {
            if (isXLevel(level)) {
                lbRect = new RectHV(node.point.x(), node.rect.xmax(), node.rect.ymin(), node.rect.ymax());
            } else {
                lbRect = new RectHV(node.rect.xmin(), node.rect.xmax(), node.point.y(), node.rect.ymax());
            }
        }

        if (compareResult < 0) {
            node.lb = put(node.lb, pointToInsert, level + 1, lbRect);
        } else if (compareResult > 0) {
            node.rt = put(node.rt, pointToInsert, level + 1, rtRect);
        }

        // update the number nodes in subtree
        node.size = 1 + size(node.lb) + size(node.rt);

        return node;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        ensureInput(p);
        root = put(root, p, 0, new RectHV(0, 0, 1, 1));
    }

    private Point2D get(final Node node, final Point2D searchedPoint, final int level) {
        if (node == null)
            return null;

        int compareResult = node.compare(searchedPoint);
        if (compareResult < 0)
            return get(node.lb, searchedPoint, level + 1);
        else if (compareResult > 0)
            return get(node.rt, searchedPoint, level + 1);
        else
            return node.point;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        ensureInput(p);
        return get(root, p, 0) != null;
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        ensureInput(rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        ensureInput(p);

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
