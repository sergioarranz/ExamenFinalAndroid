package com.utad.sergio.examenandroid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarkerProfileFragment extends Fragment {

    public TextView txtUser,txtNumber;

    public MarkerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_marker_profile, container, false);
        txtUser = v.findViewById(R.id.txtUser);
        txtNumber = v.findViewById(R.id.txtNumber);
        return v;
    }

}
