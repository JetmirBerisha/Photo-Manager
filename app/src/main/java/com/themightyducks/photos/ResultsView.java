package com.themightyducks.photos;

import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ResultsView extends AppCompatActivity {
    private GridView topGrid;
    private FloatingActionButton fab;
    private ArrayList<CustomImage> results;
    private View albumView;
    private EditText albumDialogInput;
    public Album album;
    private boolean skip;
    private PowerManager.WakeLock wakeLock;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "photos::DoNotSleep");
        // this acquires the wake lock
//        wakeLock.acquire();
        topGrid = findViewById(R.id.top_grid);
        skip = false;
        fab = findViewById(R.id.fab_results_view);
        results = (ArrayList<CustomImage>) getIntent().getExtras().getSerializable(SearchView.RESULTS);
        album = new Album("HASH UID rictumuse foa 0x2585asd5+659wd. Discriminant is square of the second alternated partial differential subtracted by the multiplication of double partial differentials of each variable. For three dimensions that is.");
        album.getImages().addAll(results);
        topGrid.setAdapter(new ImageResultAdapter(this, album.getImages()));
        fab.setOnClickListener( v -> {
            albumView = getLayoutInflater().inflate(R.layout.album_from_results, null);
            albumDialogInput = albumView.findViewById(R.id.alert_diaog_input);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Album From Results");
            builder.setView(albumView);
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("Cancel", null);
            AlertDialog alert = builder.create();
            alert.setOnShowListener( dialog -> {
                Button ok = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button cancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                ok.setOnClickListener(vi -> {
                    String name = albumDialogInput.getText().toString();
                    if (AlbumView.imageData.containsKey(name))
                        Snackbar.make(ok, "That name already exists", Snackbar.LENGTH_LONG).show();
                    else {
                        album.setName(name);
                        AlbumView.imageData.put(name, album);
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(vie -> dialog.cancel() );
            });
            alert.show();
        });
    }


    @Override
    protected void onStop() {
        if (skip){
            skip = false;
            super.onStop();
            return;
        }
        File to = new File(getApplicationContext().getFilesDir(), AlbumView.SERIALIZETOFILE);
        try {
            FileOutputStream fos = new FileOutputStream(to);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AlbumView.imageData);
        }
        catch (Exception fnf) {
            Log.e("Serialize Fail", "Serialization failed. Metadata not saved. Images/Albums are lost");
        }
//        wakeLock.release();
        super.onStop();
        skip = true;
    }

    @Override
    protected void onPause() {
        if (skip){
            skip = false;
            super.onPause();
            return;
        }        File to = new File(getApplicationContext().getFilesDir(), AlbumView.SERIALIZETOFILE);
        try {
            FileOutputStream fos = new FileOutputStream(to);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AlbumView.imageData);
        }
        catch (Exception fnf) {
            Log.e("Serialize Fail", "Serialization failed. Metadata not saved. Images/Albums are lost");
            fnf.printStackTrace();
        }
//        wakeLock.release();
        super.onPause();
        skip = true;
    }
}
