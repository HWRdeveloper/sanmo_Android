package com.ruehyeon.sanmo.Notification.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;


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
import com.ruehyeon.sanmo.Constants;
import com.ruehyeon.sanmo.Notification.misc.Const;
import com.ruehyeon.sanmo.models.ChatDatabase;
import com.ruehyeon.sanmo.models.ChatEntity;

import java.util.List;


public class NotificationHandler {

	public static final String BROADCAST = "org.hcilab.projects.nlogx.update";
	public static final String LOCK = "lock";
	List<String> uuids;
	private Context context;
	private SharedPreferences sp;
	private SharedPreferences pref;
	ChatDatabase db = ChatDatabase.getChatDatabase(context);
	NotificationHandler(Context context) {
		this.context = context;
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		pref = context.getSharedPreferences("user-info" , Context.MODE_PRIVATE);
	}

	void handlePosted(StatusBarNotification sbn) {
		Log.d("노티받아옴" , "뭐지");
//		if(sbn.isOngoing() && !sp.getBoolean(Const.PREF_ONGOING, false)) {
//			if(Const.DEBUG) System.out.println("posted ongoing!");
//			return;
//		}
		if (!sbn.getPackageName().equals("com.kakao.talk") ) {
			Log.d("노티받아옴" , "카카오아님" + sbn.getPackageName());
			return;
		}

		Log.d("노티받아옴" , "카카오");
		Log.d("알림태그", sbn.getTag());
		sbn.getTag();

		if (!pref.getString(sbn.getTag(), "").equals("")) {
			Log.d("이미 있는 상대 유저" , "카카오");
			db.chatDao().insertNotification(new ChatEntity(pref.getString(sbn.getTag(), ""), Constants.OTHER_MESSAGE, sbn.getNotification().extras.getCharSequence(NotificationCompat.EXTRA_TEXT).toString()));
			return;
		}

		String content = sbn.getNotification().extras.getCharSequence(NotificationCompat.EXTRA_TEXT).toString();
		try {

			if (content.contains("AMMO - 초대가 수락됨")) {
				Log.d("초대수락", "확인됨");
				int i = sbn.getNotification().extras.getCharSequence(NotificationCompat.EXTRA_TEXT).length() - 1;
				String object = (String) sbn.getNotification().extras.getCharSequence(NotificationCompat.EXTRA_TEXT).subSequence(17, i);
				long userid = Long.parseLong(object);

				LinkObject link = LinkObject.newBuilder()
						.setWebUrl("https://developers.kakao.com")
						.setMobileWebUrl("https://developers.kakao.com")
						.setAndroidExecutionParams("user_id=" + pref.getLong("user_id", 0) + "&message=" + "성공적으로 연결되었습니다.")
						.build();
				TemplateParams params = TextTemplate.newBuilder("성공적으로 연결되었습니다." + pref.getLong("user_id", 0), link)
						.setButtonTitle("복호화")
						.build();
				findfriendUUIDandSend(userid, params);
				//TODO : 파이어베이스 리얼타임디비로 유저 코인 차감
				String tag = sbn.getTag();
				SharedPreferences.Editor editor = pref.edit();
				editor.putString(tag, uuids.get(0));
				editor.apply();
			}


			if (content.contains("AMMO - 초대가 수락됨")) {
				Log.d("초대수락", "확인됨");
				int i = sbn.getNotification().extras.getCharSequence(NotificationCompat.EXTRA_TEXT).length() - 1;
				String object = (String) sbn.getNotification().extras.getCharSequence(NotificationCompat.EXTRA_TEXT).subSequence(17, i);
				long userid = Long.parseLong(object);

				LinkObject link = LinkObject.newBuilder()
						.setWebUrl("https://developers.kakao.com")
						.setMobileWebUrl("https://developers.kakao.com")
						.setAndroidExecutionParams("user_id=" + pref.getLong("user_id", 0) + "&message=" + "성공적으로 연결되었습니다.")
						.build();
				TemplateParams params = TextTemplate.newBuilder("성공적으로 연결되었습니다. " + pref.getLong("user_id", 0), link)
						.setButtonTitle("복호화")
						.build();
				findfriendUUIDandSend(userid, params);
				//TODO : 파이어베이스 리얼타임디비로 유저 코인 차감
				String tag = sbn.getTag();
				SharedPreferences.Editor editor = pref.edit();
				editor.putString(tag, uuids.get(0));
				editor.apply();

			}if (content.contains("성공적으로 연결되었습니다.")) {
				Log.d("초대수락", "확인됨");
				int i = sbn.getNotification().extras.getCharSequence(NotificationCompat.EXTRA_TEXT).length() - 1;
				String object = (String) sbn.getNotification().extras.getCharSequence(NotificationCompat.EXTRA_TEXT).subSequence(15, i);
				long userid = Long.parseLong(object);
				findfriendUUID(userid);
				String tag = sbn.getTag();
				SharedPreferences.Editor editor = pref.edit();
				editor.putString(tag, uuids.get(0));
				editor.apply();
			}
		} catch (Exception e) {
			if(Const.DEBUG) e.printStackTrace();
		}


	}

	void handleRemoved(StatusBarNotification sbn, int reason) {
		if(sbn.isOngoing() && !sp.getBoolean(Const.PREF_ONGOING, false)) {
			if(Const.DEBUG) System.out.println("removed ongoing!");
			return;
		}
		NotificationObject no = new NotificationObject(context, sbn, false, reason);

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
								break;
							}

						}

					}

				});
	}


	public void findfriendUUIDandSend(long user_id, TemplateParams params ) {
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
								sendMessage(params);
								break;
							}
						}

					}

				});
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

}
