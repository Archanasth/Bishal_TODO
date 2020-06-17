package com.test.Bishal_todo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Todo.class}, version = 1, exportSchema = false)
public abstract class TodoDb extends RoomDatabase {

    public abstract TodoDao appDao();

    private static volatile TodoDb INSTANCE;

    public static TodoDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoDb.class, "todo_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
