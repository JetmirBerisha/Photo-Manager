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

public class ImageResultBlock implements Serializable {
    private ConstraintLayout root;
    private LinearLayout options;
    private ImageView image;
    private Button edit;
    private Button move;
    private Button delete;
    private HashMap<CustomImage, ImageResultBlock> mep;
    private CustomImage link;
    private Context mContext;
    private static boolean someOptions = false;

    public ImageResultBlock(ViewGroup parent, Context context, CustomImage imagePathC, HashMap<CustomImage, ImageResultBlock> map) {
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
        options.setVisibility(View.GONE);

        try {
            InputStream is = context.getContentResolver().openInputStream(imagePathC.getUri());
            image.setImageBitmap(BitmapFactory.decodeStream(is));
        }
        catch (IOException e) {
            Log.e("IOException", "Couldn't load image: " + imagePathC.getUri().toString());
            e.printStackTrace();
            System.exit(-1);
        }

        // Handlers
        image.setOnClickListener((view) -> {
            // Show a separate view for this picture
            ResultsView papa = (ResultsView) mContext;
            Bundle bund = new Bundle();
            bund.putInt(EditPhotoView.IMAGE_IDX, papa.album.indexOf(link));
            bund.putSerializable(SinglePhotoView.ALBUM_KEY, papa.album);
            Intent intent = new Intent(papa, SinglePhotoView.class);
            intent.putExtras(bund);
            papa.startActivity(intent);
        });
    }

    /**
     * Returns the container that holds the image and its buttons
     * @return the root constraint layout
     */
    public ConstraintLayout getView() {
        return root;
    }


}
