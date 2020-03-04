package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "";
    EditText name, rollno, email, phone, pwd, cpwd;
    TextInputLayout pwdInputLAyout,cpwdInputLAyout;
    Button registerbt;
    LinearLayout regLinearLayout;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private static Pattern PASSWORD_PATTERN =
//            Pattern.compile("^" +
//                    "(?=.*[0-9])" +         //at least 1 digit
//                    "(?=.*[a-z])" +         //at least 1 lower case letter
//                    "(?=.*[A-Z])" +         //at least 1 upper case letter
//                    //"(?=.*[a-zA-Z])" +      //any letter
//                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                    "(?=\\S+$)" +           //no white spaces
//                    ".{8,}" +               //at least 7 characters
//                    "$");

            Pattern.compile("^" +
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        MaterialToolbar toolbar = findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);


        name = (EditText) findViewById(R.id.registerName);
        rollno = (EditText) findViewById(R.id.registerRollNo);
        email = (EditText) findViewById(R.id.registerMail);
        phone = (EditText) findViewById(R.id.registerMobile);
        pwd =  (EditText) findViewById(R.id.registerPwd);
        cpwd = (EditText) findViewById(R.id.registerCPwd);
        cpwdInputLAyout=(TextInputLayout) findViewById(R.id.cpwdInputLAyout);
        registerbt = (Button) findViewById(R.id.registerbt);
        pwdInputLAyout = (TextInputLayout) findViewById(R.id.pwdInputLAyout);
        regLinearLayout = (LinearLayout) findViewById(R.id.RegLinearLayout);


        databaseReference   = FirebaseDatabase.getInstance().getReference("Registeration_Details");
        firebaseAuth  = FirebaseAuth.getInstance();
        registerbt.setOnClickListener(this);

        pwd.addTextChangedListener(ptw);
        cpwd.addTextChangedListener(cptw);

    }

    private TextWatcher ptw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String Pwd = pwd.getText().toString().trim();
            if (TextUtils.isEmpty(Pwd)) {
                pwd.setError("Password is required");
                pwd.requestFocus();
                pwdInputLAyout.setPasswordVisibilityToggleEnabled(false);
                return;
            } else if (!PASSWORD_PATTERN.matcher(Pwd).matches()) {
                //pwdInputLAyout.setPasswordVisibilityToggleEnabled(false);
                pwd.setError("Must contain atleast 8 characters");
                pwdInputLAyout.setPasswordVisibilityToggleEnabled(false);
                pwd.requestFocus();
                return;
            } else
                pwdInputLAyout.setPasswordVisibilityToggleEnabled(true);
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher cptw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String Cpwd = cpwd.getText().toString().trim();
            String Pwd = pwd.getText().toString().trim();
            if(TextUtils.isEmpty(Cpwd)){
                cpwd.setError("Please Enter Confirm Password");
                cpwdInputLAyout.setPasswordVisibilityToggleEnabled(false);
                cpwd.requestFocus();
                return;
            }else if(!Cpwd.equals(Pwd)){
                cpwdInputLAyout.setPasswordVisibilityToggleEnabled(false);
                cpwd.setError("Password Mismatch");
                cpwd.requestFocus();
                return;
            }
            else
                cpwdInputLAyout.setPasswordVisibilityToggleEnabled(true);
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public void onClick(View v) {
        final String name = this.name.getText().toString();
        final String rollno = this.rollno.getText().toString();
        final String email = this.email.getText().toString().trim();
        final String phone = this.phone.getText().toString();
        String pwd = this.pwd.getText().toString().trim();
        String cpwd = this.cpwd.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            this.name.setError("Name is required");
            this.name.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(rollno)) {
            this.rollno.setError("Roll No is required");
            this.rollno.requestFocus();
            return;
        } else if (rollno.length() != 6) {
            this.rollno.setError("Valid 6 digit Roll No required");
            this.rollno.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            this.phone.setError("Valid Phone no is required");
            this.phone.requestFocus();
            return;
        } else if (phone.length() != 10) {
            this.phone.setError("Valid 10 digit Phone No required");
            this.phone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            this.pwd.setError("Password is required");
            this.pwd.requestFocus();
            pwdInputLAyout.setPasswordVisibilityToggleEnabled(true);
            return;
        } else if (!PASSWORD_PATTERN.matcher(pwd).matches()) {
            //pwdInputLAyout.setPasswordVisibilityToggleEnabled(false);
            this.pwd.setError("Must contain atleast 8 characters");
            this.pwd.requestFocus();
            return;
        } else
            pwdInputLAyout.setPasswordVisibilityToggleEnabled(true);

        if (TextUtils.isEmpty(cpwd)) {
            this.cpwd.setError("Please Confirm Password");
            //cpwdInputLAyout.setPasswordVisibilityToggleEnabled(true);
            this.cpwd.requestFocus();
            return;
        } else if (!cpwd.equals(pwd)) {
            cpwdInputLAyout.setPasswordVisibilityToggleEnabled(false);
            this.cpwd.setError("Password Mismatch");
            this.cpwd.requestFocus();
            return;
        } else
            cpwdInputLAyout.setPasswordVisibilityToggleEnabled(true);

//                if (TextUtils.isEmpty(batch)) {
//                    this.batch.setError("Batch is required");
//                    this.batch.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(branch)) {
//                    this.branch.setError("Branch is required");
//                    this.branch.requestFocus();
//                    return;
//                }

        this.name.onEditorAction(EditorInfo.IME_ACTION_DONE);
        this.name.clearFocus();
        this.rollno.clearFocus();
        this.rollno.onEditorAction(EditorInfo.IME_ACTION_DONE);
        this.phone.clearFocus();
        this.phone.onEditorAction(EditorInfo.IME_ACTION_DONE);
        this.pwd.clearFocus();
        this.pwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
        this.cpwd.onEditorAction(EditorInfo.IME_ACTION_DONE);
        this.cpwd.clearFocus();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registering");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Register information = new Register(
                                    name, rollno, email, phone
                            );

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                user.updateProfile(profileChangeRequest)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                    //Toast.makeText(RegisterationActivity.this,"Profile Updated ",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }

                            FirebaseDatabase.getInstance().getReference("Registeration_Details")
                                    .child(name)
                                    .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Registration Successfull! Login", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegistrationActivity.this, NavigationActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Oops! Registeration Failed " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Snackbar.make(regLinearLayout, "Trying Login", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.WHITE)
                                    .setAction("Click Here", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();
                        }

                        // ...
                    }
                });
    }
}
