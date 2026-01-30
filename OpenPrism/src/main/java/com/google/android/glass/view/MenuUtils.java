package com.google.android.glass.view;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/** A collection of extensions for {@link android.view.Menu} and related classes. */
 public final class MenuUtils {
   MenuUtils() {
      Log.e("OpenPrism", "Stub: MenuUtils()");
   }

   /**
    * Sets an additional description text on a menu item.
    * <p>
    * The description attribute can only be set through this method. The attribute is not supported in XML.
    * @param item              the menu item
    * @param description       additional description text
    * @throws RuntimeException if item is not a concrete implementation
    */
   public static void setDescription(MenuItem item, CharSequence description) {
      Log.e("OpenPrism", "Stub: setDescription(MenuItem, CharSequence)");
   }

   /**
    * Sets an additional description text on a menu item.
    * <p>
    * The description attribute can only be set through this method. The attribute is not supported in XML.
    * @param item              the menu item
    * @param description       additional description text (as a resource ID)
    * @throws RuntimeException if item is not a concrete implementation
    */
   public static void setDescription(MenuItem item, int description) {
      Log.e("OpenPrism", "Stub: setDescription(MenuItem, int)");
   }

   /**
    * Sets the initial menu item when users open a menu.
    *<p>
    * By default, the first menu item appears with all the other items to the right.
    * This method lets you set another menu item to initially show, and other items
    * can potentially appear to the left or right of the item that is initially shown.
    * @param menu              the menu on which preferred initial menu item should be set
    * @param item              the preferred initial menu item. If this item cannot be found,
    *                          the menu falls back to the default behavior
    * @throws RuntimeException if item is not a concrete implementation
    */
   public static void setInitialMenuItem(Menu menu, MenuItem item) {
      Log.e("OpenPrism", "Stub: setInitialMenuItem(Menu, MenuItem)");
   }
}
