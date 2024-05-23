package view;
import com.formdev.flatlaf.FlatDarculaLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import model.Board;
import model.IBoard;

/**
 * Created a GUI class for interactive user interactions. KeyListener interface is
 * implemented to handle keyboard events. It controls how the player and the game board
 * interact as well as the status of the game.
 *
 * @author kimjona
 * @author Eric
 * @author Corey
 * @author binaldhaliwal
 * @version 1.0
 */
// Simplification of Error Handling: decided to supress warnings for magic numbers since this
// is used for the layout of the panels. Along with that, warnings for long lambda expressions
// that shouldn't be simplified should be ignored.
//@SuppressWarnings("CheckStyle")
// Splitting the method into multiple smaller methods to eliminate same return values might
// make the code more complex or less readable.
//@SuppressWarnings("SameReturnValue")
// The methods are closely related and contribute to the initialization and control flow of the GUI.
// Extracting them into separate methods might break the logical flow and make it harder to understand
// the initialization process.
//@SuppressWarnings("ExtractMethodRecommender")
// The method is responsible for setting up the graphical user interface (GUI) for a Tetris game.
// While it is relatively lengthy, the complexity of GUI initialization often leads to longer methods
//@SuppressWarnings("OverlyLongMethod")
// The lambda expression involves conditional logic that toggles between starting a new game
// and ending the current game. It also interacts with several components like myTimer,
// frame, menuFileItemNewGame, panelLeft, and myBoard. Given the complexity of the
// logic, it seems reasonable to have a lambda expression of this length.
//@SuppressWarnings( "OverlyLongLambda")
@SuppressWarnings({"CheckStyle", "SameReturnValue", "ExtractMethodRecommender", "OverlyLongMethod", "OverlyLongLambda"})
public class GUI extends KeyAdapter implements KeyListener {
    /**
     * This is the tick rate for the Timer in milliseconds.
     */
    public static final int TICK_RATE = 1000;
    /**
     * This is a constant for panel sizing.
     */
    public static final double HALF = 0.5;
    /**
     * A string that says the text "Grid on" to display the grid.
     */
    private static final String GRID = "Grid on";
    /**
     * This is the primary model object implementing the IBoard interface.
     */
    private final IBoard myBoard = Board.getInstance();
    /**
     * This is the main audio control object implementing the IAudioManager interface.
     */
    private final IAudioManager mySound = AudioManager.getInstance();
    /**
     * This is a Timer to control the game ticks.
     */
    private final Timer myTimer;

    /**
     * Boolean status whether the game is going on or not.
     */
    private boolean myGameOver = true;



