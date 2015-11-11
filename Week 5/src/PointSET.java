import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

/**
 * Created by Bazna on 11/7/2015.
 */
public class PointSET {

    SET<Point2D> points = new SET<>();

    private void ensureInput(Object object) {
        if (object == null)
            throw new java.lang.NullPointerException();
    }

    // construct an empty set of points
    public PointSET() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }
    // number of points in the set
    public int size() {
        return points.size();
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        ensureInput(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        ensureInput(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        ensureInput(rect);

        ArrayList<Point2D> pointsInRect = new ArrayList<>();

        for (Point2D point : points) {
            if (rect.contains(point)) {
                pointsInRect.add(point);
            }
        }

        return pointsInRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        ensureInput(p);

        if (isEmpty())
            return null;

        Point2D candidate = null;
        double candidateDistance = Double.POSITIVE_INFINITY;
        for (Point2D point : points) {
            if (candidate == null || Double.compare(p.distanceSquaredTo(point), candidateDistance) < 0) {
                candidate = point;
                candidateDistance = p.distanceSquaredTo(candidate);
            }
        }

        return candidate;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}