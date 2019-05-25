package com.themightyducks.photos;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class SinglePhotoView extends AppCompatActivity {
    public static final String ALBUM_KEY =  "0x200";
    private Album album;
    private ScrollView topScroll;
    private int index;
    private int size;
    private ImageView imaageView;
    private TextView tagsField;
    private GestureDetector swipeDetector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_photo_view);
        topScroll = findViewById(R.id.scroll_top);
        swipeDetector = new GestureDetector(this, new SwipeListener());
        imaageView = findViewById(R.id.imageView);
        tagsField = findViewById(R.id.tags_view);
        Bundle bundle = getIntent().getExtras();
        index = bundle.getInt(EditPhotoView.IMAGE_IDX);
        album = (Album) bundle.getSerializable(ALBUM_KEY);
        size = album.getImages().size();

        try {
            InputStream is = getContentResolver().openInputStream(album.getImages().get(index).getUri());
            imaageView.setImageBitmap(BitmapFactory.decodeStream(is));
        }
        catch (IOException e) {
            Log.e("IOException", "Couldn't load image: " + album.getImages().get(index).getUri().toString());
            e.printStackTrace();
        }
        topScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeDetector.onTouchEvent(event);
                return false;
            }
        });
        tagsField.setText("Tags: " + album.getImages().get(index).getAllTagsAsString());
//        imaageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                swipeDetector.onTouchEvent(event);
//                return true;
//            }
//        });
    }

    class SwipeListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("\nTag", "velocityX: " + velocityX + ", velocityY: " + velocityY);
            if (velocityX > 2500) {
                // Previous pic
                if (index == 0)
                    index = size - 1;
                else
                    index--;
                try {
                    InputStream is = getContentResolver().openInputStream(album.getImages().get(index).getUri());
                    imaageView.setImageBitmap(BitmapFactory.decodeStream(is));
                    tagsField.setText("Tags: " + album.getImages().get(index).getAllTagsAsString());
                }
                catch (IOException e) {
                    Log.e("IOException", "Couldn't load image: " + album.getImages().get(index).getUri().toString());
                    e.printStackTrace();
                }
            }
            else if (velocityX < -2500) {
                // Next pic
                if (index == size - 1)
                    index = 0;
                else
                    index++;
                try {
                    InputStream is = getContentResolver().openInputStream(album.getImages().get(index).getUri());
                    imaageView.setImageBitmap(BitmapFactory.decodeStream(is));
                    tagsField.setText("Tags: " + album.getImages().get(index).getAllTagsAsString());
                }
                catch (IOException e) {
                    Log.e("IOException", "Couldn't load image: " + album.getImages().get(index).getUri().toString());
                    e.printStackTrace();
                }
            }
            return false;
        }
    }
}
