package com.test.Bishal_todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddToDoActivity extends AppCompatActivity {

    private ToDoViewModel mViewModel;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private Calendar calendar;

    private AppCompatEditText appCompatEditTextDate, appCompatEditTextTodo;
    private AppCompatButton mButtonSave;
    private AppCompatSpinner mSpinner;
private ImageView image;
private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        Toolbar toolbar = findViewById(R.id.add_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendar = Calendar.getInstance();
        mViewModel = ViewModelProviders.of(this).get(ToDoViewModel.class);

        appCompatEditTextDate = findViewById(R.id.et_date);
        appCompatEditTextTodo = findViewById(R.id.et_todo);
        mButtonSave = findViewById(R.id.btn_save);
        mSpinner = findViewById(R.id.spinner_priority);


        appCompatEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        appCompatEditTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });
        initViews();

        mViewModel.enteredTodo.setValue(appCompatEditTextTodo.getText().toString().trim());

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = appCompatEditTextTodo.getText().toString().trim();
                String date = appCompatEditTextDate.getText().toString().trim();
                String selectedPriority = mSpinner.getSelectedItem().toString();
                if (todo.isEmpty() && date.isEmpty()) {
                    Toast.makeText(AddToDoActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedPriority.equals(getString(R.string.priority_select))) {
                        Toast.makeText(AddToDoActivity.this, "Priority is not selected", Toast.LENGTH_SHORT).show();
                    } else {
                        //save to db
                        Todo todoData = new Todo();
                        todoData.setTodo(todo);
                        todoData.setDate(date);
                        todoData.setStatus("Pending");
                        todoData.setPriority(selectedPriority);
                        mViewModel.insert(todoData);
                        Toast.makeText(AddToDoActivity.this, "Saved todo successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

    private void initViews() {

      txt=findViewById(R.id.et_todo);
    }

    private void showDatePicker() {
        new DatePickerDialog(AddToDoActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mViewModel.enteredDate.setValue(simpleDateFormat.format(calendar.getTime()));
            appCompatEditTextDate.setText(mViewModel.enteredDate.getValue());
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

    public void getspeechrecog(View view) {
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
