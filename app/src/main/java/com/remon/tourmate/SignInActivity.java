package com.remon.tourmate;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    private ImageView logoIV;
    private EditText emailET;
    private TextInputLayout passwordTIL;
    private Button signInBtn;
    private TextView signUpTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();
        clickListener();

    }

    private void initialize() {

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

    }

}
