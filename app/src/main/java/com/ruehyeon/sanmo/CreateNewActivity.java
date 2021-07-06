package com.ruehyeon.sanmo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.kakaotalk.response.model.MessageFailureInfo;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.usermgmt.UserManagement;

import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.ruehyeon.sanmo.models.RuleDatabase;
import com.ruehyeon.sanmo.models.RulesEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateNewActivity extends AppCompatActivity {
    private SessionCallback sessionCallback;
    public static TextView textView;

    private Button dialogBtn;
    List<String> myNameList;
    List<String> myImageList;
    List<String> myUUIDList;
    TextView rule;
    TextView skin;
    CircleImageView pf_pic;
    TextView name;
    int viewholderPos;
    int rulePos;
    int skinPos;
    String p;
    String n;
    String id;
    TextInputEditText bp;
    TextInputEditText bp1;
    TextInputEditText bp_2;
    TextInputEditText bp1_2;
    BottomSheetDialog dialog; //dialog for primary blood pressure
    BottomSheetDialog dialog1; //dialog for secondary blood pressure
    BottomSheetDialog dialog2; ////dialog for blood sugar

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        bp = findViewById(R.id.bpinputText);
        bp1 = findViewById(R.id.bpinputText1);
        bp_2 = findViewById(R.id.bpinputText2);
        bp1_2 = findViewById(R.id.bpinputText1_2);


        skin = findViewById(R.id.skin_textview);
        Intent bsIntent = new Intent(this, com.ruehyeon.sanmo.BottomSheetDialog.class);
        bsIntent.putExtra("type", 0);


        //fileBtn_real.setOnClickListener(view -> com.ruehyeon.sanhu.BottomSheetDialog.getInstance().show(getSupportFragmentManager(), "bottomSheet"));
        dialog = new BottomSheetDialog(this, new com.ruehyeon.sanmo.BottomSheetDialog.BottomSheetDialogListener() {
            @Override
            public void exit(int data) {
                Log.d("debug", String.valueOf(data) );
                bp.setText(String.valueOf(data));
            }
        }, 0);
        dialog1 = new BottomSheetDialog(this, new com.ruehyeon.sanmo.BottomSheetDialog.BottomSheetDialogListener() {
            @Override
            public void exit(int data) {
                Log.d("debug", String.valueOf(data) );
                bp.setText(String.valueOf(data));
            }
        }, 1);

        dialog1 = new BottomSheetDialog(this, new com.ruehyeon.sanmo.BottomSheetDialog.BottomSheetDialogListener() {
            @Override
            public void exit(int data) {
                Log.d("debug", String.valueOf(data) );
                bp.setText(String.valueOf(data));
            }
        }, 2);






        kakao_login();


        KakaoTalkService.getInstance()
                .requestProfile(new TalkResponseCallback<KakaoTalkProfile>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.e("KAKAO_API", "카카오톡 사용자가 아님");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "카카오톡 프로필 조회 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(KakaoTalkProfile result) {
                        Log.i("KAKAO_API", "카카오톡 닉네임: " + result.getNickName());
                        Log.i("KAKAO_API", "카카오톡 프로필이미지: " + result.getProfileImageUrl());
                    }
                });






// 조회 요청





// 기본 템플릿 만들기
// 타입별 템플릿 만들기 상세 예제코드는 [메시지 만들기] 참고


// 기본 템플릿으로 친구에게 보내기




    }




    public void sendMessage(TemplateParams params){
        Log.d("KAKAO_API", "메시지 전송 진입 " );


        List<String> uuids = new ArrayList<String>();
        uuids.add("1ufX797r3-bQ5cn7zPrN_c38zfrW49Hl0OfWRg");
        Log.d("KAKAO_API", "uuids: " + uuids.toString());
        //uuids.add("1584124181");
        KakaoTalkService.getInstance()
                .sendMessageToFriends(uuids, params, new TalkResponseCallback<MessageSendResponse>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.e("KAKAO_API", "카카오톡 사용자가 아님");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "친구에게 보내기 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(MessageSendResponse result) {
                        if (result.successfulReceiverUuids() != null) {
                            Log.i("KAKAO_API", "친구에게 보내기 성공");
                            Log.d("KAKAO_API", "전송에 성공한 대상: " + result.successfulReceiverUuids());
                        }
                        if (result.failureInfo() != null) {
                            Log.e("KAKAO_API", "일부 사용자에게 메시 보내기 실패");
                            for (MessageFailureInfo failureInfo : result.failureInfo()) {
                                Log.d("KAKAO_API", "code: " + failureInfo.code());
                                Log.d("KAKAO_API", "msg: " + failureInfo.msg());
                                Log.d("KAKAO_API", "failure_uuids: " + failureInfo.receiverUuids());
                            }
                        }
                    }
                });
    }








    public void dismissDialog() {

        dialog.dismiss();
    }

    public void kakao_login() {
        if (sessionCallback == null) {
            sessionCallback = new SessionCallback();
            Session.getCurrentSession().addCallback(sessionCallback);
        }
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this);

    }



    public void sendImage(){
        File imageFile = new File("{로컬 이미지 파일 경로}");
        KakaoLinkService.getInstance()
                .uploadImage(this, true,imageFile, new ResponseCallback<ImageUploadResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "이미지 업로드 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(ImageUploadResponse result) {
                        Log.i("KAKAO_API", "이미지 업로드 성공");

                        Log.d("KAKAO_API", "URL: " + result.getOriginal().getUrl());

                        // TODO: 템플릿 컨텐츠로 이미지 URL 입력
                    }
                })
        ;
    }








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
                    SharedPreferences pref = getSharedPreferences("user-info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putLong("user_id",result.getId());
                    editor.apply();
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

    public void save(View v) {
        save();
    }


    public void save() {
        SharedPreferences pref = getSharedPreferences("user-info", Activity.MODE_PRIVATE);
        Date date = new Date();

        RuleDatabase db = RuleDatabase.getRuleDatabase(getApplicationContext(), "user");

        Log.d("viewholderpos", Integer.toString(viewholderPos));
        db.rulesDao().insertNotification(new RulesEntity(skinPos , myImageList.get(viewholderPos), "카카오톡", rulePos, myNameList.get(viewholderPos), myUUIDList.get(viewholderPos), date, 1));
        finish();
    }

    public void InputInfo(View view) {
        switch (view.getId()) {
            case R.id.bpText:
                dialog.show(getSupportFragmentManager(), "bottomSheet");
                break;
            case R.id.bpText1 :
                dialog1.show(getSupportFragmentManager(), "bottomSheet");
                break;
            case R.id.bpText2 :
                dialog.show(getSupportFragmentManager(), "bottomSheet");
                break;
            case R.id.bpText1_2 :
                dialog1.show(getSupportFragmentManager(), "bottomSheet");
                break;
        }

    }



}