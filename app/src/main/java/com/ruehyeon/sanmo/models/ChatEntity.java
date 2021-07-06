package com.ruehyeon.sanmo.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

//@Entity(foreignKeys = {
//        @ForeignKey(entity = WordEntity.class,
//                parentColumns = "id",
//                childColumns = "first_prime_word_id"),
//        @ForeignKey(entity = WordEntity.class,
//                parentColumns = "id",
//                childColumns = "second_prime_word_id"),
//        @ForeignKey(entity = WordEntity.class,
//                parentColumns = "id",
//                childColumns = "third_prime_word_id"),
//        @ForeignKey(entity = WordEntity.class,
//                parentColumns = "id",
//                childColumns = "fourth_prime_word_id")
//})
@Entity
public class ChatEntity {
        public ChatEntity(String uuid, int type, String message) {
                this.uuid = uuid;
                this.type = type;
                this.message = message;
        }

        public ChatEntity() {
        }
        @PrimaryKey(autoGenerate = true)
        public int id;

        //알림 수신 즉시 저장하는 값입니다.
        public String uuid;
        public int type;
        public String message;

        //첫번째 서버 통신 후 저장하는 값입니다.


        //vec2like 신경망을 돌린 후 저장하는 값입니다.
        public double this_user_expect_evaluation;

    //사용자의 평가가 있은 후 저장하는 값입니다.
    @ColumnInfo()
    public int this_user_real_evaluation;
    public Date evaluation_time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
