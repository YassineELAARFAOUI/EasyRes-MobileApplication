package com.first.easyrespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class modiferProfil extends AppCompatActivity {
    private String EmailOfUser;
    DatabaseReference usersRef;
    FirebaseDatabase database;
    private EditText firstName,lastName,description,phone,emailUser,entreprise,address,city;
    private ImageButton returnbtn,saveBtn,delataBtn;
    private ConstraintLayout continerLoading;
    private ImageView imgloading;
    String firstNameInput,lastNameInput,descriptionInput,phoneInput,entrepriseInput,addresInput,cityInput,emailInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_modifer_profil);

        String getEmail =getIntent().getStringExtra("EmailOfUser");
        EmailOfUser=getEmail;
        System.out.println("-----------------------");
        System.out.println(EmailOfUser);

        imgloading=(ImageView) findViewById(R.id.imgloading);
        continerLoading=(ConstraintLayout) findViewById(R.id.page);

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");


        firstName=findViewById(R.id.firstName);
        lastName=findViewById(R.id.lastName);
        description=findViewById(R.id.description);
        phone=findViewById(R.id.phone);

        entreprise=findViewById(R.id.entreprise);
        address=findViewById(R.id.address);
        city=findViewById(R.id.city);

        saveBtn=findViewById(R.id.saveBtn);
        delataBtn=findViewById(R.id.delataBtn);


        returnbtn=findViewById(R.id.returnbtn);
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(modiferProfil.this,profil.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName1 =firstName.getText().toString().trim();
                String lastName1 =lastName.getText().toString().trim();
                String description1 =description.getText().toString().trim();
                String phone1 =phone.getText().toString().trim();
                String entreprise1 =entreprise.getText().toString().trim();
                String address1 =address.getText().toString().trim();
                String city1 =city.getText().toString().trim();

                System.out.println("----------------------------------------");

                updatedata(firstName1,lastName1,description1,phone1,entreprise1,address1,city1);

            }
        });

        delataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(modiferProfil.this);
                builder.setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete Your account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteUser();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();




            }
        });





        getNameByEmail(EmailOfUser);

    }

    private void deleteUser(){
        FirebaseUser Delateuser = FirebaseAuth.getInstance().getCurrentUser();
        Delateuser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference usersRef = firebaseDatabase.getReference("users");
                    usersRef.orderByChild("email").equalTo(Delateuser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                childSnapshot.getRef().removeValue();
                            }
                            System.out.println("User deleted successfully.");
                            loading(continerLoading,imgloading,2);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("Error deleting user data: " + databaseError.getMessage());
                        }
                    });
                }
            }
        });

    }

    private void getDataOfUser(){

        Query query = usersRef.orderByChild("email").equalTo(EmailOfUser);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String userId = childSnapshot.getKey();
                    DatabaseReference userRef = database.getReference("users/" + userId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            String firstName = snapshot.child("firstName").getValue(String.class);
                            int lastName = snapshot.child("last").getValue(Integer.class);
                            System.out.println("---------------------------------------------------");
                            System.out.println("firstName: " + firstName + ", lastName: " + lastName);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.out.println("The read failed: " + error.getCode());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }

    public void getNameByEmail(String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        Query query = usersRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String userId = childSnapshot.getKey();
                    if(userId != null) {
                        DatabaseReference userRef = database.getReference("users/" + userId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                firstNameInput = snapshot.child("firstName").getValue(String.class);
                                lastNameInput = snapshot.child("lastName").getValue(String.class);
                                descriptionInput = snapshot.child("description").getValue(String.class);
                                phoneInput = snapshot.child("phoneN").getValue(String.class);
                                entrepriseInput = snapshot.child("entreprise").getValue(String.class);
                                addresInput = snapshot.child("address").getValue(String.class);
                                cityInput = snapshot.child("city").getValue(String.class);
                                emailInput = EmailOfUser;

                                firstName.setText(firstNameInput);
                                lastName.setText(lastNameInput);
                                description.setText(descriptionInput);
                                phone.setText(phoneInput);
                                entreprise.setText(entrepriseInput);
                                address.setText(addresInput);
                                city.setText(cityInput);




                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                System.out.println("The read failed: " + error.getCode());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }

    private void loading(ConstraintLayout page, ImageView iconeReload,int type){
        iconeReload.setVisibility(View.VISIBLE);
        page.setBackgroundColor(Color.parseColor("#72000000"));
        // Create a new RotateAnimation object
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setRepeatCount(8);

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change the background of the button
                if(type==1){
                    Intent intent = new Intent(modiferProfil.this,profil.class);
                    intent.putExtra("EmailOfUser",EmailOfUser);
                    page.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    iconeReload.setVisibility(View.GONE);
                    finish();
                    startActivity(intent);
                    Toast.makeText(modiferProfil.this,"Your information saved",Toast.LENGTH_SHORT).show();
                }else if(type==2){
                    Intent intent = new Intent(modiferProfil.this,startPage.class);
                    page.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    iconeReload.setVisibility(View.GONE);
                    finish();
                    startActivity(intent);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do nothing
            }
        });

        iconeReload.startAnimation(rotate);
    }

    public void updatedata(String firstNameChanged, String lastNameChanged,String descriptionChanged,String phoneChange,String entrepriseChange,String addressChange,String cityChange) {
        usersRef.orderByChild("email").equalTo(EmailOfUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();

                        usersRef.child(key).child("firstName").setValue(firstNameChanged);
                        usersRef.child(key).child("lastName").setValue(lastNameChanged);
                        usersRef.child(key).child("description").setValue(descriptionChanged);
                        usersRef.child(key).child("phoneN").setValue(phoneChange);
                        usersRef.child(key).child("entreprise").setValue(entrepriseChange);
                        usersRef.child(key).child("address").setValue(addressChange);
                        usersRef.child(key).child("city").setValue(cityChange);

                        loading(continerLoading,imgloading,1);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

