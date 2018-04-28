package com.example.emily.table;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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

    public class GuestViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public GuestViewHolder(final View v) {
            super(v);
            textView = v.findViewById(R.id.guest_name);
            imageView = v.findViewById(R.id.guest_pic);
        }
    }

    public GuestAdapter(Context _context, ArrayList<User> guests) {
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
