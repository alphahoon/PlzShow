package com.example.q.plzshow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static com.example.q.plzshow.R.id.res_full_name;
import static com.example.q.plzshow.R.id.res_full_phone;

public class FullReservation extends AppCompatActivity {
    private TextView rest_name;
    private TextView date;
    private TextView time;
    private TextView fee;
    private TextView people;
    private TextView request;
    private Button call;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_detail);

        rest_name = (TextView) findViewById(R.id.reserv_detail_rest_name);
        date = (TextView) findViewById(R.id.reserv_detail_date);
        time = (TextView) findViewById(R.id.reserv_detail_time);
        fee = (TextView) findViewById(R.id.reserv_detail_fee);
        people = (TextView) findViewById(R.id.reserv_detail_people);
        request = (TextView) findViewById(R.id.reserv_detail_request);
        call = (Button) findViewById(R.id.reserv_detail_call);
        cancel = (Button) findViewById(R.id.reserv_detail_cancel);

        call.setOnClickListener(callButtonOnClicked);
        cancel.setOnClickListener(cancelButtonOnClicked);

        try {
            Intent intent = getIntent();
            final JSONObject res_json = new JSONObject(intent.getExtras().getString("res_json"));
            String _status_msg = res_json.getString("status_msg");
            String _status_res = res_json.getString("status_res");
            String _rest_name = res_json.getString("rest_name");
            String _reserv_time = res_json.getString("reserv_time");
            String _send_time = res_json.getString("send_time");
            String _checked_time = res_json.getString("checked_time");
            String _respond_time = res_json.getString("respond_time");
            String _reserv_fee = res_json.getString("reserv_fee");
            String _request = res_json.getString("request");
            String _people = res_json.getString("people");

            if (_status_msg.equals("NOT_READ_YET")) {
            } else if (_status_msg.equals("CHECKED")) {
            } else if (_status_msg.equals("ACCEPTED")) {
            } else if (_status_msg.equals("DECLINED")) {
                cancel.setClickable(false);
                cancel.setEnabled(false);
            }

            rest_name.setText(_rest_name);
            date.setText(formatDateString(_reserv_time));
            time.setText(formatTimeString(_reserv_time));
            fee.setText(_reserv_fee + "원");
            people.setText(_people + "인");
            request.setText(_request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener callButtonOnClicked = new View.OnClickListener() {
      @Override
        public void onClick(View v) {

      }
    };

    View.OnClickListener cancelButtonOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

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

    private String formatTimeString (String datetime) {
        String AMPM = "";
        String hh = datetime.substring(11, 13);
        String mm = datetime.substring(14, 16);
        int hour = Integer.parseInt(hh);
        if (hour < 12) {
            AMPM = "오전 ";
        } else if (hour == 12) {
            AMPM = "오후 ";
        } else if (hour > 12) {
            AMPM = "오후 ";
            hour -= 12;
        }
        hh = String.valueOf(hour);

        String formatted = AMPM + trimZero(hh) + "시 " + trimZero(mm) + "분";
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
}