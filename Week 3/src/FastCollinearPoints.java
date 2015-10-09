import java.util.*;

/**
 * Created by user on 09.10.2015.
 */
public class FastCollinearPoints {
    LineSegment[] segments;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        HashMap<Double, Point[]> foundSegments = new HashMap<>();
        Point[] copyOfPoints = Arrays.copyOfRange(points, 0, points.length - 1);

        for (int p = 0;  p < points.length; p++) {
            Arrays.sort(copyOfPoints, points[p].slopeOrder());
            int startingPointOfSegment = 0, numberOfPoints = 1;
            double segmentSlope = Double.NEGATIVE_INFINITY;
            for (int q = 0; q < copyOfPoints.length; q++) {
                double currentSlope = points[p].slopeTo(copyOfPoints[q]);
                if (Double.compare(currentSlope, segmentSlope) == 0) {
                    numberOfPoints++;
                } else {
                    // add new segment
                    if (numberOfPoints >= 3) {
                        addUniqueSegment(foundSegments, Arrays.copyOfRange(copyOfPoints, startingPointOfSegment, q - 1), segmentSlope);
                    }
                    numberOfPoints = 1;
                    startingPointOfSegment = q;
                    segmentSlope = currentSlope;
                }
            }

            if (numberOfPoints >= 3) {
                addUniqueSegment(foundSegments, Arrays.copyOfRange(copyOfPoints, startingPointOfSegment, copyOfPoints.length - 1), segmentSlope);
            }
        }

        this.segments = new LineSegment[foundSegments.size()];
        int iSegment = 0;
        Iterator it = foundSegments.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Point[] segmentPoints = (Point[])pair.getValue();
            segments[iSegment++] = (new LineSegment(segmentPoints[0], segmentPoints[segmentPoints.length - 1]));
        }
    }

    private void addUniqueSegment(HashMap<Double, Point[]> foundSegments, Point[] newSegment, double slope) {
        Point[] points = foundSegments.get(slope);

        Arrays.sort(newSegment);

        if (points != null) {
            if (newSegment.length > points.length)
                foundSegments.replace(slope, newSegment);
        }
        else {
            foundSegments.put(slope, newSegment);
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
