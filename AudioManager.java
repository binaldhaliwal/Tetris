package view;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Singleton class to manage all sounds played. Controls background music, sfx,
 * pause/play status, and muted/unmuted status.
 * @author Corey Young
 * @version 1.0
 */
// Simplification of Error Handling: decided to simplify error handling by catching a generic
// Exception rather than handling multiple specific exceptions individually. The focus is on
// simplicity, and detailed exception handling is not critical.
//@SuppressWarnings("CheckStyle")
@SuppressWarnings("CheckStyle")
public final class AudioManager implements IAudioManager {
    /**
     * This represents the property name for a change in the mute status.
     */
    public static final String PROPERTY_MUTE = "Mute";
    /**
     * Implements singleton design pattern.
     */
    private static final AudioManager INSTANCE = new AudioManager();
    /**
     * Logger for logging messages.
     */
    private static final Logger LOGGER = Logger.getLogger(AudioManager.class.getName());
    /**
     * The Background music clip.
     */
    private Clip myBackgroundMusicClip;
    /**
     * Where the Background music was last paused at.
     */
    private long myClipPosition;
    /**
     * Boolean whether the background music is muted.
     */
    private boolean myMute;


    /**
     * Private constructor to prevent instantiation
     */
    private AudioManager() {
        super();
    }

    /**
     * This constructor returns the singleton instance of the AudioManager class.
     *
     * @return returns the singleton instance of the AudioManager.
     */
    public static AudioManager getInstance() {
        return INSTANCE;
    }
    /**
     * Makes initial call to play default background music.
     */
    @Override
    public void playBGMusic() {
        try {
            final File path = new File("./audio/theme.wav");
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(path);
            final Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            myBackgroundMusicClip = clip;
            audioStream.close();
        } catch (final Exception e) {
            LOGGER.log(Level.INFO, "BG Music unavailable");
        }
    }

    /**
     * Pauses the the background music if it is playing and saves the location
     * the music was paused at.
     */
    @Override
    public void pauseBGMusic() {
        if (myBackgroundMusicClip != null && myBackgroundMusicClip.isRunning()) {
            myClipPosition = myBackgroundMusicClip.getMicrosecondPosition();
            myBackgroundMusicClip.stop();
        }
    }

    /**
     * Resumes the background music from where it was last paused at if it is
     * currently paused and is not muted. If it is muted, it must first be
     * unmuted to resume.
     */
    @Override
    public void resumeBGMusic() {
        if (myBackgroundMusicClip != null && !myMute && !myBackgroundMusicClip.isRunning()) {
            myBackgroundMusicClip.setMicrosecondPosition(myClipPosition);
            myBackgroundMusicClip.start();
        }
    }

    /**
     * Changes the background music to a new song.
     * @param thePathName the File path of the new song.
     */
    @Override
    public void changeBGMusic(final String thePathName) {
        try {
            pauseBGMusic();
            final File myMusic = new File(thePathName);
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(myMusic);
            final Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            myBackgroundMusicClip = clip;
            myClipPosition = 0;
            audioStream.close();
        } catch (final Exception e) {
            LOGGER.log(Level.INFO, "New BG Music unavailable");
        }
    }

    /**
     * Plays a provided sound effect.
     * @param thePathName File path of the provided sound effect.
     */
    @Override
    public void playSFX(final File thePathName) {
        try {
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(thePathName);
            final Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            audioStream.close();
        } catch (final Exception e) {
            LOGGER.log(Level.INFO, "SFX unavailable");
        }
    }

    /**
     * If the background music is running, mutes background music. If the background
     * music is currently muted, then unmutes music and resumes from where it
     * was last stopped at.
     */
    @Override
    public void muteBGMusic() {
        if (myBackgroundMusicClip != null) {
            if (myBackgroundMusicClip.isRunning()) {
                myMute = true;
                pauseBGMusic();
            } else {
                myMute = false;
                resumeBGMusic();
            }
        }
    }

    /**
     * Returns whether the background music is currently running.
     * @return whether the background music is currently running.
     */
    @Override
    public boolean isBGMusicRunning() {
        return myBackgroundMusicClip != null && myBackgroundMusicClip.isRunning();
    }

    /**
     * Helper method that exists in case a user wants to play a new clip.
     * @param thePathName File path of music to be played.
     * @param theLoop int value to represent looping status.
     */
    @Override
    public void playMusic(final File thePathName, final int theLoop) {
        try {
            final AudioInputStream audioStream = AudioSystem.getAudioInputStream(thePathName);
            final Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(theLoop);
            clip.start();
            audioStream.close();
        } catch (final Exception e) {
            LOGGER.log(Level.INFO, "Music unavailable");
        }
    }

    /**
     * Sets background music to Easter Egg.
     */
    @Override
    public void egg() {
        try {
            changeBGMusic("./audio/egg.wav");
        } catch (final Exception ignored) { }
    }
}
