package com.example.wilfashionpage.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wilfashionpage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password;
    private FirebaseAuth auth;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        //getSupportActionBar().hide();



    }

    public void signup(View view) {

        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Enter your name!", Toast.LENGTH_SHORT).show();

        }

        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Enter your email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Enter your password!", Toast.LENGTH_SHORT).show();
            return;

        }
        if(userPassword.length() < 6){
            Toast.makeText(this, "Password must be minimum 6 characters long!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "Registration failed"+task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

    public void signin(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}