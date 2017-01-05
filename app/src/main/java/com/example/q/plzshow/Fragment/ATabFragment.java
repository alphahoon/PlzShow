package com.example.q.plzshow.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.q.plzshow.R;
import com.example.q.plzshow.adapter.restaurantAdapter;


public class ATabFragment extends Fragment {

    private RecyclerView recyclerView;
    private CardView resCardView;
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
        resAdapter = new restaurantAdapter(getActivity(), null);


        return rootView;
    }


}
