package com.google.android.glass.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 *A special form of a {@link android.widget.BaseAdapter}.
 * <p>
 * Use this in combination with a {@link com.google.android.glass.widget.CardScrollView} to implement horizontally scrolling views, also
 * referred to as cards. This adapter binds (possibly dynamic) data to the {@link com.google.android.glass.widget.CardScrollView} by
 * retrieving the data (if needed) and converting each data item into a card. Each card visually
 * represents a certain {@link java.lang.Object} item.
 * <p>
 * To be consistent with the Glass UI, create cards with the {@link com.google.android.glass.widget.CardBuilder} class, which supports
 * several content layouts. If you require more flexibility, you can create your own XML layouts
 * or create views programmatically.
 * <p>
 * See <a href="https://developers.google.com/glass/develop/gdk/card-scroller#scrolling_cards_in_activities">Scrolling cards in activities</a> for more information.
 */
public abstract class CardScrollAdapter extends BaseAdapter {
    public abstract int getCount();

    public abstract Object getItem(int i);

    /**
     * Finds the position of the given item.
     * @param item the item to find
     * @return     the position of the given item, or {@code INVALID_POSITION} if the item cannot be found
     */
    public abstract int getPosition(Object item);

    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public int getItemViewType(int position) {
        return -1;
    }

    /**
     * The default implementation returns 0 to signal lack of recycling. Users can override this
     * method (only called once when adapter is set) and {@link #getItemViewType(int)} to enable view
     * recycling.
     */
    public int getViewTypeCount() {
        return 0;
    }

    /**
     * The default implementation simply assigns the card's position as row identifier and assumes
     * this property holds even across data changes. When each data item has a truly unique row
     * identifier, users can override this method to return the real row identifier and override
     * {@link android.widget.BaseAdapter#hasStableIds()} to return true. By doing so, the card scroller is more likely to maintain its
     * view on selected cards across data changes.
     */
    public long getItemId(int position) {
        return (long) position;
    }

    /**
     * Returns the home position. The default implementation simply assumes position 0 is the home position, but the user can override this method to move the home position to a different card.
     * <p>
     * For example, the home position of the Glass timeline is the clock card, and other cards can be located both to the left and to the right of it.
     * @return the position of the item that represents the home position
     */
    public int getHomePosition() {
        return 0;
    }

    @Deprecated
    public void recycleView(View view) {
    }
}
