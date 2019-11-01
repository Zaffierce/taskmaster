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
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateTaskInput;
import type.TaskState;


public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
                // response has all of the Team data from the cloud
                // let's put it into those radio buttons... are in the view thread :/
                // so send it to the main thread to actually put data into radio buttons
                Handler h = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        teams.addAll(response.data().listTeams().items());

                        LinkedList<String> teamNames = new LinkedList<>();
                        for (ListTeamsQuery.Item team : teams) {
                            teamNames.add(team.name());
//                            System.out.println(team.name());
                        }

                        Spinner spinner =  findViewById(R.id.spinner_teamNames);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTaskActivity.this, android.R.layout.simple_spinner_item, teamNames);
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(AddTaskActivity.this);
                    }
                };
                h.obtainMessage().sendToTarget();

            }

//        ListTeamsQuery query = ListTeamsQuery.builder().build();
//        awsAppSyncClient.query(query)
//                .responseFetcher(AppSyncResponseFetchers.NETWORK_ONLY)
//                .enqueue(new GraphQLCall.Callback<ListTeamsQuery.Data>() {
//            @Override
//            public void onResponse(@Nonnull Response<ListTeamsQuery.Data> response) {
//                Handler handler = new Handler(Looper.getMainLooper()) {
//                    @Override
//                    public void handleMessage(Message message) {
//                        teams.addAll(response.data().listTeams().items());
//
//                        LinkedList<String> teamNames = new LinkedList<>();
//                        for (ListTeamsQuery.Item team : teams) {
//                            teamNames.add(team.name());
//                        }
//
//                        Spinner spinner = findViewById(R.id.spinner_teamNames);
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTaskActivity.this, android.R.layout.simple_spinner_item, teamNames);
//
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spinner.setAdapter(adapter);
//                        spinner.setOnItemSelectedListener(AddTaskActivity.this);
//
//
////                        RadioButton radioButton1 = findViewById(R.id.Team1radioButton);
////                        radioButton1.setText(response.data().listTeams().items().get(0).name());
////                        RadioButton radioButton2 = findViewById(R.id.Team2radioButton);
////                        radioButton2.setText(response.data().listTeams().items().get(1).name());
////                        RadioButton radioButton3 = findViewById(R.id.Team3radioButton);
////                        radioButton3.setText(response.data().listTeams().items().get(2).name());
////                        teams.addAll(response.data().listTeams().items());
//                    }
//                };
//                handler.obtainMessage().sendToTarget();
//            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("veach", e.getMessage());

            }
        });

        addTaskButton.setOnClickListener((event) -> {

//            String teamName = null;

//            public void onRadioButtonClicked(View view) {
//                boolean checked = ((RadioButton) view).isChecked();
//
//            }
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

//    public void onRadioButtonClicked(View view) {
//        RadioButton teamOne, teamTwo, teamThree;
//        teamOne = findViewById(R.id.Team1radioButton);
//        teamTwo = findViewById(R.id.Team2radioButton);
//        teamThree = findViewById(R.id.Team3radioButton);
//
//        if(teamOne.isChecked()) { teamName = teams.get(0).id(); }
//        else if (teamTwo.isChecked()) { teamName = teams.get(1).id(); }
//        else if (teamThree.isChecked()) { teamName = teams.get(2).id(); }
//
//    }

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
}
