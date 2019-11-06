package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String username = prefs.getString("username", "null");
        String username = AWSMobileClient.getInstance().getUsername();
        if (username == null) {
            username = "user";
        }
        TextView textView = findViewById(R.id.MainUsernameHeader);
        textView.setText("こんにちは、" +username+"!");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails result) {
                        Log.i("veach", result.getUserState().toString());
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("veach", e.getMessage());
                    }
                });
//        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>());
//        AWSMobileClient.getInstance().initialize(getApplicationContext(), new com.amazonaws.mobile.client.Callback<UserStateDetails>())
//        {
//        }

        setContentView(R.layout.activity_main);

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener((event) -> {
            startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
        });

        Button allTaskButton = findViewById(R.id.allTasksButton);
        allTaskButton.setOnClickListener((event) -> {
            startActivity(new Intent(MainActivity.this, AllTasksActivity.class));
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener((event) -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
    }

    public void onPicolasCageClick(View view) {
        startActivity(new Intent(MainActivity.this, PicolasActivity.class));
    }

    public void onSignoutClick(View view) {
        String username = AWSMobileClient.getInstance().getUsername();
        TextView textView = findViewById(R.id.MainUsernameHeader);
        textView.setText("Bye " +username+ "!");
        AWSMobileClient.getInstance().signOut();
    }

    public void onSignInClick(View view) {
        AWSMobileClient.getInstance().showSignIn(MainActivity.this,
                SignInUIOptions.builder()
                        .backgroundColor(1)
                        .logo(R.mipmap.picolas_cage_foreground)
                        .build(),
                new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                Log.i("veach", result.getUserState().toString());
                Log.i("veach", AWSMobileClient.getInstance().getUsername());
                Log.i("veach", AWSMobileClient.getInstance().currentUserState().getDetails().toString());
            }

            @Override
            public void onError(Exception e) {
                Log.e("veach", e.getMessage());
            }
        });
    }
}
