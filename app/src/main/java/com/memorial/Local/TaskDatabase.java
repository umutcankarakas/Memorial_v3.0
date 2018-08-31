package com.memorial.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.memorial.Model.Task;

import static com.memorial.Local.TaskDatabase.DATABASE_VERSION;

@Database(entities = Task.class,version = DATABASE_VERSION,  exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "TASK-Database-Room2";

    public abstract TaskDAO taskDAO();

    public static TaskDatabase mInstance;

    public static TaskDatabase getInstance(Context context){
        if(mInstance == null){
            mInstance = Room.databaseBuilder(context, TaskDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }
}
