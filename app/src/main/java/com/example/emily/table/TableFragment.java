package com.example.emily.table;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by emily on 3/29/18.
 */

public class TableFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TableAdapter adapter;


    //Code from FCListRecyclerView
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.table_frag, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.tableRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "table onDataChange");
                ArrayList<Table> tables = new ArrayList<>();
                for (DataSnapshot tableSnapShot : dataSnapshot.getChildren()) {
                    Table table = tableSnapShot.getValue(Table.class);
                    tables.add(table);
                    Log.w(TAG, "added " + table.getName() + " to adapter");
                }
                adapter = new TableAdapter(container.getContext(), tables);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return v;
    }
}
