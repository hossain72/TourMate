package com.remon.tourmate;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remon.tourmate.databinding.TourInformationBinding;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private TourInformationBinding tourInformationBinding;
    private List<TourInformation> informationList;
    private Context context;
    private ImageView popUpMenu;
    private FirebaseDatabase firebaseDatabase;
    private String tourID;
    private TourItemActionListener listener;

    public CustomAdapter(List<TourInformation> informationList, Context context, Fragment fragment) {
        this.informationList = informationList;
        this.context = context;
        listener = (TourItemActionListener) fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        tourInformationBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.tour_information, viewGroup, false);

        return new ViewHolder(tourInformationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        firebaseDatabase = FirebaseDatabase.getInstance();

        TourInformation tourInformation = informationList.get(i);

        viewHolder.informationBinding.tourNameTV.setText(tourInformation.getTourName());
        viewHolder.informationBinding.tourDescriptionTV.setText(tourInformation.getTourDescription());
        viewHolder.informationBinding.tourBudgetTV.setText(tourInformation.getTourBudget());
        viewHolder.informationBinding.startDateTV.setText(tourInformation.getStartDate());
        viewHolder.informationBinding.endDateTV.setText(tourInformation.getEndDate());

        viewHolder.informationBinding.popUpMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, viewHolder.informationBinding.popUpMenuIV);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String tourIdRef = informationList.get(i).getTourId();
                        switch (item.getItemId()) {

                            case R.id.deleteItem:
                                listener.onItemDelete(tourIdRef);
                                break;
                            case R.id.updateItem:
                                listener.onItemUpdate(tourIdRef);
                                break;

                        }

                        return false;
                    }
                });

            }
        });

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
    interface TourItemActionListener{
        void onItemDelete(String tourId);
        void onItemUpdate(String tourId);
    }
}
