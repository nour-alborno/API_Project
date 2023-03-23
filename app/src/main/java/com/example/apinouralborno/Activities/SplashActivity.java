package com.example.apinouralborno.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.apinouralborno.Activities.StoreActivity;
import com.example.apinouralborno.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getBaseContext(), StoreActivity.class));
                finish();
            }
        },3000);
    }
}