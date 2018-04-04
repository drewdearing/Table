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
    private TableListViewAdapter adapter;


    //Code from DemoListFragView
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = new TableListViewAdapter(container.getContext());

        // Code from Google's Firebase example
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG, "table onDataChange");
                for (DataSnapshot tableSnapShot : dataSnapshot.getChildren()) {
                    Table table = tableSnapShot.getValue(Table.class);
                    adapter.addItem(table);
                    Log.w(TAG, "added " + table.getName() + " to adapter");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return inflater.inflate(R.layout.table_frag, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        // Get the ListView so we can work with it

        //This line wont work as of rn, it will work if I insert a list view into the Home.xml
        theListView = (ListView) getView().findViewById(R.id.tableListView);
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
    }
}
