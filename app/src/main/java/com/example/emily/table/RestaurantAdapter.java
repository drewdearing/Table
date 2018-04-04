package com.example.emily.table;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restList){
        mContext = context;
        mData = restList;
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

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        public RestaurantViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.restaurant_text);
        }
    }


}
