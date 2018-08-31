package com.memorial;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Ignore;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.memorial.Model.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomListViewAdapter extends ArrayAdapter<Task> {

    private final LayoutInflater inflater;
    private final Context context;
    private ViewHolder holder;
    private final ArrayList<Task> tasks;

    public CustomListViewAdapter(Context context, ArrayList<Task> tasks) {
        super(context,0, tasks);
        this.context = context;
        this.tasks = tasks;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_view_item, null);

            holder = new ViewHolder(convertView);
            //holder.taskCheck = (CheckBox) convertView.findViewById(R.id.task_check);
            //holder.taskTitleLabel = (TextView) convertView.findViewById(R.id.task_title_label);
            //holder.taskDetailLabel = (TextView) convertView.findViewById(R.id.task_detail_label);
            convertView.setTag(holder);

        }
        else{
            //Get viewholder we already created
            holder = (ViewHolder)convertView.getTag();
        }

        Task task = tasks.get(position);
        if(task != null){
            holder.taskCheck.setChecked(false);
            holder.taskTitleLabel.setText(task.getTitle());
            holder.taskDetailLabel.setText(task.getDetail());

        }

       /* holder.taskCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final boolean isChecked = holder.taskCheck.isChecked();
                // Do something here.
            }
        });*/
        return convertView;
    }

    //View Holder Pattern for better performance
    public static class ViewHolder {
        @BindView(R.id.task_title_label) TextView taskTitleLabel;
        @BindView(R.id.task_detail_label) TextView taskDetailLabel;
        @BindView(R.id.task_check) CheckBox taskCheck;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
