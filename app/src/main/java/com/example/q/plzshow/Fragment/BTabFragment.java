package com.example.q.plzshow.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.q.plzshow.sendToServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class BTabFragment extends Fragment {
    private RecyclerView recyclerView;
    private reservationAdapter reservAdapter;
    private String user_id;

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
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        user_id = pref.getString("user_id", "");

        JSONObject getObj = new JSONObject();
        try {
            getObj.put("type", "GET_RESERV_LIST");
            getObj.put("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // GET RESPONSE
        sendToServer server = new sendToServer();
        JSONObject res = null;
        try {
            res = new sendToServer.sendJSON(getString(R.string.server_ip), getObj.toString(), "application/json").execute().get();
            Log.e("REQUEST", getObj.toString());
            Log.e("RESERV_LIST", res.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONArray resArray= new JSONArray();
        try {
            resArray = res.getJSONArray("reservations");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reservAdapter = new reservationAdapter(getActivity(), resArray);
        recyclerView.setAdapter(reservAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_btab, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.reservation_recyclerView);
        return rootView;
    }
}
