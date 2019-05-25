package com.themightyducks.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class SearchView extends AppCompatActivity {
    public static final String TAGS_KEY = "0x600";
    public static final String RESULTS = "0x601";
    private EditText tag1, tag2, value1, value2;
    private Button andOrButton, cancel, search;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);
        tag1 = findViewById(R.id.tag1);
        tag2 = findViewById(R.id.tag2);
        value1 = findViewById(R.id.value1);
        value2 = findViewById(R.id.value2);
        andOrButton = findViewById(R.id.and_or_button);
        cancel = findViewById(R.id.cancel_search_btn);
        search = findViewById(R.id.search_search_btn);

        andOrButton.setOnClickListener((view) -> andOrButton.setText(("AND".equals(andOrButton.getText().toString()))? "OR": "AND"));
        cancel.setOnClickListener((view) -> finish());
        search.setOnClickListener((view) -> {
            String t1 = tag1.getText().toString();
            String t2 = tag2.getText().toString();
            String v1 = value1.getText().toString();
            String v2 = value2.getText().toString();
            Searcher searcher = new Searcher(null, t1 + "=" + v1 + (andOrButton.getText().toString().equals("AND")? "&&" : "||") + t2 + "=" + v2);
            ArrayList<CustomImage> results =  searcher.search();
            if (results.size() == 0) {
                Snackbar.make(tag1, "That query has no results!", Snackbar.LENGTH_LONG).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(RESULTS, results);
            Intent inte = new Intent();
            inte.putExtras(bundle);
            setResult(RESULT_OK, inte);
            finish();
        });
    }
}
