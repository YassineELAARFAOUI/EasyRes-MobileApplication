package com.first.easyrespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class login extends AppCompatActivity implements View.OnClickListener {
    private TextView goToRegister, ForgotPasswordBtn;
    private ImageButton returnbtn;
    private EditText password, email;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private ConstraintLayout continerLoading;
    private ImageView imgloading;
    private String emailPassInActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        imgloading = (ImageView) findViewById(R.id.imgloading);
        continerLoading = (ConstraintLayout) findViewById(R.id.page);

        mAuth = FirebaseAuth.getInstance();
        String returnPage = getIntent().getStringExtra("returnPage");
        System.out.println("------------------------------------------");
        System.out.println(returnPage);

        ForgotPasswordBtn = findViewById(R.id.ForgotPasswordBtn);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        returnbtn = findViewById(R.id.returnbtn);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);


        if (returnPage.equals("startPage")) {

            returnbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(login.this, startPage.class);
                    finish();
                    startActivity(intent);
                }
            });

        } else if (returnPage.equals("feed")) {

            returnbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(login.this, feed.class);
                    intent.putExtra("EmailOfUser", "null");
                    finish();
                    startActivity(intent);
                }
            });

        }


        ForgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, forgetPassword.class);
                startActivity(intent);
            }
        });

        goToRegister = findViewById(R.id.goToRegister);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });


    }//fin

    // This is an onClick method that gets called when a view is clicked
    @Override
    public void onClick(View view) {
// When the view is clicked, the userLogin() method is called
        userLogin();
    }

    // Method to handle user login
    private void userLogin() {
        // Get the email and password entered by the user
        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        emailPassInActivity = email1;

        // Print email and password to console for debugging
        System.out.println(email1);
        System.out.println(password1);

        // Check if email is empty
        if (email1.isEmpty()) {
            email.setError("email is requierd");
            email.requestFocus();
            return;
        }
        // Check if email is in valid format
        if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            email.setError("please provide valid email");
            email.requestFocus();
            return;
        }
        // Check if password is empty
        if (password1.isEmpty()) {
            password.setError("email is requierd");
            password.requestFocus();
            return;
        }
        // Check if password is less than 6 characters
        if (password1.length() < 6) {
            password.setError("min password lenght should be 6 characters");
            password.requestFocus();
            return;
        }

        // Sign in user with email and password
        mAuth.signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Check if sign in is successful
                        if (task.isSuccessful()) {
                            // Show loading animation
                            loading(continerLoading, imgloading, 1);
                        } else {
                            // Hide loading animation
                            loading(continerLoading, imgloading, 0);
                        }
                    }
                });
    }


    private void loading(ConstraintLayout page, ImageView iconeReload, int pass) {
        //set the iconeReload ImageView visibility to visible
        iconeReload.setVisibility(View.VISIBLE);
        //set the background color of the page ConstraintLayout to a semi-transparent black color
        page.setBackgroundColor(Color.parseColor("#72000000"));

        // Create a new RotateAnimation object
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //set the duration of the animation to 500 milliseconds
        rotate.setDuration(500);
        //set the repetition count of the animation to 8
        rotate.setRepeatCount(3);

        //set an AnimationListener to listen for the start, end, and repeat of the animation
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Check the value of the pass variable
                if (pass == 1) {
                    //start the feed activity and pass the emailPassInActivity variable as an extra
                    Intent intent = new Intent(login.this, feed.class);
                    intent.putExtra("EmailOfUser", emailPassInActivity);
                    // Change the background of the page ConstraintLayout to transparent
                    page.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    //set the iconeReload ImageView visibility to gone
                    iconeReload.setVisibility(View.GONE);
                    //finish the login activity
                    finish();
                    //start the feed activity
                    startActivity(intent);
                    //show a toast message indicating successful connection
                    Toast.makeText(login.this, "connexion successful", Toast.LENGTH_SHORT).show();
                } else if (pass == 0) {
                    // Change the background of the page ConstraintLayout to transparent
                    page.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    //set the iconeReload ImageView visibility to gone
                    iconeReload.setVisibility(View.GONE);
                    //show a toast message indicating failed connection
                    Toast.makeText(login.this, "connexion Failed", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do nothing
            }
        });

        //start the animation on iconeReload ImageView
        iconeReload.startAnimation(rotate);
    }
}