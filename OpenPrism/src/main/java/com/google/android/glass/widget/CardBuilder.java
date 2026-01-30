package com.google.android.glass.widget;

import static androidx.core.widget.TextViewCompat.setAutoSizeTextTypeUniformWithPresetSizes;
import static androidx.core.widget.TextViewCompat.setAutoSizeTextTypeWithDefaults;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.android.glass.R;

import java.util.ArrayList;

/**
 * The {@link com.google.android.glass.widget.CardBuilder} class helps with building Glass-themed cards with various layouts.
 * <p>
 * See <a href="https://developers.google.com/glass/develop/gdk/card-design#glass-styled_cards">
 * Creating Glass-styled cards</a> for more information and sample code.
 *
 * <h3>General usage</h3>
 * <ol>
 *   <li>Create a {@link com.google.android.glass.widget.CardBuilder} object, passing the desired
 *       {@link com.google.android.glass.widget.CardBuilder.Layout} to the constructor.</li>
 *   <li>Set content on the card with the {@code add/set*} family of methods.</li>
 *   <li>Get the {@link android.view.View} by calling {@link #getView()} or get a
 *       {@link android.widget.RemoteViews} object with {@link #getRemoteViews()}.</li>
 * </ol>
 *
 * <h3>Recycling note for embedded layouts</h3>
 * When using {@link CardBuilder.Layout#EMBED_INSIDE}, {@code CardBuilder} makes no distinction
 * between different embedded layouts when recycling views. If you have two {@code CardBuilder}
 * instances with {@code EMBED_INSIDE} but one has layout A and the other has layout B, they will
 * be treated as the same by {@code CardBuilder}.
 * <p>
 * If this behavior is undesirable (for example, when using a {@link com.google.android.glass.widget.CardScrollAdapter} with
 * different embedded layouts), you must distinguish them by returning different view types from
 * {@link com.google.android.glass.widget.CardScrollAdapter#getItemViewType(int)} so that the recycler only sends you
 * {@code convertView}s with nested layouts that you expect for a particular item.
 * <p>
 * If you need to support cards with multiple embedded layouts alongside the built-in layouts,
 * we recommend that you return view types numbered {@link #getViewTypeCount()},
 * {@code CardBuilder.getViewTypeCount() + 1}, and so on.
 */
public class CardBuilder {

   private final Context context;
   private final Layout layout;
   private CharSequence heading;
   private CharSequence subheading;
   private CharSequence text;
   private CharSequence footnote;
   private CharSequence timestamp;
   private Drawable icon;
   private Drawable attributionIcon;
   private Boolean showStackIndicator = false;
   private int embeddedLayoutId = 0;
   private ArrayList<Drawable> images = new ArrayList<>();

   /**
    * Constructs a new {@code CardBuilder}
    * @param context the {@code Context} that will be used by the builder to create its views.
    * @param layout  the desired layout for the card
    */
   public CardBuilder(Context context, CardBuilder.Layout layout) {
      this.context = context;
      this.layout = layout;
   }

   /**
    * Sets the main text for the card.
    * @param text main text for this card
    * @return     this object for call chaining
    */
   public CardBuilder setText(CharSequence text) {
      this.text = text;
      return this;
   }

   /**
    * Sets the main text for the card using a string resource.
    * @param textId main text resource ID for this card
    * @return       this object for call chaining
    */
   public CardBuilder setText(int textId) {
      this.text = context.getString(textId);
      return this;
   }

   public CardBuilder setFootnote(CharSequence footnote) {
      this.footnote = footnote;
      return this;
   }

   /**
    * Sets the footnote text for the card using a string resource.
    * @param footnoteId the footnote text resource ID for this card
    * @return           this object for call chaining
    */
   public CardBuilder setFootnote(int footnoteId) {
      this.footnote = context.getString(footnoteId);
      return this;
   }

   /**
    * Sets the timestamp text for the card.
    * @param timestamp the timestamp text for this card
    * @return          this object for call chaining
    */
   public CardBuilder setTimestamp(CharSequence timestamp) {
      this.timestamp = timestamp;
      return this;
   }

