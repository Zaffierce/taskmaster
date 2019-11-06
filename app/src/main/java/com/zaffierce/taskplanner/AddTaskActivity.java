package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.amplify.generated.graphql.ListTasksQuery;
import com.amazonaws.amplify.generated.graphql.ListTeamsQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import type.CreateTaskInput;
import type.TaskState;


public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static int READ_REQUEST_CODE = 6304;

//    public AppDatabase db;

    AWSAppSyncClient awsAppSyncClient;

    public String teamName = null;

    final List<ListTeamsQuery.Item> teams = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        Button addTaskButton = findViewById(R.id.addTaskButton);

        awsAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
        ListTeamsQuery query = ListTeamsQuery.builder().build();
        awsAppSyncClient.query(query).enqueue(new GraphQLCall.Callback<ListTeamsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListTeamsQuery.Data> response) {
                Handler handler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        teams.addAll(response.data().listTeams().items());

                        LinkedList<String> teamNames = new LinkedList<>();
                        for (ListTeamsQuery.Item team : teams) {
                            teamNames.add(team.name());
                        }

                        Spinner spinner =  findViewById(R.id.spinner_teamNames);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTaskActivity.this, android.R.layout.simple_spinner_item, teamNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(AddTaskActivity.this);
                    }
                };
                handler.obtainMessage().sendToTarget();

            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("veach", e.getMessage());
            }
        });

        addTaskButton.setOnClickListener((event) -> {
            String enteredTaskTitle;
            String enteredTaskBody;

            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

//            db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasks")
//                    .allowMainThreadQueries().build();

            EditText addTaskTitle = findViewById(R.id.taskTitleInputBox);
            enteredTaskTitle = addTaskTitle.getText().toString();

            EditText addTaskBody = findViewById(R.id.taskDescriptionInputBox);
            enteredTaskBody = addTaskBody.getText().toString();

//            Task addNewTask = new Task(enteredTaskTitle, enteredTaskBody, Task.TaskState.NEW);
//            db.taskDao().addTask(addNewTask);

            runTaskMutation(enteredTaskTitle, enteredTaskBody);

//            Intent intent = new Intent(this, MainActivity.class);
//            AddTaskActivity.this.startActivity(intent);


            Toast toast = Toast.makeText(this, "Task successfully saved", Toast.LENGTH_LONG);
            toast.show();
            AddTaskActivity.this.finish();
        });
    }

    public void runTaskMutation(String title, String body){
        System.out.println(title + '\n'+ body);
        CreateTaskInput createTaskInput = CreateTaskInput.builder()
                .taskTeamId(teamName)
                .title(title)
                .body(body)
                .taskState(TaskState.NEW)
                .build();
        awsAppSyncClient.mutate(CreateTaskMutation.builder().input(createTaskInput).build())
                .enqueue(addTaskCallback);
    }

    public GraphQLCall.Callback<CreateTaskMutation.Data> addTaskCallback = new GraphQLCall.Callback<CreateTaskMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateTaskMutation.Data> response) {
            Log.i("GraphQLResponse", "A new task has been added.");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("GraphQLFail", e.getMessage());
        }
    };


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        teamName = teams.get(position).id();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onUploadClick(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                String uriString = uri.toString();
//                File myFile = new File(uriString);
                File myFile = new File(getApplicationContext().getFilesDir(), "/"+uriString);
                String path = myFile.getAbsolutePath();

                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int size = cursor.getColumnIndex(OpenableColumns.SIZE);
                cursor.moveToFirst();

                String nameStr = cursor.getString(nameIndex);

                Log.i("veach", nameStr);
//                String sizeStr = cursor.getString(size);


//                Log.i("veach", cursor.getString(nameIndex));

//                final File file = new File(uri.getPath());
//                Log.i("veach", file.toString());





//                Log.i("veach", "File info? " + uriString + " info" + myFile + " " + path);
//
//
//                Log.i("veach", "Uri: " + uri.toString());
//                Log.i("veach", "URI Path: " + uri.getPath());
                Uri finalUri = uri;
                AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
                    @Override
                    public void onResult(UserStateDetails result) {
                        Log.i("veach", "file successfully uploaded");
                        uploadWithTransferUtility(nameStr, myFile);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("veach", e.getMessage());
                    }
                });
            }

        }
    }

    public void uploadWithTransferUtility(String str, File ourFile) {
        TransferUtility transferUtility = TransferUtility.builder()
                .context(getApplicationContext())
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                .build();

        Log.i("veach", "into transferUtility");
//        Log.i("veach", ourFile.getName());
//        Log.i("veach", ourFile.toString());

        File file = new File(getApplicationContext().getFilesDir(), str);
        Log.i("veach", file.toString());



        TransferObserver uploadObserver = transferUtility.upload("public/"+str, file);
//        TransferObserver uploadObserver = transferUtility.upload("public/"+str, new File(getApplicationContext().getFilesDir(), str));
//        TransferObserver uploadObserver = transferUtility.upload("public/test.png", new File(getApplicationContext().getFilesDir(), "test.png"));
//
        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Log.i("veach", "File uploaded?");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("veach", ex.getMessage());

            }
        });
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            Log.i("veach", "File uploaded.......?");
        }
    }
}
