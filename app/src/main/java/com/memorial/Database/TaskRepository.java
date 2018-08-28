package com.memorial.Database;

import com.memorial.Model.Task;

import java.util.List;

import io.reactivex.Flowable;

public class TaskRepository implements ITaskDataSource{

    private ITaskDataSource mLocalDataSource;

    private static TaskRepository mInstance;

    public TaskRepository(ITaskDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static TaskRepository getInstance(ITaskDataSource mLocalDataSource){
        if(mInstance == null){
            mInstance = new TaskRepository(mLocalDataSource);

        }
        return mInstance;
    }

    @Override
    public Flowable<Task> getTaskById(int taskid) {
        return mLocalDataSource.getTaskById(taskid);
    }

    @Override
    public Flowable<List<Task>> getAllTasks() {
        return mLocalDataSource.getAllTasks();
    }

    @Override
    public void insertTask(Task... tasks) {
        mLocalDataSource.insertTask(tasks);
    }

    @Override
    public void updateTask(Task... tasks) {
        mLocalDataSource.updateTask(tasks);
    }

    @Override
    public void deleteTask(Task task) {
        mLocalDataSource.deleteTask(task);
    }

    @Override
    public void deleteAllTasks() {
        mLocalDataSource.deleteAllTasks();
    }
}
