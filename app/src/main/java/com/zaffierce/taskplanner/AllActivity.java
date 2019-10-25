package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

public class AllActivity extends AppCompatActivity implements TasksAdapter.OnTaskInteractionListener {

    private List<Task> tasks;

    public AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allactivity);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasks")
                .allowMainThreadQueries().build();
        this.tasks = new LinkedList<>();
//        db.taskDao().deleteAllTasks(); // Uncomment to clear DB
        this.tasks.addAll(db.taskDao().getAll());
        RecyclerView recyclerView = findViewById(R.id.recycler_AllTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TasksAdapter(this.tasks, this));
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
