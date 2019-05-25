package com.themightyducks.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


public class EditPhotoView extends AppCompatActivity {
    public static final String PERSON_VALUES = "0x100";
    public static final String LOCATION_VALUES = "0x101";
    public static final String IMAGE_IDX = "0x102";
    public static final int EDIT_CODE = 0x103;

    private CustomImage img;
    private EditText person;
    private EditText location;
    private int index;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_photo_view);
        person = findViewById(R.id.person_values);
        location = findViewById(R.id.location_values);
        // Update the textfields
        String per, loc;
        Bundle bund = getIntent().getExtras();
        per = bund.getString(PERSON_VALUES);
        loc = bund.getString(LOCATION_VALUES);
        index = bund.getInt(IMAGE_IDX);
        person.setText(per);
        location.setText(loc);
    }

    /**
     * Save button handler
     * @param view The save button as a view
     */
    public void save(View view) {
        String personVals, locationVals;
        personVals = person.getText().toString();
        locationVals = location.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(PERSON_VALUES, personVals);
        bundle.putString(LOCATION_VALUES, locationVals);
        bundle.putInt(IMAGE_IDX, index);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Cancel button handler
     * @param view the cancel button as a view
     */
    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
