package com.zaffierce.taskplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private static PinpointManager pinpointManager;

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w("veach", "getInstanceId failed", task.getException());
                            return;
                        }
                        final String token = task.getResult().getToken();
                        Log.d("veach", "Registering push notifications token: " + token);
                        pinpointManager.getNotificationClient().registerDeviceToken(token);
                    });
        }
        return pinpointManager;
    }


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

        getPinpointManager(this);

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
