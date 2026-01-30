package com.cato.cardsample;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.app.ContextualNotification;
import com.google.android.glass.eye.EyeGesture;
import com.google.android.glass.eye.EyeGestureManager;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

public class FunctionsTestActivity extends Activity {
    private List<CardBuilder> mCards;
    private CardScrollView mCardScrollView;
    private ExampleCardScrollAdapter mAdapter;
    private static final String TAG = "MainActivity";

    private EyeGestureManager mEyeGestureManager = null;
    private EyeGestureManager.Listener mEyeGestureListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createCards();
        mCardScrollView = new CardScrollView(this);
        mAdapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(mAdapter);
        mCardScrollView.activate();
        setupClickListener();
        setupEyeGestures();
        setupScrollListener();
        setContentView(mCardScrollView);
    }


    private void createCards() {
        mCards = new ArrayList<CardBuilder>();
        mCards.add(new CardBuilder(this, CardBuilder.Layout.MENU)
                .setText("Send notification"));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                .setText("No eye gestures detected yet."));
    }

    private class ExampleCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return CardBuilder.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position){
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).getView(convertView, parent);
        }
    }

    private void setupClickListener() {
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Item clicked: " + position);
                if (position == 0) {
                    Intent notiIntent = new Intent(FunctionsTestActivity.this, MenuActivity.class);
                    PendingIntent menuIntent = PendingIntent.getActivity(FunctionsTestActivity.this, 0, notiIntent, PendingIntent.FLAG_IMMUTABLE);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    // Create the NotificationChannel, but only on API 26+ because
                    // the NotificationChannel class is not in the Support Library.
                    Notification.Builder notiBuilder = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = "OpenPrism Notification Test";
                        String description = "Test notification";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel("OpenPrismNotiTest", name, importance);
                        channel.setDescription(description);
                        // Register the channel with the system. You can't change the importance
                        // or other notification behaviors after this.
                        notificationManager.createNotificationChannel(channel);
                        notiBuilder = new Notification.Builder(FunctionsTestActivity.this, "OpenPrismNotiTest");
                    } else {
                        notiBuilder = new Notification.Builder(FunctionsTestActivity.this);
                    }
                    ContextualNotification style = new ContextualNotification(notiBuilder)
                            .setMenu(R.menu.my_menu, menuIntent)
                            .setReveal(true);
                    style.setBuilder(notiBuilder);

                    Bundle extras = new Bundle();
                    extras.putBoolean("whitelist", true);

                    Notification noti = notiBuilder.setExtras(extras)
                            .setContentTitle("Test Notification")
                            .setContentText("Test Description")
                            .setSmallIcon(R.drawable.ic_notification)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setStyle(style)
                            .build();

                    notificationManager.notify((int) System.currentTimeMillis(), noti);
                }
            }
        });
    }
    private void setupScrollListener() {
        mCardScrollView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    mEyeGestureManager.register(EyeGesture.LOOK_AT_SCREEN, mEyeGestureListener);
                    mEyeGestureManager.register(EyeGesture.LOOK_AWAY_FROM_SCREEN, mEyeGestureListener);
                    mEyeGestureManager.register(EyeGesture.BLINK, mEyeGestureListener);
                    mEyeGestureManager.register(EyeGesture.WINK, mEyeGestureListener);
                    mEyeGestureManager.register(EyeGesture.DOUBLE_BLINK, mEyeGestureListener);
                    mEyeGestureManager.register(EyeGesture.DOUBLE_WINK, mEyeGestureListener);
                } else {
                    mEyeGestureManager.unregister(EyeGesture.LOOK_AT_SCREEN, mEyeGestureListener);
                    mEyeGestureManager.unregister(EyeGesture.LOOK_AWAY_FROM_SCREEN, mEyeGestureListener);
                    mEyeGestureManager.unregister(EyeGesture.BLINK, mEyeGestureListener);
                    mEyeGestureManager.unregister(EyeGesture.WINK, mEyeGestureListener);
                    mEyeGestureManager.unregister(EyeGesture.DOUBLE_BLINK, mEyeGestureListener);
                    mEyeGestureManager.unregister(EyeGesture.DOUBLE_WINK, mEyeGestureListener);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupEyeGestures() {

        mEyeGestureManager = EyeGestureManager.from(this);

        mEyeGestureListener = new EyeGestureManager.Listener(){
            public void onDetected(EyeGesture gesture) {
                Log.i("EyeGestureListener", "Gesture: " + gesture.getId());

                String gestureName;
                if (gesture.getId() == EyeGesture.LOOK_AT_SCREEN.getId()) {
                    gestureName = "Look at screen";
                } else if (gesture.getId() == EyeGesture.LOOK_AWAY_FROM_SCREEN.getId()) {
                    gestureName = "Look away from screen";
                } else if (gesture.getId() == EyeGesture.BLINK.getId()) {
                    gestureName = "Blink";
                } else if (gesture.getId() == EyeGesture.WINK.getId()) {
                    gestureName = "Wink";
                } else if (gesture.getId() == EyeGesture.DOUBLE_BLINK.getId()) {
                    gestureName = "Double blink";
                } else if (gesture.getId() == EyeGesture.DOUBLE_WINK.getId()) {
                    gestureName = "Double wink";
                } else {
                    gestureName = "Unknown gesture";
                }
                mCards.get(1).setText("Eye gesture detected: " + gestureName);
                mAdapter.notifyDataSetChanged();
            }
        };
    }
}