package com.example.project;


 import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextUtils;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.widget.Toolbar;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.common.SignInButton;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.android.material.appbar.MaterialToolbar;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.android.material.textfield.TextInputLayout;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button login;
    TextInputLayout pwdLayout;
    TextView register,ForgotPassword;
    EditText Email, pwd;
    ProgressBar progressBar;
    private LinearLayout loginLayout;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        MaterialToolbar toolbar = findViewById(R.id.LoginToolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("LOGIN");
//        toolbar.setTitleTextColor(Color.WHITE);

        login = (Button) findViewById(R.id.loginbt);
        register = (TextView) findViewById(R.id.loginRegisterbt);
        Email = (EditText) findViewById(R.id.loginEmail);
        pwd = (EditText) findViewById(R.id.loginPwd);
        pwdLayout = (TextInputLayout) findViewById(R.id.loginPwdLayout);
        //loginLayout = (LinearLayout) findViewById(R.id.loginLayout);
        progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
        ForgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        progressBar.setIndeterminate(true);

        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        ForgotPassword.setOnClickListener(this);
        pwd.addTextChangedListener(ptw);

    }


    TextWatcher ptw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String password = pwd.getText().toString();
            if(TextUtils.isEmpty(password)){
                //Snackbar.make(loginLayout,"Password required",Snackbar.LENGTH_SHORT).show();
                pwdLayout.setPasswordVisibilityToggleEnabled(false);
                pwd.setError("Password required");
                return;
            }
            else
                pwdLayout.setPasswordVisibilityToggleEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.loginbt:
                final String email = Email.getText().toString();
                final String password = pwd.getText().toString();
                if(TextUtils.isEmpty(email)){
                    //Snackbar.make(loginLayout,"Email required",Snackbar.LENGTH_LONG).show();
                    Email.setError("Email required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    // Snackbar.make(loginLayout,"Password required",Snackbar.LENGTH_LONG).show();
                    pwdLayout.setPasswordVisibilityToggleEnabled(false);
                    pwd.setError("Password required");
                    return;
                }
                login.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                pwd.setEnabled(false);
                Email.setEnabled(false);
                Email.clearFocus();
                register.setEnabled(false);
                ForgotPassword.setEnabled(false);
                pwd.clearFocus();

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if(email.equals("Pranav.juit@gmail.com")){
                                       Intent intentl = new Intent(LoginActivity.this, AdminActivity.class);
                                        Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();
                                        startActivity(intentl);
                                        finish();
                                    }
                                    else {
                                         Intent intent2 = new Intent(LoginActivity.this, NavigationActivity.class);
                                         startActivity(intent2);
                                         finish();
                                        Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();
                                        // Sign in success, update UI with the signed-in user's information
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w( "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    login.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    pwd.getText().clear();
                                    Email.setEnabled(true);
                                    pwd.setEnabled(true);
                                    ForgotPassword.setEnabled(true);
                                    register.setEnabled(true);
                                }
                            }
                        });
                break;
            case R.id.loginRegisterbt:
                Intent intent = new Intent(this,RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.ForgotPassword:
                Intent intpwd = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intpwd);
                break;
        }
    }
}

