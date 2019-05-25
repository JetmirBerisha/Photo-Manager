package com.themightyducks.photos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter {
    private ArrayList<Album> data;
    private int count;
    private final Context mContext;
    private final int mResource;
    private AlbumView caller;
    private long touchTime;

    public GridAdapter(Context con, int resource, ArrayList<Album> data, AlbumView call) {
        super(con, resource, data);
        mContext = con;
        mResource = resource;
        // Deep copy
        this.data = new ArrayList<>();
        this.data.addAll(data);
        caller = call;
        touchTime = 0;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    public int getCount() {
        return count;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Album getItem(int position) {
        return data.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    public long getItemId(int position) {
        // This is probably gonna cause issues
        return getView(0, null, null).getId();
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        FrameLayout root;
        TextView name;
        LinearLayout options;
        Button edit, delete;

//        if (convertView == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = (FrameLayout) inflater.inflate(mResource, parent, false);
//        }
//        else
//            root = (FrameLayout) convertView;
        name = root.findViewById(R.id.textView);
        options = root.findViewById(R.id.options);
        edit = root.findViewById(R.id.edit_btn);
        delete = root.findViewById(R.id.del_btn);
        options.setVisibility(View.INVISIBLE);

        edit.setOnClickListener((l) -> {
            // Start the edit screen
            Bundle bund = new Bundle();
            bund.putInt(AddEditAlbum.ALBUM_INDEX, data.indexOf(root));
            bund.putString(AddEditAlbum.ALBUM_NAME, name.getText().toString());
            Intent intent = new Intent(caller, AddEditAlbum.class);
            intent.putExtras(bund);
            caller.startActivityForResult(intent, AlbumView.EDIT_CODE);
        });

        delete.setOnClickListener((l) -> {
            data.remove(root);
        });
        root.setOnClickListener((lo) -> {
            /*
             Open photo view
             */
        });
        root.setOnTouchListener( (view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                options.setVisibility(View.GONE);
                touchTime = System.currentTimeMillis();
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (touchTime != 0 && System.currentTimeMillis() - touchTime > 1000){
                    touchTime = 0;
                    options.setVisibility(View.VISIBLE);
                }
            }
            return true;
        });
        return root;
    }

    /**
     * A way to retrieve the elements that this adapter stores
     * @return The arrayList of albums this adapter uses.
     */
    public ArrayList<Album> getData() {
        return data;
    }
}
