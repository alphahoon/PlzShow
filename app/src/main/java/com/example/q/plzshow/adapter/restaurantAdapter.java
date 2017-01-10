package com.example.q.plzshow.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by q on 2017-01-05.
 */

public class restaurantAdapter extends RecyclerView.Adapter<restaurantAdapter.ViewHolder> {

    private final Activity activity;
    private JSONArray resArray;

    public restaurantAdapter(Activity activity, JSONArray resArray) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_preview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.position = position;
        final JSONObject resobj = resArray.optJSONObject(position);
        try {
            String url = resobj.getString("pic");
//          이미지 서버 url에서 불러와야됨. loadPhoto는 불러오는 함수(밑에 있음) need to get image from server
            Bitmap img = new loadPhoto().execute(url).get();
            holder.imageView.setImageBitmap(img);

            holder.res_name.setText(resobj.getString("name"));
            holder.res_loc.setText(resobj.getString("rest_type"));
            holder.res_des.setText(resobj.getString("description"));
            holder.res_phone.setText(resobj.getString("phone"));
            //when click carview, need to start a new intent (FullRestaurant.class)
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resIntent = new Intent(activity, FullRestaurant.class);
                    resIntent.putExtra("res_json", resobj.toString());
                    activity.startActivity(resIntent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int position;
        private CardView cardView;
        private ImageView imageView;
        private TextView res_name;
        private TextView res_loc;
        private TextView res_des;
        private TextView res_phone;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.restaurant_cardView);
            imageView = (ImageView) view.findViewById(R.id.res_img);
            res_name = (TextView) view.findViewById(R.id.res_name);
            res_loc = (TextView) view.findViewById(R.id.res_loc);
            res_des = (TextView) view.findViewById(R.id.res_descrip);
            res_phone = (TextView) view.findViewById(R.id.res_phone);
        }
    }
}

class loadPhoto extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public loadPhoto () { }

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

