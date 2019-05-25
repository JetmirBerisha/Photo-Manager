package com.themightyducks.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AlbumPicker extends AppCompatActivity {
    public static final int PICK_ALBUM = 0x400;
    public static final String FILE_TO_MOVE = "0x401";
    public static final String MOVE_TO = "0x402";
    public static final String MOVE_FROM = "0x403";


    private ListView topList;
    private Album sourceAlbum;
    private HashMap<String, Album> allAlbums;
    private ArrayList<Album> albumsList;
    private CustomImage imageToMove;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_picker);
//        setResult(RESULT_CANCELED);  //It is already the default result code
        albumsList = new ArrayList<>();
        topList = findViewById(R.id.top_list);
        Bundle bundle = getIntent().getExtras();
        allAlbums = (HashMap<String, Album>) bundle.getSerializable(AlbumView.ALL_ALBUMS);
        sourceAlbum = (Album) bundle.getSerializable(MOVE_FROM);
        allAlbums.remove(sourceAlbum.getName());
        imageToMove = (CustomImage) bundle.getSerializable(FILE_TO_MOVE);
        for (String i : allAlbums.keySet())
            albumsList.add(allAlbums.get(i));
        topList.setAdapter(new ArrayAdapter<Album>(this, R.layout.album_text, albumsList));
        topList.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bun = new Bundle();
            bun.putSerializable(MOVE_FROM, sourceAlbum);
            bun.putSerializable(MOVE_TO, allAlbums.get(((TextView)view).getText()));
            bun.putSerializable(FILE_TO_MOVE, imageToMove);
            Intent intent = new Intent();
            intent.putExtras(bun);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
