package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onSaveButtonPressed(View view) {
        EditText usernameEditText = findViewById(R.id.settingsUsernameInputBox);
        String name = usernameEditText.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", name);
        editor.apply();
        Toast toast = Toast.makeText(this, "Username saved", Toast.LENGTH_LONG);
        toast.show();
    }

}
