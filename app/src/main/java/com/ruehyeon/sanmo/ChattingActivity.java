package com.ruehyeon.sanmo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.kakaotalk.response.model.MessageFailureInfo;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.ruehyeon.sanmo.Util.UnicodeConverter;
import com.ruehyeon.sanmo.models.ChatDatabase;
import com.ruehyeon.sanmo.models.ChatEntity;


import java.util.ArrayList;
import java.util.List;

//TODO: 생명주기 고려해서 Listener 삭제 및 다시 붙이기 / Image, Audio 파일 전송 추가 (Type 을 정해서 0=텍스트, 1=사진, 2=음성) / 나가기 시 MainActivity 로 다시 되돌아감 / Storage Room 폴더 삭제
public class ChattingActivity extends AppCompatActivity {
    RecyclerView listView;
    ListAdapter adapter;
    ArrayList<MessageData> messageList;
    LottieAnimationView sendBtn;
    LottieAnimationView fileBtn;
    EditText sendEdit;
    String room;
    String uuid;
    ImageButton imageButton;
    ChattingViewModel model;
    ImageButton fileBtn_real;
    Boolean isOnly = false;
    ChatRecyclerAdapter recyclerViewAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    List<String> uuids;
    SharedPreferences pref;
    Intent intent = new Intent(this, ChattingActivity.class);
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = getSharedPreferences("user-info", MODE_PRIVATE);
        Log.d(Constants.TAG, "Creating!");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatting);
        ChatDatabase db = ChatDatabase.getChatDatabase(getApplicationContext());

        Intent intent = getIntent();

        Spinner spinner_skin = findViewById(R.id.rules);
        spinner_skin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //skin.setText(view);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            Log.d("MyTag", "Intent.ACTION_VIEW.equals(intent.getAction() ");
            if (uri != null) {
                long user_id = Long.parseLong(uri.getQueryParameter("user_id"));
                Log.d("MyTag", "user_id : " + user_id);
                uuids = new ArrayList<String>();
                findfriendUUID(user_id);

                try{
                    db.chatDao().loadChatroomByUUID(uuids.get(0)).getValue().size();
                }catch (Exception e) {
                    showAcceptDialog();
                }


                Log.d("MyTag", "uuid : " + this.uuid);

                //uuids.add(this.uuid);
                message = uri.getQueryParameter("message");
            }

        } else {
            uuid = intent.getStringExtra("uuid");
            uuids = new ArrayList<String>();
            uuids.add(uuid);
            Log.d("uuid", uuid);
        }


        //final DatabaseReference dbRef = References.getDbRef();
        messageList = new ArrayList<>();
        SharedPreferences pref = getSharedPreferences("user-info", MODE_PRIVATE);


        //listView = findViewById(R.id.messageList);
        sendBtn = findViewById(R.id.sendBtn);
        sendEdit = findViewById(R.id.sendEdit);
        imageButton = findViewById(R.id.imageButton);
        fileBtn_real = findViewById(R.id.fileBtn_real);


        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewAdapter = new ChatRecyclerAdapter(getApplicationContext());
        recyclerView = findViewById(R.id.messageList);
        recyclerView.setLayoutManager(linearLayoutManager);

        //recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setAdapter(recyclerViewAdapter);

        model = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChattingViewModel.class);
        model.getUUIDMessage(uuid).observe(this, entities -> {
            if(entities.size() == 0) {

                db.chatDao().insertNotification(new ChatEntity(uuid, Constants.OTHER_MESSAGE, message));
            }
            recyclerView.post(() -> {
                recyclerViewAdapter.notifyItemInserted(entities.size());
                recyclerViewAdapter.notifyDataSetChanged();
            });
            recyclerViewAdapter.setEntities(entities);
            runOnUiThread(() -> recyclerView.smoothScrollToPosition(recyclerViewAdapter.getItemCount()));
        });

