package com.google.android.glass.media;

import android.media.AudioManager;

/**
 * Extra constants for Glass-specific sounds effects.
 * <p>
 * This set extends the existing {@link android.media.AudioManager} constants with Glass-specific sounds.
 *
 * <h3>General usage</h3>
 * To play a sound, call {@link android.media.AudioManager#playSoundEffect(int)} with the desired sound.
 * For example, to play the {@link #TAP} sound:
 * <pre>{@code
 * AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
 * audio.playSoundEffect(Sounds.TAP);
 * }</pre>
 */
public class Sounds {
    /**
     * User tried a disallowed action.
     * @see android.media.AudioManager#playSoundEffect(int)
     */
    public static final int DISALLOWED = 10;
    /**
     * User dismissed an item.
     * @see android.media.AudioManager#playSoundEffect(int)
     */
    public static final int DISMISSED = 15;
    /**
     * An error occurred.
     * @see android.media.AudioManager#playSoundEffect(int)
     */
    public static final int ERROR = 11;
    public static final int NOTIFICATION = 18;
    public static final int NOTIFICATION_MULTIPLE = 19;
    /**
     * An item became selected.
     * @see android.media.AudioManager#playSoundEffect(int)
     */
    public static final int SELECTED = 14;
    /**
     * An action completed successfully.
     * @see android.media.AudioManager#playSoundEffect(int)
     */
    public static final int SUCCESS = 12;
    /**
     * User tapped on item.
     * @see android.media.AudioManager#playSoundEffect(int)
     */
    public static final int TAP = AudioManager.FX_KEY_CLICK;
    public static final int VOICE_COMPLETED = 16;
    public static final int VOICE_PENDING = 17;

    private Sounds() {
    }
}
