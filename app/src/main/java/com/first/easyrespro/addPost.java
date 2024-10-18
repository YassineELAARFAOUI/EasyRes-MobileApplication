package com.first.easyrespro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class addPost extends AppCompatActivity {

    private ImageView imageP,image1,image2,image3,addAnnounce;
    private ImageButton addImage,add1,add2,add3,localisationBtn,returnbtn;
    private EditText description,price,city,address;
    private Uri imagePath,imagePath1,imagePath2,imagePath3;
    private int numImage=0;
    private String currentDate,currentTime,AnnounceRandomKey;
    private DatabaseReference AnnounceRef;
    private String idAnnounce;
    private String latitude="null",longitude="null";
    private static final int REQUEST_LOCATION=1;
    private Spinner Catigorie;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private ConstraintLayout continerLoading;
    private ImageView imgloading;
    private String EmailOfUser;
    private TextView userRight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_post);

        String getEmail =getIntent().getStringExtra("EmailOfUser");
        EmailOfUser=getEmail;
        System.out.println("------------------------------------------");
        System.out.println(getEmail);

        userRight=findViewById(R.id.userRight);

        imgloading=(ImageView) findViewById(R.id.imgloading);
        continerLoading=(ConstraintLayout) findViewById(R.id.page);

        AnnounceRef =FirebaseDatabase.getInstance().getReference().child("announces");
        Catigorie=findViewById(R.id.Catigorie);
        imageP=findViewById(R.id.imageP);
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        addAnnounce=findViewById(R.id.addAnnounce);

        addImage=findViewById(R.id.addImage);
        add1=findViewById(R.id.add1);
        add2=findViewById(R.id.add2);
        add3=findViewById(R.id.add3);
        localisationBtn=findViewById(R.id.localisationBtn);

        description=findViewById(R.id.description);
        price=findViewById(R.id.price);
        city=findViewById(R.id.city);
        address=findViewById(R.id.address);

        returnbtn=findViewById(R.id.returnbtn);

        userRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addpostPage = new Intent(addPost.this,userRight.class);
                startActivity(addpostPage);
            }
        });

        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addpostPage = new Intent(addPost.this,feed.class);
                addpostPage.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(addpostPage);
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numImage=1;
                Intent photoIntent =new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });

        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numImage=2;
                Intent photoIntent =new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numImage=3;
                Intent photoIntent =new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });

        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numImage=4;
                Intent photoIntent =new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });

        localisationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(addPost.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("----------------1-------------");
                    ActivityCompat.requestPermissions(addPost.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }else {
                    System.out.println("----------------2-------------");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                }
                Toast.makeText(addPost.this,"Your location is stored",Toast.LENGTH_SHORT).show();
            }
        });

        addAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((imagePath!=null)||(imagePath1!=null)||(imagePath2!=null)||(imagePath3!=null)){

                    sendAnnounce();
                    loading(continerLoading,imgloading);
                }

                if(!(imagePath==null)){
                    upLoadImage(imagePath,1);
                }
                if(!(imagePath1==null)){
                    upLoadImage(imagePath1,2);
                }
                if(!(imagePath2==null)){
                    upLoadImage(imagePath2,3);
                }
                if(!(imagePath3==null)){
                    upLoadImage(imagePath3,4);
                }





            }
        });


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude=Double.toString(location.getLatitude());
                longitude=Double.toString(location.getLongitude());
                System.out.println("Latitude: " +latitude);
                System.out.println("Longitude: " +longitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data !=null && resultCode==RESULT_OK){
            if(numImage==1){
                imagePath = data.getData();
                getImageImageView(imageP,imagePath);
            }else if(numImage==2){
                imagePath1 = data.getData();
                getImageImageView(image1,imagePath1);
            }else if(numImage==3){
                imagePath2 = data.getData();
                getImageImageView(image2,imagePath2);
            }else if(numImage==4){
                imagePath3 = data.getData();
                getImageImageView(image3,imagePath3);
            }
        }
    }

    private void getImageImageView(ImageView imgInDesing,Uri imgPath){
        Bitmap bitmap =null;
        try {
            bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imgPath);
        }catch (IOException e){
            e.printStackTrace();
        }
        imgInDesing.setImageBitmap(bitmap);
    }

    private void upLoadImage(Uri imgPath,int nbimg) {
        FirebaseStorage.getInstance().getReference("image p/"+ UUID.randomUUID().toString()).putFile(imgPath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                updateData(task.getResult().toString(),nbimg);
                            }
                        }
                    });
                }else{
                    Toast.makeText(addPost.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void sendAnnounce() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateP =(new SimpleDateFormat("dd MM yyy"));
        String dateofPost=dateP.format(calendar.getTime());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Announces");


        SimpleDateFormat currentD = new SimpleDateFormat("MMddyyy");
        currentDate = currentD.format(calendar.getTime());
        SimpleDateFormat currenT = new SimpleDateFormat("HHmmss");
        currentTime = currenT.format(calendar.getTime());
        AnnounceRandomKey=currentDate+currentTime;
        System.out.println("---------------------------------------------");
        System.out.println(AnnounceRandomKey);

        String description1 = description.getText().toString().toLowerCase();
        String price1 = price.getText().toString();
        String city1 = city.getText().toString();
        String address1 = address.getText().toString();
        String Catigorie1 = Catigorie.getSelectedItem().toString();



        Post announce = new Post(AnnounceRandomKey,description1,price1,city1,address1,"","","","",EmailOfUser,latitude,longitude,Catigorie1,"1",dateofPost);
        myRef.child(AnnounceRandomKey).setValue(announce);
    }

    private void updateData(String url,int nbImg){
        if(nbImg==1){
            FirebaseDatabase.getInstance().getReference("Announces/"+AnnounceRandomKey+"/img1").setValue(url);
        }else if(nbImg==2){
            FirebaseDatabase.getInstance().getReference("Announces/"+AnnounceRandomKey+"/img2").setValue(url);
        }else if(nbImg==3){
            FirebaseDatabase.getInstance().getReference("Announces/"+AnnounceRandomKey+"/img3").setValue(url);
        }else if(nbImg==4){
            FirebaseDatabase.getInstance().getReference("Announces/"+AnnounceRandomKey+"/img4").setValue(url);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private void loading(ConstraintLayout page,ImageView iconeReload){
        iconeReload.setVisibility(View.VISIBLE);
        page.setBackgroundColor(Color.parseColor("#72000000"));
        // Create a new RotateAnimation object
        RotateAnimation rotate = new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
                page.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                iconeReload.setVisibility(View.GONE);
                Toast.makeText(addPost.this,"Announce uploaded",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do nothing
            }
        });

        iconeReload.startAnimation(rotate);
    }


}