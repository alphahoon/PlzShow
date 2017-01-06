package com.example.q.plzshow.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.q.plzshow.R;
import com.example.q.plzshow.adapter.reservationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BTabFragment extends Fragment {

    private RecyclerView recyclerView;
    private reservationAdapter reservAdapter;

    public BTabFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BTabFragment newInstance() {
        return new BTabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_btab, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.reservation_recyclerView);

        JSONObject tempobj = new JSONObject();
        try {
            tempobj.put("reserv_id","12693023");
            tempobj.put("name", "류니끄");
            tempobj.put("fromTo", "2017년 1월 9일 18:00 ~ 20:00");
            tempobj.put("numPeople", "8인");
            tempobj.put("elapsed", "8분 전");
            tempobj.put("status", "미수락");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray tempArray = new JSONArray();
        tempArray.put(tempobj);
        Log.i("JSONARRAY", tempArray+"");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reservAdapter = new reservationAdapter(getActivity(), tempArray);
        recyclerView.setAdapter(reservAdapter);

        return rootView;
    }
}
