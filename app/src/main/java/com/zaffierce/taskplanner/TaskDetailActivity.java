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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String task = prefs.getString("task", "null");
        TextView textView = findViewById(R.id.taskDetailHeaderTextBox);
        textView.setText(task);
    }
}
