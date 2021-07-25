package com.ruehyeon.sanmo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.ruehyeon.sanmo.models.ui.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyActivity extends BaseActivity{
    EditText phoneNumber;
    ImageView backToLogreg;
    Button authSend;

    Myservice service;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register_phone_verification);
        final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://158.247.199.175:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(Myservice.class);

        phoneNumber = findViewById(R.id.editTextPhone_verify);
        backToLogreg = findViewById(R.id.backtologreg);
        authSend = findViewById(R.id.getAuthNumberBtn);

        backToLogreg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        authSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAuth();
            }
        });
    }

    private long mBackPressed = 0;
    @Override
    public void onBackPressed() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
// 두번 눌러서 종료
        if (count == 0) {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), "한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                mBackPressed = System.currentTimeMillis();
            }
        } else {
            finish();
        }
    }

    public void  sendAuth(){
        JsonObject object = new JsonObject();
        object.addProperty("phone", phoneNumber.getText().toString());
        Call<JsonObject> call_verify = service.verification(object);
        call_verify.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(getApplicationContext(), "인증번호 전송성공", Toast.LENGTH_SHORT).show();
                Log.d("sendAuth", "successfully sent message");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("sendAuth", t.getMessage());
            }
        });
    }
}
