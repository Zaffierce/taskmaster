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
import android.widget.TextView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AllActivity extends AppCompatActivity implements TasksAdapter.OnTaskInteractionListener {

    private List<Task> tasks;

//    public AppDatabase db;

    public void renderResponseData(String data) {
//        System.out.println(data);
//          this.tasks = new LinkedList<>();
//          this.tasks.addAll(data);
//        TextView tasks =
        RecyclerView recyclerView = findViewById(R.id.recycler_AllTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TasksAdapter(this.tasks, this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allactivity);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://taskmaster-api.herokuapp.com/tasks")
                .build();
        client.newCall(request).enqueue(new OkHTTPResponse(AllActivity.this));







//        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasks")
//                .allowMainThreadQueries().build();
//        this.tasks = new LinkedList<>();
////        db.taskDao().deleteAllTasks(); // Uncomment to clear DB
//        this.tasks.addAll(db.taskDao().getAll());
//        RecyclerView recyclerView = findViewById(R.id.recycler_AllTasks);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new TasksAdapter(this.tasks, this));
    }

    @Override
    public void taskInterface(Task task) {
    Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("body", task.getBody());
        intent.putExtra("state", task.getTaskState());
        AllActivity.this.startActivity(intent);
    }
}
