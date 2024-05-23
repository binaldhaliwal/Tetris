package view;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import model.Board;
import model.Point;
import model.TetrisPiece;

/**
 * NextTetris shows the T tetromino centered inside the blue(next tetris piece) region.
 *
 * @author binaldhaliwal
 * @version 1.0
 */
public class NextTetrisCanvas extends JPanel implements PropertyChangeListener, ITheme {
    /**
     * Float that sets the stroke(line thickness).
     */
    public static final float WIDTH1 = 2.0f;
    /**
     * The Tetris piece to be dispalyed.
     */
    private TetrisPiece myNextPiece;
    /**
     * Int representation of the current theme.
     */
    private int myTheme;
    /**
     * Constructor to instantiate myNextPiece.
     */
    public NextTetrisCanvas() {
        super();
        myTheme = 1;
    }
    @Override
    protected void paintComponent(final Graphics theG) {
        super.paintComponent(theG);
        final Graphics2D graphics2D = (Graphics2D) theG;
        setupGradientPaint(graphics2D);
        drawNextPiece(graphics2D);
    }

    /**
     * Draws the next Tetris piece on the graphics context.
     *
     * @param theGraphics2D The Graphics2D context on which to draw the Tetris piece.
     */
    private void drawNextPiece(final Graphics2D theGraphics2D) {
        if (myNextPiece != null) {
            final int width = getWidth();
            final int height = getHeight();
            final Point[] points = myNextPiece.getPoints();
            final int size = 25;
            int offX = (width - (myNextPiece.getWidth() * size)) / 2;
            int offY = (height - (myNextPiece.getHeight() * size)) / 2;
            if (myNextPiece == TetrisPiece.O) {
                offX -= size / 2;
            } else if (myNextPiece == TetrisPiece.I) {
                offY += size;
            }
            for (final Point point : points) {
                final int x = (point.x() * size) + offX;
                final int y = size * (myNextPiece.getHeight() - point.y()) + offY;
                theGraphics2D.setPaint(Color.WHITE);
                theGraphics2D.fill(new Rectangle2D.Double(x, y, size, size));
                theGraphics2D.setStroke(new BasicStroke(WIDTH1));
                theGraphics2D.setPaint(Color.BLACK);
                theGraphics2D.draw(new Rectangle2D.Double(x, y, size, size));
            }
        }
    }

    /**
     * Sets up a gradient paint on the graphics context.
     *
     * @param theGraphics2D The Graphics2D context on which to set up the gradient paint.
     */
    private void setupGradientPaint(final Graphics2D theGraphics2D) {
        final GradientPaint gradient = new GradientPaint(0, 0, MY_NEXT_BG_COLORS[myTheme * 2],
                getWidth(), getHeight(), MY_NEXT_BG_COLORS[myTheme * 2 + 1]);
        theGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        theGraphics2D.setPaint(gradient);
        theGraphics2D.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvt) {
        if (theEvt.getPropertyName().equals(Board.PROPERTY_NEXT_PIECE)) {
            myNextPiece = (TetrisPiece) theEvt.getNewValue();
            repaint();
        }
        if (theEvt.getPropertyName().equals(Board.PROPERTY_GAME_OVER)) {
            repaint();
        }
    }

    @Override
    public void setTheme(final int theTheme) {
        myTheme = theTheme;
        repaint();
    }
}
