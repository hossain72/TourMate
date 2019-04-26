package com.remon.tourmate;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.remon.tourmate.databinding.FragmentTourExpenseBinding;


public class TourExpenseFragment extends Fragment {
    private FragmentTourExpenseBinding expenseBinding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String userId, tourId, tourName;
    private EditText expenseNameET, expenseET;
    private Button saveExpenseBtn;
    private DatabaseReference rootRef, userIdRef, tourRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        expenseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tour_expense, container, false);
        View view = expenseBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        clickListener();

    }

    private void clickListener() {

        expenseBinding.addExpenseFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  builder = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.add_tour_expense_dialogbox, null);

                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();

                expenseNameET = view.findViewById(R.id.expenseNameET);
                expenseET = view.findViewById(R.id.expenseET);
                saveExpenseBtn = view.findViewById(R.id.saveExpenseBtn);

                saveExpenseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String expenseName = expenseNameET.getText().toString();
                        String expense  = expenseET.getText().toString();

                        if (expenseName.equals("") && expense.equals("")){
                            Toast.makeText(getContext(), "Enter Required Field", Toast.LENGTH_SHORT).show();
                        } else {
                            saveExpenseFirebase();
                        }

                    }
                });

            }
        });

    }

    private void saveExpenseFirebase() {

    }


}
