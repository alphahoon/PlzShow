package com.example.q.plzshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.q.plzshow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by q on 2017-01-08.
 */

public class photoAdapter extends PagerAdapter {

    private Activity activity;
    private JSONArray photoArray;
    private LayoutInflater inflater;

    public photoAdapter(Activity activity, JSONArray photoArray) {
        this.activity = activity;
        this.photoArray = photoArray;
    }

    @Override
    public int getCount() {
        return this.photoArray.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View photoView = inflater.inflate(R.layout.resimage, container, false);

//            ImageView imageView = new ImageView(activity);
            ImageView imageView = (ImageView) photoView.findViewById(R.id.image);

            String url = photoArray.getJSONObject(position).getString("photo");
            Bitmap img = new loadPhoto().execute(url).get();

            imageView.setImageBitmap(img);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            ((ViewPager)container).addView(photoView);


            return photoView;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }

}

