
package com.test.Bishal_todo;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TodoAdapter mTodoAdapter;
    private ArrayList<Todo> mTodoList;
    private AppCompatTextView mTextViewEmpty;
//    private CheckBox chk;
//    private TextView txt;
    private long BackPressedTimes;



    AlertDialog.Builder mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.rv_todo);
        mTextViewEmpty = findViewById(R.id.tv_empty);
        setSupportActionBar(toolbar);
        ToDoViewModel mViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);
        mTodoList = new ArrayList<>();
        mTodoAdapter = new TodoAdapter(mTodoList, mViewModel);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mTodoAdapter);

        LiveData<List<Todo>> allTodo = mViewModel.getAllTodo();
        allTodo.observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todoData) {
                mTodoList.clear();
                mTodoList.addAll(todoData);
                if (mTodoList.isEmpty()) {
                    mTextViewEmpty.setVisibility(View.VISIBLE);
                } else {
                    mTextViewEmpty.setVisibility(View.GONE);
                }


                mTodoAdapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddToDoActivity.class);
                startActivity(intent);
            }
        });


    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
//            case R.id.menu_delete_all:
//
//// setting Dialogbox for Delete all.
//                mAlertDialog = new AlertDialog.Builder(MainActivity.this);
//
//                mAlertDialog.setMessage(getString(R.string.edit_delete_all))
//                        .setCancelable(false)
//                        .setTitle(getString(R.string.app_name))
//                        .setIcon(R.drawable.ic_todoicon);
//
//                mAlertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mTodoViewModel.deleteAll();
//
//                    }
//                });
//
//                mAlertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                mAlertDialog.show();
//
//                break;

            //setting Alertdialogbox for logout.

            case R.id.menu_logout:

                mAlertDialog = new AlertDialog.Builder(MainActivity.this);

                mAlertDialog.setMessage(getString(R.string.edit_logout))
                        .setCancelable(false)
                        .setTitle(getString(R.string.app_name))
                        .setIcon(R.drawable.ic_todoicon);

                mAlertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("todo_pref", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

                mAlertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                mAlertDialog.show();

                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (BackPressedTimes+2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;

        }
        else {
            Toast.makeText(getBaseContext(),"Press Back again to Exit",Toast.LENGTH_SHORT).show();
        }
        BackPressedTimes=System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                queryList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when query submitted
                queryList(query);
                return false;
            }
        });
        return true;
    }

    private void queryList(String query) {
        if (query.length() >= 2) {
            mTodoAdapter.getFilter().filter(query.toLowerCase());
        } else {
            mTodoAdapter.getFilter().filter("");
        }
        mTodoAdapter.notifyDataSetChanged();
    }


    
}
