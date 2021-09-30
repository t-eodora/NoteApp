package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText dEmailLogin,dPasswordLogin;
    private RelativeLayout dLogin,dGoToSignUp;
    private  TextView dGoToForgotPassword;

    private FirebaseAuth firebaseAuth;

    ProgressBar dProgressBarActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        dEmailLogin=findViewById(R.id.emailLogin);
        dPasswordLogin=findViewById(R.id.passwordLogin);
        dLogin=findViewById(R.id.login);
        dGoToForgotPassword=findViewById(R.id.goToForgotPassword);
        dGoToSignUp=findViewById(R.id.goToSignup);
        dProgressBarActivity=findViewById(R.id.progressBarOfMainActivity);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this, NoteActivity.class));
        }



        dGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

        dGoToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });


        dLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=dEmailLogin.getText().toString().trim();
                String password=dPasswordLogin.getText().toString().trim();

                if(mail.isEmpty()|| password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();

                }
                else
                {

                    dProgressBarActivity.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if(task.isSuccessful())
                            {
                                checkEmailVerfication();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Account doesn't exist",Toast.LENGTH_SHORT).show();
                                dProgressBarActivity.setVisibility(View.INVISIBLE);
                            }


                        }
                    });




                }
            }
        });

    }


    private void checkEmailVerfication()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser.isEmailVerified()==true)
        {
            Toast.makeText(getApplicationContext(),"Logged In!",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, NoteActivity.class));
        }
        else
        {
            dProgressBarActivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Verify your e-mail!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }




}