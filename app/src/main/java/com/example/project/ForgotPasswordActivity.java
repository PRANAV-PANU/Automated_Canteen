package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.LoginActivity;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText resetemail;
    Button resetBt;
    FirebaseAuth firebaseAuth;
    TextView ForgotPasswordlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

//        MaterialToolbar toolbar = findViewById(R.id.ForgotPasswordToolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Reset ");
//        toolbar.setTitleTextColor(Color.WHITE);

        resetemail = (EditText)findViewById(R.id.ForgotPwdResetemail);
        resetBt = (Button) findViewById(R.id.ForgotPwdResetButton);
        ForgotPasswordlogin = (TextView)findViewById(R.id.ForgotPasswordlogin);

        firebaseAuth = FirebaseAuth.getInstance();
        resetBt.setOnClickListener(this);
        ForgotPasswordlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ForgotPasswordlogin:
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ForgotPwdResetButton:
                String Email = resetemail.getText().toString();
                if(TextUtils.isEmpty(Email)){
                    resetemail.setError("Email Required");
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(resetemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, " Password sent to your email ", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
        }

    }
}
