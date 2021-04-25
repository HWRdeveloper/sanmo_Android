package com.ruehyeon.sanmo.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ruehyeon.sanmo.ui.main.DataTransformer;


@Database(entities = {RulesEntity.class}, version = 1)
@TypeConverters({DataTransformer.class})
public abstract class RuleDatabase extends RoomDatabase {
    private static RuleDatabase ruleDatabase;

    public abstract RulesDao rulesDao();

    public static RuleDatabase getRuleDatabase(Context context, String uid) {
        if (ruleDatabase == null) {
            ruleDatabase = Room.databaseBuilder(context.getApplicationContext(), RuleDatabase.class, "user-database")
                    .allowMainThreadQueries()
                    .build();
        }
        return ruleDatabase;
    }

    public static void destroyInstance() {
        ruleDatabase = null;
    }
}
