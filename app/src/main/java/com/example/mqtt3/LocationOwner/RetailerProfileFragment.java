package com.example.mqtt3.LocationOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mqtt3.Authentication.LoginActivity;
import com.example.mqtt3.MainActivity;
import com.example.mqtt3.R;
import com.example.mqtt3.SharedPreferences.MyPreferences;

public class RetailerProfileFragment extends Fragment {

    private EditText nameEditText;
    private EditText ageEditText;
    private TextView nameTextView;
    private TextView ageTextView;
    private EditText genderEditText;
    private TextView genderTextView;
    private EditText weightEditText;
    private TextView weightTextView;
    private EditText heightEditText;
    private TextView heightTextView;

    Button mBtn_login, mBtn_logout;

    private MyPreferences myPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_retailer_profile, container, false);

        mBtn_login=(Button) v.findViewById(R.id.btn_login);
        mBtn_logout=(Button) v.findViewById(R.id.logoutButton);

        nameEditText = v.findViewById(R.id.nameEditText);
        ageEditText = v.findViewById(R.id.ageEditText);
        genderEditText = v.findViewById(R.id.genderEditText);
        genderTextView = v.findViewById(R.id.genderTextView);
        weightEditText = v.findViewById(R.id.weightEditText);
        weightTextView = v.findViewById(R.id.weightTextView);
        heightEditText = v.findViewById(R.id.heightEditText);
        heightTextView = v.findViewById(R.id.heightTextView);
        nameTextView = v.findViewById(R.id.nameTextView);
        ageTextView = v.findViewById(R.id.ageTextView);
        Button btnsavename = v.findViewById(R.id.nameSaveButton);
        Button btnsaveage = v.findViewById(R.id.ageSaveButton);
        Button btnsavegender = v.findViewById(R.id.genderSaveButton);
        Button btnsaveweight = v.findViewById(R.id.weightSaveButton);
        Button btnsaveheight = v.findViewById(R.id.heightSaveButton);

        btnsavename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                myPreferences.setUserName(name);
                showUserName();
            }
        });

        btnsaveage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    int age = Integer.parseInt(ageEditText.getText().toString());
                    myPreferences.setAge(age);

                    showUserAge();
            }
        });

        btnsavegender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender = genderEditText.getText().toString();
                myPreferences.setGender(gender);
                showGender();
            }
        });

        btnsaveweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = weightEditText.getText().toString();
                myPreferences.setWeight(weight);
                showWeight();
            }
        });

        btnsaveheight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String height = heightEditText.getText().toString();
                myPreferences.setHeight(height);
                showHeight();
            }
        });

        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(iLogin);
            }
        });

        mBtn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).logout();
            }
        });


        myPreferences = MyPreferences.getPreferences(getActivity());

        showUserName();
        showUserAge();
        showGender();
        showWeight();
        showHeight();
        //showStudentStatus();


        return v;
    }

    private void showUserName() {
        String name = myPreferences.getUserName();
        nameTextView.setText(name);
    }

    private void showUserAge(){
        int age = myPreferences.getAge();
        ageTextView.setText(String.valueOf(age));
    }

    private void showGender(){
        String gender = myPreferences.getGender();
        genderTextView.setText(gender);
    }

    private void showWeight(){
        String weight = myPreferences.getWeight();
        weightTextView.setText(weight);
    }

    private void showHeight(){
        String height = myPreferences.getHeight();
        heightTextView.setText(height);
    }
}
