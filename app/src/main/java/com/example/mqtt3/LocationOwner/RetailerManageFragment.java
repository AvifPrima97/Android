package com.example.mqtt3.LocationOwner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.mqtt3.MainActivity;
import com.example.mqtt3.R;

import helpers.ChartHelper;
import helpers.ChartHelper1;

public class RetailerManageFragment extends Fragment {
    Button mBtn_history;
    ChartHelper mChart;
    ChartHelper1 mChart1;
    //LineChart chart;
    //LineChart chart1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_retailer_manage, container, false);

        mBtn_history = (Button) v.findViewById(R.id.historysensor);
        mBtn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).findMany();
            }
        });

        return v;
    }
}
