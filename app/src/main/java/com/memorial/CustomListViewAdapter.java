package com.memorial;

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

            holder = new ViewHolder();
            holder.taskCheck = (CheckBox) convertView.findViewById(R.id.task_check);
            holder.taskTitleLabel = (TextView) convertView.findViewById(R.id.task_title_label);
            holder.taskDetailLabel = (TextView) convertView.findViewById(R.id.task_detail_label);
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
        return convertView;
    }

    //View Holder Pattern for better performance
    private static class ViewHolder {
        TextView taskTitleLabel;
        TextView taskDetailLabel;
        CheckBox taskCheck;

    }
}
