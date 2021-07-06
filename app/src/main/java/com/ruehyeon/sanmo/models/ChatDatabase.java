package com.ruehyeon.sanmo.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ruehyeon.sanmo.ui.main.DataTransformer;


@Database(entities = {ChatEntity.class}, version = 1)
@TypeConverters({DataTransformer.class})
public abstract class ChatDatabase extends RoomDatabase {
    private static ChatDatabase chatDatabase;

    public abstract ChatDao chatDao();

    public static ChatDatabase getChatDatabase(Context context) {
        if (chatDatabase == null) {
            chatDatabase = Room.databaseBuilder(context.getApplicationContext(), ChatDatabase.class, "chat-data")
                    .allowMainThreadQueries()
                    .build();
        }
        return chatDatabase;
    }

    public static void destroyInstance() {
        chatDatabase = null;
    }
}
