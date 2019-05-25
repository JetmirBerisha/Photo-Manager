package com.themightyducks.photos;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Jetmir Berisha
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CustomImage> data;
    private HashMap<CustomImage, ImageBlock> map;

    /**
     * Adapter for GridView to create custom views for images
     * @param context The context for this view to live on
     * @param entries List of images to display
     */
    public ImageAdapter(Context context, Collection<CustomImage> entries) {
        super();
        mContext = context;
        data = new ArrayList<>();
        map = new HashMap<>();
        if (entries != null)
            data.addAll(entries);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public CustomImage getItem(int position) {
        return data.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Sets all of the linear layouts to invisible.
     */
    public void hideAllOptions() {
        for (CustomImage i : map.keySet())
            map.get(i).hideOptions();
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
    @Override
    public ConstraintLayout getView(int position, View convertView, ViewGroup parent) {
        ConstraintLayout cons;
        CustomImage link = getItem(position);
        if (map.get(link) != null) {
            cons = map.get(link).getView();
        }
        else {
            ImageBlock block = new ImageBlock(parent, mContext, link, map);
            map.put(link, block);
            cons = block.getView();
        }
        return cons;
    }
}
