package com.lasalle2020android.travelcalculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Model.TripInfoModel;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private List<TripInfoModel> mTripList;

    // Pass in the contact array into the constructor
    public RecycleViewAdapter(List<TripInfoModel> trips) {
        mTripList = trips;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView countryImg;
        public TextView tripNameTextView;
        public TextView tripDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            countryImg = (ImageView) itemView.findViewById(R.id.doublerow_icon);
            tripNameTextView = (TextView) itemView.findViewById(R.id.doublerow_title);
            tripDateTextView = (TextView) itemView.findViewById(R.id.doublerow_tripDate);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.listview_row_double_img, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder viewHolder, int position) {
        TripInfoModel tripInfo = mTripList.get(position);

        // Get the country info by the countryId
        // Get the country icon from database (or maybe drawable)
        Bitmap bitmap = BitmapFactory.decodeFile("");
        ImageView imgCountry = viewHolder.countryImg;
        imgCountry.setImageResource(R.drawable.circle_canada);

        TextView textViewName = viewHolder.tripNameTextView;
        textViewName.setText(tripInfo.getTripName());

        TextView textViewDate = viewHolder.tripDateTextView;
        textViewDate.setText(tripInfo.getStartDate() + " - " + tripInfo.getEndDate());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTripList.size();
    }

    public void add(int position, TripInfoModel item) {
        mTripList.add(item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mTripList.remove(position);
        notifyItemRemoved(position);
    }
}