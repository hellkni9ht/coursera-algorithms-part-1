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
        if (points == null || checkDuplicate(points)) {
            throw new IllegalArgumentException();
        }

        ArrayList<LineSegment> foundSegments = new ArrayList<>();

        for (int p = 0;  p < points.length - 3; p++) {
            for (int q = p + 1; q < points.length - 2; q++) {
                for (int r = q + 1; r < points.length - 1; r++) {
                    for (int s = r + 1; s < points.length; s++) {
                        Point[] segment = {
                          points[p], points[q], points[r], points[s]
                        };

                        if (segment[0].slopeTo(segment[1]) == segment[0].slopeTo(segment[2]) &&
                            segment[0].slopeTo(segment[1]) == segment[0].slopeTo(segment[3])) {
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

    private boolean checkDuplicate(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++)
                if (points[i] == points[j]) {
                    return true;
                }
        }
        return false;
    }
}
