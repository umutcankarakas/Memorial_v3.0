package com.memorial.Model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "tasks")
public class Task {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="taskid")
    private int taskid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "detail")
    private String detail;

    @ColumnInfo(name = "status")
    private boolean status;

    public Task(){
    }

    @Ignore
    public Task(String title, String detail) {
        this.title = title;
        this.detail = detail;
        this.status = false;
    }

    @NonNull
    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(@NonNull int taskid) {
        this.taskid = taskid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringBuilder(title).append("\n").append(detail).toString();
    }
}
