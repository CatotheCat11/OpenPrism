package com.google.android.glass.widget;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * A {@link Slider} from which slider appearances can be drawn: {@link Slider.Scroller}, {@link Slider.Determinate},
 * {@link Slider.Indeterminate}, and {@link Slider.GracePeriod}.
 * <p>
 * A {@link Slider} instance is obtained by calling the from-method on a {@link android.view.View}. A new slider appearance is
 * drawn from this instance with the corresponding start-method. A pending show request on such a
 * slider appearance is only honored as soon as the owning view has or gains focus. When the view
 * loses focus, or another slider appearance is requested, the first slider appearance is hidden,
 * and the client is responsible for requesting a re-show.
 * <p>
 * Although each slider appearance has its own state, there is only one global visual representation
 * shared between all components on the device, even between users. This approach enables smooth
 * transitions from one appearance into another. Each slider appearance is a widget, not a {@link android.view.View},
 * i.e. it cannot be inflated from a layout or added at a particular position in a view hierarchy.
 */
public class Slider {
   Slider() {
      Log.e("OpenPrism", "Stub: Slider()");
   }

   static final int GRACE_PERIOD_TIME_IN_MS = 2000;
   private GracePeriod mGracePeriod;
   private GracePeriod.Listener mGracePeriodListener;
   private Handler mHandler = new Handler(Looper.getMainLooper());
   private Runnable mGracePeriodEndRunnable;
   private ViewGroup sliderView = null;

   private Slider(View setView) {
      if (setView.getParent() instanceof ViewGroup) {
         this.sliderView = (ViewGroup) setView.getParent();
      }
   }
   /**
    * Constructs a {@link Slider} object from which different sliders can be drawn.
    * <p>
    * Drawing multiple slider appearances for the same view is done most efficiently by keeping a
    * single {@link Slider} instance around. A {@link Slider} that fails to connect to the shared visual
    * representation will only draw null sliders.
    * @param view whose focus controls whether show requests are honored, cannot be {@code null}
    */
   public static Slider from(View view) {
      Log.e("OpenPrism", "Stub: from(View)");
      return new Slider(view);
   }

   /**
    * Constructs and shows a scroller slider that indicates the current position within a fixed-size
    * collection. The slider hides automatically after a short time of inactivity.
    * @param maxPosition     maximum position within slider
    * @param initialPosition initial position of slider
    * @return                scroller slider
    */
   public Slider.Scroller startScroller(int maxPosition, float initialPosition) {
      Log.e("OpenPrism", "Stub: startScroller(int, float)");
        return new Slider.Scroller() {

           @Override
           public int getMax() {
              return 0;
           }

           @Override
           public float getPosition() {
              return 0;
           }

           @Override
           public void setPosition(float var1) {

           }

           @Override
           public void show() {

           }

           @Override
           public void hide() {

           }
        };
   }

   /**
    * Constructs and shows a determinate slider that tracks a position from left to right. Remains
    * visible until hidden, or focus of the owning {@link android.view.View} is lost.
    * @param maxPosition     maximum position within slider
    * @param initialPosition initial position of slider
    * @return                determinate slider
    */
   public Slider.Determinate startDeterminate(int maxPosition, float initialPosition) {
      Log.e("OpenPrism", "Stub: startDeterminate(int, float)");
         return new Slider.Determinate() {
            @Override
            public int getMax() {
              return 0;
           }

            @Override
            public float getPosition() {
              return 0;
           }

            @Override
            public void setPosition(float position) {

            }

            @Override
            public void show() {

            }

            @Override
            public void hide() {

            }
         };
   }

   /**
    * Constructs and shows a slider that animates from left to right during a default grace period
    * and then dismisses itself. For a non-{@code null} listener, a proper callback on cancellation or
    * completion is performed.
    * @param listener for grace period's end or cancellation, may be {@code null}
    * @return         grace period slider
    */
   public Slider.GracePeriod startGracePeriod(Slider.GracePeriod.Listener listener) {
      Log.e("OpenPrism", "Stub: startGracePeriod(Slider.GracePeriod.Listener)");
      this.mGracePeriodListener = listener;
      if (mGracePeriodEndRunnable != null) {
         mHandler.removeCallbacks(mGracePeriodEndRunnable);
      }
      mGracePeriodEndRunnable = new Runnable() {
         @Override
         public void run() {
            if (mGracePeriodListener != null) {
               mGracePeriodListener.onGracePeriodEnd();
            }
         }
      };
      mHandler.postDelayed(mGracePeriodEndRunnable, GRACE_PERIOD_TIME_IN_MS);
      this.mGracePeriod = new Slider.GracePeriod() {
         @Override
         public void cancel() {
            mHandler.removeCallbacks(mGracePeriodEndRunnable);
            if (mGracePeriodListener != null) {
               mGracePeriodListener.onGracePeriodCancel();
            }
         }
      };
      return this.mGracePeriod;
   }

