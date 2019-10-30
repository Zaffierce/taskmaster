package com.zaffierce.taskplanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.amplify.generated.graphql.ListTasksQuery;

import java.util.List;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long id;

    //Thank you all mighty Stack Overflow && Michelle
    //▄█▀█● ▄█▀█● ▄█▀█●
    //https://stackoverflow.com/questions/44498616/android-architecture-components-using-enums

    @TypeConverters(StatusConverter.class)
    public TaskState taskState;

    public enum TaskState {
     NEW(0),
     ASSIGNED(1),
     IN_PROGRESS(2),
     COMPLETE(3);

     private int code;

     TaskState(int code){
         this.code = code;
     }

     public int getCode() {
         return code;
     }
    }

    private String title;
    private String body;
//    List<Task> tasks;
//    private TaskState taskState;

    public Task(String title, String body, TaskState taskState){
        this.title = title;
        this.body = body;
        this.taskState = taskState;
    }

    public Task(CreateTaskMutation.CreateTask task) {
        this.title = task.title();
        this.body = task.body();
//        this.taskState = ;
    }

    public Task(ListTasksQuery.Item task){
        this.title = task.title();
        this.body = task.body();
    }
    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }

    public String getBody(){ return body; }

    public TaskState getTaskState() { return taskState; }

    public String toString() {
        return String.format("Task Title is: %s -- Task body is:  %s -- State is: %s", this.title, this.body, this.taskState);
    }
}
