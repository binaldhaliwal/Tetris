package model;

/**
 * The interface for the MovableTetrisPiece.
 * @author Eric John
 * @version 11/14/2023
 */
public interface IMovableTetrisPiece {

    /**
     * Get the TetrisPiece type of this movable TetrisPiece.
     *
     * @return The TetrisPiece describing this piece.
     */
    TetrisPiece getTetrisPiece();

    /**
     * The current board position of the TetrisPiece.
     *
     * @return the board position.
     */
    Point getPosition();

    /**
     * Get the current rotation value of the movable TetrisPiece.
     *
     * @return current rotation value.
     */
    Rotation getRotation();

    /**
     * Gets the TetrisPiece points rotated and translated to board coordinates.
     *
     * @return the board points for the TetrisPiece blocks.
     */
    Point[] getBoardPoints();

    /**
     * Rotates the TetrisPiece clockwise.
     *
     * @return A new rotated movable TetrisPiece
     */
    MovableTetrisPiece rotateCW();

    /**
     * Rotates the TetrisPiece counter clockwise.
     *
     * @return A new rotated movable TetrisPiece
     */
    MovableTetrisPiece rotateCCW();

    /**
     * Moves the TetrisPiece to the left on the game board.
     *
     * @return A new left moved movable TetrisPiece
     */
    MovableTetrisPiece left();

    /**
     * Moves the TetrisPiece to the right on the game board.
     *
     * @return A new right moved movable TetrisPiece
     */
    MovableTetrisPiece right();

    /**
     * Moves the TetrisPiece down on the game board.
     *
     * @return A new movable TetrisPiece moved down.
     */
    MovableTetrisPiece down();

    /**
     * Returns a new MovableTetrisPiece of the current piece type and same Rotation
     * at the specified location.
     *
     * @param thePosition the location for the returned MovableTetrisPiece
     * @return A new movable TetrisPiece at the specified location
     */
    MovableTetrisPiece setPosition(Point thePosition);
}
