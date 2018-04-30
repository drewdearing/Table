package com.example.emily.table;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by drewdearing on 4/4/18.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context mContext;
    private ArrayList<Restaurant> mData;
    private String userId;

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restList, String userId){
        mContext = context;
        mData = restList;
        this.userId = userId;
    }

    private void createTable(int position){
        Restaurant tableRestaurant = mData.get(position);
        Intent form = new Intent(mContext, Form.class);
        Bundle b = new Bundle();
        b.putString("name", tableRestaurant.name);
        b.putString("photo", tableRestaurant.photo);
        b.putString("userId", userId);
        b.putString("address", tableRestaurant.address);
        b.putDouble("lat", tableRestaurant.lat);
        b.putDouble("lon", tableRestaurant.lon);
        form.putExtras(b);
        mContext.startActivity(form);
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.restaurant_row, parent, false);
        return new RestaurantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        Restaurant restaurant = mData.get(position);
        holder.text.setText(restaurant.name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView text;
        public RestaurantViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.restaurant_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            createTable(getAdapterPosition());
        }
    }


}