//        adapter = new ListAdapter(messageList, getApplicationContext(), uuid);
////        listView.setAdapter(adapter);
////        listView.setLayoutManager(new LinearLayoutManager(this));
////        listView.setItemAnimator(new DefaultItemAnimator());
////
////        model = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChattingViewModel.class);
////        model.getAllNotification().observe(this, entities -> {
////            MessageData messageData = new MessageData();
////
////            messageList.add(messageData);
////            listView.post(() -> {
////                adapter.notifyItemInserted(messageList.size() - 1);
////                adapter.notifyDataSetChanged();
////            });
////            runOnUiThread(() -> listView.scrollToPosition(adapter.getItemCount() - 1));
////
////        });
////        MessageData messageData = new MessageData();
////        //MessageData messageData = dataSnapshot.getValue(MessageData.class);


        sendBtn.setOnClickListener(v -> {
                if (sendEdit.getText().toString().isEmpty()) {
                Toast.makeText(this, "보낼 내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                return;


            }

            String converted = UnicodeConverter.convertToUnicode(sendEdit.getText().toString());
            LinkObject link = LinkObject.newBuilder()
                    .setWebUrl("https://developers.kakao.com")
                    .setMobileWebUrl("https://developers.kakao.com")
                    .setAndroidExecutionParams("user_id="+pref.getLong("user_id",0) +"&message="+sendEdit.getText().toString())
                    .build();
            TemplateParams params = TextTemplate.newBuilder(converted, link)
                    .setButtonTitle("복호화")
                    .build();

            Log.d("sent_userid", Long.toString(pref.getLong("user_id",0)));
            sendMessage(params);

            db.chatDao().insertNotification(new ChatEntity(uuid, Constants.USER_MESSAGE, sendEdit.getText().toString()));
            Log.d("send", sendEdit.getText().toString());
            //dbRef.child(room).push().setValue(new MessageData(Constants.TYPE_TEXT, uuid, sendEdit.getText().toString()));
            sendEdit.setText(null);
        });


        imageButton.setOnClickListener(v -> {
            if (!isOnly) {
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                finish();
            } else if (isOnly) {
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Constants.TAG, "Starting!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.TAG, "Resuming");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(Constants.TAG, "Restarting!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Constants.TAG, "Pause!");

    }

    @Override
    protected void onDestroy() {
//        if(!isOnly ) {
//
//
//            Intent intent1 = new Intent(this, MainActivity.class);
//            startActivity(intent1);
//            finish();
//            References.getDbRef().child(room).removeEventListener(childEventListener);
//            References.getDbRef().child(room).push().setValue(new MessageData(Constants.TYPE_NOTICE, uuid, "상대방이 나갔습니다"));
//        }
//
//        else if(isOnly) {
//
//            Intent intent1 = new Intent(this, MainActivity.class);
//            startActivity(intent1);
//            References.getDbRef().child(room).setValue(null);
//            References.getDbRef().child(room).removeEventListener(childEventListener);
//            References.getDbRef().child(room).removeValue();
//            References.getStorageRef().child(room).delete();
//        }
        super.onDestroy();
        Log.d(Constants.TAG, "Destroyed");

    }


    @Override
    public void onBackPressed() {

        if (!isOnly) {
            Intent intent1 = new Intent(this, MainActivity.class);
            startActivity(intent1);
            finish();
        } else if (isOnly) {
            Intent intent1 = new Intent(this, MainActivity.class);
            startActivity(intent1);
        }


    }


    public void sendMessage(TemplateParams params) {
        Log.d("KAKAO_API", "메시지 전송 진입 ");


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



    public void findfriendUUID(long user_id) {

        AppFriendContext context =
                new AppFriendContext(false, 0, 100, "asc");

        KakaoTalkService.getInstance()
                .requestAppFriends(context, new TalkResponseCallback<AppFriendsResponse>() {
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
                        Log.e("KAKAO_API", "친구 조회 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(AppFriendsResponse result) {
                        Log.i("KAKAO_API", "친구 조회 성공" + result.getFriends().toString() + result.getTotalCount());

                        for (AppFriendInfo friend : result.getFriends()) {
                            Log.d("KAKAO_API", friend.toString() + "//" + friend.getUUID());
                            if (friend.getId() == user_id) {
                                uuids.add(friend.getUUID());
                                setUUID(friend.getUUID());
                                Log.d("myTag", "setuuid : " + uuid);
                                break;
                            }

                        }

                    }

                });
    }

    public void setUUID(String uuid){
        Log.d("myTag", "setuuid : " + uuid);
        this.uuid = uuid;
        Log.d("myTag", "setuuid : " + this.uuid);
    }

    void showAcceptDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("초대수락");
        builder.setMessage("초대를 수락하시겠습니까");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"초대가 수락되었습니다.",Toast.LENGTH_LONG).show();


                        LinkObject link = LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .setAndroidExecutionParams("user_id="+pref.getLong("user_id",0) +"&message=초대가 수락되었습니다.")
                                .build();
                        TemplateParams params = TextTemplate.newBuilder("AMMO - 초대가 수락됨 : " + pref.getLong("user_id",0), link)
                                .setButtonTitle("복호화")
                                .build();
                        sendMessage(params);
                        startNewChatActivity(uuids.get(0));
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"해당 암호의 해석만 제공됩니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    void startNewChatActivity(String uuid) {
        Intent intent = new Intent(this, ChattingActivity.class);
        intent.putExtra("uuid", uuid);
        startActivity(intent);
        finish();
    }
}
