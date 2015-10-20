import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by user on 09.10.2015.
 */
public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // check input
        if (ensureInput(points)) {
            throw new IllegalArgumentException();
        }

        ArrayList<LineSegment> foundSegments = new ArrayList<>();

        for (int p = 0;  p < points.length - 3; p++) {
            for (int q = p + 1; q < points.length - 2; q++) {
                double slopeToQ = points[p].slopeTo(points[q]);
                for (int r = q + 1; r < points.length - 1; r++) {
                    double slopeToR = points[p].slopeTo(points[r]);
                    if (Double.compare(slopeToQ, slopeToR) != 0)
                        continue;
                    for (int s = r + 1; s < points.length; s++) {
                        double slopeToS = points[p].slopeTo(points[s]);

                        if (Double.compare(slopeToQ, slopeToS) == 0) {
                            Point[] segment = {
                                    points[p], points[q], points[r], points[s]
                            };
                            Arrays.sort(segment);
                            foundSegments.add(new LineSegment(segment[0], segment[3]));
                        }
                    }
                }
            }
        }

        this.segments = foundSegments;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private boolean ensureInput(Point[] points) {
        if (points == null || points[0] == null)
            return false;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (i == 0 && (points[j] == null))
                    return true;

                if (points[i].compareTo(points[j]) == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
