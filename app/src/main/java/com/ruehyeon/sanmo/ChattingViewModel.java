package com.ruehyeon.sanmo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ruehyeon.sanmo.models.ChatDao;
import com.ruehyeon.sanmo.models.ChatDatabase;
import com.ruehyeon.sanmo.models.ChatEntity;

import java.util.List;

public class ChattingViewModel extends AndroidViewModel {
    LiveData<ChatEntity> entity = new MutableLiveData<>();
    LiveData<List<ChatEntity>> entities_all; // = NotificationDatabase.getNotificationDatabase().notificationDao().loadAllNotificationLiveData();
    LiveData<List<ChatEntity>> entities_positive;
    LiveData<List<ChatEntity>> entities_negative;
    LiveData<List<ChatEntity>> entities_specific;


    private ChatDatabase cdb;
    private ChatDao chatDao;

    long id;

    public ChattingViewModel(@NonNull Application application) {
        super(application);

        cdb = ChatDatabase.getChatDatabase(application);
        chatDao = cdb.chatDao();
        entities_all = chatDao.loadAllNotificationLiveData();

    }

    public ChatDao getNotificationDao() {
        return chatDao;
    }

    //모든 노티피케이션을 가져옵니다.
    public LiveData<List<ChatEntity>> getAllNotification() {
        return entities_all;
    }

    //처음 노티 크롤링할때 유저평가 0인 디폴트 노티피케이션을 가져옵니다.
    public LiveData<List<ChatEntity>> getUUIDMessage(String uuid) {
        return chatDao.loadChatroomByUUID(uuid);
    }

    //관심 노티피케이션을 가져옵니다.
    public LiveData<List<ChatEntity>> getPositiveNotification() {
        return entities_positive;
    }

    //무관심 노티피케이션을 가져옵니다.
    public LiveData<List<ChatEntity>> getNegativeNotification() {
        return entities_negative;
    }

    public LiveData<ChatEntity> LoadNotification(long id) {
        //entity = notiDao.loadNotification(id);
        return entity;
    }

    // TODO 카테고리별로 다 위에 처럼 만들어 놓기.

    public void insert(ChatEntity entity) {
        chatDao.insertNotification(entity);
    }

}
