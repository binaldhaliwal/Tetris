/*
 * TCSS 305
 * 
 * An implementation of the classic game "Tetris".
 */

package model;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import model.wallkicks.WallKick;

/**
 * Represents a Tetris board. Board objects communicate with clients via Observer pattern. 
 * <p>Clients can expect Board objects to call norifyObservers with four different 
 * data types:</p>
 * <dl>
 * <dt>{@code List<Block[]>}</dt>
 * <dd>Represents the non-moving pieces on the Board. i.e. Frozen Blocks</dd>
 * <dt>{@link MovableTetrisPiece MovableTerisPiece}</dt>
 * <dd>Represents current moving Piece.</dd>
 * <dt>{@link TetrisPiece TertisPiece}</dt>
 * <dd>Represents next Piece.</dd>
 * <dt>{@code Integer[]}</dt>
 * <dd>The size of the array represents the number of rows of Frozen Blocks removed.</dd>
 * <dt>{@code Boolean}</dt>
 * <dd>When true, the game is over. </dd>
 * </dl>
 * 
 * @author Charles Bryan
 * @author Alan Fowler
 * @version 1.3
 */

// Magic numbers suppressed in this class are constants that represent specific values
// integral to the application's logic and functionality.
// These numbers are used with specific meanings and are not arbitrary, enhancing code readability.
//@SuppressWarnings("CheckStyle")
// The Board class encapsulates the essential elements of a Tetris game, including board dimensions,
// scoring mechanisms, game state tracking, and piece management. The constants help maintain
// clarityin the code, while the instance fields manage theaspects of the game during runtime.
// @SuppressWarnings("ClassWithTooManyFields")
// The Board class has too many methods because it can be attributed to the complexity of managing
// a Tetris game. It handles the game logic and state management, event handling, piece management,
// observer pattern, scoring and level progression, encapsulation and modularity, singleton design pattern,
// and code organization.
// @SuppressWarnings("ClassWithTooManyMethods")
// The Board is a overly complex class because of components for the Tetris game. The game itself has complex
// rules and interactions, making the Board class might naturally become complex to accommodate and manage
// these rules.
// @SuppressWarnings("OverlyComplexClass")
// The checkRows method is a overly long method is responsible for checking and processing completed rows
// in a Tetris game. It identifies complete rows, collecting indices of complete rows, removing complete rows,
// tracking game state, scoring mechanism, and level advancement.
// @SuppressWarnings("OverlyLongMethod") @SuppressWarnings("OverlyComplexMethod")
@SuppressWarnings({"CheckStyle", "ClassWithTooManyFields", "ClassWithTooManyMethods", "OverlyComplexClass", "OverlyLongMethod", "OverlyComplexMethod"})
public final class Board implements IBoard {

    /**
     * This represents the property name for a change in the game board state.
     */
    public static final String PROPERTY_GAME_BOARD = "GameBoard";
    /**
     * This represents the property name for a change in the current piece.
     */
    public static final String PROPERTY_CURRENT_PIECE = "CurrentPiece";
    /**
     * This represents the propert name for a change in the game board due to row clearance.
     */
    public static final String PROPERTY_ROW_CLEAR = "RowClear";
    /**
     * This represents the property name for a change in the next Tetris piece.
     */
    public static final String PROPERTY_NEXT_PIECE = "NextPiece";
    /**
     * This represents the property name for a change indicating the game is over.
     */
    public static final String PROPERTY_GAME_OVER = "GameOver";
    /**
     * This represents the property name for when score is updated.
     */
    public static final String PROPERTY_SCORE_UPDATE = "Score";
    /**
     * This represents the property name for when advancing to the next level.
     */
    public static final String PROPERTY_NEXT_LEVEL_COUNTER = "NextLevel";
    /**
     * This represents the property name for when current game level changes.
     */
    public static final String PROPERTY_LEVEL = "Level";



    // Class constants
    
    /**
     * Default width of a Tetris game board.
     */
    private static final int DEFAULT_WIDTH = 10;

