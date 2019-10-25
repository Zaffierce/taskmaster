package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity {

    public AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        Button addTaskButton = findViewById(R.id.addTaskButton);


        addTaskButton.setOnClickListener((event) -> {
            String enteredTaskTitle;
            String enteredTaskBody;

            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasks")
                    .allowMainThreadQueries().build();

            EditText addTaskTitle = findViewById(R.id.taskTitleInputBox);
            enteredTaskTitle = addTaskTitle.getText().toString();

            EditText addTaskBody = findViewById(R.id.taskDescriptionInputBox);
            enteredTaskBody = addTaskBody.getText().toString();

            Task addNewTask = new Task(enteredTaskTitle, enteredTaskBody, Task.TaskState.NEW);
            db.taskDao().addTask(addNewTask);
            Toast toast = Toast.makeText(this, "Toast successfully saved", Toast.LENGTH_LONG);
            toast.show();

            Intent intent = new Intent(this, MainActivity.class);
            AddTaskActivity.this.startActivity(intent);
        });


    }
}
