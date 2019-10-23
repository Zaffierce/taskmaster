package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = prefs.getString("username", "null");
        TextView textView = findViewById(R.id.MainUsernameHeader);
        textView.setText("こんにちは、" +username+"!");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener((event) -> {
            startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
        });

        Button allTaskButton = findViewById(R.id.allTasksButton);
        allTaskButton.setOnClickListener((event) -> {
            startActivity(new Intent(MainActivity.this, AllActivity.class));
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener((event) -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
    }


}
