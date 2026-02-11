package com.google.android.glass.touchpad;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * A gesture detector optimized to recognize touch gestures for the touchpad.
 * <p>
 * There are discrete gesture listeners (e.g., {@link com.google.android.glass.touchpad.GestureDetector.BaseListener},
 * {@link com.google.android.glass.touchpad.GestureDetector.FingerListener}) and continuous gesture listeners, (e.g.,
 * {@link com.google.android.glass.touchpad.GestureDetector.ScrollListener}, {@link com.google.android.glass.touchpad.GestureDetector.OneFingerScrollListener},
 * {@link com.google.android.glass.touchpad.GestureDetector.TwoFingerScrollListener}).
 * <p>
 * For more information, see the Touch gestures developer guide.
 * <h3>General usage</h3>
 * <ol>
 *     <li>Implement any desired listener interfaces and set them on the
 *     {@code GestureDetector}. </li>
 *     <li>Override input event callbacks such as {@link android.app.Activity#onGenericMotionEvent(MotionEvent)}
 *     or {@link android.view.View#dispatchGenericMotionEvent(android.view.MotionEvent)} and pass the {@link android.view.MotionEvent} to the
 *     gesture detector's {@link #onMotionEvent(MotionEvent)} method to process the event. </li>
 *     <li>Handle the event appropriately in your gesture detector listeners. <b>Note:</b> When implementing the onXXX methods
 *     for listeners, set the return value to true only if you do not want to dispatch the MotionEvent to any other
 *     listening entities in the input dispatch pipeline. </li>
 * </ol>
 */
public class GestureDetector {
   private GestureDetector.BaseListener mBaseListener;

   public GestureDetector(Context context) {
      Log.e("OpenPrism", "Not implemented: GestureDetector(Context)");
   }

   /** Sets the basic gesture listener. */
   public GestureDetector setBaseListener(GestureDetector.BaseListener listener) {
      this.mBaseListener = listener;
      return this;
   }

   /** Sets the finger listener. */
   public GestureDetector setFingerListener(GestureDetector.FingerListener listener) {
      Log.e("OpenPrism", "Not implemented: setFingerListener(Context)");
      return this;
   }

   /** Sets the listener that detects horizontal and one finger scrolling. */
   public GestureDetector setOneFingerScrollListener(GestureDetector.OneFingerScrollListener listener) {
      Log.e("OpenPrism", "Not implemented: setOneFingerScrollListener(OneFingerScrollListener)");
      return this;
   }

   /** Sets the listener that detects horizontal scrolling independent of the finger count. */
    public GestureDetector setScrollListener(GestureDetector.ScrollListener listener) {
     Log.e("OpenPrism", "Not implemented: setScrollListener(ScrollListener)");
     return this;
   }

   /** Sets the listener that detects horizontal and two finger scrolling. */
    public GestureDetector setTwoFingerScrollListener(GestureDetector.TwoFingerScrollListener listener) {
      Log.e("OpenPrism", "Not implemented: setTwoFingerScrollListener(TwoFingerScrollListener)");
      return this;
   }

   /** Sets if the gesture detector should consume events passed to {@link #onMotionEvent(MotionEvent)}, regardless of whether they were actually handled. */
   public GestureDetector setAlwaysConsumeEvents(boolean enabled) {
      Log.e("OpenPrism", "Not implemented: setAlwaysConsumeEvents(boolean)");
      return this;
   }

   private float startTouchX;
   private float startTouchY;
   private int fingerCount = 0;
   /**
    * Processes a motion event, returning {@code true} if events should always be consumed or if a gesture was detected.
    * @return reflects whether touch event is consumed
    */
   public boolean onMotionEvent(MotionEvent event) {
      //TODO: Detect long presses
      //Log.d("OpenPrism", "onMotionEvent called");
      if (mBaseListener == null) return false;
      Gesture gesture = null;
      //Log.d("OpenPrism", "Pointer count: " + event.getPointerCount());
      //Log.d("OpenPrism", "Action:" + event.getAction());
      if (event.getPointerCount() > fingerCount) fingerCount = event.getPointerCount();
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
         startTouchX = event.getX(0);
         startTouchY = event.getY(0);
         return false;
      } else if (event.getAction() == MotionEvent.ACTION_UP && event.getPointerCount() == 1) {
         //Log.d("OpenPrism", "ACTION_UP total fingers:" + fingerCount);
         if (event.getX(0) - startTouchX > 50) {
            if (fingerCount == 1) {
               gesture = Gesture.SWIPE_RIGHT;
            } else if (fingerCount == 2) {
               gesture = Gesture.TWO_SWIPE_RIGHT;
            }
         } else if (event.getX(0) - startTouchX < -50) {
            if (fingerCount == 1) {
               gesture = Gesture.SWIPE_LEFT;
            } else if (fingerCount == 2) {
               gesture = Gesture.TWO_SWIPE_LEFT;
            }
         } else if (event.getY(0) - startTouchY > 50) {
            if (fingerCount == 1) {
               gesture = Gesture.SWIPE_DOWN;
            } else if (fingerCount == 2) {
               gesture = Gesture.TWO_SWIPE_DOWN;
            }
         } else if (event.getY(0) - startTouchY < -50) {
            if (fingerCount == 1) {
               gesture = Gesture.SWIPE_UP;
            } else if (fingerCount == 2) {
               gesture = Gesture.TWO_SWIPE_UP;
            }
         } else {
            // Treat as a tap if there was no significant movement
            if (fingerCount == 1) {
               gesture = Gesture.TAP;
            } else if (fingerCount == 2) {
               gesture = Gesture.TWO_TAP;
            } else if (fingerCount == 3) {
               gesture = Gesture.THREE_TAP;
            }
         }
         fingerCount = 0;
      }
      if (gesture != null) {
         return mBaseListener.onGesture(gesture);
      } else return false;
   }

   public boolean onKeyEvent(int keyCode) {
      if (mBaseListener == null) return false;
      Gesture gesture = null;
      switch (keyCode) {
         case KeyEvent.KEYCODE_DPAD_CENTER:
         case KeyEvent.KEYCODE_ENTER:
            gesture = Gesture.TAP;
            break;
         case KeyEvent.KEYCODE_DPAD_UP:
            gesture = Gesture.SWIPE_UP;
            break;
         case KeyEvent.KEYCODE_DPAD_DOWN:
         case KeyEvent.KEYCODE_ESCAPE:
         case KeyEvent.KEYCODE_BACK:
            gesture = Gesture.SWIPE_DOWN;
            break;
         case KeyEvent.KEYCODE_DPAD_LEFT:
         case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
            gesture = Gesture.SWIPE_LEFT;
            break;
         case KeyEvent.KEYCODE_DPAD_RIGHT:
         case KeyEvent.KEYCODE_NAVIGATE_NEXT:
            gesture = Gesture.SWIPE_RIGHT;
            break;
         default:
            Log.e("OpenPrism", "Unknown key code: " + keyCode);
      }
      if (gesture != null) {
         return mBaseListener.onGesture(gesture);
      }
      return false;
   }

   /**
    * This method only makes sense for lateral swipes and throws an exception if called on other gestures.
    * @return {@code true} if the given gesture corresponds to forward motion on the touchpad.
    */
   public static boolean isForward(Gesture gesture) {
      Log.e("OpenPrism", "Not implemented: isForward(Gesture)");
      return false;
   }

   /** @return {@code true} if the given displacement corresponds to forward motion on the touchpad. */
   public static boolean isForward(float deltaX) {
      Log.e("OpenPrism", "Not implemented: isForward(float)");
      return false;
   }

   /** This listener receives continuous two finger horizontal scrolling events. */
    public interface TwoFingerScrollListener {
      /**
       * Called while the user is scrolling with two fingers.
       * @param displacement average distance between scroll state entering x value
       * @param delta        average delta between two consecutive x motion events
       * @param velocity     average velocity of current x motion event
       * @return             {@code true} if the events were handled
       */
       boolean onTwoFingerScroll(float displacement, float delta, float velocity);
   }

   /** This listener receives continuous horizontal scrolling events independent of the finger count. */
    public interface ScrollListener {
      /**
       * Called while the user is scrolling after initial horizontal scroll.
       * @param displacement distance between current event and first down event x value
       * @param delta        delta between two consecutive x motion events
       * @param velocity     velocity of current x motion event
       * @return             {@code true} if the events were handled
       */
       boolean onScroll(float displacement, float delta, float velocity);
   }

   /**
    * This listener receives continuous one finger horizontal scrolling events.
    * <p>
    * Note that this listener will not report scrolling events if there has been two or more fingers on the touch pad after the initial down event.
    */
   public interface OneFingerScrollListener {
      /**
       * Called while the user is scrolling after initial horizontal scroll.
       * @param displacement distance between scroll state entering x value
       * @param delta        delta between two consecutive x motion events
       * @param velocity     velocity of current x motion event
       * @return             {@code true} if the events were handled
       */
      boolean onOneFingerScroll(float displacement, float delta, float velocity);
   }

   /** This listener reports when the detected finger count changes on the touchpad. */
   public interface FingerListener {
      /** Called when the finger count changes. */
       void onFingerCountChanged(int previousCount, int currentCount);
   }

   /** Receives detection results. This listener receives discrete gestures: TAP, LONG_PRESS SWIPE_UP, SWIPE_LEFT, SWIPE_RIGHT, SWIPE_DOWN */
   public interface BaseListener {
      /**
       * Called when a gesture was recognized.
       * <p>
       * Only one gesture will be recognized per event sequence.
       * @param gesture the detected gesture
       * @return        {@code true} if the gesture was handled
       */
      boolean onGesture(Gesture gesture);
   }
}
