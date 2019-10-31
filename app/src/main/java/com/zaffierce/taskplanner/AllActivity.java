package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListTasksQuery;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AllActivity extends AppCompatActivity implements TasksAdapter.OnTaskInteractionListener {

    private List<Task> tasks;

//    public AppDatabase db;

    private TasksAdapter tasksAdapter;

    AWSAppSyncClient awsAppSyncClient;

    RecyclerView recyclerView;

    public void renderResponseData(String data) {
//        System.out.println(data);
//          this.tasks = new LinkedList<>();
//          this.tasks.addAll(data);
//        TextView tasks =
//        this.tasksAdapter.addNewTasks(data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allactivity);

        this.tasks = new LinkedList<>();

        awsAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();

        queryAllTasks();

        recyclerView = findViewById(R.id.recycler_AllTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TasksAdapter(this.tasks, this));



//        queryAllTasks();
//
//        recyclerView = findViewById(R.id.recycler_AllTasks);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new TasksAdapter(this.tasks, this));

//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://taskmaster-api.herokuapp.com/tasks")
//                .build();
//        client.newCall(request).enqueue(new OkHTTPResponse(AllActivity.this));
//        this.taskAdapter.notifyDataSetChanged();







//        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasks")
//                .allowMainThreadQueries().build();
//        this.tasks = new LinkedList<>();
////        db.taskDao().deleteAllTasks(); // Uncomment to clear DB
//        this.tasks.addAll(db.taskDao().getAll());
//        RecyclerView recyclerView = findViewById(R.id.recycler_AllTasks);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new TasksAdapter(this.tasks, this));
    }

//    AddTaskActivity addTaskActivity = new AddTaskActivity();

    public void queryAllTasks() {
        awsAppSyncClient.query(ListTasksQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(getAllTasksQuery);
    }

    public GraphQLCall.Callback<ListTasksQuery.Data> getAllTasksQuery = new GraphQLCall.Callback<ListTasksQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListTasksQuery.Data> response) {
            Log.i("GraphQLgetTasks", "Successfully queried all tasks");
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    List<ListTasksQuery.Item> tasksFromMagicLand = response.data().listTasks().items();
                    tasks.clear();
                    for (ListTasksQuery.Item task : tasksFromMagicLand) {
                        tasks.add(new Task(task));
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            };
            handler.obtainMessage().sendToTarget();
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("GraphQLgetFail", e.getMessage());
        }
    };

    @Override
    public void taskInterface(Task task) {
    Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("body", task.getBody());
        intent.putExtra("state", task.getTaskState());
        AllActivity.this.startActivity(intent);
    }
}
