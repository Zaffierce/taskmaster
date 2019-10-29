package com.zaffierce.taskplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    public List<Task> taskList;
    private OnTaskInteractionListener listener;

    public TasksAdapter(List<Task> taskList, OnTaskInteractionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        Task task;
        TextView taskTitle;
        TextView taskBody;
        TextView taskStatus;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.taskTitle = itemView.findViewById(R.id.fragment_taskTitle);
            this.taskBody = itemView.findViewById(R.id.fragment_taskTitle);
            this.taskStatus = null;
            //TODO: Implement taskStatus somewhere
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tasks, parent, false);
        final TaskViewHolder holder = new TaskViewHolder(v);
        v.setOnClickListener((view) -> listener.taskInterface(holder.task));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task taskAtPosition =this.taskList.get(position);
        holder.task = taskAtPosition;
        holder.taskTitle.setText(taskAtPosition.getTitle());
        holder.taskBody.setText(taskAtPosition.getBody());
    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }

//    public int getItemCount() {
//        return 3;
//    }

    public static interface OnTaskInteractionListener {
        public void taskInterface(Task task);
    }
}
