package com.test.Bishal_todo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodo(Todo todo);

    @Query("SELECT * from todo_table ORDER BY date ASC")
    LiveData<List<Todo>> getAllToDo();

    @Delete
    void deleteTodo(Todo todo);

//    @Query("DELETE FROM todo_table")
//    void deleteAll();


}
