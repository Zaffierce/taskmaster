package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        String s3URL = "https://taskmasterbce990cca82f43549a274e60d28d5518-local.s3-us-west-2.amazonaws.com/";

        Picasso.get().load("http://i.imgur.com/DvpvklR.png").into((ImageView)findViewById(R.id.image_taskDetail));
    }
}
