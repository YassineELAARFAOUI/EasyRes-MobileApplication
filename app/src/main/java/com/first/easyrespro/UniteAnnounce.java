package com.first.easyrespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UniteAnnounce extends AppCompatActivity {
    private String EmailOfUser,idAnnounce;
    Button call_now;
    Button contact_info;
    Dialog mDialog;
    ImageButton map;
    TextView see_on_maps;
    TextView descriptionShow,categoryShow,priceShow,addressShow,cityShow,nameOfferMakerShow,EmailOfferShow,numberShow;
    double latitude1 =  31.63103869313744;
    double longitude1 = -8.017315859971875;
    private String latitude,longitude,nameOfOfferMaker,EmailOfferMaker,EmailOfferMakerForgetData;
    private ImageView HotelIcon,SportIcon,HouseIcone,RestoIcon,ImagePrinc,Image1,Image2,Image3;
    FirebaseDatabase database;
    DatabaseReference productsRef,offerMakerData;
    DatabaseReference productRef,usersRef;
    StorageReference storageRefImage;
    ImageView star1,star2,star3,star4,star5,starGray1,starGray2,starGray3,starGray4,starGray5;
    ConstraintLayout popup;
    int pupupNum=0;
    private String phoneOffer;
    private ImageButton back_arrow;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_unite_announce);

        String getEmail =getIntent().getStringExtra("EmailOfUser");
        String numberAnnounce =getIntent().getStringExtra("idAnnounce");
        EmailOfUser=getEmail;
        idAnnounce=numberAnnounce;
        System.out.println("------------------------------------------");
        System.out.println(EmailOfUser);
        System.out.println(idAnnounce);

        back_arrow=findViewById(R.id.back_arrow);

        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("Announces");
        productRef = productsRef.child(idAnnounce);
        descriptionShow=findViewById(R.id.description);
        categoryShow=findViewById(R.id.category);
        priceShow=findViewById(R.id.price);
        addressShow=findViewById(R.id.address);
        cityShow=findViewById(R.id.city);
        RestoIcon=findViewById(R.id.RestoCategory);
        SportIcon=findViewById(R.id.SportCategory);
        HouseIcone=findViewById(R.id.HouseCategory);
        HotelIcon=findViewById(R.id.HotelCategory);

        ImagePrinc=findViewById(R.id.ImagePrinc);
        Image1=findViewById(R.id.Image1);
        Image2=findViewById(R.id.Image2);
        Image3=findViewById(R.id.Image3);

        star1=findViewById(R.id.star1);
        star2=findViewById(R.id.star2);
        star3=findViewById(R.id.star3);
        star4=findViewById(R.id.star4);
        star5=findViewById(R.id.star5);
        starGray1=findViewById(R.id.starGray1);
        starGray2=findViewById(R.id.starGray2);
        starGray3=findViewById(R.id.starGray3);
        starGray4=findViewById(R.id.starGray4);
        starGray5=findViewById(R.id.starGray5);
        popup=findViewById(R.id.popup);
        EmailOfferShow=findViewById(R.id.EmailShow);
        numberShow=findViewById(R.id.numberShow);
        nameOfferMakerShow=findViewById(R.id.nameOfferMakerShow);


        getAnnounce();





        call_now=(Button)findViewById(R.id.call_now);
        //contact button
        contact_info = findViewById(R.id.contact_info);
        mDialog = new Dialog(this);
        map = findViewById(R.id.map);
        see_on_maps=findViewById(R.id.see_on_maps);


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UniteAnnounce.this,feed.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);
            }
        });



        Glide.with(this).asGif().load(R.drawable.pinonmap).into(map);

        call_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneOffer));
                startActivity(intent);
            }
        });

        contact_info.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(pupupNum==0){
                    popup.setVisibility(View.VISIBLE);
                    pupupNum=1;
                }
            }
        });

        popup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(pupupNum==1){
                    popup.setVisibility(View.GONE);
                    pupupNum=0;
                }
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude1, longitude1);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        see_on_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });



    }// start function


    private void getAnnounce(){
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String descriptionAnnounce= dataSnapshot.child("description").getValue(String.class);
                String categoryAnonouce= dataSnapshot.child("Catigorie").getValue(String.class);
                String priceAnnounce= dataSnapshot.child("price").getValue(String.class);
                String addressAnnounce= dataSnapshot.child("address").getValue(String.class);
                String cityAnnounce= dataSnapshot.child("city").getValue(String.class);
                longitude=dataSnapshot.child("latitude").getValue(String.class);
                latitude=dataSnapshot.child("longitude").getValue(String.class);
                String imgP=dataSnapshot.child("img1").getValue(String.class);
                String img1=dataSnapshot.child("img2").getValue(String.class);
                String img2=dataSnapshot.child("img3").getValue(String.class);
                String img3=dataSnapshot.child("img4").getValue(String.class);
                String category=dataSnapshot.child("Catigorie").getValue(String.class);
                String stars= dataSnapshot.child("star").getValue(String.class);
                EmailOfferMakerForgetData= dataSnapshot.child("email").getValue(String.class);

                DataOfferMaker();

                DecimalFormat df = new DecimalFormat("0.00");
                double Price = Double.parseDouble(priceAnnounce);;
                String PriceFormat = df.format(Price);

                descriptionShow.setText(descriptionAnnounce);
                categoryShow.setText(categoryAnonouce);
                priceShow.setText(PriceFormat.toString()+" DH");
                addressShow.setText(addressAnnounce);
                cityShow.setText(cityAnnounce);





                if(Integer.parseInt(stars)==0){
                    starGray1.setVisibility(View.VISIBLE);
                    starGray2.setVisibility(View.VISIBLE);
                    starGray3.setVisibility(View.VISIBLE);
                    starGray4.setVisibility(View.VISIBLE);
                    starGray5.setVisibility(View.VISIBLE);
                }else if(Integer.parseInt(stars)==1){
                    star1.setVisibility(View.VISIBLE);
                    starGray2.setVisibility(View.VISIBLE);
                    starGray3.setVisibility(View.VISIBLE);
                    starGray4.setVisibility(View.VISIBLE);
                    starGray5.setVisibility(View.VISIBLE);
                }else if(stars.equals("2")){
                    star1.setVisibility(View.VISIBLE);
                    star2.setVisibility(View.VISIBLE);
                    starGray3.setVisibility(View.VISIBLE);
                    starGray4.setVisibility(View.VISIBLE);
                    starGray5.setVisibility(View.VISIBLE);
                }else if(stars.equals("3")){
                    star1.setVisibility(View.VISIBLE);
                    star2.setVisibility(View.VISIBLE);
                    star3.setVisibility(View.VISIBLE);
                    starGray4.setVisibility(View.VISIBLE);
                    starGray5.setVisibility(View.VISIBLE);
                }else if(stars.equals("4")){
                    star1.setVisibility(View.VISIBLE);
                    star2.setVisibility(View.VISIBLE);
                    star3.setVisibility(View.VISIBLE);
                    star4.setVisibility(View.VISIBLE);
                    starGray5.setVisibility(View.VISIBLE);
                }else if(stars.equals("5")){
                    star1.setVisibility(View.VISIBLE);
                    star2.setVisibility(View.VISIBLE);
                    star3.setVisibility(View.VISIBLE);
                    star4.setVisibility(View.VISIBLE);
                    star5.setVisibility(View.VISIBLE);
                }



                SetImageInImageView(ImagePrinc,imgP);
                if(img1.length()!=1){
                    SetImageInImageView(Image1,img1);
                    ViewGroup.LayoutParams params = Image1.getLayoutParams();
                    params.height = 500;
                    Image1.setLayoutParams(params);
                }
                if(img2.length()!=0){
                    SetImageInImageView(Image2,img2);
                    ViewGroup.LayoutParams params = Image2.getLayoutParams();
                    params.height = 500;
                    Image2.setLayoutParams(params);
                }
                if(img3.length()!=0){
                    SetImageInImageView(Image3,img3);
                    ViewGroup.LayoutParams params = Image3.getLayoutParams();
                    params.height = 500;
                    Image3.setLayoutParams(params);
                }

                System.out.println("-----------------------------"+category+"-----------------------------");
                if(category.equals("Sport")){
                    SportIcon.setVisibility(View.VISIBLE);
                }else if(category.equals("House")){
                    HouseIcone.setVisibility(View.VISIBLE);
                    System.out.println(category);
                }else if(category.equals("Hotel")){
                    HotelIcon.setVisibility(View.VISIBLE);
                }else if(category.equals("Resturant")){
                    RestoIcon.setVisibility(View.VISIBLE);
                }


                if(longitude.equals("null")&&latitude.equals("null")){
                    System.out.println("-------------------1------------------");
                    map.setVisibility(View.GONE);
                    see_on_maps.setVisibility(View.GONE);
                }else{
                    System.out.println("-------------------2------------------");
                    map.setVisibility(View.VISIBLE);
                    see_on_maps.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error: " + error);
            }
        });

    }
    private String extractImageInLink(String url) {
        Pattern pattern = Pattern.compile("o/(.+)\\?alt");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return (matcher.group(1)).replace("%20", " ").replace("%2F", "/");
        }
        return null;
    }

    private void SetImageInImageView(ImageView img,String url){
        //image start

        String linkImage = extractImageInLink(url);
        storageRefImage = FirebaseStorage.getInstance().getReference().child(linkImage);

        storageRefImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        //image end
    }


    private void DataOfferMaker(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").orderByChild("email").equalTo(EmailOfferMakerForgetData).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through users
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    String number = userSnapshot.child("phoneN").getValue(String.class);
                    phoneOffer="+212 "+number;
                    String firstName = userSnapshot.child("firstName").getValue(String.class);
                    String lastName = userSnapshot.child("lastName").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    System.out.println("-------------------------------------------------");
                    System.out.println(number);
                    System.out.println(firstName);
                    System.out.println(lastName);
                    EmailOfferShow.setText(EmailOfferMakerForgetData);
                    nameOfferMakerShow.setText(firstName+" "+lastName);
                    numberShow.setText("+212 "+number);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });

    }



}


