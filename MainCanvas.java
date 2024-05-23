package view;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.JPanel;
import model.Block;
import model.Board;
import model.IMovableTetrisPiece;
import model.Point;


/**
 * This class shows a tetris compoment that displays all tetrominos on the game screen.
 *
 * @author kimjona
 * @author Corey
 * @version 1.0
 */
// Magic numbers suppressed in this class are constants that represent specific values
// integral to the application's logic and functionality.
// These numbers are used with specific meanings and are not arbitrary, enhancing code readability.
// Such as, setting the paint color with a specified RGB value.
//@SuppressWarnings({"unchecked", "CheckStyle"})
@SuppressWarnings({"unchecked", "CheckStyle"})
public class MainCanvas extends JPanel implements PropertyChangeListener, ITheme {

    /**
     * Constant size of each grid size for drawing a tetromino and the font size.
     */
    public static final int SIZE = 25;

    /**
     * Float that sets the stroke(line thickness).
     */
    public static final float WIDTH = 2.0f;
    /**
     * Constant for the y value in score.
     */
    public static final int Y = 200;
    /**
     * Constant for the x value in score.
     */
    public static final int X = 85;
    /**
     * Constant for the y value in lines cleared.
     */
    public static final int Y1 = 175;
    /**
     * Constant for the x value in lines cleared.
     */
    public static final int X1 = 45;
    /**
     * Constant for the y value for font.
     */
    public static final int SIZE1 = 14;
    /**
     * Constant for the y value in game over.
     */
    public static final int Y2 = 150;
    /**
     * Constant for the x value in game over.
     */
    public static final int X2 = 45;
    /**
     * Constant representing the y-coordinate value for the end point
     * of lines drawn in a graphics operation.
     */
    public static final int Y_2 = 500;
    /**
     * Constant representing the x-coordinate value for the end point
     * of lines drawn in a graphics operation.
     */
    public static final int X_2 = 250;
    /**
     * Constant representing the limit for the loop iterations.
     */
    public static final int INT = 20;
    /**
     * myRandom is a Random object for generating random values.
     */
    private final Random myRandom = new Random();
    /**
     * This is a list representing the game board. Each inner
     * array represents a row.
     */
    private List<Block[]> myGameBoard;
    /**
     * Map mapping Block types to dsiplay colors for rendering.
     */
    private IMovableTetrisPiece myCurrentPiece;
    /**
     * Map mapping Block types to display colors for rendering.
     */
    private final Map<Block, Color[]> myColorMappings;
    /**
     * Boolean for whether a game over message should be shown.
     */
    private boolean myGameOver;
    /**
     * Boolean for whether a game should draw a grid.
     */
    private boolean myGrid;
    /**
     * Int for final score.
     */
    private int myFinalScore;
    /**
     * Int for final clear.
     */
    private int myFinalClear;
    /**
     * Int representation of the current theme.
     */
    private int myTheme = 1;

    MainCanvas() {
        super();
        myColorMappings = new HashMap<>();
        mapColors();
        myFinalClear = 0;
        myFinalScore = 0;
        myGrid = false;
    }

