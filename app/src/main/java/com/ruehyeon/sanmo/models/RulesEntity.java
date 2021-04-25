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
public class RulesEntity {
        public RulesEntity(int skin, String profile_url, String app_name, int rule, String counterpart, String counterpart_UUID, Date created_time, long server_id) {
                this.skin = skin;
                this.profile_url = profile_url;
                this.app_name = app_name;
                this.rule = rule;
                this.counterpart = counterpart;
                this.created_time = created_time;
                this.server_id = server_id;
                this.counterpart_UUID = counterpart_UUID;
        }

        public RulesEntity() {
        }

        @PrimaryKey(autoGenerate = true)
        public long id;

        //알림 수신 즉시 저장하는 값입니다.
        public String app_name;
        public String profile_url;
        public int rule;
        public String counterpart;
        public String counterpart_UUID;
        public String cls_intent;
        public String imageFile_path;
        public int skin;
        public Date created_time;
        public String pendingIntent;
        public String category;

        //첫번째 서버 통신 후 저장하는 값입니다.
        public long server_id;
        public long first_prime_word_id;
        public long second_prime_word_id;
        public long third_prime_word_id;
        public long fourth_prime_word_id;
        public String wordVector;
        public int expect_positive_evaluation_num;
        public int expect_negative_evaluation_num;

        //vec2like 신경망을 돌린 후 저장하는 값입니다.
        public double this_user_expect_evaluation;

    //사용자의 평가가 있은 후 저장하는 값입니다.
    @ColumnInfo()
    public int this_user_real_evaluation;
    public Date evaluation_time;

}
