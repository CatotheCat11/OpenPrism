package com.cato.cardsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private List<CardBuilder> mCards;
    private CardScrollView mCardScrollView;
    private ExampleCardScrollAdapter mAdapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createCards();

        mCardScrollView = new CardScrollView(this);
        mAdapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(mAdapter);
        mCardScrollView.activate();
        setupClickListener();
        setContentView(mCardScrollView);
    }

    private void createCards() {
        mCards = new ArrayList<CardBuilder>();
        mCards.add(new CardBuilder(this, CardBuilder.Layout.MENU)
                .setText("Test functions")
                .setIcon(R.drawable.ic_settings));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                .setText("A stack indicator can be added to the corner of a card to indicate that it represents a bundle of other items.")
                .setAttributionIcon(R.drawable.ic_smile)
                .showStackIndicator(true));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                .setText("This is the TEXT layout. The text size will adjust dynamically.")
                .setFootnote("This is the footnote")
                .setTimestamp("just now"));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT)
                .setText("You can also add images to the background of a TEXT card.")
                .setFootnote("This is the footnote")
                .setTimestamp("just now")
                .addImage(R.drawable.cat1)
                .addImage(R.drawable.cat2)
                .addImage(R.drawable.cat3)
                .addImage(R.drawable.cat4)
                .addImage(R.drawable.cat5));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.TEXT_FIXED)
                .setText("This is the TEXT_FIXED layout. The text size is always the same.")
                .setFootnote("This is the footnote")
                .setTimestamp("just now"));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
                .setText("This is the COLUMNS layout with dynamic text.")
                .setFootnote("This is the footnote")
                .setTimestamp("just now")
                .addImage(R.drawable.cat1)
                .addImage(R.drawable.cat2)
                .addImage(R.drawable.cat3)
                .addImage(R.drawable.cat4)
                .addImage(R.drawable.cat5));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS)
                .setText("You can even put a centered icon on a COLUMNS card instead of a mosaic.")
                .setFootnote("This is the footnote")
                .setTimestamp("just now")
                .setIcon(R.drawable.ic_wifi));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.COLUMNS_FIXED)
                .setText("This is the COLUMNS_FIXED layout. The text size is always the same.")
                .setFootnote("This is the footnote")
                .setTimestamp("just now")
                .addImage(R.drawable.cat1)
                .addImage(R.drawable.cat2)
                .addImage(R.drawable.cat3)
                .addImage(R.drawable.cat4)
                .addImage(R.drawable.cat5));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.CAPTION)
                .setText("The CAPTION layout.")
                .setFootnote("This is the footnote")
                .setTimestamp("just now")
                .addImage(R.drawable.beach)
                .setAttributionIcon(R.drawable.ic_smile));
        mCards.add(new CardBuilder(this, CardBuilder.Layout.CAPTION)
                .setText("The CAPTION layout with an icon.")
                .setFootnote("This is the footnote")
                .setTimestamp("just now")
                .addImage(R.drawable.beach)
                .setIcon(R.drawable.ic_avatar)
                .setAttributionIcon(R.drawable.ic_smile));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.TITLE)
                .setText("TITLE Card")
                .setIcon(R.drawable.ic_phone)
                .addImage(R.drawable.beach));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.AUTHOR)
                .setText("The AUTHOR layout lets you display a message or conversation with a focus on the author.")
                .setIcon(R.drawable.ic_avatar)
                .setHeading("Joe Lastname")
                .setSubheading("Mountain View, California")
                .setFootnote("This is the footnote")
                .setTimestamp("just now")
                .setAttributionIcon(R.drawable.ic_smile));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.MENU)
                .setText("MENU layout")
                .setIcon(R.drawable.ic_phone)
                .setFootnote("Optional menu description"));

        mCards.add(new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE)
                .setEmbeddedLayout(R.layout.food_table)
                .setFootnote("Foods you tracked")
                .setTimestamp("today"));
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
                    Intent intent = new Intent(MainActivity.this, FunctionsTestActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
