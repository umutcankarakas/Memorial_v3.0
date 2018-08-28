package com.memorial.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.memorial.Model.Task;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM tasks WHERE taskid=:taskid")
    Flowable<Task> getTaskById(int taskid);

    @Query("SELECT * FROM tasks")
    Flowable<List<Task>> getAllTasks();

    @Insert
    void insertTask(Task... tasks);

    @Update
    void updateTask(Task... tasks);

    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM tasks")
    void deleteAllTasks();
}
