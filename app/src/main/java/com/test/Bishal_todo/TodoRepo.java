package com.test.Bishal_todo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TodoRepo {

    private TodoDao mTodoDao;
    private LiveData<List<Todo>> mAllToDoList;

    public TodoRepo(Application application) {
        TodoDb db = TodoDb.getDatabase(application);
        mTodoDao = db.appDao();
        mAllToDoList = mTodoDao.getAllToDo();
    }

    public LiveData<List<Todo>> getAllToDo() {
        return mAllToDoList;
    }


    public void insert (Todo todo) {
        new insertAsyncTask(mTodoDao).execute(todo);
    }

//    public void deleteAll()
//    {
//        new deleteAllTodoAsynchTask(mTodoDao).execute();
//    }

    private static class insertAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao mAsyncTaskDao;

        insertAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Todo... params) {
            mAsyncTaskDao.insertTodo(params[0]);
            return null;
        }
    }


    public void delete (Todo todo) {
        new deleteAsyncTask(mTodoDao).execute(todo);
    }

    private static class deleteAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao mAsyncTaskDao;

        deleteAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Todo... params) {
            mAsyncTaskDao.deleteTodo(params[0]);
            return null;
        }
    }

//    private static class deleteAllTodoAsynchTask extends AsyncTask<Todo, Void, Void>
//
//    {
//        private TodoDao mTodoDAO;
//        private deleteAllTodoAsynchTask(TodoDao todoDAO)
//        {
//            mTodoDAO = todoDAO;
//        }
//
//        //Generating Override Method (doInBackground)
//
//
//        @Override
//        protected Void doInBackground(final Todo... parms) {
//
//            mTodoDAO.deleteAll();
//            return null;
//        }
//    }
}