package com.example.mqtt3.LocationOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mqtt3.Datasensor;
import com.example.mqtt3.R;

public class RetailerDashboardFragment extends Fragment {

    Button mBtn_data, mBtn_motion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_retailer_dashboard, container, false);

        mBtn_data = (Button) v.findViewById(R.id.btn_data);

        mBtn_motion=(Button) v.findViewById(R.id.btn_motion);


        // function tombol
        mBtn_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(getActivity(), Datasensor.class);
                startActivity(iLogin);
            }
        });

        mBtn_motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.wearnotch.notchdemo");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(getActivity(), "There is no package available in android", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
}