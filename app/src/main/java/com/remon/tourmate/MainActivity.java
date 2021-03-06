package com.remon.tourmate;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.remon.tourmate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ActivityMainBinding activityMainBinding;
    private String userId;
    private User dataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        initialize();
        clickListener();

    }

    private void initialize() {

        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    private void clickListener() {

        activityMainBinding.acceptCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false) {
                    activityMainBinding.signUpBtn.setVisibility(View.GONE);
                } else if (isChecked == true) {
                    activityMainBinding.signUpBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        activityMainBinding.signInTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        });

        activityMainBinding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = activityMainBinding.userNameET.getText().toString();
                String email = activityMainBinding.emailET.getText().toString();
                String password = activityMainBinding.passwordTIL.getEditText().getText().toString();
                String confirmPassword = activityMainBinding.confirmPasswordTIL.getEditText().getText().toString();

                if (name.equals("") || email.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter required field", Toast.LENGTH_LONG).show();
                } else if (password.contains(confirmPassword)) {
                    createUser(email, password);
                    dataUser = new User(name, email, password);
                }

            }
        });

        activityMainBinding.imageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });

    }

    private void createUser(String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userId = firebaseAuth.getCurrentUser().getUid();
                    Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    signIn(dataUser);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                activityMainBinding.signInTV.setText(e.getMessage());
            }
        });

    }

    private void signIn(User user) {

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("tourMate").child("userId");
        dataUser.setUserId(userId);

        databaseReference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && requestCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            activityMainBinding.imageTV.setImageBitmap(bitmap);
        }
    }
}
