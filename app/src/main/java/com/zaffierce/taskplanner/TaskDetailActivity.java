package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        String taskTitleFromExtras = getIntent().getStringExtra("title");
        TextView titleTextBox = findViewById(R.id.taskDetail_TaskTitleBox);
        titleTextBox.setText(taskTitleFromExtras);

        String taskBodyFromExtras = getIntent().getStringExtra("body");
        TextView bodyTextBox = findViewById(R.id.taskDetail_TaskBodyBox);
        bodyTextBox.setText(taskBodyFromExtras);

        String taskStateFromExtras = getIntent().getStringExtra("state");
        TextView stateTextBox = findViewById(R.id.taskDetail_TaskStateBox);
        stateTextBox.setText(taskStateFromExtras);
    }
}
