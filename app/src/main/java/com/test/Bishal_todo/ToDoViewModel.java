package com.test.Bishal_todo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {
    public MutableLiveData<String> enteredTodo = new MutableLiveData<>();
    public MutableLiveData<String> enteredDate = new MutableLiveData<>();
    private TodoRepo mRepository;
    private LiveData<List<Todo>> mAllTodo;

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TodoRepo(application);
        mAllTodo = mRepository.getAllToDo();

    }

    public LiveData<List<Todo>> getAllTodo() {
        return mAllTodo;
    }
    public void insert(Todo todo) {
        mRepository.insert(todo);
    }


    public void delete(Todo todo) {
        mRepository.delete(todo);
    }

//    public void deleteAll() {
//        mRepository.deleteAll();
//    }

}
