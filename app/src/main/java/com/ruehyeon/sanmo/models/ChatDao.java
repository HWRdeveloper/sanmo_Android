package com.ruehyeon.sanmo.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {

    //알림 여러개를 한번에 저장합니다.
    //insert된 row의 id 값들을 long형 배열로 반환합니다.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long[] insertNotification(List<ChatEntity> notificationEntities);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public long insertNotification(ChatEntity chatEntity);

    //디비에 있는 단어 모델 값들을 수정합니다.
    @Update
    public void updateNotifications(List<ChatEntity> notificationEntities);

    //디비에 있는 단어 모델을 삭제합니다.
    @Delete
    public void deleteNotifications(List<ChatEntity> notificationEntities);


    @Query("SELECT COUNT(*) FROM ChatEntity ")
    public int number_of_notification();

    @Query("DELETE FROM ChatEntity " +
            "WHERE uuid = :id")
    public void deleteNotification(long id);


    //하나의 알림 모델을 가져옵니다.
    @Query("SELECT * FROM ChatEntity " +
            "WHERE uuid = :id")
    ChatEntity loadNotification(long id);

    @Query("SELECT * FROM ChatEntity ")
    LiveData<List<ChatEntity>> loadAllNotificationLiveData();

    @Query("SELECT * FROM ChatEntity " +
            "WHERE uuid = :uuid ")
    LiveData<List<ChatEntity>> loadChatroomByUUID(String uuid);

    @Query("SELECT * FROM ChatEntity " +
            "WHERE this_user_real_evaluation = 1 ")
    LiveData<List<ChatEntity>> loadPositiveNotificationLiveData();

    @Query("SELECT * FROM ChatEntity " +
            "WHERE this_user_real_evaluation = -1 ")
    LiveData<List<ChatEntity>> loadNegativeNotificationLiveData();

//    @Query("SELECT * FROM NotificationEntity ORDER BY id DESC LIMIT 1")
//    public Temp loadLastNotification();

    //모든 알림 모델을 가져옵니다.
    @Query("SELECT * FROM ChatEntity")
    public ChatEntity[] loadAllNotification();

    //긍정적으로 예상되는 확률이 50% 이상인 단어만 가져옵니다.
    @Query("SELECT * FROM ChatEntity " +
            "WHERE this_user_expect_evaluation > 0.5 " +
            "AND this_user_expect_evaluation is NULL")
    public ChatEntity[] loadPositiveNotification();

    //긍정적으로 예상되는 확률이 50% 이하인 단어만 가져옵니다.
    @Query("SELECT * FROM ChatEntity " +
            "WHERE this_user_expect_evaluation < 0.5 " +
            "AND this_user_expect_evaluation is NULL")
    public ChatEntity[] loadNegativeNotification();

    //해당 기간 동안에 수신된 알림을 검색합니다.

    //해당 단어를 포함한 내용을 가진 알림을 검색합니다.

}
