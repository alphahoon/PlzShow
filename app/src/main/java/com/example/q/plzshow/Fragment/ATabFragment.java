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

        sendToServer server = new sendToServer();
        server.get(getObj);


        JSONObject tempobj = new JSONObject();
        try {
            tempobj.put("res_id","12693023");
            tempobj.put("name", "류니끄");
            tempobj.put("location", "가로수길·컨템퍼러리");
            tempobj.put("description", "가로수길에 위치한 류태환 셰프의 비스트로");
            tempobj.put("phone", "전화번호: 02-546-9279");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray tempArray = new JSONArray();
        tempArray.put(tempobj);
        Log.i("JSONARRAY", tempArray+"");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        resAdapter = new restaurantAdapter(getActivity(), tempArray);
        recyclerView.setAdapter(resAdapter);


        return rootView;
    }


}
