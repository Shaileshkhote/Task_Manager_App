package com.comparedost.taskmanagerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {

    Context context;

    TextView tasknameview,taskdescview,taskstatus;

   public ArrayList<Task_Data>  arraytaskData;

    public TaskAdapter(Context context, ArrayList<Task_Data> arraytaskData) {
        this.context = context;
        this.arraytaskData = arraytaskData;
    }


    @Override
    public int getCount() {
        return arraytaskData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.task_list,parent,false);
        tasknameview=convertView.findViewById(R.id.tasknameview);
        taskdescview=convertView.findViewById(R.id.taskdesview);
        taskstatus=convertView.findViewById(R.id.taskstatus);

        tasknameview.setText(arraytaskData.get(position).getTaskname());
        taskdescview.setText(arraytaskData.get(position).getTaskdescription());
        taskstatus.setText(arraytaskData.get(position).getTaskstatus());
        return convertView;
    }
}
