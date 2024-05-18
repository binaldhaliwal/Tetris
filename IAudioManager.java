package view;

import java.io.File;

/**
 * Interface for the AudioManager.
 * @author Corey Young
 * @version 1.0
 */
public interface IAudioManager {

    /**
     * Makes initial call to play default background music.
     */
    void playBGMusic();

    /**
     * Pauses the the background music if it is playing and saves the location
     * the music was paused at.
     */
    void pauseBGMusic();

    /**
     * Resumes the background music from where it was last paused at if it is
     * currently paused and is not muted. If it is muted, it must first be
     * unmuted to resume.
     */
    void resumeBGMusic();

    /**
     * Changes the background music to a new song.
     * @param thePathName the File path of the new song.
     */
    void changeBGMusic(String thePathName);

    /**
     * Plays a provided sound effect.
     * @param thePathName File path of the provided sound effect.
     */
    void playSFX(File thePathName);

    /**
     * If the background music is running, mutes background music. If the background
     * music is currently muted, then unmutes music and resumes from where it
     * was last stopped at.
     */
    void muteBGMusic();

    /**
     * Returns whether the background music is currently running.
     * @return whether the background music is currently running.
     */
    boolean isBGMusicRunning();

    /**
     * Helper method that exists in case a user wants to play a new clip.
     * @param thePathName File path of music to be played.
     * @param theLoop int value to represent looping status.
     */
    void playMusic(File thePathName, int theLoop);

    /**
     * Sets background music to Easter Egg.
     */
    void egg();
}
