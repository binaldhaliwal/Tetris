package view;

import java.awt.Color;

/**
 * Functional Interface for all panels that can have their theme set. Additionally
 * stores all colors needed.
 *
 * @author Corey Young
 * @version 1.0
 */
@FunctionalInterface
public interface ITheme {
    /**
     * An indigo color.
     */
    Color INDIGO = new Color(75, 0, 130);
    /**
     * A light purple color.
     */
    Color LIGHT_PURPLE = new Color(110, 97, 171);
    /**
     * Gold color.
     */
    Color GOLD = new Color(212, 175, 55);
    /**
     * Cardinal color.
     */
    Color CARDINAL = new Color(196, 30, 58);
    /**
     * Cherry color.
     */
    Color CHERRY = new Color(210, 4, 45);
    /**
     * Chestnut color.
     */
    Color CHESTNUT = new Color(149, 69, 53);
    /**
     * A color that could be described as pale green.
     */
    Color PALE_GREEN = new Color(65, 140, 65);
    /**
     * A color that could be described as lime green.
     */
    Color LIME = new Color(55, 200, 55);
    /**
     * A color that could be described as bright green.
     */
    Color BRIGHT_GREEN = new Color(60, 215, 60);
    /**
     * A color that could be described as dark green.
     */
    Color DARK_GREEN = new Color(5, 90, 5);
    /**
     * A color that could be described as pine green.
     */
    Color PINE_GREEN = new Color(5, 120, 5);
    /**
     * A color that could be described as medium green.
     */
    Color MEDIUM_GREEN = new Color(35, 125, 35);
    /**
     * A color that could be described as grass green.
     */
    Color GRASS_GREEN = new Color(15, 160, 15);
    /**
     * A pale blue color in the ice theme.
     */
    Color ICE_1 = new Color(112, 225, 225);
    /**
     * A pale blue color in the ice theme.
     */
    Color ICE_2 = new Color(133, 229, 229);
    /**
     * A pale blue color in the ice theme.
     */
    Color ICE_3 = new Color(154, 234, 234);
    /**
     * A pale blue color in the ice theme.
     */
    Color ICE_4 = new Color(175, 238, 238);
    /**
     * A pale blue color in the ice theme.
     */
    Color ICE_5 = new Color(196, 242, 242);
    /**
     * A pale blue color in the ice theme.
     */
    Color ICE_6 = new Color(217, 247, 247);
    /**
     * A pale blue color in the ice theme.
     */
    Color ICE_7 = new Color(238, 251, 251);
    /**
     * An array of colors to be used for text.
     */
    Color[] MY_TEXT_COLORS = new Color[]{Color.WHITE, GOLD, Color.YELLOW, INDIGO};

    /**
     * An array of colors to be used as game backgrounds.
     */
    Color[] MY_BG_COLORS = new Color[]{Color.RED, Color.YELLOW,
        Color.DARK_GRAY, Color.BLUE, CARDINAL, CHESTNUT, Color.WHITE, Color.CYAN};
    /**
     * An array of colors to be used as screen backgrounds.
     */
    Color[] MY_NEXT_BG_COLORS = new Color[]{Color.BLUE, Color.MAGENTA,
        Color.BLUE, Color.DARK_GRAY, GRASS_GREEN, PINE_GREEN, Color.CYAN, Color.BLUE};
    /**
     * An array of colors to be used as score background.
     */
    Color[] MY_SCORE_COLORS = new Color[]{Color.GREEN, Color.CYAN,
        Color.DARK_GRAY, Color.BLUE, LIME, DARK_GREEN, Color.BLUE, Color.CYAN};


    /**
     * Sets the theme to a new theme, uses ints to represent themes.
     * @param theTheme the new theme
     */
    void setTheme(int theTheme);
}
