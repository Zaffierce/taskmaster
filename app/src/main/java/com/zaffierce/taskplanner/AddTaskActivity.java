package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.amplify.generated.graphql.ListTasksQuery;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.List;

import javax.annotation.Nonnull;

import type.CreateTaskInput;
import type.TaskState;


public class AddTaskActivity extends AppCompatActivity {

//    public AppDatabase db;

    AWSAppSyncClient awsAppSyncClient;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        Button addTaskButton = findViewById(R.id.addTaskButton);

        awsAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();


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



            Toast toast = Toast.makeText(this, "Toast successfully saved", Toast.LENGTH_LONG);
            toast.show();
            AddTaskActivity.this.finish();
        });
    }
    public void runTaskMutation(String title, String body){
        System.out.println(title + '\n'+ body);
        CreateTaskInput createTaskInput = CreateTaskInput.builder()
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
//            Handler handler = new Handler(Looper.getMainLooper()) {
//
//                @Override
//                public void handleMessage(Message inputmessage) {
//                    CreateTaskMutation.CreateTask task = response.data().createTask();
//
//                }
//            };
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("GraphQLFail", e.getMessage());
        }
    };


}
