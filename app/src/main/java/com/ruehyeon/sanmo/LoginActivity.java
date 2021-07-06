package com.ruehyeon.sanmo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.data.OAuthLoginState;
import com.ruehyeon.sanmo.Util.GoogleSignInOptionSingleTone;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends BaseActivity{
    private SessionCallback sessionCallback;
    OAuthLogin mOAuthLoginModule;
    private static String OAUTH_CLIENT_ID = "klSyKjt_QdjVd0tSvkw8";
    private static String OAUTH_CLIENT_SECRET = "qf55TpzJfi";
    private static String OAUTH_CLIENT_NAME = "산후관리앱";
    private GoogleSignInClient mGoogleSignInClient ;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                LoginActivity.this
                ,OAUTH_CLIENT_ID
                ,OAUTH_CLIENT_SECRET
                ,OAUTH_CLIENT_NAME
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );

        GoogleSignInOptionSingleTone gso = new GoogleSignInOptionSingleTone();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso.getInstance(getApplicationContext()));
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    public void onLoginClick(View v) {
        switch(v.getId()) {
            case R.id.google_login :
                signInWithGoogle();
                break;
            case R.id.naver_login :
                mOAuthLoginModule.startOauthLoginActivity((Activity)context, mOAuthLoginHandler);
                break;
            case R.id.kakao_login :
            if (sessionCallback == null) {
                sessionCallback = new SessionCallback();
                Session.getCurrentSession().addCallback(sessionCallback);
                break;
            }
            Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this);

        }


    }

    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(context);
                String refreshToken = mOAuthLoginModule.getRefreshToken(context);
                long expiresAt = mOAuthLoginModule.getExpiresAt(context);
                String tokenType = mOAuthLoginModule.getTokenType(context);

//                mOauthAT.setText(accessToken);
//                mOauthRT.setText(refreshToken);
//                mOauthExpires.setText(String.valueOf(expiresAt));
//                mOauthTokenType.setText(tokenType);
//                mOAuthState.setText(mOAuthLoginModule.getState(getApplicationContext()).toString());
                new RequestApiTask(context).execute();

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(context).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(context);
                Toast.makeText(context, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                //loading_dots.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        };
    };


    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {

            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                }
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //intent.putExtra("name", result.getNickname());
                    //intent.putExtra("profile", result.getProfileImagePath());
                    Log.d("카카오로그인", result.toString());

                    AccessToken accessToken;
                    accessToken = Session.getCurrentSession().getTokenInfo();
                    //startActivity(intent);
                    //finish();
                    Toast.makeText(getApplicationContext(),"카카오톡 로그인에 성공하였습니다. ",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    private class RequestApiTask extends AsyncTask<Void, Void, StringBuffer> {
        private String token;
        Context mContext;

        RequestApiTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected StringBuffer doInBackground(Void... params) {
            OAuthLogin mOAuthLoginInstance = OAuthLogin.getInstance();
            if (OAuthLoginState.NEED_REFRESH_TOKEN.equals(mOAuthLoginInstance.getState(mContext))) {  // 네이버
                mOAuthLoginInstance.refreshAccessToken(mContext);
            }
            this.token = mOAuthLoginInstance.getAccessToken(mContext);
            String header = "Bearer " + this.token;
            try {
                final String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        responseCode == 200 ? con.getInputStream() : con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(StringBuffer content) {
            super.onPostExecute(content);
            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(content));
                JSONObject response = jsonObject.getJSONObject("response");
                String id = response.getString("id");
                String name = response.getString("name");
                String birthday = response.getString("birthday");
                System.out.println(name + "981217");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
