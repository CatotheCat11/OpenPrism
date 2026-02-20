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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.app.ContextualNotification;
import com.google.android.glass.eye.EyeGesture;
import com.google.android.glass.eye.EyeGestureManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
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
    private GestureDetector mGestureDetector;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createCards();
        mCardScrollView = new CardScrollView(this);
        mAdapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(mAdapter);
        mCardScrollView.activate();
        setupClickListener();
        setupEyeGestures();
        mGestureDetector = createGestureDetector(this);
        setupScrollListener();
        setContentView(mCardScrollView);
    }


    private void createCards() {
        mCards = new ArrayList<CardBuilder>();
        mCards.add(new CardBuilder(this, CardBuilder.Layout.MENU)
                .setText("Send notification"));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                .setText("No eye gestures detected yet."));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                .setText("No gestures detected yet."));
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

    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener(gesture -> {
            switch (gesture) {
                case TAP:
                    mCards.get(2).setText("Tap detected.");
                    break;
                case TWO_TAP:
                    mCards.get(2).setText("Two tap detected.");
                    break;
                case THREE_TAP:
                    mCards.get(2).setText("Three tap detected.");
                    break;
                case LONG_PRESS:
                    mCards.get(2).setText("Long press detected.");
                    break;
                case TWO_LONG_PRESS:
                    mCards.get(2).setText("Two long press detected.");
                    break;
                case THREE_LONG_PRESS:
                    mCards.get(2).setText("Three long press detected.");
                    break;
                case SWIPE_UP:
                    mCards.get(2).setText("Swipe up detected.");
                    break;
                case TWO_SWIPE_UP:
                    mCards.get(2).setText("Two swipe up detected.");
                    break;
                case SWIPE_DOWN:
                    mCards.get(2).setText("Swipe down detected.");
                    break;
                case TWO_SWIPE_DOWN:
                    mCards.get(2).setText("Two swipe down detected.");
                    break;
                case SWIPE_RIGHT:
                    mCards.get(2).setText("Swipe right detected.");
                    break;
                case TWO_SWIPE_RIGHT:
                    mCards.get(2).setText("Two swipe right detected.");
                    break;
                case SWIPE_LEFT:
                    mCards.get(2).setText("Swipe left detected.");
                    break;
                case TWO_SWIPE_LEFT:
                    mCards.get(2).setText("Two swipe left detected.");
                    break;
                default:
                    Log.e(TAG, "Unknown gesture detected");
            }
            mAdapter.notifyDataSetChanged();
            return true;
        });
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                Log.d(TAG, "Scroll detected. Displacement: " + displacement + ", Delta: " + delta + ", Velocity: " + velocity);
                return true;
            }
        });
        return gestureDetector;
    }
    /* Send generic motion events to the gesture detector */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            boolean handled = mGestureDetector.onMotionEvent(event);
            return handled;
        }
        return false;
    }
}