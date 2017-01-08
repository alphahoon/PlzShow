package com.example.q.plzshow.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.q.plzshow.FullRestaurant;
import com.example.q.plzshow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by q on 2017-01-05.
 */

public class reservationAdapter extends RecyclerView.Adapter<reservationAdapter.ViewHolder> {

    private final Activity activity;
    private JSONArray resArray;

    public reservationAdapter(Activity activity, JSONArray resArray) {
        assert activity != null;

        this.activity = activity;
        this.resArray = resArray;
    }


    @Override
    public int getItemCount() {
        return resArray.length();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.position = position;
        final JSONObject resobj = resArray.optJSONObject(position);
        try {
            //String url = resobj.getString("url");
            //이미지 서버 url에서 불러와야됨. loadPhoto는 불러오는 함수(밑에 있음) need to get image from server
            //Bitmap img = new loadPhoto().execute(url).get();
            //holder.imageView.setImageDrawable();
            holder.res_name.setText(resobj.getString("name"));
            holder.reserv_from_to.setText(resobj.getString("fromTo"));
            holder.reserv_num_people.setText(resobj.getString("numPeople"));
            holder.reserv_elapsed.setText(resobj.getString("elapsed"));
            holder.reserv_status.setText(resobj.getString("status"));
            //when click carview, need to start a new intent (FullRestaurant.class)

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int position;
        private CardView cardView;
        private TextView res_name;
        private TextView reserv_from_to;
        private TextView reserv_num_people;
        private TextView reserv_elapsed;
        private TextView reserv_status;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.reservation_cardView);
            res_name = (TextView) view.findViewById(R.id.txtView_reserv_res_name);
            reserv_from_to = (TextView) view.findViewById(R.id.txtView_reserv_from_to);
            reserv_num_people = (TextView) view.findViewById(R.id.txtView_reserv_num_people);
            reserv_elapsed = (TextView) view.findViewById(R.id.txtView_reserv_elapsed_time);
            reserv_status = (TextView) view.findViewById(R.id.txtView_reserv_status);

        }
    }
}


