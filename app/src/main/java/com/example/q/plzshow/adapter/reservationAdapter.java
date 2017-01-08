package com.example.q.plzshow.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.q.plzshow.FullRestaurant;
import com.example.q.plzshow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by q on 2017-01-05.
 */

public class reservationAdapter extends RecyclerView.Adapter<reservationAdapter.ViewHolder> {

    private static final int COLOR_GREY = Color.parseColor("#8c8c8c");
    private static final int COLOR_ORANGE = Color.parseColor("#d8d841");
    private static final int COLOR_RED = Color.parseColor("#d84141");
    private static final int COLOR_GREEN = Color.parseColor("#41d88c");

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
            String _status_msg = resobj.getString("status_msg");
            String _status_res = resobj.getString("status_res");
            String _rest_name = resobj.getString("rest_name");
            String _reserv_time = resobj.getString("reserv_time");
            String _send_time = resobj.getString("send_time");
            String _checked_time = resobj.getString("checked_time");
            String _respond_time = resobj.getString("respond_time");
            String _reserv_fee = resobj.getString("reserv_fee");
            String _request = resobj.getString("request");
            String _people = resobj.getString("people");

            if (_status_msg.equals("NOT_READ_YET")) {
                holder.status_color.setBackgroundColor(COLOR_GREY);
                holder.status_color.invalidate();
                holder.reserv_status.setText("읽지 않음");
            } else if (_status_msg.equals("CHECKED")) {
                holder.status_color.setBackgroundColor(COLOR_ORANGE);
                holder.status_color.invalidate();
                holder.reserv_status.setText("확인함");
            } else if (_status_msg.equals("ACCEPTED")) {
                holder.status_color.setBackgroundColor(COLOR_GREEN);
                holder.status_color.invalidate();
                holder.reserv_status.setText("수락됨");
            } else if (_status_msg.equals("DECLINED")) {
                holder.status_color.setBackgroundColor(COLOR_RED);
                holder.status_color.invalidate();
                holder.reserv_status.setText("거절됨");
            }

            holder.rest_name.setText(_rest_name);
            holder.reserv_time.setText(formatDateTimeString(_reserv_time));
            holder.reserv_num_people.setText("(" + _people + "인)");
            holder.reserv_elapsed.setText(passingTime(_send_time) + ", ");
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
        private TextView rest_name;
        private TextView reserv_time;
        private TextView reserv_num_people;
        private TextView reserv_elapsed;
        private TextView reserv_status;
        private LinearLayout status_color;

        public ViewHolder(View view) {
            super(view);
            status_color = (LinearLayout) view.findViewById(R.id.status_color_indicator);
            cardView = (CardView) view.findViewById(R.id.reservation_cardView);
            rest_name = (TextView) view.findViewById(R.id.txtView_reserv_rest_name);
            reserv_time = (TextView) view.findViewById(R.id.txtView_reserv_time);
            reserv_num_people = (TextView) view.findViewById(R.id.txtView_reserv_num_people);
            reserv_elapsed = (TextView) view.findViewById(R.id.txtView_reserv_elapsed_time);
            reserv_status = (TextView) view.findViewById(R.id.txtView_reserv_status);
        }
    }

    private String trimZero (String number) {
        return String.valueOf(Integer.parseInt(number));
    }

    private String formatDateString (String datetime) {
        String yyyy = datetime.substring(0,4);
        String mm = datetime.substring(5,7);
        String dd = datetime.substring(8,10);
        String formatted = trimZero(yyyy) + "년 " + trimZero(mm) + "월 " + trimZero(dd) + "일";
        return formatted;
    }

    private String formatDateTimeString (String datetime) {
        String yyyy = datetime.substring(0,4);
        String mm = datetime.substring(5,7);
        String dd = datetime.substring(8,10);
        String hh = datetime.substring(11, 13);
        String mm_ = datetime.substring(14, 16);
        String formatted = trimZero(yyyy) + "년 " + trimZero(mm) + "월 " + trimZero(dd) + "일 " + trimZero(hh) + "시 " + trimZero(mm_) + "분" ;
        return formatted;
    }

    private String passingTime (String datetime) {
        String passed = "";
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        try {
            Date date = dateFormat.parse(datetime);
            long passedSec = (now.getTime() - date.getTime()) / 1000;
            if (passedSec < 60) {
                passed = "방금";
            } else if (passedSec < 60 * 60) {
                passed = String.valueOf(Math.floor(passedSec / 60)) + "분 전";
            } else if (passedSec < 60 * 60 * 24) {
                passed = String.valueOf(Math.floor(passedSec / (60 * 60))) + "시간 전";
            } else if (passedSec > 60 * 60 * 24) {
                passed = String.valueOf(Math.floor(passedSec / (60 * 60))) + "일 전";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return passed;
    }
}