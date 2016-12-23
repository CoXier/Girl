package com.hackerli.retrofit.splash;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



import com.hackerli.retrofit.main.MainActivity;
import com.hackerli.retrofit.module.showgirl.GirlPresenter;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        // load data from db while app is starting
        GirlPresenter.setLocalGirl();
    }

}