    /**
     * This constructor creates a new GUI instance and initializes the frame, panels,
     * and menu for the Tetris game.
     */
    public GUI() {
        super();
        System.setProperty("flatlaf.menuBarEmbedded", "false");
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (final Exception ignored) {
        }
        final ActionListener tickListener = e -> myBoard.step();
        myTimer = new Timer(TICK_RATE, tickListener);

        final int frameWidth = 500;
        final int frameHeight = 560;
        final int contentHeight = 500;
        final int HALF_FRAME = (int) Math.floor(frameWidth * HALF);
        final JFrame frame = new JFrame("Tetris");
        frame.setLocationRelativeTo(null);
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        final MainCanvas panelLeft = new MainCanvas();
        myBoard.addPropertyChangeListener(panelLeft);

        panelLeft.setPreferredSize(new Dimension(HALF_FRAME, contentHeight));
        frame.add(panelLeft, BorderLayout.WEST);

        final JPanel panelRight = new JPanel();
        panelRight.setBackground(Color.PINK);
        panelRight.setBounds(0, 0, HALF_FRAME, contentHeight);
        final BoxLayout boxLayout = new BoxLayout(panelRight, BoxLayout.Y_AXIS);
        panelRight.setLayout(boxLayout);
        panelRight.setPreferredSize(new Dimension(HALF_FRAME, contentHeight));
        frame.add(panelRight, BorderLayout.EAST);

        final NextTetrisCanvas nextTetris = new NextTetrisCanvas();
        myBoard.addPropertyChangeListener(nextTetris);
        panelRight.add(nextTetris, boxLayout);

        final ScoreCanvas scorePanel = new ScoreCanvas(myTimer);
        myBoard.addPropertyChangeListener(scorePanel);
        panelRight.add(scorePanel, boxLayout);



        final JMenuBar menuBar = new JMenuBar();
        final JMenu menuFile = new JMenu("File");
        final JMenu scoreFile = new JMenu("Info");
        menuBar.add(menuFile);
        menuBar.add(scoreFile);

        final JMenuItem menuFileItemNewGame = new JMenuItem("New Game");
        menuFileItemNewGame.addActionListener(e -> {
            if (myTimer.isRunning()) {
                myGameOver = true;
                myTimer.stop();
                JOptionPane.showMessageDialog(frame, "You have ended the game. "
                        +  "Start a new game to restart!");
                menuFileItemNewGame.setText("New game");
                panelLeft.endGame();
            } else {
                myGameOver = false;
                JOptionPane.showMessageDialog(frame, "The game is starting!");
                menuFileItemNewGame.setText("End game");
                myBoard.newGame();
                myTimer.start();
                panelLeft.removeEndMsg();
            }
        });
        menuFile.add(menuFileItemNewGame);

        final JMenuItem menuFileItemExit = new JMenuItem("Exit");
        menuFileItemExit.addActionListener(e -> System.exit(0));
        menuFile.add(menuFileItemExit);

        final JMenuItem menuFileItemAbout = new JMenuItem("About ");
        menuFileItemAbout.addActionListener(
                e -> JOptionPane.showMessageDialog(frame,
                        """
                                Tetris by group 6:
                                 Jonathan Kim
                                 Corey Young
                                 Eric John
                                 Binal Dhaliwal
                                 
                                Background music:
                                 https://archive.org/details/TetrisThemeMusic""",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE)
        );
        menuFile.add(menuFileItemAbout);
        final JMenuItem menuFileItemMute = new JMenuItem("Mute ");
        menuFileItemMute.addActionListener(e -> mute(mySound, menuFileItemMute));
        menuFile.add(menuFileItemMute);

        final JMenuItem menuFileItemTheme = new JMenu("Theme");
        final JMenuItem menuFileItemDevelopment = new JMenuItem("Development");
        final JMenuItem menuFileItemClassic = new JMenuItem("Classic");
        final JMenuItem menuFileItemHoliday = new JMenuItem("Holiday");
        final JMenuItem menuFileItemWinter = new JMenuItem("Winter");

        menuFileItemTheme.add(menuFileItemDevelopment);
        menuFileItemTheme.add(menuFileItemClassic);
        menuFileItemTheme.add(menuFileItemHoliday);
        menuFileItemTheme.add(menuFileItemWinter);
        menuFile.add(menuFileItemTheme);

        menuFileItemDevelopment.addActionListener(e -> {
            setTheme(panelLeft, nextTetris, scorePanel, 0, mySound);
            JOptionPane.showMessageDialog(frame, "Play in the unrefined development "
                   + "& testing theme!");
        });
        menuFileItemClassic.addActionListener(e -> {
            setTheme(panelLeft, nextTetris, scorePanel, 1, mySound);
            JOptionPane.showMessageDialog(frame, "Play in the classic Tetris theme!");
        });
        menuFileItemHoliday.addActionListener(e -> {
            setTheme(panelLeft, nextTetris, scorePanel, 2, mySound);
            JOptionPane.showMessageDialog(frame, "Enjoy the cozy feeling of"
                    + " a Christmas Tree!");

        });
        menuFileItemWinter.addActionListener(e -> {
            setTheme(panelLeft, nextTetris, scorePanel, 3, mySound);
            JOptionPane.showMessageDialog(frame, "Face the frozen wastes of "
                    + "a winter wonderland!");
        });
        final JMenuItem menuFileItemGrid = new JMenuItem(GRID);
        menuFileItemGrid.addActionListener(e -> gridOnOff(panelLeft, menuFileItemGrid));
        menuFile.add(menuFileItemGrid);

        final JMenuItem menuInfoControls = new JMenuItem("Controls ");
        menuInfoControls.addActionListener(
                e -> controlPane(frame, myTimer));
        scoreFile.add(menuInfoControls);
        final JMenuItem menuInfoScoring = new JMenuItem("Scoring ");
        menuInfoScoring.addActionListener(
                e -> scorePane(frame, myTimer));
        scoreFile.add(menuInfoScoring);
        final JMenuItem menuInfoEgg = new JMenuItem("Click me! ");
        menuInfoEgg.addActionListener(
                e -> mySound.egg());
        scoreFile.add(menuInfoEgg);
        frame.addKeyListener(new ControlKeyListener(menuFileItemMute, menuFileItemGrid));
        frame.pack();
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
        try {
            mySound.playBGMusic();
        } catch (final Exception ignored) {
        }

    }

