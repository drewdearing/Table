package com.example.emily.table;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

/**
 * Created by emily on 4/2/18.
 * Code based off of DemoListViewFrag assignment
 * Meant to be used inside TableFragment
 */

public class TableListViewAdapter extends ArrayAdapter {

    private LayoutInflater theInflater = null;

    public TableListViewAdapter(Context context) {
        super(context, R.layout.table_row);
        // The LayoutInflator puts a layout into the right View
        theInflater = LayoutInflater.from(getContext());
        Log.d("ViewHolderList", "Inflate");
    }
}
