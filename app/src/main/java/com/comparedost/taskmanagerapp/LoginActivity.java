package com.comparedost.taskmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

  TextInputLayout usernamelayout,passwordlayout ;
          TextInputEditText username,password ;
    Button login;
    TextView Register,EmailVerify,forgotpass;

    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username =findViewById(R.id.username);
        password=findViewById(R.id.password);
        usernamelayout=findViewById(R.id.usernamelayout);
        passwordlayout=findViewById(R.id.passwordlayout);
        login=findViewById(R.id.login);
        Register=findViewById(R.id.Register);
        EmailVerify=findViewById(R.id.EmailVerify);
        forgotpass=findViewById(R.id.forgotpass);

        mauth=FirebaseAuth.getInstance();

        onStart();
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email =username.getText().toString();
                if(!(email.isEmpty()))
                {
                    mauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Reset Link Send to " + email, Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }

                else {
                    usernamelayout.setError("Email Needed");
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usern = username.getText().toString();
                String pass = password.getText().toString();


                if( !( usern.isEmpty()) && !(pass.isEmpty())) {

                    Login(usern,pass);

                }
                else {
                    if(usern.isEmpty()) { usernamelayout.setError("Username Needed");
                    }

                    if(pass.isEmpty()) { passwordlayout.setError("Password Needed");
                    }

                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class)
                        ;
                startActivity(intent);

            }
        });




    }

    public void Login(String usern,String pass)
    {


        mauth.signInWithEmailAndPassword(usern,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    if(mauth.getCurrentUser().isEmailVerified())
                    {
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);

                        startActivity(intent);
                    }
                    else {

                        Toast.makeText(LoginActivity.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                        EmailVerify.setVisibility(View.VISIBLE);
                        EmailVerify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(LoginActivity.this, "Email Send", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this, "Faile To Send Email", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }

                }

                else{
                    Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    public void onStart(){
        super.onStart();

        try {

            FirebaseUser user=mauth.getCurrentUser();

            if(user != null){

                if(user.isEmailVerified())
                {
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }


            }

        }catch (NullPointerException e)
        {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }




    }
}
