package com.first.easyrespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private EditText firstName,lastName,phone,email,password,confirmPassword,city,address,entreprise,description;

    private Button registerbtn;
    private ImageButton returnBtn;
    private ArrayList<String> emailData = new ArrayList<>();
    private int valideinpute;

    private ConstraintLayout continerLoading;
    private ImageView imgloading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        imgloading=(ImageView) findViewById(R.id.imgloading);
        continerLoading=(ConstraintLayout) findViewById(R.id.page);

        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        phone = (EditText)findViewById(R.id.phoneNumber);
        email= (EditText) findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        confirmPassword= (EditText) findViewById(R.id.confirmpassword);
        city= (EditText) findViewById(R.id.city);
        address= (EditText) findViewById(R.id.address);
        entreprise= (EditText) findViewById(R.id.entreprise);
        description= (EditText) findViewById(R.id.description);
        registerbtn = (Button) findViewById(R.id.RegisterBtn);
        registerbtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();


        returnBtn=findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,login.class);
                intent.putExtra("returnPage","startPage");
                finish();
                startActivity(intent);
            }
        });


        EmailTakedFromData();
    }//fin

    @Override
    public void onClick(View view) {
        CheckEmpty();
        int existDeja=0;
        if(valideinpute==1){


            for (String d:emailData){
                System.out.println(d);
                System.out.println(email.getText().toString().trim());
                System.out.println("--------------------------");
                if((email.getText().toString().trim()).equals(d)){
                    existDeja=1;
                }
            }
            if(existDeja==0){
                System.out.println("New email");
                try {
                    //save data
                    SaveDataUser();
                    //change page
                }catch (Exception e){

                }
            }else{
                System.out.println("Email already exist");
                email.setError("Email already exist");
                email.requestFocus();
                return;
            }

        }





    }
    private void SaveDataUser(){
        String firstName1 =firstName.getText().toString().trim();
        String lastName1 =lastName.getText().toString().trim();
        String email1 =email.getText().toString().trim();
        String password1 =password.getText().toString().trim();
        String phone1 =phone.getText().toString().trim();
        String city1 =city.getText().toString().trim();
        String address1 =address.getText().toString().trim();
        String entreprise1 =entreprise.getText().toString().trim();
        String description1 =description.getText().toString().trim();
        String imageProfile1="";


        mAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user = new User(firstName1,lastName1,phone1,email1,password1,city1,address1,entreprise1,description1,imageProfile1);

                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        loading(continerLoading,imgloading,1);
                                    }
                                    else {
                                        loading(continerLoading,imgloading,0);
                                    }
                                }
                            });


                }
                else {
                    loading(continerLoading,imgloading,0);
                }
            }
        });








    }
    private void EmailTakedFromData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                emailData.removeAll(emailData);
                for (DataSnapshot dataUser : snapshot.getChildren()) {
                    System.out.println("------------------------------------------------");
                    emailData.add(dataUser.child("email").getValue().toString());
                }
                System.out.println("----------------------------------------------");
                System.out.println(emailData.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void CheckEmpty(){
        valideinpute=0;
        String email1 =email.getText().toString().trim();

        if ((firstName.getText().toString().trim()).isEmpty()){
            firstName.setError("This field is Empty");
            firstName.requestFocus();
            return;
        }
        if ((lastName.getText().toString().trim()).isEmpty()){
            lastName.setError("This field is Empty");
            lastName.requestFocus();
            return;
        }
        if ((phone.getText().toString().trim()).isEmpty()){
            phone.setError("This field is Empty");
            phone.requestFocus();
            return;
        }
        if (!((phone.getText().toString().trim()).length()==9)){
            phone.setError("phone number is inccorect (9 number)");
            phone.requestFocus();
            return;
        }
        if ((email.getText().toString().trim()).isEmpty()){
            email.setError("This field is Empty");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("please provide valid email");
            email.requestFocus();
            return;
        }
        if ((password.getText().toString().trim()).isEmpty()){
            password.setError("This field is Empty");
            password.requestFocus();
            return;
        }
        if ((password.getText().toString().trim()).length()<7){
            password.setError("You mast at lest 7 caracter");
            password.requestFocus();
            return;
        }
        if ((confirmPassword.getText().toString().trim()).isEmpty()){
            confirmPassword.setError("This field is Empty");
            confirmPassword.requestFocus();
            return;
        }
        if(!(password.getText().toString().trim()).equals(confirmPassword.getText().toString().trim())){
            confirmPassword.setError("Password is not the same");
            confirmPassword.requestFocus();
            return;
        }
        if ((city.getText().toString().trim()).isEmpty()){
            city.setError("This field is Empty");
            city.requestFocus();
            return;
        }
        if ((address.getText().toString().trim()).isEmpty()){
            address.setError("This field is Empty");
            address.requestFocus();
            return;
        }
        if ((entreprise.getText().toString().trim()).isEmpty()){
            entreprise.setError("This field is Empty");
            entreprise.requestFocus();
            return;
        }
        if ((description.getText().toString().trim()).isEmpty()){
            description.setError("This field is Empty");
            description.requestFocus();
            return;
        }
        valideinpute=1;

    }

    private void loading(ConstraintLayout page, ImageView iconeReload , int pass){
        iconeReload.setVisibility(View.VISIBLE);
        page.setBackgroundColor(Color.parseColor("#72000000"));
        // Create a new RotateAnimation object
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setRepeatCount(8);

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change the background of the button
                if(pass==1){
                    Intent intent = new Intent(MainActivity.this,login.class);
                    intent.putExtra("returnPage","startPage");
                    page.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    iconeReload.setVisibility(View.GONE);
                    finish();
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"User has been registred successfully",Toast.LENGTH_SHORT).show();
                }else if(pass==0){
                    page.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    iconeReload.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Failed register try again",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do nothing
            }
        });

        iconeReload.startAnimation(rotate);
    }




}