    /**
     * Displays the information about the controls of the game.
     * @param theFrame - The frame to set it to.
     * @param theTimer - The timer to stop or resume the falling blocks.
     */
    private static void controlPane(final JFrame theFrame, final Timer theTimer) {
        if (theTimer.isRunning()) {
            theTimer.stop();
            JOptionPane.showMessageDialog(theFrame, showControls());
            theTimer.start();
        } else {
            JOptionPane.showMessageDialog(theFrame, showControls());
        }
    }

    /**
     * Displays the information about the scoring of the game.
     * @param theFrame - The frame to set it to.
     * @param theTimer - The timer to stop or resume the falling blocks.
     */
    private static void scorePane(final JFrame theFrame, final Timer theTimer) {
        if (theTimer.isRunning()) {
            theTimer.stop();
            JOptionPane.showMessageDialog(theFrame, showScoring());
            theTimer.start();
        } else {
            JOptionPane.showMessageDialog(theFrame, showScoring());
        }
    }

    /**
     * A helper method that sets the theme of the game.
     *
     * @param theMC    - The Main Canvas.
     * @param theNTC   - The Next Tetris Canvas.
     * @param theSC    - The Score Canvas.
     * @param theThm   - The number for the theme.
     * @param theSound - The audioManager.
     */
    private static void setTheme(final MainCanvas theMC, final NextTetrisCanvas theNTC,
                                 final ScoreCanvas theSC, final int theThm, final IAudioManager theSound) {

        theMC.setTheme(theThm);
        theNTC.setTheme(theThm);
        theSC.setTheme(theThm);
        if(theThm < 2) {
            theSound.changeBGMusic("./audio/theme.wav");
        } else if(theThm == 2) {
            theSound.changeBGMusic("./audio/holiday.wav");
        } else if(theThm == 3){
            theSound.changeBGMusic("./audio/winter.wav");
        }

    }

    /**
     * Helper method that allows music to be paused and cleans up GUI calls.
     * @param theSound The audioManager controling game audio.
     * @param theMuteItem the FileMenuItem that updates based on mute status.
     */
    private static void mute(final IAudioManager theSound, final JMenuItem theMuteItem) {
        theSound.muteBGMusic();
        if (theSound.isBGMusicRunning()) {
            theMuteItem.setText("Mute");
        } else {
            theMuteItem.setText("Unmute");
        }
    }
    private static void gridOnOff(final MainCanvas theCanvas, final JMenuItem theGridItem) {
        if (GRID.equals(theGridItem.getText())) {
            theGridItem.setText("Grid off");
            theCanvas.setGrid(true);
        } else {
            theGridItem.setText(GRID);
            theCanvas.setGrid(false);
        }
    }

