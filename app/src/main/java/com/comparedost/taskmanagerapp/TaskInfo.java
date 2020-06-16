package com.comparedost.taskmanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

public class TaskInfo extends AppCompatActivity {
    public static final String TAG="Projectinfo";
    TextView taskname, taskdesc,Assignedtoview;
    Button viewtaskinfo,edittask,deletetask;
    ImageView img;
    public String fromintent,DownloadUrl,filepath,Assigned_to;
    public String  TaskDe,currentUser,tasknamefromintent,projectnamefromintent;
    int isadmin;

    DatabaseReference ref;
    private FirebaseStorage fstore;
    private StorageReference storeref;

    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        taskname=findViewById(R.id.TaskNameText);
        taskdesc=findViewById(R.id.TaskDescriptionText);
        Assignedtoview=findViewById(R.id.Assignedtoview);

        edittask=findViewById(R.id.EditTaskBtn);
        deletetask=findViewById(R.id.DeleteTaskBtn);
        img=findViewById(R.id.TaskImageView);


        Bundle extras=getIntent().getExtras();
        tasknamefromintent=extras.getString("tasknameclicked");
        projectnamefromintent=extras.getString("projectname");
        taskname.setText(tasknamefromintent);
        filepath="Images/"+tasknamefromintent;

        mauth=FirebaseAuth.getInstance();
        currentUser=mauth.getCurrentUser().getDisplayName();
        fstore=FirebaseStorage.getInstance();
        storeref=fstore.getReference().child(filepath);


        //Fetching Image From Firebase Storage........

        try {
            storeref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    DownloadUrl=uri.toString();
                    Glide.with(TaskInfo.this).load(uri).into(img);

                    Toast.makeText(TaskInfo.this, DownloadUrl, Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){}




        Toast.makeText(this, projectnamefromintent, Toast.LENGTH_SHORT).show();

        //Database refrences for Quick Realtime DB Access....

           ref = FirebaseDatabase.getInstance().getReference("Users");


           DatabaseReference user=ref.child(currentUser);
           DatabaseReference curuser=user.child("Projects");
           DatabaseReference Projects=curuser.child(projectnamefromintent);
           DatabaseReference Tasks=Projects.child("Tasks");
          final DatabaseReference Taskfromdb=Tasks.child(tasknamefromintent);
           final DatabaseReference Assigneddto=Taskfromdb.child("AssignedTo-"+tasknamefromintent);
           DatabaseReference taskdescrip=Taskfromdb.child("Task_Description");


           //Readding From Firebase Realtime DB May throw error

            taskdescrip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    TaskDe=dataSnapshot.getValue().toString();

                    Log.e(TAG, "Project des"+TaskDe );
                    taskdesc.setText(TaskDe);
                    Assignedtoview.setText(Assigned_to);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            Assigneddto.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Assigned_to=dataSnapshot.getValue().toString();
                    Assignedtoview.setText(Assigned_to);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });









        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    if(dataSnapshot.exists())
                    {
                        isadmin =Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                        if(isadmin==1){
                            deletetask.setEnabled(true);


                        }
                    }

                }catch (NullPointerException e) {}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //Delete Data By clicking Delete Button
        deletetask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference deleteAssigned=ref.child(Assigned_to+"/Projects/"+projectnamefromintent
                +"/Tasks/"+tasknamefromintent);

                deleteAssigned.removeValue();
                Taskfromdb.removeValue();
                Intent i=new Intent(TaskInfo.this,MainActivity.class);
                startActivity(i);

            }
        });


    }
}