    /**
     * Default height of a Tetris game board.
     */
    private static final int DEFAULT_HEIGHT = 20;
    /**
     * Constant for score, if score tracker is equal to 4.
     */
    public static final int INT = 1200;
    /**
     * Constant for score, if score tracker is equal to 3.
     */
    public static final int INT1 = 300;
    /**
     * Constant for score, if score tracker is equal to 1.
     */
    public static final int INT2 = 40;
    /**
     * Tracks number of rows cleared in current game.
     */
    private int myRowsCleared;
    /**
     * Holds current player score.
     */
    private int myScore;
    /**
     * Tracks number of rows until next level increase.
     */
    private int myNextLevelCounter = 5;
    /**
     * Represents the current difficulty level.
     */
    private int myCurrentLevel = 1;
    /**
     * This field used when calculating score.
     */
    private int myScoreTracker;
    /**
     * Implements singleton design pattern.
     */
    private static final Board INSTANCE = new Board(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    
    // Instance fields
    
    /**
     * Width of the game board.
     */
    private final int myWidth;

    /**
     * Height of the game board.
     */
    private final int myHeight;
    
    /**
     * The frozen blocks on the board.
     */
    private final List<Block[]> myFrozenBlocks;
    
    /**
     * The game over state.
     */
    private boolean myGameOver;

    /**
     * Contains a non random sequence of TetrisPieces to loop through.
     */
    private List<TetrisPiece> myNonRandomPieces;

    /**
     * The current index in the non random piece sequence.
     */
    private int mySequenceIndex;
    
    /**
     * Piece that is next to play.
     */
    private TetrisPiece myNextPiece;
    
    /**
     * Piece that is currently movable.
     */
    private MovableTetrisPiece myCurrentPiece;

    /**
     * A flag to indicate when moving a piece down is part of a drop operation.
     * This is used to prevent the Board from notifying observers for each incremental
     * down movement in the drop.
     */
    private boolean myDrop;

    /**
     * Add a refernce to a PropertyChangeSupport object.
     */
    private final PropertyChangeSupport myPcs = new PropertyChangeSupport(this);


    /**
     * Tetris board constructor for non-default sized boards.
     *
     * @param theWidth Width of the Tetris game board.
     * @param theHeight Height of the Tetris game board.
     */
    private Board(final int theWidth, final int theHeight) {
        super();
        myWidth = theWidth;
        myHeight = theHeight;
        myFrozenBlocks = new LinkedList<>();

        myNonRandomPieces = new ArrayList<>();
        mySequenceIndex = 0;

        /*  myNextPiece and myCurrentPiece
         *  are initialized by the newGame() method.
         */
    }

    /**
     * This constructor returns the singleton instance of the Board class.
     *
     * @return returns the singleton instance of the Board.
     */
    public static Board getInstance() {
        return INSTANCE;
    }

    // Constructors


    /**
     * Adds a property change listener to this Board.
     *
     * @param theListener is the property change listener to be added.
     */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        this.myPcs.addPropertyChangeListener(theListener);
    }

