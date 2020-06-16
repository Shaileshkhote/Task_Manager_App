package com.comparedost.taskmanagerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskList extends AppCompatActivity {

    ArrayList<Task_Data> arrayTaskdata =new ArrayList<> ();
    ListView tasklist;
    ExtendedFloatingActionButton addtaskfab;
    TaskAdapter adapter;

    private FirebaseDatabase mydb;
    private DatabaseReference myref,Users,curruser,projects,projname,fromin;

    String fromintent,from;
    String dbrefrence;
    public static final String TAG="TaskName";
    private int isadmin;

    public String TaskNameEditText,currentUser,currentUsername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        try {
            Bundle extras=getIntent().getExtras();
            from=extras.getString("fromprojectinfo");


        }catch (NullPointerException e){}

        addtaskfab=findViewById(R.id.addtaskfab);

        FirebaseAuth mauth =FirebaseAuth.getInstance();
        currentUsername=mauth.getCurrentUser().getDisplayName();
        //dbrefrence="Users/"+currentUsername+"/Projects/"+from+"/Tasks/";
        Log.e(TAG, "oDbrefrence"+dbrefrence );




        tasklist=findViewById(R.id.taskListview);

        adapter=new TaskAdapter(getApplicationContext(),arrayTaskdata);
        mydb= FirebaseDatabase.getInstance();

        //myref = mydb.getReference(dbrefrence);
        myref=mydb.getReference();
         Users=myref.child("Users");
         curruser=Users.child(currentUsername);
         projects=curruser.child("Projects");
         projname=projects.child(from);
         fromin=projname.child("Tasks");

         tasklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 TextView clicked= view.findViewById(R.id.tasknameview);
                 Intent i =new Intent(TaskList.this,TaskInfo.class);
                 i.putExtra("tasknameclicked",clicked.getText().toString().trim());
                 i.putExtra("projectname",from);
                 startActivity(i);

                 Toast.makeText(TaskList.this,clicked.getText().toString(), Toast.LENGTH_SHORT).show();
             }
         });

        curruser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isadmin=Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                if(isadmin == 1){
                    addtaskfab.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        fetchdata();




        addtaskfab.shrink();
        addtaskfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addtaskfab.isExtended()){

                    addtaskfab.shrink();

                    Intent intent=new Intent(TaskList.this,AddTask.class);
                    intent.putExtra("fromintent",from);

                    startActivity(intent);
                }

                else{
                    addtaskfab.extend();
                }
            }
        });








//
    }

    public void fetchdata(){

        fromin.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            try {
                TaskNameEditText=dataSnapshot.child("Task_Name").getValue(String.class);
                String Assignedto=dataSnapshot.child("AssignedTo-"+TaskNameEditText).getValue(String.class);

                arrayTaskdata.add(new Task_Data(TaskNameEditText,Assignedto,"null","null"));
                Log.e(TAG, "DB Returned :"+TaskNameEditText );
                tasklist.setAdapter(adapter);


            }catch (NullPointerException e){}


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
