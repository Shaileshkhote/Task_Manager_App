package com.comparedost.taskmanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


   TextInputLayout usernameRegisterLayout,passwordRegisterLayout,NameRegisterLayout;
    TextInputEditText usernameRegister,passwordRegister,NameRegister;
    Button Register;
    private String username,password,Name,uid;
    ProgressBar loadingregister;

   private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameRegister =findViewById(R.id.usernameRegister);
        passwordRegister=findViewById(R.id.passwordRegister);
        Register=findViewById(R.id.Registerbtn);
        NameRegister=findViewById(R.id.Name);
        loadingregister=findViewById(R.id.loadingregister);
        usernameRegisterLayout=findViewById(R.id.usernameRegisterLayout);
        passwordRegisterLayout=findViewById(R.id.passwordRegisterLayout);
        NameRegisterLayout=findViewById(R.id.NameLayout);

        mauth=FirebaseAuth.getInstance();
        onStart();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Register();

            }
        });



    }
    public void onStart(){
        super.onStart();

        FirebaseUser user=mauth.getCurrentUser();

        if(user != null){

            if(user.isEmailVerified())
            {
                Intent intent=new Intent(RegisterActivity.this,AddProject.class);
                startActivity(intent);
            }


        }

    }

    public void Register (){

        loadingregister.setVisibility(View.VISIBLE);

        Name=NameRegister.getText().toString().trim();
        username=usernameRegister.getText().toString();
        password=passwordRegister.getText().toString();

        if(Name.isEmpty()){
           NameRegisterLayout.setError("Name Needed");

        }
        if(username.isEmpty()){
            usernameRegisterLayout.setError("Email Needed");
        }
        if(password.isEmpty()){
            passwordRegisterLayout.setError("Password Needed");
        }


        if(((!(Name.isEmpty())) && (!(username.isEmpty()))   && (!(password.isEmpty()))))
        {
            mauth.createUserWithEmailAndPassword(username,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())



                            {


                                FirebaseDatabase mydb = FirebaseDatabase.getInstance();
                                final DatabaseReference myref = mydb.getReference("Users");

                                uid= mauth.getCurrentUser().getUid().toString();
                                FirebaseUser user=mauth.getCurrentUser();

                                UserProfileChangeRequest updateprofile =new UserProfileChangeRequest.Builder().setDisplayName(Name).build();

                                mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Email Verification Send", Toast.LENGTH_SHORT).show();
                                    }
                                    }
                                });

                                user.updateProfile(updateprofile);




                                myref.child(Name).child("Name").setValue(Name).addOnCompleteListener( new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            myref.child(Name).child("admin").setValue("0");

                                            loadingregister.setVisibility(View.GONE);

                                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);

                                        startActivity(intent);
                                        }
                                    }
                                });


                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }



    }

}