   /**
    * Constructs and shows an indeterminate slider that animates continuously to indicate ongoing
    * but otherwise unknown progress. Remains visible until hidden, or focus of the owning {@link android.view.View} is
    * lost.
    * @return indeterminate slider
    */
   public Slider.Indeterminate startIndeterminate() {
      Log.e("OpenPrism", "Stub: startIndeterminate()");
         Slider.Indeterminate slider = new Slider.Indeterminate() {
            ProgressBar sliderBar = null;

            @Override
            public void show() {
               if (sliderView != null) {
                  sliderBar = new ProgressBar(sliderView.getContext(), null, android.R.attr.progressBarStyleHorizontal);
                  sliderBar.setIndeterminate(true);
                  sliderBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                  sliderBar.setVisibility(View.VISIBLE);
                  android.widget.FrameLayout.LayoutParams lp =
                          new android.widget.FrameLayout.LayoutParams(
                                  android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                  android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                                  android.view.Gravity.BOTTOM);
                  sliderView.addView(sliderBar, lp);
               }
            }

            @Override
            public void hide() {
               if (sliderBar != null) {
                  sliderBar.setVisibility(View.GONE);
               }
               if (sliderView != null) {
                  sliderView.removeView(sliderBar);
               }
            }
         };
         slider.show();
         return slider;
   }

   /**
    * An indeterminate slider that animates continuously to indicate ongoing but otherwise unknown
    * progress. Remains visible until hidden. Clients are responsible for hiding and possibly
    * re-showing the slider at proper points in the owning context' life-cycle.
    * <p>
    * Sample usage:
    * <pre>{@code
    * Indeterminate slider = Slider.from(view).startIndeterminate();
    *   // do something while animation is ongoing
    * slider.hide();
    * }</pre>
    */
   public interface Indeterminate {
      /** Shows the indeterminate slider, if not visible already. */
      void show();

      /** Hides the indeterminate slider, if not hidden already. */
      void hide();
   }

   /**
    * A slider that animates from left to right during the given grace period in timeInMs and then
    * dismisses itself after running the appropriate callback. Remains visible until grace period
    * runs out or is cancelled.
    * <p>
    * Sample usage:
    * <pre>{@code
    * GracePeriod slider = Slider.from(view).startGracePeriod(listener);
    *    // animates for timeInMs milliseconds and then dismisses itself,
    *    // unless slider.cancel() is called in the meanwhile
    * }</pre>
    */
   public interface GracePeriod {
      /**
       * Cancels grace period. If called during the grace period, dismisses the grace period. Does
       * nothing if grace period already completed normally.
       */
      void cancel();

      /** Listener call-backs. */
      public interface Listener {
         /** Called when the grace period completes normally. */
         void onGracePeriodEnd();

         /** Called when the grace period is cancelled before completion. */
         void onGracePeriodCancel();
      }
   }

   /**
    * A determinate slider that tracks a position from left to right. Remains visible until hidden.
    * Clients are responsible for hiding and possibly re-showing the slider at proper points in the
    * owning context' life-cycle.
    * <p>
    * Sample usage:
    * <pre>{@code
    * Determinate slider = Slider.from(view).startDeterminate(10, 0);
    * ....
    *   slider.setPosition(p);  // slide through p in [0,10]
    * ....
    * slider.hide();
    * }</pre>
    */
   public interface Determinate {
      /** Returns maximum value for position. */
      int getMax();

      /** Returns current position. */
      float getPosition();

      /**
       * Sets the position, typically in the range 0 and {@link #getMax()}. Values that fall outside this
       * range may be used for a tugging effect.
       */
      void setPosition(float position);

      /** Shows the determinate slider, if not visible already. */
      void show();

      /** Hides the determinate slider, if not hidden already. */
      void hide();
   }

   /**
    * A scroll slider that indicates the current position within a fixed-size collection. The slider
    * hides automatically after a short time of inactivity.
    * <p>
    * Sample usage:
    * <pre>{@code
    * Scroller slider = Slider.from(view).startScroller(10, 0);
    * ....
    *   slider.setPosition(p);  // slide through p in [0,10]
    * ....
    * }</pre>
    */
   public interface Scroller {
      /** Returns maximum value for position. */
      int getMax();

      /** Returns current position. */
      float getPosition();

      /**
       * Sets the position, typically in the range 0 and {@link #getMax()}. Brings the slider back into view
       * if hidden. Values that fall outside this range (viz. position < 0 or position > {@link #getMax()})
       * may be used for a tugging effect.
       */
      void setPosition(float position);

      /** Shows the slider, if not visible already. The slider hides automatically after a short time of inactivity. */
      void show();

      /** Hides the slider, if not hidden already. */
      void hide();
   }
}
