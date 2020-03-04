package com.example.project;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=1000;

    FirebaseUser firebaseUser;
    DatabaseReference ref;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        //this will bind your MainActivity.class file with activity_main.
        Thread t = new Thread() {
            @Override
            public void run() {
                // job of thread
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //explicit Intent
//                    Intent intent  = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
                    if (firebaseUser != null) {
                        if (firebaseUser.getEmail().equals("Pranav.juit@gmail.com")) {
                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //startActivity(new Intent(Home.this,StudentDashboard.class));
                            Intent intent = new Intent(MainActivity.this,NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else if(firebaseUser == null)
                    {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    //the current activity will get finished.
                }
            }
        };
        t.start();
    }
}