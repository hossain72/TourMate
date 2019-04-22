package com.remon.tourmate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remon.tourmate.databinding.FragmentTourInformationBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TourInformationFragment extends Fragment implements CustomAdapter.TourItemActionListener {

    private FragmentTourInformationBinding informationBinding;
    private CustomAdapter customAdapter;
    private List<TourInformation> informationList;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String userId, tourId;
    private DatabaseReference rootRef, userIdRef, tourRef;
    private EditText tourNameET, tourDescriptionET, tourBudgetET, startDateET, endDateET;
    private Button updateBtn;
    private TourInformation information;
    ;

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
    public void onItemUpdate(final String tourId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.update_tour_information, null);

        tourNameET = view.findViewById(R.id.tourNameET);
        tourDescriptionET = view.findViewById(R.id.tourDescriptionET);
        tourBudgetET = view.findViewById(R.id.tourBudgetET);
        startDateET = view.findViewById(R.id.startDateET);
        endDateET = view.findViewById(R.id.endDateET);
        updateBtn = view.findViewById(R.id.updateTourInformationBtn);

        tourRef.child(tourId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TourInformation tourInformation = dataSnapshot.getValue(TourInformation.class);
                tourNameET.setText(tourInformation.getTourName());
                tourDescriptionET.setText(tourInformation.getTourDescription());
                tourBudgetET.setText(tourInformation.getTourBudget());
                startDateET.setText(tourInformation.getStartDate());
                endDateET.setText(tourInformation.getEndDate());

                startDateET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        openDatePicker();
                    }
                });

                endDateET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endDatePicker();
                    }
                });

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newTourName = tourNameET.getText().toString();
                        String newTourDescription = tourDescriptionET.getText().toString();
                        String newTourBudget = tourBudgetET.getText().toString();
                        String newStartDate = startDateET.getText().toString();
                        String newEndDate = endDateET.getText().toString();
                        String id = tourId;

                        information = new TourInformation
                                (id, newTourName, newTourDescription, newTourBudget, newStartDate, newEndDate);

                        tourRef.child(id).setValue(information).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Successful Update", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void openDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                startDateET.setText("" + year + "/" + month + "/" + dayOfMonth);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
        datePickerDialog.show();

    }

    private void endDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                endDateET.setText("" + year + "/" + month + "/" + dayOfMonth);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
        datePickerDialog.show();
    }
}
