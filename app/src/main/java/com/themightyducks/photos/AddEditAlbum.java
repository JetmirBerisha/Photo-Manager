package com.themightyducks.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddEditAlbum extends AppCompatActivity {
    public static final String ALBUM_INDEX = "the index of ht e asdkab a";
    public static final String ALBUM_NAME = "*CLAP* *CLAP* MEME REVIEW";
    public static final int RESULT_DELETE = 0x56;

    private String passedName;
    private EditText albumName;
    private int albumIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_album);
        getIntent().setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        getIntent().setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        albumName = findViewById(R.id.album_name);
        findViewById(R.id.add_edit_del_btn).setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            albumIndex = bundle.getInt(ALBUM_INDEX);
            passedName = bundle.getString(ALBUM_NAME);
            albumName.setText(passedName);
            findViewById(R.id.add_edit_del_btn).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Cancel button handler.
     * @param view The view that contained this button.
     */
    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Save button handler
     * @param view The view that contained this button.
     */
    public void save(View view) {
        String name = albumName.getText().toString();
        /* Build a list of album names from AlbumView */
        if (name == null || name.length() == 0) {
            Snackbar.make(view, "Album name can't be empty!", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (name.equals(passedName))
            return;
        if (AlbumView.imageData.containsKey(name)) {
            Snackbar.make(view, "An album exists with that name!", Snackbar.LENGTH_LONG).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ALBUM_INDEX, albumIndex);
        bundle.putString(ALBUM_NAME, name);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Delete button handler
     * @param view The view that represents the button
     */
    public void deletThis(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(ALBUM_INDEX, albumIndex);
        bundle.putString(ALBUM_NAME, passedName);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_DELETE, intent);
        finish();
    }
}
