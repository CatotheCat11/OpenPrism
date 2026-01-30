package com.google.android.glass.content;

/** Definition of platform intent actions and extras. */
public final class Intents {
    public static final String ACTION_CAMERA_BUTTON_DOWN = "com.google.android.glass.action.CAMERA_BUTTON_DOWN";
    /**
     * Broadcast action: sent when the user starts or stops wearing the device.
     * <p>
     * This is a protected intent that can only be sent by the system.
     * @see #EXTRA_IS_ON_HEAD
     */
    public static final String ACTION_ON_HEAD_STATE_CHANGED = "com.google.android.glass.action.ON_HEAD_STATE_CHANGED";
    public static final String CATEGORY_DIRECTORY = "com.google.android.glass.category.DIRECTORY";
    /**
     * Boolean extra denoting whether the user is wearing the device.
     * @see #ACTION_ON_HEAD_STATE_CHANGED
     */
    public static final String EXTRA_IS_ON_HEAD = "is_on_head";
    public static final String EXTRA_LOCKED = "locked";
    /**
     * String extra holding the file path of the picture.
     * @see android.provider.MediaStore#ACTION_IMAGE_CAPTURE
     */
    public static final String EXTRA_PICTURE_FILE_PATH = "picture_file_path";
    public static final String EXTRA_SCREEN_OFF = "screen_off";
    public static final String EXTRA_SCREEN_TURNED_ON = "screen_turned_on";
    /**
     * String extra holding the file path of the thumbnail representing a captured picture or video.
     * @see android.provider.MediaStore#ACTION_IMAGE_CAPTURE
     * @see android.provider.MediaStore#ACTION_VIDEO_CAPTURE
     */
    public static final String EXTRA_THUMBNAIL_FILE_PATH = "thumbnail_file_path";
    /**
     * String extra holding the file path of the video.
     * @see android.provider.MediaStore#ACTION_VIDEO_CAPTURE
     */
    public static final String EXTRA_VIDEO_FILE_PATH = "video_file_path";
    public static final String EXTRA_WAKE_UP_REASON = "wake_up_reason";
    public static final String EXTRA_WAKE_UP_TIME = "wake_up_time";

    private Intents() {
    }
}
