package com.example.emily.table;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.GuestViewHolder> {

    private Context context;
    private ArrayList<User> guests;
    String tableOwner;
    String viewerId;

    public class GuestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        ImageView imageView;
        TextView desc;

        public GuestViewHolder(final View v) {
            super(v);
            textView = v.findViewById(R.id.guest_name);
            desc = v.findViewById(R.id.guest_desc);
            imageView = v.findViewById(R.id.guest_pic);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            launchProfile(getAdapterPosition());
        }
    }

    private void launchProfile(int position){
        User u = guests.get(position);
        Bundle b = new Bundle();
        b.putString("profileId", u.getId());
        b.putString("id", viewerId);
        Intent intent = new Intent(context, ExternalProfile.class);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    public GuestAdapter(Context _context, ArrayList<User> guests, String owner, String viewer) {
        tableOwner = owner;
        viewerId = viewer;
        context = _context;
        this.guests = guests;
    }

    @Override
    public GuestAdapter.GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.guest_row,parent,false);
        GuestAdapter.GuestViewHolder gvh = new GuestAdapter.GuestViewHolder(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GuestAdapter.GuestViewHolder holder, int position) {
        User u = guests.get(position);
        holder.textView.setText(u.getName());
        if(u.getId().equals(tableOwner)){
            holder.desc.setText("Owner");
        }
        Glide
                .with(getApplicationContext())
                .load(u.getPic())
                .dontTransform()
                .into(holder.imageView);
    }


    public int getItemCount() {
        return guests.size();
    }
}
