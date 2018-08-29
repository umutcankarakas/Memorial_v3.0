package com.memorial;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.memorial.Database.TaskRepository;
import com.memorial.Local.TaskDataSource;
import com.memorial.Local.TaskDatabase;
import com.memorial.Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ListTaskActivity extends AppCompatActivity implements AddTaskDialog.AddTaskDialogListener{

    @BindView(R.id.lstTasks) ListView lstTask;
    @BindView(R.id.fab) FloatingActionButton fab;


    //Adapter
    List<Task> taskList = new ArrayList<>();
    CustomListViewAdapter adapter;

    //Database
    private CompositeDisposable compositeDisposable;
    private TaskRepository taskRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);
        ButterKnife.bind(this);


        initialize();
        fillArrayList((ArrayList<Task>) taskList);

        //Init

        compositeDisposable = new CompositeDisposable();

        //Init View

        //adapter = new ArrayAdapter(ListTaskActivity.this, taskList);

        //lstTask.setAdapter(adapter);

        //Database
        TaskDatabase taskDatabase = TaskDatabase.getInstance(this);//Create database
        taskRepository = TaskRepository.getInstance(TaskDataSource.getInstance((taskDatabase.taskDAO())));
        registerForContextMenu(lstTask);
        //Load all data from Database
        loadData();

        //Event
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();

            }
        });

    }

    private void initialize() {
        taskList = new ArrayList<Task>();
        lstTask = (ListView) findViewById(R.id.lstTasks);
        adapter = new CustomListViewAdapter(ListTaskActivity.this,(ArrayList<Task>) taskList);
        lstTask.setAdapter(adapter);
    }

    private void fillArrayList(ArrayList<Task>tasks) {
        for (int index = 0; index < 20; index++) {
            Task task = new Task("Mr. Android " + index, "Nowhere");
            tasks.add(task);
        }
    }
    public void openDialog() {
        AddTaskDialog addTaskDialog = new AddTaskDialog();
        addTaskDialog.show(getSupportFragmentManager(), "add task dialog");
    }

    private void loadData(){
        //Using RxJava
        Disposable disposable = taskRepository.getAllTasks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Task>>() {
                    @Override
                    public void accept(List<Task> tasks) throws Exception {
                        onGetAllTaskSuccess(tasks);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(ListTaskActivity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);

    }

    private void onGetAllTaskSuccess(List<Task> tasks) {
        taskList.clear();
        taskList.addAll(tasks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_clear:
                deleteAllTasks();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllTasks() {

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                taskRepository.deleteAllTasks();
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(ListTaskActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData(); // Refresh data
                            }
                        }


                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Select action:");

        menu.add(Menu.NONE, 0, Menu.NONE, "UPDATE");
        menu.add(Menu.NONE, 1, Menu.NONE, "DELETE");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final Task task = taskList.get(info.position);
        switch ((item.getItemId())){

            case 0: { //update
                final EditText edtTitle = new EditText(ListTaskActivity.this);
                edtTitle.setText(task.getTitle());
                edtTitle.setHint("Task Title");
                final EditText edtDetail = new EditText(ListTaskActivity.this);
                edtDetail.setText(task.getDetail());
                edtDetail.setHint("Task Detail");

                LinearLayout layout = new LinearLayout(ListTaskActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                layout.addView(edtTitle);
                layout.addView(edtDetail);


                new AlertDialog.Builder(ListTaskActivity.this)
                        .setTitle("Update Task")
                        .setView(layout)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(TextUtils.isEmpty(edtTitle.getText().toString()))
                                    return;
                                else{
                                    task.setTitle(edtTitle.getText().toString());
                                    task.setDetail(edtDetail.getText().toString());
                                    updateTask(task);
                                }
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
            break;
            case 1:{ //Delete
                new AlertDialog.Builder(ListTaskActivity.this)
                        .setMessage("Do you want to delete "+task.toString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteTask(task);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
            break;
        }
        return true;
    }

    private void deleteTask(final Task task) {

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                taskRepository.deleteTask(task);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(ListTaskActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData(); // Refresh data
                            }
                        }


                );

        compositeDisposable.add(disposable);
    }

    private void updateTask(final Task task) {
        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                taskRepository.updateTask(task);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(ListTaskActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData(); // Refresh data
                            }
                        }


                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void applyTexts(final String title, final String detail) {

        //Add new task

        Disposable disposable = io.reactivex.Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Task task = new Task(title,
                        detail);
                taskList.add(task);
                taskRepository.insertTask(task);
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object o) throws Exception {
                                   Toast.makeText(ListTaskActivity.this, "Task added!", Toast.LENGTH_SHORT).show();
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(ListTaskActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData(); // Refresh data
                            }
                        }


                );
    }
}
