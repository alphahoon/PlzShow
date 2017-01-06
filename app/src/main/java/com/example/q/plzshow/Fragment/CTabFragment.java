package com.example.q.plzshow.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.q.plzshow.R;
import com.example.q.plzshow.adapter.RoundedImageView;


public class CTabFragment extends Fragment {
    private ImageView user_profile_img;

    public CTabFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CTabFragment newInstance() {
        return new CTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ctab, container, false);
        user_profile_img = (ImageView) rootView.findViewById(R.id.imgView_profile);

        Bitmap pic = BitmapFactory.decodeResource(rootView.getContext().getResources(), R.drawable.default_user);
        int width = pic.getWidth();
        pic = RoundedImageView.getCroppedBitmap(pic, width);
        user_profile_img.setImageBitmap(pic);

        return rootView;
    }

}
