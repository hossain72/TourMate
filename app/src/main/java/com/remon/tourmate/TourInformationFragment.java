package com.remon.tourmate;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remon.tourmate.databinding.FragmentTourInformationBinding;

import java.util.ArrayList;
import java.util.List;

public class TourInformationFragment extends Fragment implements CustomAdapter.TourItemActionListener {

    private FragmentTourInformationBinding informationBinding;
    private CustomAdapter customAdapter;
    private List<TourInformation> informationList;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String userId, tourId;
    private DatabaseReference rootRef, userIdRef, tourRef;
    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        informationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_information, container, false);
        View view = informationBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        initialization();
        clickListener();
        initRecyclerView();
        getTourInformationDb();

    }

    private void initialization() {

        informationList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

    }

    private void clickListener() {

        informationBinding.addTourFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTourInformationBottomSheetDialog bottomSheetDialog = new AddTourInformationBottomSheetDialog();
                bottomSheetDialog.show(getFragmentManager(), "addTourInformation");
            }
        });

    }

    private void initRecyclerView() {
        informationBinding.tourInformationList.setLayoutManager(new LinearLayoutManager(getContext()));
        customAdapter = new CustomAdapter(informationList, getContext(), this);
        informationBinding.tourInformationList.setAdapter(customAdapter);
    }

    private void getTourInformationDb() {

        rootRef = firebaseDatabase.getReference().child("tourMate");
        userIdRef = rootRef.child("userId").child(userId);
        tourRef = userIdRef.child("tourInformation");

        tourRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    informationList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        tourId = data.getKey();
                        TourInformation tourInformation = data.getValue(TourInformation.class);
                        informationList.add(tourInformation);
                        customAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onItemDelete(String tourId) {
        tourRef.child(tourId).removeValue();
    }

    @Override
    public void onItemUpdate(String tourId) {

    }
}
