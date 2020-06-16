package com.comparedost.taskmanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class AddTask extends AppCompatActivity  {


    EditText TaskName,TaskDescription;
    Button SaveTask,captureimage;
    ImageView imageView;
    Spinner userspinner;
    ProgressBar addtaskprogress;
    private String ProName,ProDesc,currentuser;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerdata;
    String fromintent,DownloadUrl;
    private String selected,Taskname,Taskdescription;
    public static final String TAG="Addtask";

    private FirebaseDatabase mydb;
    private DatabaseReference myref;
    private FirebaseAuth mauth;

    FirebaseStorage storage;
    StorageReference mstoreref;

    private Uri imgurl;
    Bitmap imageBitmap;
    private Uri filePath;


    private final int PICK_IMAGE_REQUEST = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        TaskName=findViewById(R.id.TaskName);
        TaskDescription=findViewById(R.id.TaskDescription);
        SaveTask=findViewById(R.id.SaveTaskBtn);
        userspinner=findViewById(R.id.spinner);
        imageView=findViewById(R.id.imageView);
        captureimage=findViewById(R.id.captureimage);
        addtaskprogress=findViewById(R.id.addtaskprogress);

        captureimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });


        mydb = FirebaseDatabase.getInstance();
        myref=mydb.getReference().child("Users");
        mauth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();


        Bundle extras=getIntent().getExtras();
        fromintent=extras.getString("fromintent");



        retrivedata();


        spinnerdata=new ArrayList<>();
         adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, spinnerdata);
         adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


        userspinner.setAdapter(adapter);

        userspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selected=parent.getItemAtPosition(position).toString().trim();
                Log.e(TAG, "onItemSelected: "+selected );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        SaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata();

            }
        });





    }


    private void retrivedata() {


        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerdata.clear();
                for(DataSnapshot spin:dataSnapshot.getChildren())
                {

                    spinnerdata.add(spin.child("Name").getValue().toString());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void savedata() {

        Taskname = TaskName.getText().toString();
        Taskdescription=TaskDescription.getText().toString();

        addtaskprogress.setVisibility(View.VISIBLE);
        mstoreref= storage.getReference().child("Images/").child(Taskname);

        SaveTask.setEnabled(false);

        if (filePath !=  null)
        {

            mstoreref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(AddTask.this, "Img Uploaded"+DownloadUrl, Toast.LENGTH_SHORT).show();
                    }
                }
            });}





        myref.child(mauth.getCurrentUser().getDisplayName())
                .child("Projects").child(fromintent).child("Tasks").child(Taskname).child("Task_Name").setValue(Taskname);
        myref.child(mauth.getCurrentUser().getDisplayName())
                .child("Projects").child(fromintent).child("Tasks").child(Taskname).child("AssignedTo-"+Taskname).setValue(selected);




        myref.child(mauth.getCurrentUser().getDisplayName())
                .child("Projects").child(fromintent).child("Tasks").child(Taskname).child("Task_Description").setValue(Taskdescription).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){}
//                {
//                    Intent intent=new Intent(AddTask.this,MainActivity.class);
//                    startActivity(intent);
////                }
//                else{
//                    Toast.makeText(AddTask.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                }

            }


        });
        myref.child(selected)
                .child("Projects").child(fromintent).child("Tasks").child(Taskname).child("AssignedTo-"+Taskname).setValue(selected);
        myref.child(selected)
                .child("Projects").child(fromintent).child("Project_Name").setValue(fromintent);
        myref.child(selected)
                .child("Projects").child(fromintent).child("Project_Description").setValue(fromintent);

        myref.child(selected)
                .child("Projects").child(fromintent).child("Tasks").child(Taskname).child("Task_Name").setValue(Taskname);
        myref.child(selected)
                .child("Projects").child(fromintent).child("Tasks").child(Taskname).child("Task_Description").setValue(Taskdescription);

        addtaskprogress.setVisibility(View.GONE);
        SaveTask.setEnabled(true);
    }

    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
                Log.e(TAG, "onActivityResult: "+filePath);


            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


}
