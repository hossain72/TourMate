package com.remon.tourmate;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText nameET, emailET;
    private CheckBox acceptsCB;
    private Button signUpBtn;
    private TextView signInTV;
    private TextInputLayout passwordTIL, confirmPasswordTIL;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        clickListener();

    }

    private void initialize() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        imageView = findViewById(R.id.imageTV);
        nameET = findViewById(R.id.userNameET);
        emailET = findViewById(R.id.emailET);
        passwordTIL = findViewById(R.id.passwordTIL);
        confirmPasswordTIL = findViewById(R.id.confirmPasswordTIL);
        acceptsCB = findViewById(R.id.acceptCB);
        signUpBtn = findViewById(R.id.signUpBtn);
        signInTV = findViewById(R.id.signInTV);

    }

    private void clickListener() {

        acceptsCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false) {
                    signUpBtn.setVisibility(View.GONE);
                } else if (isChecked == true) {
                    signUpBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordTIL.getEditText().getText().toString();
                String confirmPassword = confirmPasswordTIL.getEditText().getText().toString();

                if (name.equals("") || email.equals("")){
                    Toast.makeText(MainActivity.this, "Enter required field", Toast.LENGTH_LONG).show();
                }else if (password.contains(confirmPassword)){
                    createUserWithEmailAndPassword(email,password);
                    signIn(new User(name, email, password));
                }

            }
        });

    }

    private void createUserWithEmailAndPassword(String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void signIn(User user) {

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("tourMate").child("userId");

        String userId = databaseReference.push().getKey();
        user.setUserId(userId);

        databaseReference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
