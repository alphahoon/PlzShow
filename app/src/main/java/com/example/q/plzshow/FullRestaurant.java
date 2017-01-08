package com.example.q.plzshow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.q.plzshow.adapter.photoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by q on 2017-01-06.
 */

public class FullRestaurant extends AppCompatActivity {

    //서버랑 통신해서 그 레스토랑의 자세한 정보 가져와야됨

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.restaurant_full);

            final Intent intent = getIntent();
            String temp = intent.getExtras().getString("res_json");

            final JSONObject res_json = new JSONObject(intent.getExtras().getString("res_json"));
            Log.e("res_id", res_json.toString());

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(res_json.getString("name"));

            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final TextView res_full_name = (TextView) findViewById(R.id.res_full_name);
            TextView reserv_no = (TextView) findViewById(R.id.reserv_no);
            TextView res_full_descrip = (TextView) findViewById(R.id.res_full_descrip);
            TextView res_full_rating = (TextView) findViewById(R.id.res_full_rating);
            TextView res_full_loc = (TextView) findViewById(R.id.res_full_loc);
            TextView res_text_address = (TextView) findViewById(R.id.res_text_address);
            final TextView res_full_phone = (TextView) findViewById(R.id.res_full_phone);
            TextView res_full_operation_time = (TextView) findViewById(R.id.res_full_operation_time);
            TextView res_full_rest_time = (TextView) findViewById(R.id.res_full_rest_time);
            TextView res_full_holiday = (TextView) findViewById(R.id.res_full_holiday);
            TextView res_full_price = (TextView) findViewById(R.id.res_full_price);
            TextView res_full_coin = (TextView) findViewById(R.id.res_full_coin);
            TextView res_full_respond_time = (TextView) findViewById(R.id.res_full_respond_time);

            res_full_name.setText(res_json.getString("name") + " / ");
            reserv_no.setText("예약 50 X DB");
            res_full_descrip.setText(res_json.getString("description"));
            res_full_rating.setText("4.1점"+ "/");
            res_full_loc.setText(res_json.getString("location") + "·" + res_json.getString("type"));
            res_text_address.setText("자세한 주소가 필요한데 DB에도 없네");
            res_full_phone.setText(res_json.getString("phone"));
            res_full_operation_time.setText(res_json.getString("oper_time"));
            res_full_rest_time.setText(res_json.getString("rest_time"));
            res_full_holiday.setText(res_json.getString("holiday"));
            res_full_price.setText(res_json.getString("price"));
            res_full_coin.setText(res_json.getString("reserv_price")+"코인 / 1인");
            res_full_respond_time.setText(res_json.getString("respond_time"));

            LinearLayout res_full_contact = (LinearLayout) findViewById(R.id.res_full_contact);
            res_full_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AlertDialog.Builder alertDlg = new AlertDialog.Builder(v.getContext());
                        if (res_json.getString("phone") != "미등록"){
                            TextView title = new TextView(v.getContext());
                            title.setPadding(10, 20, 10, 10);
                            title.setGravity(Gravity.CENTER);
                            title.setText(res_json.getString("name")+ " (으)로 전화를 거시겠습니까?");
                            title.setTextSize(17);
                            alertDlg.setCustomTitle(title);

                            alertDlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent call_intent = new Intent(Intent.ACTION_DIAL);
                                    call_intent.setData(Uri.parse("tel:" + res_full_phone.getText()));
                                    startActivity(call_intent);
                                }
                            });

                            alertDlg.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        else{
                            alertDlg.setTitle("전화번호가 등록되지 않았습니다");
                            alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        alertDlg.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


            // 네이버 지도 표시
            final Handler handler = new Handler();
            new Thread() {
                public void run() {
                    try {
                        String clientID = "a3h1sgm1T4A099FNlI1C";
                        String clientSecret = "EUYqZGGwan";
                        String addr = URLEncoder.encode("서울시 강남구 신사동 520-1", "UTF-8");
                        String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; //json
                        //String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr; // xml
                        URL url = new URL(apiURL);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("X-Naver-Client-Id", clientID);
                        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                        int responseCode = con.getResponseCode();
                        BufferedReader br;
                        if (responseCode == 200) { // 정상 호출
                            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        } else {  // 에러 발생
                            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        }
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = br.readLine()) != null) {
                            response.append(inputLine);
                        }
                        br.close();

                        JSONObject tempobj = new JSONObject(response.toString());
                        String x_point = tempobj.getJSONObject("result").getJSONArray("items").getJSONObject(0).getJSONObject("point").getString("x");
                        String y_point = tempobj.getJSONObject("result").getJSONArray("items").getJSONObject(0).getJSONObject("point").getString("y");

                        apiURL = "https://openapi.naver.com/v1/map/staticmap.bin?clientId="
                                + clientID + "&url=http://www.naver.com&center="
                                + x_point + "," + y_point + "&level=12&w=1200&h=400&baselayer=default&markers="
                                + x_point + "," + y_point;
                        Log.d("URL", apiURL);
                        InputStream is = new URL(apiURL).openStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);
                        handler.post(new Runnable() {
                            public void run() {
                                ImageView img = (ImageView) findViewById(R.id.res_full_map);
                                img.setImageBitmap(bitmap);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            TextView res_full_reserve = (TextView) findViewById(R.id.res_full_reserve);
            res_full_reserve.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent reservIntent = new Intent(getBaseContext(), Reservation.class);
                    try {
                        reservIntent.putExtra("res_id", res_json.getString("rest_id"));
                        reservIntent.putExtra("res_name", res_json.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //아 시발 user_id 예전부터~ 쭈욱 해서 불러와야되네. 짜증난다 ㅠㅠ
                    startActivity(reservIntent);
                }
            });

            JSONObject getObj = new JSONObject();
            try {
                getObj.put("type", "GET_REST_PHOTOS");
                getObj.put("rest_id", res_json.getString("rest_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // GET RESPONSE
            JSONObject res = null;
            try {
                res = new sendToServer.sendJSON(getString(R.string.server_ip), getObj.toString(), "application/json").execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.e("cs496_photos", res+"");
            JSONArray array = res.getJSONArray("photos");

            photoAdapter madapter = new photoAdapter(this, array);
            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(madapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class loadPhoto extends AsyncTask<String, Void, Bitmap> {

        public loadPhoto() {
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

    }

}

