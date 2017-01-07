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

/**
 * Created by q on 2017-01-07.
 */

public class Reservation extends AppCompatActivity{

    EditText reserv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_full);
        Intent intent = getIntent();
        final String res_id = intent.getExtras().getString("res_id");
        String res_name = intent.getExtras().getString("res_name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.reserv_toolbar);
        toolbar.setTitle("예약하기");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView res_reserv_name = (TextView) findViewById(R.id.res_reserv_name);
        res_reserv_name.setText(res_name);

        //인원
        Button btn_minus = (Button) findViewById(R.id.btn_minus);
        Button btn_plus = (Button) findViewById(R.id.btn_plus);
        final EditText edit_text = (EditText) findViewById(R.id.edit_text);
        btn_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int number = Integer.valueOf(edit_text.getText().toString());
                if (number > 0)
                    edit_text.setText(String.valueOf(number-1));
            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int number =  Integer.valueOf(edit_text.getText().toString());
                edit_text.setText(String.valueOf(number+1));
            }
        });

        // Calendar for date using fragment. DatePicker
        reserv_date = (EditText) findViewById(R.id.reserv_date);
        reserv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        // TimePicker using timepickerDialog
        final EditText reserv_time = (EditText) findViewById(R.id.reserv_time);
        reserv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        reserv_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        Button reserv_button = (Button) findViewById(R.id.reserv_button);
        reserv_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDlg = new AlertDialog.Builder(v.getContext());
                alertDlg.setView(R.layout.reservation_confirm);
                final AlertDialog dialog = alertDlg.show();

                TextView reserv_confirm_name = (TextView) dialog.findViewById(R.id.reserv_confirm_name);
                final TextView reserv_confirm_date = (TextView) dialog.findViewById(R.id.reserv_confirm_date);
                final TextView reserv_confirm_time = (TextView) dialog.findViewById(R.id.reserv_confirm_time);
                final TextView reserv_confirm_people = (TextView) dialog.findViewById(R.id.reserv_confirm_people);
                final TextView reserv_confirm_fee = (TextView) dialog.findViewById(R.id.reserv_confirm_fee);
                final TextView reserv_confirm_request = (TextView) dialog.findViewById(R.id.reserv_confirm_request);
                Button reserv_confirm_cancel = (Button) dialog.findViewById(R.id.reserv_confirm_cancel);
                Button reserv_confirm_confirm = (Button) dialog.findViewById(R.id.reserv_confirm_confirm);

                reserv_confirm_cancel.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                reserv_confirm_confirm.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        JSONObject resObj = new JSONObject();
                        try {
                            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                            String user_id = pref.getString("user_id", "");

                            resObj.put("user_id", user_id);
                            resObj.put("restaurant_id", res_id);
                            resObj.put("date", reserv_confirm_date.getText());
                            resObj.put("time", reserv_confirm_time.getText());
                            resObj.put("people", reserv_confirm_people.getText());
                            resObj.put("reserv_fee", reserv_confirm_fee.getText());
                            resObj.put("request", reserv_confirm_request.getText());
                            resObj.put("timestamp", System.currentTimeMillis());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        });
    }

    public Context getActivity() {
        return this;
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            Log.d("testMK", formattedDate);
            EditText editText = (EditText) getActivity().findViewById(R.id.reserv_date);
            editText.setText(formattedDate);
        }
    }

}

