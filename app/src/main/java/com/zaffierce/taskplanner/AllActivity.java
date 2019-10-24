package com.zaffierce.taskplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

public class AllActivity extends AppCompatActivity implements TasksAdapter.OnTaskInteractionListener {

    private List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allactivity);
        this.tasks = new LinkedList<>();
        tasks.add(new Task("Finish Lab", "Finish this super complicated lab.", TaskState.NEW));
        tasks.add(new Task("Drink beer", "After completion of lab, enjoy some nice tasty beer.", TaskState.NEW));
        tasks.add(new Task("Contemplate Life", "After completion of beer, contemplate life for a litte while.", TaskState.NEW));

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
