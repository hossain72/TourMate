package com.remon.tourmate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.remon.tourmate.databinding.AddTourInformationBinding;

import java.util.Calendar;

public class AddTourInformationBottomSheetDialog extends BottomSheetDialogFragment {

    View view;
    private AddTourInformationBinding addTourInformationBinding;
    private EditText tourNameET, tourDescriptionET, startDateET, endDateET;
    private Button saveTourInformationBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_tour_information, container, false);

        initialize();
        clickListener();


        return view;
    }

    private void initialize() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        tourNameET = view.findViewById(R.id.tourNameET);
        tourDescriptionET = view.findViewById(R.id.tourDescriptionET);
        startDateET = view.findViewById(R.id.startDateET);
        endDateET = view.findViewById(R.id.endDateET);
        saveTourInformationBtn = view.findViewById(R.id.saveTourInformationBtn);
    }

    private void clickListener() {

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

        saveTourInformationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tourName = tourNameET.getText().toString();
                String tourDescription = tourDescriptionET.getText().toString();
                String stratDate = startDateET.getText().toString();
                String endDate = endDateET.getText().toString();

                if (tourName.equals("") && tourDescription.equals("") && stratDate.equals("") && endDate.equals("")) {
                    Toast.makeText(getActivity(), "Enter required field", Toast.LENGTH_SHORT).show();
                } else {
                    saveTourInformationDatabase(new TourInformation(tourName, tourDescription, stratDate, endDate));
                }

            }
        });

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

    private void saveTourInformationDatabase(TourInformation tourInformation) {

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("tourMate").child("tourInformation");

        String tourID = databaseReference.push().getKey();
        tourInformation.setTourId(tourID);

        databaseReference.child(tourID).setValue(tourInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Save Information", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
