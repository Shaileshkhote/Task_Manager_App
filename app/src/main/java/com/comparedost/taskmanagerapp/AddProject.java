package com.comparedost.taskmanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProject extends AppCompatActivity {
    private static final String TAG = "AddProject";


    EditText ProjectName,ProjectDescription;
    Button Saveproject;
    ImageButton UploadImage;
    private ProgressBar loader;
    public String ProName,ProDesc,currentuser;

    private FirebaseDatabase mydb;
   private DatabaseReference myref;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        ProjectName=findViewById(R.id.ProjectName);
        ProjectDescription=findViewById(R.id.ProjectDescription);
        Saveproject=findViewById(R.id.Savebtn);
        UploadImage=findViewById(R.id.Uploadimg);
        loader=findViewById(R.id.addprojectloader);

        ProName=ProjectName.getText().toString();
        ProDesc=ProjectDescription.getText().toString();

        Log.i(TAG, ProName);

        mydb=FirebaseDatabase.getInstance();
        myref=mydb.getReference("Users");

        mauth = FirebaseAuth.getInstance();
        currentuser=mauth.getCurrentUser().getDisplayName().toString();




        Saveproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loader.setVisibility(View.VISIBLE);

                ProName=ProjectName.getText().toString();
                ProDesc=ProjectDescription.getText().toString();
                Log.i(TAG, ProName);

                if(!(ProName.isEmpty()) && !(ProDesc.isEmpty())  ) {
                    try {
                        ProjectName.setEnabled(false);
                        ProjectDescription.setEnabled(false);

                        myref.child(currentuser).child("Projects").child(ProName).child("Project_Name").setValue(ProName);

                        myref.child(currentuser).child("Projects").child(ProName).child("Project_Description").setValue(ProDesc);


                        Saveproject.setEnabled(false);

                        loader.setVisibility(View.GONE);

                    }catch (Exception e){}


                }
                else {
                    Toast.makeText(AddProject.this, "Fields missing", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