    /**
     * Maps Block types to display colors.
     */
    private void mapColors() {
        myColorMappings.put(Block.I, new Color[]{Color.CYAN, Color.CYAN, DARK_GREEN, ICE_1});
        myColorMappings.put(Block.J, new Color[]{Color.BLUE, Color.ORANGE, PALE_GREEN, ICE_2});
        myColorMappings.put(Block.L, new Color[]{Color.ORANGE, Color.YELLOW, BRIGHT_GREEN, ICE_3});
        myColorMappings.put(Block.O, new Color[]{Color.YELLOW, Color.BLUE, GOLD, ICE_4});
        myColorMappings.put(Block.S, new Color[]{Color.GREEN, Color.GREEN, LIME, ICE_5});
        myColorMappings.put(Block.T, new Color[]{Color.MAGENTA, Color.MAGENTA, CHERRY, ICE_6});
        myColorMappings.put(Block.Z, new Color[]{Color.PINK, Color.RED, PINE_GREEN, ICE_7});
    }

    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        final Graphics2D graphics2D = (Graphics2D) theG;
        setupGradientPaint(graphics2D);
        drawGameBoard(graphics2D);
        drawNextPiece(graphics2D);
        if (myGameOver) {
            drawGameOver(graphics2D);
        }
    }

    /**
     * Draws the game board.
     * @param theGraphics2D the graphics context.
     */
    private void drawGameBoard(final Graphics2D theGraphics2D) {
        if(myGrid) {
            theGraphics2D.setPaint(new Color(0, 0, 0, 100));

            for (int a = 0; a < INT; a++) {
                theGraphics2D.drawLine(0, a * SIZE, X_2, a * SIZE);
            }
            for (int a = 0; a < 10; a++) {
                theGraphics2D.drawLine(a * SIZE, 0, a * SIZE, Y_2);
            }
        }

        if (myGameBoard != null) {
            for (int i = 0; i < myGameBoard.size(); i++) {
                int x = 0;
                final int y = (Board.getInstance().getHeight() - i - 2) * SIZE;
                for (final Block block : myGameBoard.get(i)) {
                    if (block != Block.EMPTY && block != null) {
                        theGraphics2D.setStroke(new BasicStroke(WIDTH));
                        theGraphics2D.setColor(Color.BLACK);
                        theGraphics2D.drawRect(x * SIZE, y, SIZE, SIZE);
                        theGraphics2D.setColor(myColorMappings.get(block)[myTheme]);
                        theGraphics2D.fill(new Rectangle2D.Double((x * SIZE) + 1,
                                y + 1, SIZE - 1, SIZE - 1));
                    }
                    x++;
                }
            }
        }
    }
    /**
     * Draws the current Tetris piece.
     *
     * @param theGraphics2D The graphics context.
     */
    private void drawNextPiece(final Graphics2D theGraphics2D) {
        if (myCurrentPiece != null) {
            final Point[] pos = myCurrentPiece.getBoardPoints();
            for (final Point point : pos) {
                final int x = point.x() * SIZE;
                final int y = SIZE * (Board.getInstance().getHeight() - 2 - point.y());
                theGraphics2D.setStroke(new BasicStroke(WIDTH));
                theGraphics2D.setPaint(Color.WHITE);
                theGraphics2D.draw(new Rectangle2D.Double(x, y, SIZE, SIZE));
                //TODO maybe add more functionallity here for various themes
                if(myTheme != 0) {
                    //This functionality makes use of the way the moveable tetris
                    //piece is set up, which is provided. A moveable tetris piece
                    //will always be a type of tetris piece.
                    //noinspection LawOfDemeter
                    theGraphics2D.setColor(myColorMappings.get(myCurrentPiece.getTetrisPiece().getBlock())[myTheme]);
                    theGraphics2D.fill(new Rectangle2D.Double(x + 1, y + 1, SIZE - 1, SIZE - 1));
                }
            }
        }
    }
    /**
     * Draws the game over message.
     * @param theGraphics2D the graphics context.
     */
    private void drawGameOver(final Graphics theGraphics2D) {
        theGraphics2D.setColor(MY_BG_COLORS[myTheme * 2]);
        theGraphics2D.fillRect(X2 - 5,Y1 - (SIZE * 3) + 10, X * 2, SIZE * 4);
        theGraphics2D.setColor(MY_TEXT_COLORS[myTheme]);
        theGraphics2D.drawRect(X2 - 6,Y1 - (SIZE * 3) + 9, X * 2 + 2, SIZE * 4 + 2);

        theGraphics2D.setFont(new Font(" Verdana", Font.BOLD, SIZE));
        theGraphics2D.drawString("GAME OVER", X2, Y2);
        theGraphics2D.setFont(new Font("Verdana", Font.BOLD, SIZE1));
        theGraphics2D.drawString("LINES CLEARED: " + myFinalClear, X1, Y1);
        theGraphics2D.drawString("SCORE: " + myFinalScore, X, Y);
    }

    /**
     * Sets up a gradient paint for the background.
     *
     * @param theGraphics2D The graphics context.
     */
    private void setupGradientPaint(final Graphics2D theGraphics2D) {
        final GradientPaint gradient = new GradientPaint(0, 0,
                MY_BG_COLORS[myTheme * 2], 0, getHeight(), MY_BG_COLORS[myTheme * 2 + 1]);
        theGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        theGraphics2D.setPaint(gradient);
        theGraphics2D.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals(Board.PROPERTY_GAME_BOARD)) {
            if (theEvent.getNewValue() instanceof List<?>) {
                // Suppressing unchecked cast warning because the setup ensures that the value
                // will always be a List<Block[]>.
                //@SuppressWarnings("unchecked")
                myGameBoard = (List<Block[]>) theEvent.getNewValue();
                repaint();
            }

        }
        if (theEvent.getPropertyName().equals(Board.PROPERTY_CURRENT_PIECE)) {
            myCurrentPiece = (IMovableTetrisPiece) theEvent.getNewValue();
            repaint();
        }
        if (theEvent.getPropertyName().equals(Board.PROPERTY_ROW_CLEAR)) {
            myFinalClear = (int) theEvent.getNewValue();
        }
        if (theEvent.getPropertyName().equals(Board.PROPERTY_SCORE_UPDATE)) {
            myFinalScore = (int) theEvent.getNewValue();
        }
        if (theEvent.getPropertyName().equals
                (Board.PROPERTY_GAME_OVER) && myGameBoard != null) {
            myGameBoard.clear();
            myCurrentPiece = null;
            if (theEvent.getOldValue() == null) {
                myGameOver = false;
                myFinalClear = 0;
            } else {
                myGameOver = true;
            }

            repaint();
        }
    }

    /**
     * Lets other classes forciblly end the game before the user has lost.
     */
    protected void endGame() {
        myGameOver = true;
        repaint();
    }
    /**
     * Lets other classes forciblly remove the game over message.
     */
    protected void removeEndMsg() {
        myGameOver = false;
        repaint();
    }

    /**
     * Sets the theme of the game.
     * @param theTheme the new theme.
     */
    @Override
    public void setTheme(final int theTheme) {
        myTheme = theTheme;
        repaint();
    }
    protected void setGrid(final boolean theGrid) {
        myGrid = theGrid;
        repaint();
    }
}
