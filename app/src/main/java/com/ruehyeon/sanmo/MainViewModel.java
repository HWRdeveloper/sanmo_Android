package com.ruehyeon.sanmo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ruehyeon.sanmo.models.RuleDatabase;
import com.ruehyeon.sanmo.models.RulesDao;
import com.ruehyeon.sanmo.models.RulesEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    LiveData<RulesEntity> entity = new MutableLiveData<>();
    LiveData<List<RulesEntity>> entities_all; // = NotificationDatabase.getNotificationDatabase().notificationDao().loadAllNotificationLiveData();
    LiveData<List<RulesEntity>> entities_positive;
    LiveData<List<RulesEntity>> entities_negative;
    LiveData<List<RulesEntity>> entities_default;


    private RuleDatabase rdb;
    private RulesDao rulesDao;

    long id;

    public MainViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences pref = application.getSharedPreferences("data", Context.MODE_PRIVATE);

        rdb = RuleDatabase.getRuleDatabase(application, pref.getString("uid",""));
        rulesDao = rdb.rulesDao();
        entities_all = rulesDao.loadAllNotificationLiveData();
        entities_positive = rulesDao.loadPositiveNotificationLiveData();
        entities_negative = rulesDao.loadNegativeNotificationLiveData();
        entities_default = rulesDao.loadDefaultNotificationLiveData();

    }

    public RulesDao getNotificationDao() {
        return rulesDao;
    }

    //모든 노티피케이션을 가져옵니다.
    public LiveData<List<RulesEntity>> getAllNotification() {
        return entities_all;
    }

    //처음 노티 크롤링할때 유저평가 0인 디폴트 노티피케이션을 가져옵니다.
    public LiveData<List<RulesEntity>> getDefaultNotifications() {
        return entities_default;
    }

    //관심 노티피케이션을 가져옵니다.
    public LiveData<List<RulesEntity>> getPositiveNotification() {
        return entities_positive;
    }

    //무관심 노티피케이션을 가져옵니다.
    public LiveData<List<RulesEntity>> getNegativeNotification() {
        return entities_negative;
    }

    public LiveData<RulesEntity> LoadNotification(long id) {
        //entity = notiDao.loadNotification(id);
        return entity;
    }

    // TODO 카테고리별로 다 위에 처럼 만들어 놓기.

    public void insert(RulesEntity entity) {
        rulesDao.insertNotification(entity);
    }

}
