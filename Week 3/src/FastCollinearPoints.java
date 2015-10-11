import java.util.*;

/**
 * Created by user on 09.10.2015.
 */
public class FastCollinearPoints {
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        HashMap<Double, List<Point[]>> foundSegments = new HashMap<>();
        Point[] copyOfPoints = Arrays.copyOfRange(points, 0, points.length - 1);

        for (int p = 0;  p < points.length; p++) {

            // For each other point q, determine the slope it makes with p.
            Arrays.sort(copyOfPoints, points[p].slopeOrder());

            // Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p.
            // If so, these points, together with p, are collinear.
            int startingPointOfSegment = 0, numberOfPoints = 1;
            double segmentSlope = Double.NEGATIVE_INFINITY;
            for (int q = 0; q < copyOfPoints.length; q++) {
                double currentSlope = points[p].slopeTo(copyOfPoints[q]);
                if (Double.compare(currentSlope, segmentSlope) == 0) {
                    numberOfPoints++;
                } else {
                    // add new segment
                    if (numberOfPoints >= 3) {
                        Point[] newSegment = new Point[q - startingPointOfSegment + 1];
                        newSegment[0] = points[p];
                        for (int newSegmentPoint = 0; newSegmentPoint < q - startingPointOfSegment; newSegmentPoint++)
                            newSegment[newSegmentPoint + 1] = copyOfPoints[newSegmentPoint + startingPointOfSegment];
                        addUniqueSegment(foundSegments, newSegment, segmentSlope);
                    }

                    // reset params for the new segment
                    numberOfPoints = 1;
                    startingPointOfSegment = q;
                    segmentSlope = currentSlope;
                }
            }

            // handle the last segment
            if (numberOfPoints >= 3) {
                addUniqueSegment(foundSegments, Arrays.copyOfRange(copyOfPoints, startingPointOfSegment, copyOfPoints.length - 1), segmentSlope);
            }
        }

        // extract segments from map
        ArrayList<LineSegment> lineSegments = new ArrayList<>();
        Iterator it = foundSegments.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<Point[]> listOfSegments = (List<Point[]>)pair.getValue();
            for (Point[] segmentPoints : listOfSegments)
                lineSegments.add(new LineSegment(segmentPoints[0], segmentPoints[segmentPoints.length - 1]));
        }

        // save results
        this.segments = new LineSegment[lineSegments.size()];
        lineSegments.toArray(this.segments);
    }

    private void addUniqueSegment(HashMap<Double, List<Point[]>> foundSegments, Point[] newSegment, double slope) {
        List<Point[]> listOfSegments = foundSegments.get(slope);

        // sort points of the new segment
        Arrays.sort(newSegment);

        if (listOfSegments != null) {

            // Segments with the current slope is already exists.
            boolean foundSegment = false;
            Iterator itSegmentPoints = listOfSegments.iterator();

            // Find duplicates
            while (itSegmentPoints.hasNext()) {
                int equalPoints = 0;
                Point[] segmentPoints = (Point[])itSegmentPoints.next();

                // compare points
                for (int i = 0; i < newSegment.length; i++) {
                    if (0 <= Arrays.binarySearch(segmentPoints, newSegment[i]))
                        equalPoints++;
                }

                // if number of equal points is greater than one, it's definitely a duplicate.
                if (equalPoints > 1) {
                    // choose the segment with greater number of points
                    if (newSegment.length > segmentPoints.length)
                        segmentPoints = Arrays.copyOf(newSegment, newSegment.length);
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
            List<Point[]> newListOfSegments = new LinkedList<>();
            newListOfSegments.add(newSegment);
            foundSegments.put(slope, newListOfSegments);
        }
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
