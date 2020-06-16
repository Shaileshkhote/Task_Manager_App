package com.comparedost.taskmanagerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectAdapter extends BaseAdapter {
    Context context;
    TextView projectname,projectdes;
    public ArrayList<ListData> arrayList;

    public ProjectAdapter(Context context, ArrayList<ListData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getCount() {
        return arrayList.size();
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
    public View getView(int position, View View, ViewGroup parent) {
      View = LayoutInflater.from(context).inflate(R.layout.projects_list,parent,false);
        projectname=View.findViewById(R.id.liTextview);
        projectdes=View.findViewById(R.id.projectDescription);
        projectname.setText(arrayList.get(position).getProjectName());
        projectdes.setText(arrayList.get(position).getProjectdescription());
        return View;
    }
}
