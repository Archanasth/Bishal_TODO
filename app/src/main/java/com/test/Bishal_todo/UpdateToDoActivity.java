package com.test.Bishal_todo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class UpdateToDoActivity extends AppCompatActivity {
    private ToDoViewModel mViewModel;

    private AppCompatEditText mEditTextDate, mEditTextTodo;
    private AppCompatButton mBtnEdit;
    private AppCompatSpinner mSpinner;
    Todo mTodo;
    TextView txt;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo);
        calendar = Calendar.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar_update);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);
        mEditTextDate = findViewById(R.id.et_edit_date);
        mEditTextTodo = findViewById(R.id.et_edit_todo);
        mBtnEdit = findViewById(R.id.btn_edit_save);
        mSpinner = findViewById(R.id.spinner_edit_priority);

        mTodo = getIntent().getParcelableExtra("todo");
        mEditTextTodo.setText(mTodo.getTodo());
        mEditTextDate.setText(mTodo.getDate());

        if (mTodo.getPriority().equals(getString(R.string.priority_low))) {
            mSpinner.setSelection(1);
        } else if (mTodo.getPriority().equals(getString(R.string.priority_medium))) {
            mSpinner.setSelection(2);
        } else if (mTodo.getPriority().equals(getString(R.string.priority_high))) {
            mSpinner.setSelection(3);
        }

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = mEditTextTodo.getText().toString().trim();
                String date = mEditTextDate.getText().toString().trim();
                if (todo.isEmpty() && date.isEmpty()) {
                    Toast.makeText(UpdateToDoActivity.this, "Fields cannot be empty",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (mSpinner.getSelectedItem().equals(getString(R.string.priority_select))) {
                        Toast.makeText(UpdateToDoActivity.this, "Priority is not selected", Toast.LENGTH_SHORT).show();
                    }
                    //save to db
                    Todo todoData = new Todo();
                    todoData.setId(mTodo.getId());
                    todoData.setTodo(todo);
                    todoData.setDate(date);
                    todoData.setPriority(mSpinner.getSelectedItem().toString());
                    todoData.setStatus(mTodo.getStatus());
                    mViewModel.insert(todoData);
                    Toast.makeText(UpdateToDoActivity.this, "Edited todo successfully",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        mEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        mEditTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });
        initviews();
    }

    private void initviews() {
        txt=findViewById(R.id.et_edit_todo);
    }

    private void showDatePicker() {
        new DatePickerDialog(UpdateToDoActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mViewModel.enteredDate.setValue(simpleDateFormat.format(calendar.getTime()));
            mEditTextDate.setText(mViewModel.enteredDate.getValue());
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if (resultCode==RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt.setText(result.get(0));
                }
                break;
        }
    }

    public void getrecog(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,10);
        } else {
            Toast.makeText(this,"Your device doesn't support speech input",Toast.LENGTH_SHORT).show();
        }

    }
}
