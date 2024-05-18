package view;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import model.Board;
import model.TetrisPiece;

/**
 * NextTetris shows the T tetromino centered inside the blue(next tetris piece) region.
 *
 * @author ejohn5
 * @version Autumn 2023
 */

// Magic numbers suppressed in this class are constants that represent specific values
// integral to the application's logic and functionality.
// These numbers are used with specific meanings and are not arbitrary, enhancing code readability.
// Such as, the tick rate, and next level counter.
//@SuppressWarnings("CheckStyle")
@SuppressWarnings("CheckStyle")
public class ScoreCanvas extends JPanel implements PropertyChangeListener, ITheme {

    /**
     * Constant for the x value for count until next level.
     */
    public static final int X = 10;
    /**
     * Constant of the y value for count until next level string.
     * */
    public static final int Y = 175;
    /**
     * Constant of the y incrementation for each description string.
     */
    public static final int YIncrement = 15;
    /**
     * Constant for font.
     */
    public static final int SIZE = 11;
    /**
     * An int that returns the score of the player.
     */
    private int myScore;

    /**
     * An int that returns how many lines the player cleared.
     */
    private int myCurrentLines;

    /**
     * An int that returns the level the game.
     */
    private int myCurrentLevel;

    /**
     * An int that counts down how many lines until the next level.
     */
    private int myNextLevelCounter;

    /**
     * The Tetris piece to be dispalyed.
     */
    private TetrisPiece myNextPiece;

    /**
     * Timer for the game
     */
    private final Timer myTimer;

    /**
     * The tick rate of the game
     */
    private int myTickRate;
    /**
     * Int representation of the current theme.
     */
    private int myTheme = 1;

    /**
     * Constructor to instantiate myNextPiece.
     *
     */
    public ScoreCanvas(final Timer theTimer) {
        super();
        myScore = 0;
        myCurrentLines = 0;
        myCurrentLevel = 1;
        myNextLevelCounter = 5;
        myTimer = theTimer;
        myTickRate = 1000;
    }
    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        final GradientPaint gradient = new GradientPaint(0, 0, MY_SCORE_COLORS[myTheme * 2],
                getWidth(), getHeight(), MY_SCORE_COLORS[myTheme * 2 + 1]);
        final Graphics2D graphics2D = (Graphics2D) theG;
        graphics2D.setPaint(gradient);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        drawText(graphics2D);
    }

    /**
     * Private helper method to handle all text for the ScoreCanvas.
     * @param theGraphics2D the Graphics controls.
     */
    private void drawText(final Graphics2D theGraphics2D) {
        if(myTheme == 3) {
            theGraphics2D.setColor(Color.WHITE);
        } else if(myTheme != 0){
            theGraphics2D.setColor(MY_TEXT_COLORS[myTheme]);
        } else {
            theGraphics2D.setColor(Color.BLACK);
        }
        theGraphics2D.setFont(new Font("Verdana", Font.BOLD, SIZE));

        theGraphics2D.drawString("Move Left: Left Arrow or A/a key", X, Y - (YIncrement * 10) - 5);
        theGraphics2D.drawString("Move Right: Right Arrow or D/d key", X, Y - (YIncrement * 9) - 5);
        theGraphics2D.drawString("Move Down: Down Arrow or S/s key", X, Y - (YIncrement * 8) - 5);
        theGraphics2D.drawString("Rotate cw: Up Arrow or W/w key", X, Y - (YIncrement * 7) - 5);

        theGraphics2D.drawString("Rotate ccw: Z/z key ", X, Y - (YIncrement * 6));
        theGraphics2D.drawString("Drop: Space", X, Y - (YIncrement * 5));
        theGraphics2D.drawString("Pause: P/p", X, Y - (YIncrement * 4));
        theGraphics2D.drawString("Mute / Unmute: M/m", X, Y - (YIncrement * 3));

        theGraphics2D.drawString("Current score: " + myScore, X, Y - (YIncrement * 2) + 10);
        theGraphics2D.drawString("Current lines cleared: " + myCurrentLines, X, Y - YIncrement + 10);
        theGraphics2D.drawString("Current level: " + myCurrentLevel, X, Y + 10);
        theGraphics2D.drawString("Count until next level: " + myNextLevelCounter, X, Y + YIncrement + 10);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvt) {
        if (theEvt.getPropertyName().equals(Board.PROPERTY_ROW_CLEAR)) {
            myCurrentLines = (int) theEvt.getNewValue();
        }
        if (theEvt.getPropertyName().equals(Board.PROPERTY_SCORE_UPDATE)) {
            myScore = (int) theEvt.getNewValue();
        }
        if (theEvt.getPropertyName().equals(Board.PROPERTY_NEXT_LEVEL_COUNTER)) {
            myNextLevelCounter = (int) theEvt.getNewValue();
        }
        if (theEvt.getPropertyName().equals(Board.PROPERTY_LEVEL)) {
            myCurrentLevel = (int) theEvt.getNewValue();
            myTickRate = 1000 - ((myCurrentLevel - 1) * 100);
            if (myTickRate < 10) {
                myTickRate = 10;
            }

            myTimer.setDelay(myTickRate);
        }
        repaint();
    }

    @Override
    public void setTheme(final int theTheme) {
        myTheme = theTheme;
        repaint();
    }
}
