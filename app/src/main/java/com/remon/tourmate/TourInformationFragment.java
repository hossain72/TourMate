package com.remon.tourmate;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.remon.tourmate.databinding.FragmentTourInformationBinding;

public class TourInformationFragment extends Fragment {

    private FragmentTourInformationBinding informationBinding;

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

        clickListener();

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
}
