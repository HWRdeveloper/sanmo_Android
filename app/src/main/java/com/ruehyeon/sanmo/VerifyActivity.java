package com.ruehyeon.sanmo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyActivity extends BaseActivity{
    EditText phoneNumber;
    Myservice service;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://158.247.199.175:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(Myservice.class);


        phoneNumber = findViewById(R.id.editTextPhone);
        Button authSend = findViewById(R.id.getAuthNumberBtn);
        authSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ;
            sendAuth();
            }
        });
    }

    public void  sendAuth(){
        JsonObject object = new JsonObject();
        object.addProperty("phone", phoneNumber.getText().toString());
        Call<JsonObject> call_verify = service.verification(object);
        call_verify.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(getApplicationContext(), "인증번호 전송성공", Toast.LENGTH_SHORT);
                Log.d("sendAuth", "successfully sent message");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ;
                Log.d("sendAuth", t.getMessage());
            }
        });
    }
}
