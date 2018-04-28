package com.example.emily.table;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TableDetails extends AppCompatActivity {

    private Table table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_details);
        table = (Table) getIntent().getSerializableExtra("Table");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(table.getName());
        initViews();
    }

    private void initViews() {
        TextView desc = findViewById(R.id.details_desc);
        desc.setText(table.getDescription());

        ImageView img = findViewById(R.id.details_image);
        String photoURL = table.getRestaurant().photo;
        Glide
                .with(getApplicationContext())
                .load(photoURL)
                .dontTransform()
                .into(img);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.details_guest_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GuestAdapter adapter = new GuestAdapter(this, table.getGuests());
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public void joinTable(View v) {
//
//    }
}
