package com.example.emily.table;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by emily on 4/2/18.
 * Code based off of DemoListViewFrag assignment
 * Meant to be used inside TableFragment
 */

public class TableListViewAdapter extends ArrayAdapter<Table> {

    static class ViewHolder {
        TextView theTextView;
        ImageView theImageView;

        public ViewHolder(View v) {
            theTextView = (TextView) v.findViewById(R.id.table_row_text);
            theImageView = (ImageView) v.findViewById(R.id.table_row_pic);
        }
    }

    private LayoutInflater theInflater = null;

    public TableListViewAdapter(Context context) {
        super(context, R.layout.table_row);
        // The LayoutInflator puts a layout into the right View
        theInflater = LayoutInflater.from(getContext());
        Log.d("ViewHolderList", "Inflate");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TableListViewAdapter.ViewHolder vh = null;
        if (convertView == null) {
            convertView = theInflater.inflate(R.layout.table_row, parent, false);
            vh = new TableListViewAdapter.ViewHolder(convertView);
            convertView.setTag(vh);
            Log.d("Adapter create ", Integer.toString(position));
        } else {
            vh = (TableListViewAdapter.ViewHolder) convertView.getTag();
        }
        // We retrieve the text from the array
        Table table = getItem(position);
        vh.theTextView.setText(table.getName());
        Log.d("ViewAdapter", "table name: " + table.getName() + " at pos " + position);
        vh.theTextView.setTextColor(Color.BLACK);
        return convertView;
    }

    public void addItem(final Table item) {
        add(item);
        notifyDataSetChanged();
    }
}
