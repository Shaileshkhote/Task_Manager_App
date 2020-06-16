package com.comparedost.taskmanagerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProjectInfo extends AppCompatActivity {
    public static final String TAG="Projectinfo";
    TextView projname, projdesc;
    Button viewtask,editproj,deleteproj;
    public String fromintent;
    public String  ProjDe,currentUser;
    int isadmin;

    DatabaseReference  ref;

    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        projname=findViewById(R.id.projectNametext);
        projdesc=findViewById(R.id.descriptiontext);
        viewtask=findViewById(R.id.viewtaskbtn);
        editproj=findViewById(R.id.editprojbtn);
        deleteproj=findViewById(R.id.deleteprojbtn);


        Bundle extras=getIntent().getExtras();
        fromintent=extras.getString("projectnameclicked");
        projname.setText(fromintent);

        mauth=FirebaseAuth.getInstance();
        currentUser=mauth.getCurrentUser().getDisplayName();

        Toast.makeText(this, currentUser, Toast.LENGTH_SHORT).show();

        ref = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference user=ref.child(currentUser);
        DatabaseReference curuser=user.child("Projects");
        final DatabaseReference Projects=curuser.child(fromintent);
        DatabaseReference projname=Projects.child("Project_Description");
        projname.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if(dataSnapshot.exists()){

                 ProjDe=dataSnapshot.getValue().toString();
                 Log.e(TAG, "Project des"+ProjDe );
                 projdesc.setText(ProjDe);
             }

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
                            deleteproj.setEnabled(true);


                        }
                    }

                }catch (NullPointerException e) {}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        deleteproj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Projects.removeValue();
                Intent i=new Intent(ProjectInfo.this,MainActivity.class);
                startActivity(i);

            }
        });

        viewtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProjectInfo.this,TaskList.class);
                intent.putExtra("fromprojectinfo",fromintent);
                startActivity(intent);
            }
        });



//        editproj.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
}