    /**
     * Removes a property change listener to this Board.
     *
     * @param theListener is the property change listener to be added.
     */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        this.myPcs.removePropertyChangeListener(theListener);
    }
    

    // public queries
    
    /**
     * Get the width of the board.
     * 
     * @return Width of the board.
     */
    @Override
    public int getWidth() {
        return myWidth;
    }

    /**
     * Get the height of the board.
     * 
     * @return Height of the board.
     */
    @Override
    public int getHeight() {
        return myHeight;
    }
    


    /**
     * Resets the board for a new game.
     * This method must be called before the first game
     * and before each new game.
     */
    @Override
    public void newGame() {
        mySequenceIndex = 0;
        myFrozenBlocks.clear();
        for (int h = 0; h < myHeight; h++) {
            myFrozenBlocks.add(new Block[myWidth]);
        }
        // Reset game over state
        myGameOver = false;
        // Get the next movable Tetris piece
        // Initialize the current movable Tetris piece
        myCurrentPiece = nextMovablePiece(true);
        // Reset the drop flag
        // Reset the flag indicating whether the piece is dropped
        myDrop = false;
        // Reset the score
        // Initialize the game score
        myScore = 0;
        // Reset the number of rows cleared
        // Initialize the number of cleared rows
        myRowsCleared = 0;
        // Reset the counter for the next level
        // Initialize the counter for the next level increase
        myNextLevelCounter = 5;
        // Reset the current level
        // Initialize the current game level
        myCurrentLevel = 1;
        // Reset the score tracker
        // Initialize the score tracker
        myScoreTracker = 0;

        myPcs.firePropertyChange(PROPERTY_GAME_BOARD, null,
                myFrozenBlocks.toArray());
        myPcs.firePropertyChange(PROPERTY_GAME_OVER, null, false);
        myPcs.firePropertyChange(PROPERTY_SCORE_UPDATE, null, myScore);
        myPcs.firePropertyChange(PROPERTY_ROW_CLEAR, null, myRowsCleared);
        myPcs.firePropertyChange(PROPERTY_NEXT_LEVEL_COUNTER, null, myNextLevelCounter);
        myPcs.firePropertyChange(PROPERTY_LEVEL, null, myCurrentLevel);
    }

    /**
     * Sets a non random sequence of pieces to loop through.
     * 
     * @param thePieces the List of non random TetrisPieces.
     */
    @Override
    public void setPieceSequence(final List<TetrisPiece> thePieces) {
        myNonRandomPieces = new ArrayList<>(thePieces);
        mySequenceIndex = 0;
        myCurrentPiece = nextMovablePiece(true);
    }
    
    /**
     * Advances the board by one 'step'.
     * <p>
     * This could include
     * - moving the current piece down 1 line
     * - freezing the current piece if appropriate
     * - clearing full lines as needed
     */
    @Override
    public void step() {
        /*
         * Calling the down() method from here should be sufficient
         * to advance the board by one 'step'.
         * However, more code could be added to this method
         * to implement additional functionality
         */
        down();
    }
    
    /**
     * Try to move the movable piece down.
     * Freeze the Piece in position if down tries to move into an illegal state.
     * Clear full lines.
     */
    @Override
    public void down() {
        if (!move(myCurrentPiece.down())) {
            // the piece froze, so clear lines and update current piece
            addPieceToBoardData(myFrozenBlocks, myCurrentPiece);
            checkRows();
            if (!myGameOver) {
                // Increment the score for moving the piece down
                myScore += 4;
                myCurrentPiece = nextMovablePiece(false);
                myPcs.firePropertyChange(PROPERTY_CURRENT_PIECE, null, myCurrentPiece);
                myPcs.firePropertyChange(PROPERTY_GAME_BOARD, null, getBoard());
                myPcs.firePropertyChange(PROPERTY_SCORE_UPDATE, null, myScore);
            }
        }
    }

    /**
     * Try to move the movable piece left.
     */
    @Override
    public void left() {
        if (myCurrentPiece != null) {
            move(myCurrentPiece.left());
        }
    }

    /**
     * Try to move the movable piece right.
     */
    @Override
    public void right() {
        if (myCurrentPiece != null) {
            move(myCurrentPiece.right());
        }
    }

    /**
     * Try to rotate the movable piece in the clockwise direction.
     */
    @Override
    public void rotateCW() {
        if (myCurrentPiece != null) {
            if (myCurrentPiece.getTetrisPiece() == TetrisPiece.O) {
                move(myCurrentPiece.rotateCW());
            } else {
                final MovableTetrisPiece cwPiece = myCurrentPiece.rotateCW();
                final Point[] offsets = WallKick.getWallKicks(cwPiece.getTetrisPiece(),
                                                    myCurrentPiece.getRotation(),
                                                    cwPiece.getRotation());
                for (final Point p : offsets) {
                    final Point offsetLocation = cwPiece.getPosition().transform(p);
                    final MovableTetrisPiece temp = cwPiece.setPosition(offsetLocation);
                    if (move(temp)) {
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Try to rotate the movable piece in the counter-clockwise direction.
     */
    @Override
    public void rotateCCW() {
        if (myCurrentPiece != null) {
            if (myCurrentPiece.getTetrisPiece() == TetrisPiece.O) {
                move(myCurrentPiece.rotateCCW());
            } else {
                final MovableTetrisPiece ccwPiece = myCurrentPiece.rotateCCW();
                final Point[] offsets = WallKick.getWallKicks(ccwPiece.getTetrisPiece(),
                                                    myCurrentPiece.getRotation(),
                                                    ccwPiece.getRotation());
                for (final Point p : offsets) {
                    final Point offsetLocation = ccwPiece.getPosition().transform(p);
                    final MovableTetrisPiece temp = ccwPiece.setPosition(offsetLocation);
                    if (move(temp)) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Drop the piece until piece is set.
     */
    @Override
    public void drop() {
        if (!myGameOver) {
            myDrop = true;
            while (isPieceLegal(myCurrentPiece.down())) {
                down();  // move down as far as possible
            }
            myDrop = false;
            down();  // move down one more time to freeze in place
        }
    }
    


    @SuppressWarnings("OverlyLongMethod")
    @Override
    public String toString() {
        final List<Block[]> board = getBoard();
        board.add(new Block[myWidth]);
        board.add(new Block[myWidth]);
        board.add(new Block[myWidth]);
        board.add(new Block[myWidth]);
        final String line = "-".repeat(this.myWidth);
        if (myCurrentPiece != null) {
            addPieceToBoardData(board, myCurrentPiece);
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = board.size() - 1; i >= 0; i--) {
            final Block[] row = board.get(i);
            sb.append('|');
            for (final Block c : row) {
                if (c == null) {
                    sb.append(' ');
                } else {
                    sb.append('*');
                }
            }
            sb.append("|\n");
            if (i == this.myHeight) {
                sb.append(' ');
                sb.append(line);
                sb.append('\n');
            }
        }
        sb.append('|');
        sb.append(line);
        sb.append('|');
        return sb.toString();
    }

    
    // private helper methods
    
    /**
     * Helper function to check if the current piece can be shifted to the
     * specified position.
     * 
     * @param theMovedPiece the position to attempt to shift the current piece
     * @return True if the move succeeded
     */
    private boolean move(final MovableTetrisPiece theMovedPiece) {
        boolean result = false;
        if (isPieceLegal(theMovedPiece)) {
            myCurrentPiece = theMovedPiece;
            result = true;
            if (!myDrop) {
                myPcs.firePropertyChange(PROPERTY_CURRENT_PIECE, null, myCurrentPiece);
            }
        }
        return result;
    }

    /**
     * Helper function to test if the piece is in a legal state.
     * <p>
     * Illegal states:
     * - points of the piece exceed the bounds of the board
     * - points of the piece collide with frozen blocks on the board
     * 
     * @param thePiece MovableTetrisPiece to test.
     * @return Returns true if the piece is in a legal state; false otherwise
     */
    private boolean isPieceLegal(final MovableTetrisPiece thePiece) {
        boolean result = true;
        
        for (final Point p : thePiece.getBoardPoints()) {
            if (p.x() < 0 || p.x() >= myWidth) {
                result = false;
            }
            if (p.y() < 0) {
                result = false;
            }
        }
        return result && !collision(thePiece);      
    }

    /**
     * Adds a movable Tetris piece into a list of board data.
     * <p>
     * Allows a single data structure to represent the current piece
     * and the frozen blocks.
     * 
     * @param theFrozenBlocks Board to set the piece on.
     * @param thePiece Piece to set on the board.
     */
    private void addPieceToBoardData(final List<Block[]> theFrozenBlocks,
                                     final MovableTetrisPiece thePiece) {
        for (final Point p : thePiece.getBoardPoints()) {
            //noinspection LawOfDemeter
            setPoint(theFrozenBlocks, p, thePiece.getTetrisPiece().getBlock());
        }
        myPcs.firePropertyChange(PROPERTY_GAME_BOARD, null, getBoard());
    }

    /**
     * Checks the board for complete rows.
     */
    private void checkRows() {
        final List<Integer> completeRows = new ArrayList<>();
        for (final Block[] row : myFrozenBlocks) {
            boolean complete = true;
            for (final Block b : row) {
                if (b == null) {
                    complete = false;
                    break;
                }
            }
            if (complete) {
                completeRows.add(myFrozenBlocks.indexOf(row));
            }
        }
        // loop through list backwards removing items by index
        if (!completeRows.isEmpty()) {
            for (int i = completeRows.size() - 1; i >= 0; i--) {
                // Increment the cleared rows count
                myRowsCleared++;
                // Decrement the counter for the next level
                myNextLevelCounter--;
                // Increment the score tracker
                myScoreTracker++;
                final Block[] row = myFrozenBlocks.get(completeRows.get(i));
                myFrozenBlocks.remove(row);
                myFrozenBlocks.add(new Block[myWidth]);
                // Check if it's time to move to the next level
                if (myNextLevelCounter == 0) {
                    myNextLevelCounter = 5;
                    myCurrentLevel++;
                }
            }
            // Update the score based on the score tracker
            if (myScoreTracker == 1) {
                myScore += myCurrentLevel * INT2;
            } else if (myScoreTracker == 2) {
                myScore += myCurrentLevel * 100;
            } else if (myScoreTracker == 3) {
                myScore += myCurrentLevel * INT1;
            } else if (myScoreTracker == 4) {
                myScore += myCurrentLevel * INT;
            }
            // Reset the score tracker
            myScoreTracker = 0;
            myPcs.firePropertyChange(PROPERTY_SCORE_UPDATE, null, myScore);
            myPcs.firePropertyChange(PROPERTY_ROW_CLEAR, null, myRowsCleared);
            myPcs.firePropertyChange(PROPERTY_NEXT_LEVEL_COUNTER, null, myNextLevelCounter);
            myPcs.firePropertyChange(PROPERTY_LEVEL, null, myCurrentLevel);
        }
    }
    
    /**
     * Helper function to copy the board.
     * 
     * @return A new copy of the board.
     */
    private List<Block[]> getBoard() {
        final List<Block[]> board = new ArrayList<>();
        for (final Block[] row : myFrozenBlocks) {
            board.add(row.clone());
        }
        return board;
    }

    /**
     * Determines if a point is on the game board.
     * 
     * @param theBoard Board to test.
     * @param thePoint Point to test.
     * @return True if the point is on the board otherwise false.
     */
    private boolean isPointOnBoard(final List<Block[]> theBoard, final Point thePoint) {
        return thePoint.x() >= 0 && thePoint.x() < myWidth && thePoint.y() >= 0
               && thePoint.y() < theBoard.size();
    }

    /**
     * Sets a block at a board point.
     * 
     * @param theBoard Board to set the point on.
     * @param thePoint Board point.
     * @param theBlock Block to set at board point.
     */
    private void setPoint(final List<Block[]> theBoard,
                          final Point thePoint,
                          final Block theBlock) {
        
        if (isPointOnBoard(theBoard, thePoint)) { 
            final Block[] row = theBoard.get(thePoint.y());
            row[thePoint.x()] = theBlock;
        } else if (!myGameOver) {
            myGameOver = true;
            myPcs.firePropertyChange(PROPERTY_GAME_OVER, false, true);
        }
    }

    /**
     * Returns the block at a specific board point.
     * 
     * @param thePoint the specific Point to check
     * @return the Block type at point or null if no block exists.
     */
    private Block getPoint(final Point thePoint) {
        Block b = null;
        if (isPointOnBoard(myFrozenBlocks, thePoint)) {
            b = myFrozenBlocks.get(thePoint.y())[thePoint.x()];
        }
        return b;
    }

    /**
     * Helper function to determine of a movable block has collided with set
     * blocks.
     * 
     * @param theTest movable TetrisPiece to test for collision.
     * @return Returns true if any of the blocks has collided with a set board
     *         block.
     */
    private boolean collision(final MovableTetrisPiece theTest) {
        boolean res = false;
        for (final Point p : theTest.getBoardPoints()) {
            if (getPoint(p) != null) {
                res = true;
            }
        }
        return res;
    }

    /**
     * Gets the next MovableTetrisPiece.
     * 
     * @param theRestart Restart the non random cycle.
     * @return A new MovableTetrisPiece.
     */
    private MovableTetrisPiece nextMovablePiece(final boolean theRestart) {
        
        if (myNextPiece == null || theRestart) {
            prepareNextMovablePiece();
        }
        
        final TetrisPiece next = myNextPiece;
        
        int startY = myHeight - 1;
        if (myNextPiece == TetrisPiece.I) {
            startY--; 
        }
        
        prepareNextMovablePiece();
        return new MovableTetrisPiece(
                       next,
                       new Point((myWidth - myNextPiece.getWidth()) / 2, startY));
    }
    
    /**
     * Prepares the Next movable piece for preview.
     */
    private void prepareNextMovablePiece() {
        
        final boolean share = myNextPiece != null;
        if (myNonRandomPieces == null || myNonRandomPieces.isEmpty()) {
            myNextPiece = TetrisPiece.getRandomPiece();
        } else {
            mySequenceIndex %= myNonRandomPieces.size();
            myNextPiece = myNonRandomPieces.get(mySequenceIndex++);
        }
        if (share && !myGameOver) {
            myPcs.firePropertyChange(PROPERTY_NEXT_PIECE, null, myNextPiece);
        }
    }    

    
    // Inner classes

    /**
     * A class to describe the board data to registered Observers.
     * The board data includes the current piece and the frozen blocks.
     */
    protected final class BoardData {
        
        /**
         * The board data to pass to observers.
         */
        private final List<Block[]> myBoardData;

        /**
         * Constructor of the Board Data object.
         */
        private BoardData() {
            super();
            myBoardData = getBoard();
            myBoardData.add(new Block[myWidth]);
            myBoardData.add(new Block[myWidth]);
            myBoardData.add(new Block[myWidth]);
            myBoardData.add(new Block[myWidth]);
            if (myCurrentPiece != null) {
                addPieceToBoardData(myBoardData, myCurrentPiece);
            }
        }

        /**
         * Copy and return the board's data.
         * 
         * @return Copy of the Board Data.
         */
        private List<Block[]> getBoardData() {
            final List<Block[]> board = new ArrayList<>();
            for (final Block[] row : myBoardData) {
                board.add(row.clone());
            }
            return board;
        }
        
    } // end inner class BoardData

}
