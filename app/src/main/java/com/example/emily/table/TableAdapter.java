package com.example.emily.table;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by emily on 4/2/18.
 * Code based off of DemoListViewFrag assignment
 * Meant to be used inside TableFragment
 */

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder>{
    private Context context;
    private ArrayList<Table> tables;

    public class TableViewHolder extends RecyclerView.ViewHolder{
        TextView theTextView;
        ImageView theImageView;
        Table table;

        public TableViewHolder(final View v) {
            super(v);
            theTextView = (TextView) v.findViewById(R.id.table_row_text);
            theImageView = (ImageView) v.findViewById(R.id.table_row_pic);

            //From RedFetch Homework
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TableDetails.class);
                    if (table == null) {
                        Log.d("TableViewHolder onclick", "table is null");
                    }
                    intent.putExtra("Table", table);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }


    public TableAdapter(Context _context, ArrayList<Table> tables) {
        context = _context;
        this.tables = tables;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.table_row,parent,false);
        TableViewHolder tvh = new TableViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        Table t = tables.get(position);
        holder.theTextView.setText(t.name);
        holder.table = t;
    }


    public int getItemCount() {
        return tables.size();
    }
}
