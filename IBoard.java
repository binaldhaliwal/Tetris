package model;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Board Interface.
 *
 * @author binaldhaliwal
 * @version autumn 2023
 */
public interface IBoard {
    /**
     * Get's the width of the board.
     *
     * @return the board's width.
     */
    int getWidth();

    /**
     * Get's the height of the board.
     *
     * @return the board's height.
     */
    int getHeight();
    /**
     * Resets the board for a new game.
     * This method ise called before the first game
     * and before each new game.
     */
    void newGame();
    /**
     * Sets a non random sequence of pieces to loop through.
     *
     * @param thePieces the List of non random TetrisPieces.
     */
    void setPieceSequence(List<TetrisPiece> thePieces);
    /**
     * Advances the board by one 'step'.
     * <p>
     * This could include
     * - moving the current piece down 1 line
     * - freezing the current piece if appropriate
     * - clearing full lines as needed
     */
    void step();
    /**
     * Try to move the movable piece down.
     * Freeze the Piece in position if down tries to move into an illegal state.
     * Clear full lines.
     */
    void down();
    /**
     * Try to move the movable piece left.
     */
    void left();
    /**
     * Try to move the movable piece right.
     */
    void right();
    /**
     * Rotate the movable piece in a clockwise direction.
     */
    void rotateCW();
    /**
     * Rotate the movable piece in a counter-clockwise direction.
     */
    void rotateCCW();
    /**
     * Drop the piece until piece is set.
     */
    void drop();

    /**
     * Adds a property change listener to this Board.
     *
     * @param theListener is the property change listener to be added.
     */
    void addPropertyChangeListener(PropertyChangeListener theListener);

    /**
     * Removes a property change listener to this Board.
     *
     * @param theListener is the property change listener to be added.
     */
    void removePropertyChangeListener(PropertyChangeListener theListener);
}
