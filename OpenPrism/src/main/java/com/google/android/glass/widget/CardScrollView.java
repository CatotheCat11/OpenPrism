package com.google.android.glass.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.google.android.glass.R;

/**
 * A {@link android.view.View} that shows horizontally scrolling children views, referred to as
 * cards.
 * <p>
 * The cards come from the {@link com.google.android.glass.widget.CardScrollAdapter} that is associated with the {@code CardScrollView}. Each card
 * visually represents a certain {@link java.lang.Object} item.
 * <h3>General usage</h3>
 * <ol>
 *     <li>Build a set of cards using a standard view hierarchy or the {@link com.google.android.glass.widget.CardBuilder} class.</li>
 *     <li>Implement a {@link com.google.android.glass.widget.CardScrollAdapter} to supply the set of cards to the {@code CardScrollView}. Each card has a unique {@link java.lang.Object} identifier and represents a certain {@link java.lang.Object} item. </li>
 *     <li>Set your activity's content view to be the {@link CardScrollView} or use the {@code CardScrollView} in a layout. </li>
 * </ol>
 * <h3>Handling scrolling and interaction</h3>
 * {@code CardScrollView} notifies you with the following listener interfaces that are inherited from {@link android.widget.AdapterView}:
 * <ul>
 *     <li>{@link android.widget.AdapterView.OnItemSelectedListener} - An item is <i>selected</i> after the user finishes
 *     scrolling through the list and settles on an item.</li>
 *     <li>{@link android.widget.AdapterView.OnItemClickListener} - An item is <i>clicked</i> when the user taps and releases on an item that is <i>selected.</i></li>
 *     <li>{@link android.widget.AdapterView.OnItemLongClickListener} - An item is <i>long-clicked</i> when the user taps, holds, and releases on an item that is <i>selected.</i></li>
 * </ul>
 */
public class CardScrollView extends HorizontalScrollView {
   private CardScrollAdapter adapter;
   private LinearLayout container;
   private int currentIndex = 0;
   private float startTouchX;
   private float startTouchY;

   public CardScrollView(Context context) {
      super(context);
      init(context);
   }

   public CardScrollView(Context context, AttributeSet attrs) {
      super(context, attrs);
      init(context);
   }

