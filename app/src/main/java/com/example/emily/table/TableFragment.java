package com.example.emily.table;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * Created by emily on 3/29/18.
 */

public class TableFragment extends Fragment {

    private ListView theListView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //Code from DemoListFragView
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        //Get Database information
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        // Read from the database
        // Code from Google's Firebase example
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        // We point the ListAdapter to our custom adapter
        TableListViewAdapter adapter = new TableListViewAdapter(container.getContext());
//        theAdapter.addAll(nameAndRatings);

        // Get the ListView so we can work with it
        theListView = (ListView) container.findViewById(R.id.theListView);
        theListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Connect the ListView with the Adapter that acts as a bridge between it and the array
        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Go to another intent
                    }
                }
        );
        return null;
    }
}
