package com.themightyducks.photos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class PhotoView extends AppCompatActivity {
    public static final int RESULT_LOAD_IMAGE = 1888;
    public Album album;
    public HashMap<String, Album> allAlbums;
    public GridView topGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);
        getIntent().setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        getIntent().setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        allAlbums = new HashMap<>();
        FloatingActionButton fab = findViewById(R.id.fab_results_view);
        topGrid = findViewById(R.id.top_grid);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString(AddEditAlbum.ALBUM_NAME);
        album = (Album) bundle.getSerializable(AlbumView.ACTIVE_ALBUM);
        for ( Album alb : (ArrayList<Album>) bundle.getSerializable(AlbumView.ALL_ALBUMS))
            allAlbums.put(alb.getName(), alb);
        topGrid.setAdapter(new ImageAdapter(this, album.getImages()));
        fab.setOnClickListener((view) -> {
            ((ImageAdapter)topGrid.getAdapter()).hideAllOptions();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (null == data)
            return;
        if (requestCode == RESULT_LOAD_IMAGE) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                getApplicationContext().getContentResolver().takePersistableUriPermission(selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                String picturePath = selectedImage.getPath();
                album.addImage(new CustomImage(picturePath, selectedImage.toString()));
                allAlbums.put(album.getName(), album);
                topGrid.setAdapter(new ImageAdapter(this, album.getImages()));
            }
        }
        else if (requestCode == EditPhotoView.EDIT_CODE) {
            Bundle bundle = data.getExtras();
            CustomImage img = album.getImages().get(bundle.getInt(EditPhotoView.IMAGE_IDX));
            // parse the tags
            for (String i : bundle.getString(EditPhotoView.PERSON_VALUES).split(","))
                img.setTag("person", i.trim());
            for (String j : bundle.getString(EditPhotoView.LOCATION_VALUES).split(","))
                img.setTag("location", j.trim());
            allAlbums.put(album.getName(), album);
            topGrid.setAdapter(new ImageAdapter(this, album.getImages()));
        }
        else if (requestCode == AlbumPicker.PICK_ALBUM) {
            Bundle bundle = data.getExtras();
            CustomImage img = (CustomImage) bundle.getSerializable(AlbumPicker.FILE_TO_MOVE);
            Album source = (Album) bundle.getSerializable(AlbumPicker.MOVE_FROM);
            Album dest = (Album) bundle.getSerializable(AlbumPicker.MOVE_TO);
            source.remove(img);
            dest.addImage(img);
            allAlbums.put(source.getName(), source);
            allAlbums.put(dest.getName(), dest);
            album = allAlbums.get(album.getName());
            topGrid.setAdapter(new ImageAdapter(this, album.getImages()));
        }
    }

    @Override
    protected void onStop() {
        File to = new File(getApplicationContext().getFilesDir(), AlbumView.SERIALIZETOFILE);
        try {
            FileOutputStream fos = new FileOutputStream(to);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(allAlbums);
        }
        catch (Exception fnf) {
            Log.e("Serialize Fail", "Serialization failed. Metadata not saved. Images/Albums are lost");
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        File to = new File(getApplicationContext().getFilesDir(), AlbumView.SERIALIZETOFILE);
        try {
            FileOutputStream fos = new FileOutputStream(to);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(allAlbums);
        }
        catch (Exception fnf) {
            Log.e("Serialize Fail", "Serialization failed. Metadata not saved. Images/Albums are lost");
            fnf.printStackTrace();
        }
        super.onPause();
    }
}