   /**
    * Sets the timestamp text for the card using a string resource.
    * @param timestampId the timestamp text resource ID for this card
    * @return            this object for call chaining
    */
   public CardBuilder setTimestamp(int timestampId) {
      this.timestamp = context.getString(timestampId);
      return this;
   }

   /**
    * Sets the heading text for the card.
    * @param heading the heading text for this card
    * @return        this object for call chaining
    */
   public CardBuilder setHeading(CharSequence heading) {
      this.heading = heading;
      return this;
   }

   /**
    * Sets the heading text for the card using a string resource.
    * @param headingId the heading text resource ID for this card
    * @return          this object for call chaining
    */
   public CardBuilder setHeading(int headingId) {
      this.heading = context.getString(headingId);
      return this;
   }

   /**
    * Sets the subheading text for the card.
    * @param subheading the subheading text for this card
    * @return           this object for call chaining
    */
   public CardBuilder setSubheading(CharSequence subheading) {
      this.subheading = subheading;
      return this;
   }

   /**
    * Sets the subheading text for the card using a string resource.
    * @param subheadingId the subheading text resource ID for this card
    * @return             this object for call chaining
    */
   public CardBuilder setSubheading(int subheadingId) {
      this.subheading = context.getString(subheadingId);
      return this;
   }

   /**
    * Adds an image, specified as a {@link android.graphics.drawable.Drawable}, to the card.
    * <p>
    * This method only applies to cards that are converted into views using
    * {@link #getView()}. {@link android.widget.RemoteViews} built by {@code CardBuilder}
    * only support {@link android.graphics.Bitmap} and resource-based images.
    * {@code Drawable} images on {@code RemoteViews} are not supported.
    *
    * @param imageDrawable the {@link android.graphics.drawable.Drawable} image to add
    * @return this object for call chaining
    */
   public CardBuilder addImage(Drawable imageDrawable) {
      this.images.add(imageDrawable);
      return this;
   }

   /**
    * Adds an image, specified as a {@link android.graphics.Bitmap}, to the card.
    * @param imageBitmap the {@code Bitmap} image to add
    * @return            this object for call chaining
    */
   public CardBuilder addImage(Bitmap imageBitmap) {
      this.images.add(new BitmapDrawable(context.getResources(), imageBitmap));
      return this;
   }

   /**
    * Adds an image, specified as a drawable resource, to the card.
    * @param imageId the resource ID of the image to add
    * @return        this object for call chaining
    */
   public CardBuilder addImage(int imageId) {
      this.images.add(context.getResources().getDrawable(imageId));
      return this;
   }

   /** Clears all images that were previously added to the card. */
   public void clearImages() {
      this.images.clear();
   }

   /**
    * Sets the icon for the card using a {@link android.graphics.drawable.Drawable}.
    * <p>
    * This method only applies to cards that are converted into views using {@link #getView()}.
    * {@link android.widget.RemoteViews} built by {@code CardBuilder} only support {@code Bitmap} and resource-based images.
    * {@code Drawable} images on {@code RemoteViews} are not supported.
    *
    * @param iconDrawable the {@code Drawable} to use as the icon
    * @return             this object for call chaining
    */
   public CardBuilder setIcon(Drawable iconDrawable) {
      this.icon = iconDrawable;
      return this;
   }

   /**
    * Sets the icon for the card using a {@link android.graphics.Bitmap}.
    * @param iconBitmap the {@code Bitmap} to use as the icon
    * @return           this object for call chaining
    */
   public CardBuilder setIcon(Bitmap iconBitmap) {
      this.icon = new BitmapDrawable(context.getResources(), iconBitmap);
      return this;
   }

   /**
    * Sets the icon for the card using a drawable resource.
    * @param iconId the resource ID to use as the icon
    * @return       this object for call chaining
    */
   public CardBuilder setIcon(int iconId) {
      this.icon = context.getResources().getDrawable(iconId);
      return this;
   }

