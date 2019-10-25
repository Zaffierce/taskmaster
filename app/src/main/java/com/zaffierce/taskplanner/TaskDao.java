package com.zaffierce.taskplanner;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("select * from task order by id desc")
    List<Task> getAll();

    @Insert
    void addTask(Task task);

    @Query("delete from task")
    void deleteAllTasks();
}
