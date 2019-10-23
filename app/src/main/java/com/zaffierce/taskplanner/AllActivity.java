package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allactivity);
    }

    public void onClickFirstTask(View view) {
        TextView textView = findViewById(R.id.taskOneTextView);
        String task = textView.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("task", task);
        editor.apply();
        startActivity(new Intent(AllActivity.this, TaskDetailActivity.class));
    }

    public void onClickSecondTask(View view) {
        TextView textView = findViewById(R.id.taskTwoTextView);
        String task = textView.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("task", task);
        editor.apply();
        startActivity(new Intent(AllActivity.this, TaskDetailActivity.class));
    }

    public void onClickThirdTask(View view) {
        TextView textView = findViewById(R.id.taskThreeTextView);
        String task = textView.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("task", task);
        editor.apply();
        startActivity(new Intent(AllActivity.this, TaskDetailActivity.class));
    }
}
