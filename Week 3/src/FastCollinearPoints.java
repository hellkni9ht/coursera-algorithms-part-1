import java.util.*;

/**
 * Created by user on 09.10.2015.
 */
public class FastCollinearPoints {
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {

        // check input
        if (points == null || checkDuplicate(points)) {
            throw new IllegalArgumentException();
        }

        HashMap<Double, List<ArrayList<Point>>> foundSegments = new HashMap<>();
        Point[] sortedPoints = Arrays.copyOf(points, points.length);

        for (Point origin : points) {
            // For each other point q, determine the slope it makes with p.
            Arrays.sort(sortedPoints, origin.slopeOrder());

            // Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p.
            // If so, these points, together with p, are collinear.
            double segmentSlope = Double.NEGATIVE_INFINITY;
            ArrayList<Point> segmentPoints = new ArrayList<>();
            for (int q = 0; q < sortedPoints.length; q++) {
                double currentSlope = origin.slopeTo(sortedPoints[q]);

                if (segmentPoints.isEmpty() || Double.compare(currentSlope, segmentSlope) == 0) {
                    segmentPoints.add(sortedPoints[q]);
                } else {
                    // add new segment
                    if (segmentPoints.size() >= 3) {
                        segmentPoints.add(origin);
                        addUniqueSegment(foundSegments, segmentPoints, segmentSlope);
                    }

                    // reset params for the new segment
                    segmentPoints.clear();
                    segmentPoints.add(sortedPoints[q]);
                }
                segmentSlope = currentSlope;
            }

            // handle the last segment
            if (segmentPoints.size() >= 3) {
                segmentPoints.add(origin);
                addUniqueSegment(foundSegments, segmentPoints, segmentSlope);
            }
        }

        // extract segments from map
        ArrayList<LineSegment> lineSegments = new ArrayList<>();
        Iterator it = foundSegments.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<ArrayList<Point>> listOfSegments = (List<ArrayList<Point>>)pair.getValue();
            for (ArrayList<Point> segmentPoints : listOfSegments)
                lineSegments.add(new LineSegment(segmentPoints.get(0), segmentPoints.get(segmentPoints.size() - 1)));
        }

        // save results
        this.segments = new LineSegment[lineSegments.size()];
        lineSegments.toArray(this.segments);
    }

    private void addUniqueSegment(HashMap<Double, List<ArrayList<Point>>> foundSegments, ArrayList<Point> points, double slope) {
        List<ArrayList<Point>> listOfSegments = foundSegments.get(slope);
        ArrayList<Point> newSegment = (ArrayList<Point>)points.clone();

        // sort points of the new segment
        Collections.sort(newSegment);

        if (listOfSegments != null) {

            // Segments with the current slope is already exists.
            boolean foundSegment = false;
            Iterator itSegmentPoints = listOfSegments.iterator();

            // Find duplicates
            while (itSegmentPoints.hasNext()) {
                int equalPoints = 0;
                ArrayList<Point> segmentPoints = (ArrayList<Point>)itSegmentPoints.next();

                // compare points
                for (Point point : newSegment) {
                    if (0 <= Collections.binarySearch(segmentPoints, point))
                        equalPoints++;
                }

                // if number of equal points is greater than one, it's definitely a duplicate.
                if (equalPoints > 1) {
                    // choose the segment with greater number of points
/*                    if (newSegment.get(0).compareTo(segmentPoints.get(0)) != 0) {
                        segmentPoints.add(0, newSegment.get(0));
                    }

                    if (newSegment.get(newSegment.size() - 1).compareTo(segmentPoints.get(segmentPoints.size() - 1)) != 0) {
                        segmentPoints.add(segmentPoints.size() - 1, newSegment.get(newSegment.size() - 1));
                    }*/
                    if (newSegment.size() > segmentPoints.size())
                        segmentPoints = newSegment;
                    foundSegment = true;
                    break;
                }
            }

            // add if it isn't duplicate
            if (!foundSegment)
                listOfSegments.add(newSegment);
        }
        else {
            // it's completely new slope, so add it to the map directly
            List<ArrayList<Point>> newListOfSegments = new LinkedList<>();
            newListOfSegments.add(newSegment);
            foundSegments.put(slope, newListOfSegments);
        }
    }

    private boolean checkDuplicate(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++)
            if (points[i] == points[j]) {
                return true;
            }
        }
        return false;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments;
    }

}
