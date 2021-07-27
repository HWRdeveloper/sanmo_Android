package com.ruehyeon.sanmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyActivity extends BaseActivity {

    ImageView backToSendAuth;

    Myservice service;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_phone_verification);
        final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://158.247.199.175:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(Myservice.class);

        backToSendAuth = findViewById(R.id.backtosendauth);
        String phoneNumber = getIntent().getStringExtra("phonenumber");

        backToSendAuth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), SendauthActivity.class);
                startActivity(intent);
            }
        });
    }

}
