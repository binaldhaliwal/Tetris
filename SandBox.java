package edu.uw.tcss.app;
import java.util.logging.Logger;
import view.GUI;



/**
 * Test class to practice git with a team.
 * TCSS 305 - Project Tetris
 *
 * @author Jonathan Kim, Binal Dhaliwal, Eric John, Corey Young.
 * @version 11/14/2023
 */
public final class SandBox {
    /**
     * A logger object to use instead of System.out.println as
     * the logging method.
     */
    public static final Logger LOGGER = Logger.getLogger(SandBox.class.getName());

    private SandBox() {
        super();
    }

    /**
     * A method that logs stuff in the console.
     * @param theArgs is some arguments
     */
    public static void main(final String[] theArgs) {
        new GUI();
    }
}
