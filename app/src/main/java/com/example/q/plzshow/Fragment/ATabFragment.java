package com.example.q.plzshow.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.q.plzshow.R;
import com.example.q.plzshow.adapter.restaurantAdapter;
import com.example.q.plzshow.sendToServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.q.plzshow.sendToServer.sendJSON;

import java.util.concurrent.ExecutionException;


public class ATabFragment extends Fragment {

    private RecyclerView recyclerView;
    private restaurantAdapter resAdapter;

    public ATabFragment() {
        // Required empty public constructor
    }

    public static ATabFragment newInstance() {
        return new ATabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_atab, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.res_recyclerView);

        JSONObject getObj = new JSONObject();
        try {
            getObj.put("type", "GET_REST_LIST");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // GET RESPONSE
        JSONObject res = null;
        try {
            res = new sendJSON(getString(R.string.server_ip), getObj.toString(), "application/json").execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.e("responseATab", res.toString());

        JSONArray resArray= new JSONArray();
        try {
            resArray = res.getJSONArray("restaurants");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resAdapter = new restaurantAdapter(getActivity(), resArray);
        recyclerView.setAdapter(resAdapter);


        return rootView;
    }


}
