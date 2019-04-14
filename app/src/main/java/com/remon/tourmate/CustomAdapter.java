package com.remon.tourmate;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.remon.tourmate.databinding.TourInformationBinding;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private TourInformationBinding tourInformationBinding;
    private List<TourInformation> informationList;
    private Context context;

    public CustomAdapter(List<TourInformation> informationList, Context context) {
        this.informationList = informationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        tourInformationBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.tour_information, viewGroup, false);

        return new ViewHolder(tourInformationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        TourInformation tourInformation = informationList.get(i);

        viewHolder.informationBinding.tourNameTV.setText(tourInformation.getTourName());
        viewHolder.informationBinding.tourDescriptionTV.setText(tourInformation.getTourDescription());
        viewHolder.informationBinding.startDateTV.setText(tourInformation.getStartDate());
        viewHolder.informationBinding.endDateTV.setText(tourInformation.getEndDate());

    }

    @Override
    public int getItemCount() {
        return informationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TourInformationBinding informationBinding;

        public ViewHolder(TourInformationBinding tourInformationBinding) {
            super(tourInformationBinding.getRoot());

            informationBinding = tourInformationBinding;

        }
    }
}
