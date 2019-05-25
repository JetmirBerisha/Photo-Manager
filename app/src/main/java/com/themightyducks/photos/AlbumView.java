package com.themightyducks.photos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AlbumView extends AppCompatActivity {
    public static final String SERIALIZETOFILE = "meta.dat";
    public static final int ADD_CODE = 0x12;
    public static final int EDIT_CODE = 0x24;
    public static final int OPEN_ALBUM = 0x36;
    public static final int SEARCH_CODE = 0x72;
    public static final String ALL_ALBUMS = "0x48";
    public static final String ACTIVE_ALBUM = "0x64";

    private boolean skip;
    private PowerManager.WakeLock wakeLock;
    public ListView topG;
    public ArrayList<Album> albumAdapterFeed;
    public static HashMap<String, Album> imageData = new HashMap<>();
    private static int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        getIntent().setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        setContentView(R.layout.activity_album_view);
//        getApplicationContext().getContentResolver().takePersistableUriPermission(Uri.fromFile(getApplicationContext().getFilesDir()), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "photos::DoNotSleep");
//        this acquires the wake lock
//        wakeLock.acquire();
        skip = false;
        // Load from serialized data instead
        albumAdapterFeed = new ArrayList<>();
        albumAdapterFeed.add(new Album("Stock"));
        imageData.put("Stock", new Album("Stock"));
        topG = findViewById(R.id.topGrid);
        topG.setAdapter(new ArrayAdapter<>(this, R.layout.album_text, albumAdapterFeed));
//        topG.setAdapter(new GridAdapter(this, R.layout.single_album, albumAdapterFeed, this));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) ->{
            // New activity intent bla
            Intent intent = new Intent(this, AddEditAlbum.class);
            startActivityForResult(intent, ADD_CODE);
        });
        topG.setOnItemClickListener((parent, view, position, id) -> {
            // Open the photo view
            Bundle bundle = new Bundle();
            bundle.putInt(AddEditAlbum.ALBUM_INDEX, position);
            bundle.putString(AddEditAlbum.ALBUM_NAME, topG.getAdapter().getItem(position).toString());
            bundle.putSerializable(ALL_ALBUMS, albumAdapterFeed);
            bundle.putSerializable(ACTIVE_ALBUM, imageData.get(((TextView)view).getText()));
            Intent intent = new Intent(this, PhotoView.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        topG.setOnItemLongClickListener( (parent, view, position, id) -> {
            // Edit this album
            Bundle bundle = new Bundle();
            bundle.putInt(AddEditAlbum.ALBUM_INDEX, position);
            bundle.putString(AddEditAlbum.ALBUM_NAME, topG.getAdapter().getItem(position).toString());
            Intent intent = new Intent(this, AddEditAlbum.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, EDIT_CODE);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SearchView.class);
        startActivityForResult(intent, SEARCH_CODE);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK && resultCode != AddEditAlbum.RESULT_DELETE)
            return;
        if (data == null)
            return;
        Bundle bundle = data.getExtras();
        if (bundle == null)
            return;
        if (requestCode == ADD_CODE) {
            String name = bundle.getString(AddEditAlbum.ALBUM_NAME);
            int idx = bundle.getInt(AddEditAlbum.ALBUM_INDEX);
            Album album = new Album(name);
            albumAdapterFeed.add(album);
            imageData.put(name, album);
            topG.setAdapter(new ArrayAdapter<>(this, R.layout.album_text, albumAdapterFeed));
        }
        else if (requestCode == EDIT_CODE) {
            String name = bundle.getString(AddEditAlbum.ALBUM_NAME);
            int idx = bundle.getInt(AddEditAlbum.ALBUM_INDEX);
            Album alb = albumAdapterFeed.get(idx);
            if (resultCode == AddEditAlbum.RESULT_DELETE) {
                imageData.remove(name);
                albumAdapterFeed.remove(alb);
                topG.setAdapter(new ArrayAdapter<>(this, R.layout.album_text, albumAdapterFeed));
            }
            else {
                alb.setName(name);
                imageData.put(name, alb);
                topG.setAdapter(new ArrayAdapter<>(this, R.layout.album_text, albumAdapterFeed));
            }
        }
        else if (requestCode == SEARCH_CODE) {
            /*Launch results view*/
            Intent intent = new Intent(this, ResultsView.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        skip = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        File to = new File(getApplicationContext().getFilesDir(), SERIALIZETOFILE);
        try {
            FileOutputStream fos = new FileOutputStream(to);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(imageData);
        }
        catch (Exception fnf) {
            Log.e("Serialize Fail", "Serialization failed. Metadata not saved. Images/Albums are lost");
            fnf.printStackTrace();
        }
//        wakeLock.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (skip){
            skip = false;
            return;
        }
        File from = new File(getApplicationContext().getFilesDir(), SERIALIZETOFILE);
        try {
            FileInputStream fis = new FileInputStream(from);
            ObjectInputStream ois = new ObjectInputStream(fis);
            imageData = (HashMap<String, Album>) ois.readObject();
            albumAdapterFeed = new ArrayList<>();
            for (String name : imageData.keySet())
                albumAdapterFeed.add(imageData.get(name));
            topG.setAdapter(new ArrayAdapter<>(this, R.layout.album_text, albumAdapterFeed));
        }
        catch (FileNotFoundException fnf){
            imageData = new HashMap<>();
            Album album = new Album("Stock");
            imageData.put(album.getName(), album);
            albumAdapterFeed = new ArrayList<>();
            albumAdapterFeed.add(album);
            topG.setAdapter(new ArrayAdapter<>(this, R.layout.album_text, albumAdapterFeed));
        }
        catch (IOException e) {
            Log.e("Serialize Fail", "Serialization failed. Metadata not saved. Images/Albums are lost");
            e.printStackTrace();
        }
        catch (ClassNotFoundException clnf) {
            clnf.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        File to = new File(getApplicationContext().getFilesDir(), SERIALIZETOFILE);
        try {
            FileOutputStream fos = new FileOutputStream(to);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(imageData);
        }
        catch (Exception fnf) {
            Log.e("Serialize Fail", "Serialization failed. Metadata not saved. Images/Albums are lost");
            fnf.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        File to = new File(getApplicationContext().getFilesDir(), SERIALIZETOFILE);
        try {
            FileOutputStream fos = new FileOutputStream(to);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(imageData);
        }
        catch (Exception fnf) {
            Log.e("Serialize Fail", "Serialization failed. Metadata not saved. Images/Albums are lost");
            fnf.printStackTrace();
        }
        super.onPause();
    }

}
