package com.example.q.plzshow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.q.plzshow.adapter.photoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.q.plzshow.naver.NMapPOIflagType;
import com.example.q.plzshow.naver.NMapViewerResourceProvider;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import static android.content.ContentValues.TAG;

/**
 * Created by q on 2017-01-06.
 */

public class FullRestaurant extends NMapActivity {

    private final String CLIENT_ID = "a3h1sgm1T4A099FNlI1C";// 애플리케이션 클라이언트 아이디 값

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
            reserv_no.setText("예약 50");
            res_full_descrip.setText(res_json.getString("description"));
            res_full_rating.setText("4.1점"+ "/");
            res_full_loc.setText(res_json.getString("rest_type"));
            res_text_address.setText(res_json.getString("location"));
            res_full_phone.setText(res_json.getString("phone"));
            res_full_operation_time.setText(res_json.getString("oper_time"));
            res_full_rest_time.setText(res_json.getString("rest_time"));
            res_full_holiday.setText(res_json.getString("holiday"));
            res_full_price.setText(res_json.getString("price"));
            res_full_coin.setText(res_json.getString("reserv_price")+" / 1인");
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

            //네이버 지도 API
            //네이버 지도 생성
            NMapView mapView = (NMapView) findViewById(R.id.mapView);
            mapView.setBuiltInZoomControls(true, null);
            mapView.setClientId(CLIENT_ID);
            mapView.setClickable(true);
            mapView.setFocusable(true);
            mapView.setFocusableInTouchMode(true);
            mapView.requestFocus();

            //disable coordinator layout when panning map
            final NestedScrollView restaurant_content = (NestedScrollView) findViewById(R.id.nestedScrollView);
            mapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    restaurant_content.requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            if (!res_json.getString("location").equals("")){
                // get langitude, latitude from address, change to NGeoPoint
                ArrayList<Double> location = getLocation(res_json.getString("location"));
                NGeoPoint point = new NGeoPoint(location.get(0), location.get(1));

                mapView.setScalingFactor(2f);

                NMapController mapController = mapView.getMapController();
                if(mapController == null){
                    Log.e("mapController error", ">>setNaverMapview<<"+mapController);
                }else{
                    mapController.setMapCenter(point, 13);
                }

                NMapViewerResourceProvider mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
                NMapOverlayManager mOverlayManager = new NMapOverlayManager(this, mapView, mMapViewerResourceProvider);

                int markerId = NMapPOIflagType.PIN;

                NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
                poiData.beginPOIdata(1);
                poiData.addPOIitem(point, res_json.getString("name"), markerId, 0);
                poiData.endPOIdata();

                NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
                poiDataOverlay.showFocusedItemOnly();
            }

            // when click 예약접수 (new Intent)
            TextView res_full_reserve = (TextView) findViewById(R.id.res_full_reserve);
            res_full_reserve.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent reservIntent = new Intent(getBaseContext(), Reservation.class);
                    try {
                        reservIntent.putExtra("res_id", res_json.getString("rest_id"));
                        reservIntent.putExtra("res_name", res_json.getString("name"));
                        reservIntent.putExtra("reserv_price", res_json.getString("reserv_price"));
                        reservIntent.putExtra("res_phone", res_json.getString("phone"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    //주소를 받아서 위도와 경도를 return 해줌
    private ArrayList<Double> getLocation(String address) {
        Geocoder geocoder = new Geocoder(this);
        Address addr;
        ArrayList<Double> location = new ArrayList<Double>();
        try {
            List<Address> listAddress = geocoder.getFromLocationName(address, 1);
            if (listAddress.size() > 0) { // 주소값이 존재 하면
                addr = listAddress.get(0); // Address형태로
                Double lat = (Double) (addr.getLatitude());
                Double lng = (Double) (addr.getLongitude());
                location.add(lng);
                location.add(lat);

                //Log.d(TAG, "주소로부터 취득한 경도 : " + location.get(0) + ", 위도 : " + location.get(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }


}

