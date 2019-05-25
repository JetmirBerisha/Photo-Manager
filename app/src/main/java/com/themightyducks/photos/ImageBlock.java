package com.themightyducks.photos;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;

public class ImageBlock implements Serializable {
    private ConstraintLayout root;
    private LinearLayout options;
    private ImageView image;
    private Button edit;
    private Button move;
    private Button delete;
    private HashMap<CustomImage, ImageBlock> mep;
    private CustomImage link;
    private Context mContext;
    private static boolean someOptions = false;

    public ImageBlock(ViewGroup parent, Context context, CustomImage imagePathC, HashMap<CustomImage, ImageBlock> map) {
        mep = map;
        link = imagePathC;
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        root = (ConstraintLayout) inflater.inflate(R.layout.image_block, parent, false);
        image = root.findViewById(R.id.imgView);
        options = root.findViewById(R.id.options);

        edit = root.findViewById(R.id.edit_img_button);
        move = root.findViewById(R.id.move_img_button);
        delete = root.findViewById(R.id.delete_img_button);
        hideOptions();

        try {
            InputStream is = context.getContentResolver().openInputStream(imagePathC.getUri());
            image.setImageBitmap(BitmapFactory.decodeStream(is));
            is.close();
        }
        catch (IOException e) {
            Log.e("IOException", "Couldn't load image: " + imagePathC.getUri().toString());
            e.printStackTrace();
            System.exit(-1);
        }
        // Handlers
        image.setOnClickListener((view) -> {
            if (ImageBlock.someOptions)
                hideAllOpts();
            // Show a separate view for this picture
            PhotoView papa = (PhotoView) mContext;
            Bundle bund = new Bundle();
            bund.putInt(EditPhotoView.IMAGE_IDX, papa.album.indexOf(link));
            bund.putSerializable(SinglePhotoView.ALBUM_KEY, papa.album);
            Intent intent = new Intent(papa, SinglePhotoView.class);
            intent.putExtras(bund);
            papa.startActivity(intent);
        });
        image.setOnLongClickListener((l) -> {
            if (ImageBlock.someOptions)
                hideAllOpts();
            // Show the options for this image
            showOptions();
            return true;
        });
        edit.setOnClickListener((view) -> {
            if (ImageBlock.someOptions)
                hideAllOpts();
            PhotoView papa = (PhotoView) mContext;
            // Open a new view with all of the bla
            Bundle bundle = new Bundle();
            bundle.putInt(EditPhotoView.IMAGE_IDX, papa.album.indexOf(link));
            bundle.putString(EditPhotoView.LOCATION_VALUES, link.getTagAsString("location"));
            bundle.putString(EditPhotoView.PERSON_VALUES, link.getTagAsString("person"));
            Intent intent = new Intent(papa, EditPhotoView.class);
            intent.putExtras(bundle);
            papa.startActivityForResult(intent, EditPhotoView.EDIT_CODE);
        });
        move.setOnClickListener((view) -> {
            if (ImageBlock.someOptions)
                hideAllOpts();
            // Check if this is the only album and open a snackbar
            if (AlbumView.imageData.size() == 1){
                Snackbar.make((View)root.getParent(), "You only have one album!" , Snackbar.LENGTH_LONG).show();
                return;
            }
            // If there are other albums pop up a selection list or open another view like AlbumView
            PhotoView papa = (PhotoView) mContext;
            Bundle bundle = new Bundle();
            bundle.putSerializable(AlbumPicker.MOVE_FROM, papa.album);
            bundle.putSerializable(AlbumView.ALL_ALBUMS, papa.allAlbums);
            bundle.putSerializable(AlbumPicker.FILE_TO_MOVE, link);
            Intent intent = new Intent(papa, AlbumPicker.class);
            intent.putExtras(bundle);
            papa.startActivityForResult(intent, AlbumPicker.PICK_ALBUM);
        });
        delete.setOnClickListener((view) -> {
            if (ImageBlock.someOptions)
                hideAllOpts();
            PhotoView papa = (PhotoView) mContext;
            papa.album.remove(link);
            papa.allAlbums.put(papa.album.getName(), papa.album);
            papa.topGrid.setAdapter(new ImageAdapter(papa, papa.album.getImages()));
        });
    }

    /**
     * Returns the container that holds the image and its buttons
     * @return the root constraint layout
     */
    public ConstraintLayout getView() {
        return root;
    }

    /**
     * Hides the options for this photo.
     */
    public void hideOptions() {
        options.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the options for long tapping this image.
     */
    public void showOptions() {
        ImageBlock.someOptions = true;
        options.setVisibility(View.VISIBLE);
    }

    /**
     * Hide all of the image's options. Same as hideAllOptions in ImageAdapter
     */
    public void hideAllOpts() {
        ImageBlock.someOptions = false;
        // Hide every image's option
        for (CustomImage i : mep.keySet())
            mep.get(i).hideOptions();
    }

    /**
     * Returns whether this view has visible options
     * @return true if this view is showing options and false otherwise
     */
    public boolean hasVisibleOptions() {
        return options.getVisibility() == View.VISIBLE;
    }
}
