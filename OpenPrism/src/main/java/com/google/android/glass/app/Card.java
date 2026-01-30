package com.google.android.glass.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.glass.widget.CardBuilder;

/** @deprecated */
@Deprecated
public class Card extends CardBuilder {
   /** @deprecated */
   @Deprecated
   public Card(Context context) {
      super((Context)null, (CardBuilder.Layout)null);
      Log.e("OpenPrism", "Stub: Card(Context)");
   }

   public Card setText(CharSequence text) {
      Log.e("OpenPrism", "Stub: setText(CharSequence)");
      return this;
   }

   public Card setText(int textId) {
      Log.e("OpenPrism", "Stub: setText(int)");
      return this;
   }

   /** @deprecated */
   @Deprecated
   public CharSequence getText() {
      Log.e("OpenPrism", "Stub: getText()");
      return "Not implemented";
   }

   public Card setFootnote(CharSequence footnote) {
      Log.e("OpenPrism", "Stub: setFootnote(CharSequence)");
      return this;
   }

   public Card setFootnote(int footnoteId) {
      Log.e("OpenPrism", "Stub: setFootnote(int)");
      return this;
   }

   /** @deprecated */
   @Deprecated
   public CharSequence getFootnote() {
      Log.e("OpenPrism", "Stub: getFootnote()");
      return "Not implemented";
   }

   public Card setTimestamp(CharSequence timestamp) {
      Log.e("OpenPrism", "Stub: setTimestamp(CharSequence)");
      return this;
   }

   public Card setTimestamp(int timestampId) {
      Log.e("OpenPrism", "Stub: setTimestamp(int)");
      return this;
   }

   /** @deprecated */
   @Deprecated
   public CharSequence getTimestamp() {
      Log.e("OpenPrism", "Stub: getTimestamp()");
      return "Not implemented";
   }

   public Card addImage(Drawable imageDrawable) {
      Log.e("OpenPrism", "Stub: addImage(Drawable)");
      return this;
   }

   public Card addImage(Bitmap imageBitmap) {
      Log.e("OpenPrism", "Stub: addImage(Bitmap)");
      return this;
   }

   public Card addImage(int imageId) {
      Log.e("OpenPrism", "Stub: addImage(int)");
      return this;
   }

   /** @deprecated */
   @Deprecated
   public Drawable getImage(int n) {
      Log.e("OpenPrism", "Stub: getImage(int)");
      return new Drawable() {
         @Override
         public void draw(Canvas canvas) {

         }

         @Override
         public void setAlpha(int alpha) {

         }

         @Override
         public void setColorFilter(ColorFilter colorFilter) {

         }

         @Override
         public int getOpacity() {
            return PixelFormat.UNKNOWN;
         }
      };
   }

   /** @deprecated */
   @Deprecated
   public int getImageCount() {
      Log.e("OpenPrism", "Stub: getImageCount()");
      return 0;
   }

   /** @deprecated */
   @Deprecated
   public Card setImageLayout(Card.ImageLayout imageLayout) {
      Log.e("OpenPrism", "Stub: setImageLayout(Card.ImageLayout)");
      return this;
   }

   /** @deprecated */
   @Deprecated
   public Card.ImageLayout getImageLayout() {
      Log.e("OpenPrism", "Stub: getImageLayout()");
      return ImageLayout.FULL;
   }

   /** @deprecated */
   @Deprecated
   public static enum ImageLayout {
      FULL,
      LEFT;

      private ImageLayout() {
         Log.e("OpenPrism", "Stub: ImageLayout()");
      }
   }
}
