package model;

/**
 * Point Interface.
 *
 * @author Corey and Jonathan
 * @version Autumn 2023
 */
public interface IPoint {
    /**
     * Returns the X coordinate.
     * @return the X coordinate of the point.
     */
    int x();

    /**
     * Returns the Y coordinate.
     * @return the Y coordinate of the point.
     */
    int y();

    /**
     * Creates a new point transformed by x and y.
     *
     * @param theX the X factor to transform by.
     * @param theY the Y factor to transform by.
     * @return the new transformed Point.
     */
    Point transform(int theX, int theY);
    /**
     * Creates a new point transformed by another Point.
     *
     * @param thePoint the Point to transform with.
     * @return the new transformed Point.
     */
    Point transform(Point thePoint);

}
