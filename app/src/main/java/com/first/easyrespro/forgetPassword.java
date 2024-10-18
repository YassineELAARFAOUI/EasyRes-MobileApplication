package com.first.easyrespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPassword extends AppCompatActivity {
     Button sendCode;
    private EditText emailVerfication;
    FirebaseAuth auth;
    private ConstraintLayout gmailZone;
    private Button goToEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forget_password);

        sendCode = (Button) findViewById(R.id.sendcode);
        emailVerfication = findViewById(R.id.emailVerification);
        gmailZone = findViewById(R.id.gmailZone);
        goToEmail = findViewById(R.id.goToEmail);
        auth = FirebaseAuth.getInstance();
        goToEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(new ComponentName("com.google.android.gm",
                        "com.google.android.gm.ConversationListActivityGmail"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resttPassword();


            }
        });


    }



    private void resttPassword() {
        String email2 =emailVerfication.getText().toString().trim();
        if(email2.isEmpty()){
            emailVerfication.setError("email is requierd");
            emailVerfication.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email2).matches()){
            emailVerfication.setError("plese provide a valid email");
            emailVerfication.requestFocus();
            return;
        }
        System.out.println("------------------------------------------");
        System.out.println(email2);
        auth.sendPasswordResetEmail(email2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(forgetPassword.this, "check your email to reset password", Toast.LENGTH_SHORT).show();
                    gmailZone.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(forgetPassword.this, "try again something happend", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}