   /**
    * Sets the attribution icon for the card using a {@link android.graphics.drawable.Drawable}.
    * <p>
    * This method only applies to cards that are converted into views using {@link #getView()}.
    * {@link android.widget.RemoteViews} built by {@code CardBuilder} only support {@code Bitmap} and resource-based images.
    * {@code Drawable} images on {@code RemoteViews} are not supported.
    *
    * @param iconDrawable the {@code Drawable} to use as the attribution icon
    * @return             this object for call chaining
    */
   public CardBuilder setAttributionIcon(Drawable iconDrawable) {
      this.attributionIcon = iconDrawable;
      return this;
   }

   /**
    * Sets the attribution icon for the card using a {@link android.graphics.Bitmap}.
    * @param iconBitmap the {@code Bitmap} to use as the attribution icon
    * @return           this object for call chaining
    */
   public CardBuilder setAttributionIcon(Bitmap iconBitmap) {
      this.attributionIcon = new BitmapDrawable(context.getResources(), iconBitmap);
      return this;
   }

   /**
    * Sets the attribution icon for the card using a drawable resource.
    * @param iconId the resource ID to use as the attribution icon
    * @return       this object for call chaining
    */
   public CardBuilder setAttributionIcon(int iconId) {
      this.attributionIcon = context.getResources().getDrawable(iconId);
      return this;
   }

   /**
    * Shows an indicator if {@code visible} is true that this card represents a stack of cards, rather than a single card.
    * @param visible true to show the stack indicator, or false to hide it
    * @return        this object for call chaining
    */
   public CardBuilder showStackIndicator(boolean visible) {
      this.showStackIndicator = visible;
      return this;
   }

   /**
    * Sets the resource ID of the layout to embed in the card.
    * @param layoutResId the resource ID of the layout to embed in the card
    * @return            this object for call chaining
    */
   public CardBuilder setEmbeddedLayout(int layoutResId) {
      this.embeddedLayoutId = layoutResId;
      return this;
   }

   /** @return a representation of this card. */
   public View getView() {
      return getView(null, null);
   }

