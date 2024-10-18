package com.first.easyrespro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class changeImageProfil extends AppCompatActivity {

    private Button takeMyImage,saveIt;
    private Uri imagePath;
    private ImageView returnbtn;
    private ShapeableImageView imageuser;
    DatabaseReference usersRef;
    FirebaseDatabase database;
    private String EmailOfUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_change_image_profil);
        String getEmail =getIntent().getStringExtra("EmailOfUser");
        EmailOfUser=getEmail;
        imageuser=findViewById(R.id.imageuser);
        takeMyImage=findViewById(R.id.takeMyImage);
        saveIt=findViewById(R.id.saveIt);
        returnbtn=findViewById(R.id.returnbtn);

        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(changeImageProfil.this,profil.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);
            }
        });

        saveIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upLoadImage(imagePath);
            }
        });

        takeMyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent =new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data !=null && resultCode==RESULT_OK){
            imagePath = data.getData();
            getImageImageView(imageuser,imagePath);
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
    private void upLoadImage(Uri imgPath) {
        FirebaseStorage.getInstance().getReference("image p/"+ UUID.randomUUID().toString()).putFile(imgPath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                updateData(task.getResult().toString());
                                System.out.println("---------------------1------------------");
                            }
                        }
                    });
                }else{
                    Toast.makeText(changeImageProfil.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void updateData(String url){
        usersRef.orderByChild("email").equalTo(EmailOfUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();

                        usersRef.child(key).child("imageProfile").setValue(url);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }




}