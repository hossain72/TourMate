package com.remon.tourmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private ImageView logoIV;
    private EditText emailET;
    private TextInputLayout passwordTIL;
    private Button signInBtn;
    private TextView signUpTV;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();
        clickListener();

    }

    private void initialize() {

        firebaseAuth = FirebaseAuth.getInstance();

        logoIV = findViewById(R.id.logoIV);
        emailET = findViewById(R.id.emailET);
        passwordTIL = findViewById(R.id.passwordTIL);
        signInBtn = findViewById(R.id.signInBtn);
        signUpTV = findViewById(R.id.signUpTV);

    }

    private void clickListener() {

        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailET.getText().toString();
                String password = passwordTIL.getEditText().getText().toString();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(SignInActivity.this, "Please enter required field", Toast.LENGTH_SHORT).show();
                } else {
                    loginWithEmailAndPassword(email, password);
                }

            }
        });


    }

    private void loginWithEmailAndPassword(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, NavigationActivity.class));
                } else {
                    Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

}
