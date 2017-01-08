package com.example.q.plzshow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.q.plzshow.App;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1200;
    private TextView splash_title;
    private TextView splash_body;
    private TextView splash_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_title = (TextView) findViewById(R.id.splash_title);
        splash_body = (TextView) findViewById(R.id.splash_body);
        splash_description = (TextView) findViewById(R.id.splash_description);

        splash_title.setTypeface(App.NanumBarunGothicBold);
        splash_body.setTypeface(App.NanumBarunGothicLight);
        splash_description.setTypeface(App.NanumBarunGothicUltraLight);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
}