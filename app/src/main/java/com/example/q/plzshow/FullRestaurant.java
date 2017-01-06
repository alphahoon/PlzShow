package com.example.q.plzshow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by q on 2017-01-06.
 */

public class FullRestaurant extends AppCompatActivity{

    //서버랑 통신해서 그 레스토랑의 자셓나 정보 가져와야됨

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_full);
        Intent intent = getIntent();
        String res_id = intent.getExtras().getString("res_id");
        Log.d("res_id", res_id);
    }
}
