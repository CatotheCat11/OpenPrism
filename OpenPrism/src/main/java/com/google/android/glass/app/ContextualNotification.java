package com.google.android.glass.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import com.google.android.glass.widget.CardBuilder;

/* loaded from: classes.dex */
public class ContextualNotification extends Notification.Style {
    public static final String EXTRA_MENU_ITEM_ID = "menu_item_id";
    private static final int INVALID_RESOURCE = 0;
    private static final String KEY_MENU = "menu";
    private static final String KEY_MENU_INTENT = "menu_intent";
    private static final String KEY_RENDERER = "renderer";
    private static final String KEY_RENDERER_PARAMS = "renderer_params";
    private static final String KEY_REVEAL = "reveal";
    private final Bundle mProperties;

    public ContextualNotification() {
        this.mProperties = new Bundle();
    }

    public ContextualNotification(Notification.Builder builder) {
        this();
        setBuilder(builder);
    }

    public ContextualNotification setRenderer(ComponentName renderer) {
        return setRenderer(renderer, null);
    }

    public ContextualNotification setRenderer(ComponentName renderer, Bundle params) {
        this.mProperties.putParcelable(KEY_RENDERER, renderer);
        if (params != null) {
            this.mProperties.putBundle(KEY_RENDERER_PARAMS, params);
        }
        return this;
    }

    public ContextualNotification setMenu(int resourceId, PendingIntent intent) {
        if (resourceId == 0) {
            throw new IllegalArgumentException("Cannot pass invalid resource id.");
        }
        if (intent == null) {
            throw new IllegalArgumentException("Cannot pass null intent.");
        }
        this.mProperties.putInt(KEY_MENU, resourceId);
        this.mProperties.putParcelable(KEY_MENU_INTENT, intent);
        return this;
    }

    public ContextualNotification setReveal(boolean reveal) {
        this.mProperties.putBoolean(KEY_REVEAL, reveal);
        return this;
    }

    public Notification buildStyled(Notification notification) {
        checkBuilder();
        return notification;
    }

    public void addExtras(Bundle extras) {
        extras.putAll(this.mProperties);
    }

    public static class Reader {
        private final Notification mNotification;

        public Reader(Notification notification) {
            this.mNotification = notification;
        }

        public boolean hasRenderer() {
            return getRenderer() != null;
        }

        public ComponentName getRenderer() {
            if (this.mNotification.extras == null) {
                return null;
            }
            return (ComponentName) this.mNotification.extras.getParcelable(ContextualNotification.KEY_RENDERER);
        }

        public boolean hasRendererParams() {
            return getRendererParams() != null;
        }

        public Bundle getRendererParams() {
            if (this.mNotification.extras == null) {
                return null;
            }
            return this.mNotification.extras.getBundle(ContextualNotification.KEY_RENDERER_PARAMS);
        }

        public boolean hasMenu() {
            return getMenuResourceId() != 0;
        }

        public int getMenuResourceId() {
            if (this.mNotification.extras == null) {
                return 0;
            }
            return this.mNotification.extras.getInt(ContextualNotification.KEY_MENU, 0);
        }

        public PendingIntent getMenuIntent() {
            if (this.mNotification.extras == null) {
                return null;
            }
            return (PendingIntent) this.mNotification.extras.getParcelable(ContextualNotification.KEY_MENU_INTENT);
        }

        public boolean shouldReveal() {
            return this.mNotification.extras != null && this.mNotification.extras.getBoolean(ContextualNotification.KEY_REVEAL, false);
        }

        public RemoteViews getContentView(Context context, String packageName) {
            return this.mNotification.extras.getBoolean("android.isContentRemote") ? this.mNotification.contentView : toCardBuilder(context, packageName).getRemoteViews();
        }

        CardBuilder toCardBuilder(Context context, String packageName) {
            CardBuilder card;
            Bitmap largeIcon = (Bitmap) this.mNotification.extras.getParcelable("android.largeIcon");
            if (largeIcon != null) {
                try {
                    final Resources resources = context.getPackageManager().getResourcesForApplication(packageName);
                    ContextWrapper trickContext = new ContextWrapper(context) { // from class: com.google.android.glass.app.ContextualNotification.Reader.1
                        @Override // android.content.ContextWrapper, android.content.Context
                        public Resources getResources() {
                            return resources;
                        }
                    };
                    card = new CardBuilder(trickContext, CardBuilder.Layout.COLUMNS).addImage(largeIcon);
                    int smallIconId = this.mNotification.extras.getInt("android.icon", 0);
                    if (smallIconId > 0) {
                        card.setIcon(smallIconId);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    return null;
                }
            } else {
                card = new CardBuilder(context, CardBuilder.Layout.TEXT);
            }
            card.setText(this.mNotification.extras.getCharSequence("android.title"));
            card.setFootnote(this.mNotification.extras.getCharSequence("android.text"));
            return card;
        }
    }
}