   /**
    * Useful in combination with an adapter. See
    * {@link android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)}.
    * @param convertView an old view to reuse, if possible; can be {@code null}
    *                    Note: if this view does not have the right type, this method creates a new view
    * @param parent     that this view will eventually be attached to, maybe {@code null}
    * @return            a {@link android.view.View} representation of this card.
    */
   public View getView(View convertView, ViewGroup parent) {
      LayoutInflater inflater = LayoutInflater.from(context);
      View layout = null;
      if (this.layout == Layout.MENU) {
         layout = inflater.inflate(R.layout.card_builder_menu, parent, false);
      } else if (this.layout == Layout.AUTHOR || this.layout == Layout.TEXT || this.layout == Layout.TEXT_FIXED) {
         layout = inflater.inflate(R.layout.card_builder_author, parent, false);
      } else if (this.layout == Layout.CAPTION) {
         layout = inflater.inflate(R.layout.card_builder_caption, parent, false);
      } else if (this.layout == Layout.TITLE) {
         layout = inflater.inflate(R.layout.card_builder_title, parent, false);
      } else if (this.layout == Layout.COLUMNS || this.layout == Layout.COLUMNS_FIXED) {
         layout = inflater.inflate(R.layout.card_builder_columns, parent, false);
      } else if (this.layout == Layout.EMBED_INSIDE) {
         layout = inflater.inflate(R.layout.card_builder_embed, parent, false);
      } else if (this.layout == Layout.ALERT) {
         layout = inflater.inflate(R.layout.card_builder_author, parent, false);
      } else {
         Log.e("OpenPrism", "Unsupported layout type: " + this.layout);
         layout = inflater.inflate(R.layout.card_builder_menu, parent, false); //Default to menu layout
      }

      ImageView imageView1 = layout.findViewById(R.id.card_image_1);
      ImageView imageView2 = layout.findViewById(R.id.card_image_2);
      ImageView imageView3 = layout.findViewById(R.id.card_image_3);
      ImageView imageView4 = layout.findViewById(R.id.card_image_4);
      ImageView imageView5 = layout.findViewById(R.id.card_image_5);
      ImageView[] imageViews = {imageView1, imageView2, imageView3, imageView4, imageView5};
      ImageView iconView = layout.findViewById(R.id.card_icon);
      ImageView stackIndicator = layout.findViewById(R.id.card_stack_indicator);
      ImageView attributionIconView = layout.findViewById(R.id.card_attribution_icon);
      TextView textView = layout.findViewById(R.id.card_text);
      TextView headingView = layout.findViewById(R.id.card_heading);
      TextView subheadingView = layout.findViewById(R.id.card_subheading);
      TextView footnoteView = layout.findViewById(R.id.card_footnote);
      TextView timestampView = layout.findViewById(R.id.card_timestamp);
      ViewGroup embedContainer = layout.findViewById(R.id.card_embed_container);

      if (showStackIndicator) {
         stackIndicator.setVisibility(View.VISIBLE);
      } else {
         stackIndicator.setVisibility(View.GONE);
      }

      //FIXME: Fix image mosaic layout
      for (int i = 0; i < imageViews.length; i++) {
         if (i < images.size()) {
            imageViews[i].setImageDrawable(images.get(i));
            imageViews[i].setVisibility(View.VISIBLE);
         } else {
            imageViews[i].setVisibility(View.GONE);
         }
      }

      if (iconView != null) {
         if (icon != null) {
            iconView.setVisibility(View.VISIBLE);
            iconView.setImageDrawable(icon);
         } else {
            iconView.setVisibility(View.GONE);
         }
      }

      if (attributionIconView != null) {
         if (attributionIcon != null) {
            attributionIconView.setVisibility(View.VISIBLE);
            attributionIconView.setImageDrawable(attributionIcon);
         } else {
            attributionIconView.setVisibility(View.GONE);
         }
      }

      if (headingView != null) {
         if (heading != null) {
            headingView.setVisibility(View.VISIBLE);
            headingView.setText(heading);
         } else {
            headingView.setVisibility(View.GONE);
         }
      }

      if (subheadingView != null) {
         if (subheading != null) {
            subheadingView.setVisibility(View.VISIBLE);
            subheadingView.setText(subheading);
         } else {
            subheadingView.setVisibility(View.GONE);
         }
      }

      if (timestampView != null) {
         if (timestamp != null) {
            timestampView.setVisibility(View.VISIBLE);
            timestampView.setText(timestamp);
         } else {
            timestampView.setVisibility(View.GONE);
         }
      }

      if (textView != null) {
         if (text != null) {
            textView.setVisibility(View.VISIBLE);
            if (this.layout == Layout.TEXT_FIXED || this.layout == Layout.COLUMNS_FIXED) {
               setAutoSizeTextTypeWithDefaults(textView, 0);
            }
            textView.setText(text);
         } else {
            textView.setVisibility(View.GONE);
         }
      }

      if (footnote != null) {
         footnoteView.setText(footnote);
      }
      if (embedContainer != null) {
         if (this.embeddedLayoutId == 0 && this.layout == Layout.EMBED_INSIDE) {
            throw new IllegalStateException("You must set a valid layout ID with setEmbeddedLayout() when using CardBuilder.Layout.EMBED_INSIDE.");
         } else if (this.embeddedLayoutId != 0) {
            inflater.inflate(this.embeddedLayoutId, embedContainer);
         }
      }

      return layout;
   }

   /** @return a {@link android.widget.RemoteViews} representation of this card. */
   public RemoteViews getRemoteViews() {
      throw new RuntimeException("Unimplemented method: getRemoteViews()");
   }

   /**
    * Useful in combination with an adapter. See {@link android.widget.Adapter#getItemViewType(int)}
    * @return the view type of this particular card.
    */
   public int getItemViewType() {
      return 0;
   }

   /**
    * Useful in combination with an adapter. See {@link android.widget.Adapter#getViewTypeCount()}.
    * @return the total number of view types cards can take.
    */
   public static int getViewTypeCount() {
      return 1;
   }