    /**
     * A String method that returns the controls of the Tetris game.
     * @return The controls of the game.
     */
    private static String showControls() {
        return                                 """
                                        Controls for the game:
                                         Move Left: Left Arrow or A/a key.
                                         Move Right: Right Arrow or D/d key.
                                         Move Down: Down Arrow or S/s key.
                                         Rotate cw: Up Arrow or W/w key.
                                         Rotate ccw: Z/z key.
                                         Drop: Space.
                                         Pause: P/p.
                                         Mute On/Off: M/m.
                                         Grid On/Off: G/g.""";
    }

    /**
     * A String method that returns the scoring of the Tetris game.
     *
     * @return The scoring of the game.
     */
    private static String showScoring() {
        return                         """
                                Scoring for the game:
                                 +4 Points for the frozen block.
                                 +40 Points for 1 line cleared at once.
                                 +100 Points for 2 lines cleared at once.
                                 +300 Points for 3 lines cleared at once.
                                 +1200 Points for 4 lines cleared at once.
                                 Score gets timed by the current level you're at.""";
    }


    /**
     * This ActionListener performs the tick action.
     */
  //  private final ActionListener myTickListener = e -> myBoard.step();


    class ControlKeyListener extends KeyAdapter {
        /**
         * Map that contains mappings from key codes to actions to perform when
         * that key is pressed.
         */
        private final Map<Integer, Runnable> myKeyMappings;
        /**
         * JMenuItem representing the mute option in the File menu.
         */
        private final JMenuItem myMenuFileItemMute;
        /**
         * JMenuItem representing the mute option in the File menu.
         */
        private final JMenuItem myMenuFileItemGrid;

        ControlKeyListener(final JMenuItem theMuteItem, final JMenuItem theGridItem) {
            super();
            myKeyMappings = new HashMap<>();
            mapKeys();
            myMenuFileItemMute = theMuteItem;
            myMenuFileItemGrid = theGridItem;
        }

        private void mapKeys() {
            myKeyMappings.put(KeyEvent.VK_W, myBoard::rotateCW);
            myKeyMappings.put(KeyEvent.VK_UP, myBoard::rotateCW);
            myKeyMappings.put(KeyEvent.VK_S, myBoard::down);
            myKeyMappings.put(KeyEvent.VK_DOWN, myBoard::down);
            myKeyMappings.put(KeyEvent.VK_A, myBoard::left);
            myKeyMappings.put(KeyEvent.VK_LEFT, myBoard::left);
            myKeyMappings.put(KeyEvent.VK_D, myBoard::right);
            myKeyMappings.put(KeyEvent.VK_RIGHT, myBoard::right);
            myKeyMappings.put(KeyEvent.VK_SPACE, myBoard::drop);
            myKeyMappings.put(KeyEvent.VK_Z, myBoard::rotateCCW);
            myKeyMappings.put(KeyEvent.VK_M, this::mute);
            myKeyMappings.put(KeyEvent.VK_G, this::grid);
        }

        /**
         * Helper method that forces all audio mute calls to go through the
         * menuFileItem. This causes the menuFileItemMute to be updated when
         * mute is called by the M key.
         */
        private void mute() {
            myMenuFileItemMute.doClick();
        }
        /**
         * Helper method that forces all grid calls to go through the
         * menuFileItem. This causes the menuFileItemGrid to be updated when
         * grid is called by the G key.
         */
        private void grid() {
            myMenuFileItemGrid.doClick();
        }

        @Override
        public void keyPressed(final KeyEvent theE) {
            final int keyCode = theE.getKeyCode();
            if (!myGameOver) {
                if (theE.getKeyCode() == KeyEvent.VK_P) {
                    if (myTimer.isRunning()) {
                        myTimer.stop();
                        try {
                            mySound.pauseBGMusic();
                        } catch (final Exception ignored) {
                        }
                    } else {
                        myTimer.start();
                        try {
                            mySound.resumeBGMusic();
                        } catch (final Exception ignored) {
                        }
                    }
                } else if (myKeyMappings.containsKey(keyCode) && myTimer.isRunning()) {
                    myKeyMappings.get(keyCode).run();
                }
            }
        }
    }

}
