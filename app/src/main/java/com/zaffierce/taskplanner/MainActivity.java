package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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
            startActivity(new Intent(MainActivity.this, allactivity.class));
        });


    }


}