   public static enum Layout {
      /**
       * An alert with a large centered icon and a message and footnote underneath.
       *
       * @see #setFootnote(CharSequence)
       * @see #setIcon(Drawable)
       * @see #setText(CharSequence)
       * @see #setAttributionIcon(Drawable)
       */
      ALERT,
      /**
       * Content with a focus on the author: an avatar with a heading and subheading, and body text underneath.
       *
       * @see #addImage(Drawable)
       * @see #setAttributionIcon(Drawable)
       * @see #setFootnote(CharSequence)
       * @see #setHeading(CharSequence)
       * @see #setIcon(Drawable)
       * @see #setSubheading(CharSequence)
       * @see #setText(CharSequence)
       * @see #setTimestamp(CharSequence)
       * @see #showStackIndicator(boolean)
       */
      AUTHOR,
      /**
       * Images appear full screen in the background with a text caption and optional avatar at the bottom of the card.
       *
       * @see #addImage(Drawable)
       * @see #setAttributionIcon(Drawable)
       * @see #setFootnote(CharSequence)
       * @see #setIcon(Drawable)
       * @see #setText(CharSequence)
       * @see #setTimestamp(CharSequence)
       * @see #showStackIndicator(boolean)
       */
      CAPTION,
      /**
       * A two-column layout with images on the left and text on the right. The size of the text is dynamic based on the amount of content in the card.
       * 
       * @see #addImage(Drawable) 
       * @see #setAttributionIcon(Drawable) 
       * @see #setFootnote(CharSequence) 
       * @see #setIcon(Drawable) 
       * @see #setText(CharSequence) 
       * @see #showStackIndicator(boolean)
       */
      COLUMNS,
      /**
       * A two-column layout with images on the left and text on the right. The size of the text is fixed at 40 pixels.
       * This layout should be used when displaying multiple cards of this type in a sequence, such as a scrolling list of restaurants or settings,
       * and it is important to render each card at the same size for visual consistency.
       *
       * @see #addImage(Drawable)
       * @see #setAttributionIcon(Drawable)
       * @see #setFootnote(CharSequence)
       * @see #setIcon(Drawable)
       * @see #setText(CharSequence)
       * @see #setTimestamp(CharSequence)
       * @see #showStackIndicator(boolean)
       */
      COLUMNS_FIXED,
      /**
       * Allows a custom layout to be embedded inside a card that optionally has a standard footnote, timestamp, and stack indicator.
       * <p>
       * The embedded layout will be inflated inside a {@link android.widget.RelativeLayout} that is bounded within the standard margins of the card
       * so that it does not overlap with the card's footer.
       * <p>
       * Once you have called {@link #getView()} or {@link #getRemoteViews()}, you can call {@code findViewById()} or standard {@code RemoteViews} methods in order to access the views inside your embedded layout.
       *
       * @see #setAttributionIcon(Drawable)
       * @see #setFootnote(CharSequence)
       * @see #setTimestamp(CharSequence)
       * @see #showStackIndicator(boolean)
       */
      EMBED_INSIDE,
      /**
       * Text with an optional icon centered in the card and an optional footnote underneath, like a menu item.
       *
       * @see #setFootnote(CharSequence)
       * @see #setIcon(Drawable)
       * @see #setText(CharSequence)
       */
      MENU,
      /**
       * Text that fills the whole card, with optional background images. The size of the text is dynamic based on the amount of content in the card.
       *
       * @see #addImage(Drawable)
       * @see #setAttributionIcon(Drawable)
       * @see #setFootnote(CharSequence)
       * @see #setText(CharSequence)
       */
      TEXT,
      /**
       * Text that fills the whole card, with optional background images. The size of the text is fixed at 30 pixels.
       * This layout should be used when using cards to display multiple pages of text and it is important to render each page at the same size for visual consistency.
       *
       * @see #addImage(Drawable)
       * @see #setAttributionIcon(Drawable)
       * @see #setFootnote(CharSequence)
       * @see #setText(CharSequence)
       * @see #setTimestamp(CharSequence)
       * @see #showStackIndicator(boolean)
       */
      TEXT_FIXED,
      /**
       * Images appear full screen in the background with a name and optional icon centered at the bottom.
       *
       * @see #addImage(Drawable)
       * @see #setIcon(Drawable)
       * @see #setText(CharSequence)
       * @see #showStackIndicator(boolean)
       */
      TITLE;

      /**
       * Defines the visual layouts for cards.
       * <p>
       * Refer to the "See Also" section of each layout to see which content is supported by the layout.
       */
      private Layout() {
         Log.e("OpenPrism", "Stub: Layout()");
      }
   }
}

