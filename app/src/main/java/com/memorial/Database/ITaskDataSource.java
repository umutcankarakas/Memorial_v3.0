package com.memorial.Database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.memorial.Model.Task;

import java.util.List;

import io.reactivex.Flowable;

public interface ITaskDataSource {

    Flowable<Task> getTaskById(int taskid);
    Flowable<List<Task>> getAllTasks();
    void insertTask(Task... tasks);
    void updateTask(Task... tasks);
    void deleteTask(Task task);
    void deleteAllTasks();

}
