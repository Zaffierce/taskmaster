package com.zaffierce.taskplanner;

public class Task {

    private String title;
    private String body;
    private TaskState taskState;

    public Task(String title, String body, TaskState taskState){
        this.title = title;
        this.body = body;
        this.taskState = taskState;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public String toString() {
        return String.format("Task Title is is: %s -- Task body is:  %s -- State is: %s", this.title, this.body, this.taskState);
    }
}
