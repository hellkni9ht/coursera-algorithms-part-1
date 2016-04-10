import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bazna on 11/7/2015.
 */
public class KdTree {
    private Node rootNode;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }
    // number of points in the set
    public int size() {
        return size(rootNode);
    }

    private int size(Node node) {
        return node == null ? 0 : node.size;
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
        if (node.lb == null && compareResult < 0) {
            if (node.isXLevel()) {
                lbRect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.point.x(), node.rect.ymax());
            } else {
                lbRect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.point.y());
            }
        }
        else if (node.rt == null && compareResult >= 0) {
            if (node.isXLevel()) {
                rtRect = new RectHV(node.point.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
            } else {
                rtRect = new RectHV(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.rect.ymax());
            }
        }

        if (compareResult < 0) {
            node.lb = put(node.lb, pointToInsert, level + 1, lbRect);
        } else if (compareResult > 0) {
            node.rt = put(node.rt, pointToInsert, level + 1, rtRect);
        } else if (!pointToInsert.equals(node.point)) {
            node.rt = put(node.rt, pointToInsert, level + 1, rtRect);
        }

        // update the number nodes in subtree
        node.size = 1 + size(node.lb) + size(node.rt);

        return node;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        ensureInput(p);
        rootNode = put(rootNode, p, 0, new RectHV(0, 0, 1, 1));
    }

    private Point2D get(final Node node, final Point2D searchedPoint, final int level) {
        if (node == null)
            return null;

        int compareResult = node.compare(searchedPoint);
        if (compareResult < 0)
            return get(node.lb, searchedPoint, level + 1);
        else if (compareResult > 0)
            return get(node.rt, searchedPoint, level + 1);
        else if (!searchedPoint.equals(node.point))
            return get(node.rt, searchedPoint, level + 1);
        else
            return node.point;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        ensureInput(p);
        return get(rootNode, p, 0) != null;
    }

    // draw all points to standard draw
    public void draw() {
        draw(rootNode);
    }

    private void draw(Node node) {
        if (node == null)
            return;

        // draw the node
        StdDraw.point(node.point.x(), node.point.y());

        // draw the left/bottom subtree
        draw(node.lb);

        // draw the right/top subtree
        draw(node.rt);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        ensureInput(rect);

        List<Point2D> foundPoints = new ArrayList<>();
        range(rect, rootNode, foundPoints);
        return foundPoints;
    }

    private void range(RectHV queryRect, Node node, List<Point2D> foundPoints) {
        if (node == null)
            return;

        boolean hasIntersection = node.intersectsWithSplitingLine(queryRect);
        if (hasIntersection) {
            if (queryRect.contains(node.point))
                foundPoints.add(node.point);

            range(queryRect, node.lb, foundPoints);
            range(queryRect, node.rt, foundPoints);
        } else if (node.isRightTop(queryRect)) {
            range(queryRect, node.rt, foundPoints);
        } else {
            range(queryRect, node.lb, foundPoints);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        ensureInput(p);

        if (rootNode == null)
            return null;

        return nearest(p, rootNode, rootNode.point);
    }

    private Point2D nearest(Point2D queryPoint, Node node, Point2D currentClosestPoint) {
        if (node == null)
            return null;

        Point2D closestPoint = currentClosestPoint;

        if (node.point.distanceSquaredTo(queryPoint) < currentClosestPoint.distanceSquaredTo(queryPoint)) {
            closestPoint = node.point;
        }

        // choose the subtree that is on the same side of the splitting line as the query point as the first subtree to explore
        Point2D candidate = null;
        int compare = node.compare(queryPoint);
        if (compare < 0) {
            candidate = nearest(queryPoint, node.lb, closestPoint);
        }
        else {
            candidate = nearest(queryPoint, node.rt, closestPoint);
        }

        if (candidate != null && candidate.distanceSquaredTo(queryPoint) < closestPoint.distanceSquaredTo(queryPoint))
            closestPoint = candidate;

        // Check if it possible to prune the second subtree.
        // Pruning rule: if the closest point discovered so far is closer than the distance between
        // the query point and the rectangle corresponding to a node,
        // there is no need to explore that node (or its subtrees).
        if (compare < 0) {
            if (node.rt != null) {
                if (node.rt.rect.distanceSquaredTo(queryPoint) < closestPoint.distanceSquaredTo(queryPoint)) {
                    candidate = nearest(queryPoint, node.rt, closestPoint);
                }
            }
        }
        else {
            if (node.lb != null) {
                if (node.lb.rect.distanceSquaredTo(queryPoint) < closestPoint.distanceSquaredTo(queryPoint)) {
                    candidate = nearest(queryPoint, node.lb, closestPoint);
                }
            }
        }

        if (candidate != null && candidate.distanceSquaredTo(queryPoint) < closestPoint.distanceSquaredTo(queryPoint))
            closestPoint = candidate;

        return closestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

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
            if (isXLevel()) {
                return Double.compare(pointToCompare.x(), point.x());
            } else {
                return Double.compare(pointToCompare.y(), point.y());
            }
        }

        public boolean isXLevel() {
            return level % 2 == 0;
        }

        public boolean intersectsWithSplitingLine(RectHV rectToTest) {
            if (isXLevel()) {
                return point.x() >= rectToTest.xmin() && point.x() <= rectToTest.xmax();
            }
            else {
                return point.y() >= rectToTest.ymin() && point.y() <= rectToTest.ymax();
            }
        }

        public boolean isRightTop(RectHV rect) {
            if (isXLevel()) {
                return rect.xmin() > point.x() && rect.xmax() > point.x();
            } else {
                return rect.ymin() > point.y() && rect.ymax() > point.y();
            }
        }
    }
}
