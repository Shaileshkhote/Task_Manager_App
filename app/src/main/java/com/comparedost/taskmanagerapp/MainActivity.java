package com.comparedost.taskmanagerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    ExtendedFloatingActionButton fab1,signoutfab2;
    private ProgressBar loader;
    ListView pname;
    ArrayList<ListData> arrayList= new ArrayList<>();

   private FirebaseDatabase Mydb;
    private DatabaseReference myref;

   private FirebaseAuth mauth;

    public  String projName,currentUser;
    public  String ProjectViewTextField;
    int isadmin;
    ProjectAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //projlisttext=findViewById(R.id.liTextview);

        fab1=findViewById(R.id.addprojectfab);
        signoutfab2=findViewById(R.id.signoutfab2);
        pname=findViewById(R.id.projectListview);
        loader=findViewById(R.id.mainloader);
        loader.setVisibility(View.VISIBLE);

        fab1.shrink();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fab1.isExtended()){
                    fab1.shrink();
                    Intent intent=new Intent(MainActivity.this,AddProject.class);
                    startActivity(intent);
                }
                else{
                    fab1.extend();
                }
            }
        });




        adapter=new ProjectAdapter(getApplicationContext(),arrayList);


        pname.setAdapter(adapter);
        pname.setClickable(true);
        pname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             TextView clicked= view.findViewById(R.id.liTextview);
                Intent i =new Intent(MainActivity.this,ProjectInfo.class);
                i.putExtra("projectnameclicked",clicked.getText().toString().trim());
                startActivity(i);

                Toast.makeText(MainActivity.this,clicked.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mauth=FirebaseAuth.getInstance();
        currentUser = mauth.getCurrentUser().getDisplayName();
        Mydb=  FirebaseDatabase.getInstance();
        myref= Mydb.getReference().child("Users").child(currentUser);
        DatabaseReference Users=myref.child("Projects");
        Toast.makeText(this, currentUser, Toast.LENGTH_SHORT).show();
        //arrayList.add(new ListData("ProjectViewTextField","null"));


        signoutfab2.shrink();
        signoutfab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signoutfab2.isExtended()){
                    signoutfab2.shrink();
                    mauth.signOut();
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    signoutfab2.extend();
                }

            }
        });



        Users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    String  ProjName= dataSnapshot.child("Project_Name").getValue().toString();
                    String  ProjDesc= dataSnapshot.child("Project_Description").getValue().toString();


                    arrayList.add(new ListData(ProjName,ProjDesc));
                    adapter.notifyDataSetChanged();
                    loader.setVisibility(View.GONE);

                }catch (NullPointerException e){}

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String  ProjName= dataSnapshot.child("Project_Name").getValue().toString();
                    String  ProjDesc= dataSnapshot.child("Project_Description").getValue().toString();


                    arrayList.add(new ListData(ProjName,ProjDesc));
                    adapter.notifyDataSetChanged();
                    loader.setVisibility(View.GONE);

                }catch (NullPointerException e){}
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    String  ProjName= dataSnapshot.child("Project_Name").getValue().toString();
                    String  ProjDesc= dataSnapshot.child("Project_Description").getValue().toString();


                    arrayList.add(new ListData(ProjName,ProjDesc));
                    adapter.notifyDataSetChanged();
                    loader.setVisibility(View.GONE);

                }catch (NullPointerException e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if(dataSnapshot.exists())
                    {
                        isadmin =Integer.parseInt(dataSnapshot.child("admin").getValue().toString());
                        if(isadmin==1){
                            fab1.setEnabled(true);

                        }
                    }

                }catch (NullPointerException e) {}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