   public CardScrollView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      init(context);
   }

   private void init(Context context) {
      setFocusable(true);
      setFocusableInTouchMode(true);
      requestFocus();
      if (((android.app.Activity) getContext()).getActionBar() != null) {
         ((android.app.Activity) getContext()).getActionBar().hide();
      }
      setSystemUiVisibility(
          View.SYSTEM_UI_FLAG_IMMERSIVE
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_FULLSCREEN);

      setHorizontalScrollBarEnabled(true);
      container = new LinearLayout(context);
      container.setOrientation(LinearLayout.HORIZONTAL);
      addView(container, new LayoutParams(
              LayoutParams.MATCH_PARENT,
              LayoutParams.MATCH_PARENT
      ));
   }

   /** Tells the {@link CardScrollView} to activate and be ready for display. TODO(ajcbik): investigate if we can replace [de]activate with focus events. */
    public void activate() {
      Log.e("OpenPrism", "Not implemented: activate()");
   }

   /** Tells the {@link CardScrollView} to deactivate; it will not be displayed until after {@link #activate()} is called again. */
   public void deactivate() {
      Log.e("OpenPrism", "Not implemented: deactivate()");
   }

   /**
    * Animates for card at given position, where {@code animationType} should be one of:
    * <ul>
    *     <li>{@link CardScrollView.Animation#NAVIGATION}: moves to given card from any position, regular navigation, </li>
    *     <li>{@link CardScrollView.Animation#INSERTION}: moves to given card, which appears into view, after insertion, </li>
    *     <li>{@link CardScrollView.Animation#DELETION}: given card disappears from view, if selected, after deletion. </li>
    * </ul>
    * The animation is skipped if the scroller is deactivated, if a prior autonomous animation is
    * still ongoing, or if the position is invalid. A deletion animation only occurs if the deleted
    * card is currently selected.
    * <p>
    * The insertion and deletion animation should be used after doing the corresponding mutation in
    * {@link com.google.android.glass.widget.CardScrollAdapter}. The animation will call the {@code notifyDataSetChanged()} on the adapter at the
    * proper moment in the animation, even when skipped or terminated early.
    * <p>
    * Sample usage:
    * <pre>{@code
    * .... insert card at p-th position in adapter ....
    * animate(p, INSERTION);    // calls notifyDataSetChanged() on adapter
    * }</pre>
    * @param position      of the card involved in the animation
    * @param animationType controls the type of animation to use
    * @return              whether animation was started
    */
   public boolean animate(int position, CardScrollView.Animation animationType) {
      if (animationType == CardScrollView.Animation.NAVIGATION) {
         // Smooth scroll to the position
         if (position >= 0 && position < adapter.getCount()) {
            currentIndex = position;
            smoothScrollTo(position * getWidth(), 0);
            performItemSelected();
            return true;
         }
      } else if (animationType == CardScrollView.Animation.INSERTION || animationType == CardScrollView.Animation.DELETION) {
         // No animations implemented for now
         rebuild();
      }
      return false;
   }

   public CardScrollAdapter getAdapter() {
      return adapter;
   }


   public void setAdapter(CardScrollAdapter adapter) {
      if (this.adapter != null) {
         this.adapter.unregisterDataSetObserver(observer);
      }
      this.adapter = adapter;
      container.removeAllViews();

      Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
      Point realSize = new Point();
      display.getRealSize(realSize);

      if (adapter != null) {
         for (int i = 0; i < adapter.getCount(); i++) {
            View card = adapter.getView(i, null, container);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    realSize.x,
                    LayoutParams.MATCH_PARENT
            );
            container.addView(card, params);
         }
         adapter.registerDataSetObserver(observer);
      }
   }

   private final DataSetObserver observer = new DataSetObserver() {
      @Override
      public void onChanged() {
         rebuild();
      }
   };

   private void rebuild() {
      container.removeAllViews();
      if (adapter != null) {
         for (int i = 0; i < adapter.getCount(); i++) {
            View card = adapter.getView(i, null, container);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    getWidth(),
                    LayoutParams.MATCH_PARENT
            );
            container.addView(card, params);
         }
         // keep selection in bounds
         if (currentIndex >= adapter.getCount()) {
            currentIndex = adapter.getCount() - 1;
         }
         if (currentIndex < 0) {
            currentIndex = 0;
         }
         smoothScrollTo(currentIndex * getWidth(), 0);
      }
   }

   public boolean isActivated() {
      Log.e("OpenPrism", "Stub: isActivated()");
      return true;
   }

   public void nextCard() {
      if (currentIndex < adapter.getCount() - 1) {
         currentIndex++;
         smoothScrollTo(currentIndex * getWidth(), 0);
         performItemSelected();
      }
   }

   public void prevCard() {
      if (currentIndex > 0) {
         currentIndex--;
         smoothScrollTo(currentIndex * getWidth(), 0);
         performItemSelected();
      }
   }

   public int getSelectedItemPosition() {
      return currentIndex;
   }

   public View getSelectedView() {
      return container.getChildAt(currentIndex);
   }

   public long getSelectedItemId() {
      return currentIndex;
   }

   public void setSelection(int position) {
      currentIndex = position;
      if (position >= 0 && position < adapter.getCount()) {
         smoothScrollTo(position * getWidth(), 0);
      }
   }

   private AdapterView.OnItemClickListener itemClickListener;
   private AdapterView.OnItemSelectedListener itemSelectedListener;

   public AdapterView.OnItemClickListener getOnItemClickListener() {
      return this.itemClickListener;
   }
   public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
      this.itemClickListener = listener;
   }

   public AdapterView.OnItemSelectedListener getOnItemSelectedListener() {
      return this.itemSelectedListener;
   }
   public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
      this.itemSelectedListener = listener;
   }

   /**
    * The {@link android.widget.AdapterView#setEmptyView(View)} is unsupported by the {@link CardScrollView}.
    * <p>
    * To get proper animation and tuggable behavior, use an adapter with a single empty view for empty data sets instead.
    */
   public void setEmptyView(View emptyView) {
      Log.e("OpenPrism", "Stub: setEmptyView(View)");
   }

   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
      super.onLayout(changed, left, top, right, bottom);
   }

   protected void initializeScrollbars(TypedArray a) {
      //Log.e("OpenPrism", "Stub: initializeScrollbars(TypedArray)");
   }

   public void setHorizontalScrollBarEnabled(boolean enable) {
      //Log.e("OpenPrism", "Stub: setHorizontalScrollBarEnabled(boolean)");
      super.setHorizontalScrollBarEnabled(enable);
   }

   public boolean isHorizontalScrollBarEnabled() {
      //Log.e("OpenPrism", "Stub: isHorizontalScrollBarEnabled()");
      return super.isHorizontalScrollBarEnabled();
   }

   /**
    * Awakens the horizontal scrollbar, if any.
    * @see #isHorizontalScrollBarEnabled()
    * @see #setHorizontalScrollBarEnabled(boolean)
    */
   protected boolean awakenScrollBars() {
      return super.awakenScrollBars();
   }

   public void onWindowFocusChanged(boolean hasWindowFocus) {
      super.onWindowFocusChanged(hasWindowFocus);
   }

   protected void onVisibilityChanged(View changedView, int visibility) {
      super.onVisibilityChanged(changedView, visibility);
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
         nextCard();
         return true;
      } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_NAVIGATE_PREVIOUS) {
         prevCard();
         return true;
      } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
         performItemClick();
         return true;
      } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
         // Handle back press to close the activity
         ((android.app.Activity) getContext()).finish();
         return true;
      }
      return super.onKeyDown(keyCode, event);
   }

   @Override
   public boolean onTouchEvent(MotionEvent event) {
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
         startTouchX = event.getX();
         startTouchY = event.getY();
         return true;
      }
      if (event.getAction() == MotionEvent.ACTION_UP) {
         float deltaX = startTouchX - event.getX(); // Swipe right = positive delta
         if (Math.abs(event.getX() - startTouchX) > 50) {
            // Smooth snap to closest card position
            float scrollX = getScrollX();
            float newScrollX = scrollX + deltaX;
            int cardIndex = Math.round(newScrollX / (float) getWidth());
            // Clamp to valid range
            if (cardIndex < 0) cardIndex = 0;
            if (cardIndex >= adapter.getCount()) cardIndex = adapter.getCount() - 1;
            Log.d("CardScrollView", "Snapping to card index " + cardIndex);
            currentIndex = cardIndex;
            smoothScrollTo(currentIndex * getWidth(), 0);
            performItemSelected();
         } else if (event.getY() - startTouchY > 50) { // Swiping down closes the activity
            ((android.app.Activity) getContext()).finish();
         } else {
            // Treat as a tap if there was no significant movement
            performClick();
         }
         return true;
      }
      return super.onTouchEvent(event);
   }


   @Override
   public boolean performClick() {
      performItemClick();
      return super.performClick();
   }

   private void performItemClick() {
      if (itemClickListener != null && adapter != null && currentIndex < adapter.getCount()) {
         View clickedView = container.getChildAt(currentIndex);
         long id = adapter.getItemId(currentIndex);
         itemClickListener.onItemClick(null, clickedView, currentIndex, id);
      }
   }

   private void performItemSelected() {
      if (itemSelectedListener != null && adapter != null && currentIndex < adapter.getCount()) {
         View selectedView = container.getChildAt(currentIndex);
         long id = adapter.getItemId(currentIndex);
         itemSelectedListener.onItemSelected(null, selectedView, currentIndex, id);
      }
   }

   protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
      return super.dispatchGenericFocusedEvent(event);
   }

   /** Defines animation type used to navigate to, insert, or delete a card. */
   public static enum Animation {
      DELETION,
      INSERTION,
      NAVIGATION;

      private Animation() {
         Log.e("OpenPrism", "Stub: Animation()");
      }
   }
}