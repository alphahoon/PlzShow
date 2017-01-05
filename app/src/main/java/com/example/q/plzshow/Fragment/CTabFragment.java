package com.example.q.plzshow.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.q.plzshow.R;


public class CTabFragment extends Fragment {

    public CTabFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CTabFragment newInstance() {
        return new CTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ctab, container, false);
    }

}
