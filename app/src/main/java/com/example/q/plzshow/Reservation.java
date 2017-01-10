package com.example.q.plzshow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.FragmentTransaction;
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
import android.text.Editable;
import android.text.TextWatcher;
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


/**
 * Created by q on 2017-01-07.
 */

public class Reservation extends AppCompatActivity{

    String formattedTime;
    String formattedDate;

    EditText reserv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_full);
        Intent intent = getIntent();
        final String res_id = intent.getExtras().getString("res_id");
        final String res_name = intent.getExtras().getString("res_name");
        final String reserv_price = intent.getExtras().getString("reserv_price");
        final String res_phone = intent.getExtras().getString("res_phone");
        int index = reserv_price.indexOf("원");
        String reserv_price_parsed = reserv_price.substring(0, index);
        final int reserv_price_int = Integer.parseInt(reserv_price_parsed);

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
        final TextView reserv_fee = (TextView) findViewById(R.id.reserv_fee);

        //인원
        Button btn_minus = (Button) findViewById(R.id.btn_minus);
        Button btn_plus = (Button) findViewById(R.id.btn_plus);
        final EditText reserv_people = (EditText) findViewById(R.id.edit_text);
        btn_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int number = Integer.valueOf(reserv_people.getText().toString());
                if (number > 0){
                    reserv_people.setText(String.valueOf(number-1));
                    reserv_fee.setText(Integer.parseInt(String.valueOf(reserv_people.getText())) * reserv_price_int + "원");
                }

            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int number =  Integer.valueOf(reserv_people.getText().toString());
                reserv_people.setText(String.valueOf(number+1));
                reserv_fee.setText(Integer.parseInt(String.valueOf(reserv_people.getText())) * reserv_price_int + "원");
            }
        });

        // Calendar for date using fragment. DatePicker
        reserv_date = (EditText) findViewById(R.id.reserv_date);
        reserv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        formattedDate = year+"-"+formatMonth(String.valueOf(month))+"-"+formatMonth(String.valueOf(dayOfMonth));
                        reserv_date.setText(year+"년 "+(month)+"월 "+dayOfMonth+"일");
                        Log.e("formattedDateMK", formattedDate);
                    }
                }, year, month, day);
                mDatePicker.show();
            }

        });

        // TimePicker using timepickerDialog
        final EditText reserv_time = (EditText) findViewById(R.id.reserv_time);
        reserv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        formattedTime = formatMonth(String.valueOf(selectedHour))+":"+formatMonth(String.valueOf(selectedMinute));
                        String APM = "오전";
                        if (selectedHour > 11){
                            if (selectedHour !=12)
                                selectedHour = selectedHour - 12;
                            APM = "오후";
                        }
                        Log.e("formattedTimeMK", formattedTime);
                        reserv_time.setText(APM + " " + selectedHour +"시 " + selectedMinute + "분");
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        final TextView reserv_text_length = (TextView) findViewById(R.id.reserv_text_length);
        final EditText reserv_request = (EditText) findViewById(R.id.reserv_request);
        reserv_request.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reserv_text_length.setText(reserv_request.length() + "/50");
            }

            @Override
            public void afterTextChanged(Editable s) {
                reserv_text_length.setText(reserv_request.length() + "/50");
            }
        });


        //when click 예약 접수 button
        Button reserv_button = (Button) findViewById(R.id.reserv_button);
        reserv_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                int coin = Integer.parseInt(pref.getString("coin", ""));
                final AlertDialog.Builder alertDlg = new AlertDialog.Builder(v.getContext());

                if (Integer.parseInt(String.valueOf(reserv_people.getText())) == 0){
                    alertDlg.setTitle("인원은 0명보다 많아야 합니다.");
                    alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDlg.show();
                }

                else if (coin > Integer.parseInt(String.valueOf(reserv_people.getText())) * reserv_price_int){
                    Log.d("reserv_date", String.valueOf(reserv_date.getText()));
                    Log.d("reserv_time", String.valueOf(reserv_time.getText()));
                    if (String.valueOf(reserv_date.getText()).contains("날짜")){
                        alertDlg.setTitle("날짜를 선택해주세요");
                        alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDlg.show();
                    }
                    else if (String.valueOf(reserv_time.getText()).contains("시간")){
                        alertDlg.setTitle("시간을 선택해주세요");
                        alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDlg.show();
                    }
                    else {
                        //all condition satisfied
                        alertDlg.setView(R.layout.reservation_confirm);
                        final AlertDialog dialog = alertDlg.show();

                        dialog.setCanceledOnTouchOutside(true);

                        TextView reserv_confirm_name = (TextView) dialog.findViewById(R.id.reserv_confirm_name);
                        final TextView reserv_confirm_date = (TextView) dialog.findViewById(R.id.reserv_confirm_date);
                        final TextView reserv_confirm_time = (TextView) dialog.findViewById(R.id.reserv_confirm_time);
                        final TextView reserv_confirm_people = (TextView) dialog.findViewById(R.id.reserv_confirm_people);
                        final TextView reserv_confirm_fee = (TextView) dialog.findViewById(R.id.reserv_confirm_fee);
                        final TextView reserv_confirm_request = (TextView) dialog.findViewById(R.id.reserv_confirm_request);
                        Button reserv_confirm_cancel = (Button) dialog.findViewById(R.id.reserv_confirm_cancel);
                        Button reserv_confirm_confirm = (Button) dialog.findViewById(R.id.reserv_confirm_confirm);

                        reserv_confirm_name.setText(res_name);
                        Log.e("date", formattedDate+" "+formattedTime);
                        reserv_confirm_date.setText(reserv_date.getText());
                        reserv_confirm_time.setText(reserv_time.getText());
                        reserv_confirm_people.setText(reserv_people.getText()+"명");
                        reserv_confirm_fee.setText(Integer.parseInt(String.valueOf(reserv_people.getText())) * reserv_price_int+"원");
                        String request = String.valueOf(reserv_request.getText());
                        Log.d("request", request);
                        if (request == "")
                            reserv_confirm_request.setText("요청 사항 없음");
                        else
                            reserv_confirm_request.setText(reserv_request.getText());

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
                                    Log.d("Date", formattedDate+" "+formattedTime+":00");
                                    String user_id = pref.getString("user_id", "");
                                    String user_phone = pref.getString("phone", "");
                                    String user_name = pref.getString("name", "");

                                    resObj.put("type", "MAKE_RESERVATION");
                                    resObj.put("user_id", user_id);
                                    resObj.put("user_name", user_name);
                                    resObj.put("rest_id", res_id);
                                    resObj.put("rest_name", res_name);
                                    resObj.put("rest_phone", res_phone);
                                    resObj.put("reserv_time", formattedDate+" "+formattedTime+":00"); // need to parse
                                    resObj.put("user_phone", user_phone);
                                    resObj.put("people", reserv_people.getText());
                                    resObj.put("reserv_fee", Integer.parseInt(String.valueOf(reserv_people.getText())) * reserv_price_int);
                                    resObj.put("request", reserv_request.getText());
                                    sendToServer server = new sendToServer();
                                    server.send(resObj);
                                    dialog.dismiss();


                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }
                else {
                    alertDlg.setTitle("지갑에 돈이 예약금보다 적습니다. 돈을 충전해주세요.");
                    alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDlg.show();
                }


            }
        });
    }

    public String formatMonth(String month){
        if (Integer.parseInt(month) < 10)
            return (0+month);
        else
            return month;
    }

    public Context getActivity() {
        return this;
    }

}

