package com.example.emily.table;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class TableDetails extends AppCompatActivity {

    private Table table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_details);

        table = (Table) getIntent().getSerializableExtra("Table");
        initViews();
    }

    private void initViews() {
        TextView desc = findViewById(R.id.table_details_desc);
        desc.setText(table.getDescription());

        TextView name = findViewById(R.id.table_details_name);
        name.setText(table.getName());
    }

}
