package com.first.easyrespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class profil extends AppCompatActivity {
    private ImageButton returnbtn;
    private String EmailOfUser;
    private TextView firstAndLastName,email,telephone,entreprise,address,description;
    private ConstraintLayout logout,modifierBtn;

    private String firstNameData,emailData,telephoneData,entrepriseData,addressData,descriptionData;
    private int etetplace=0;


    //debut
    StorageReference storageRefImage ;
    ScrollView listAnnonceLoading;
    ListView listAnnonce;
    DatabaseReference ref;

    ArrayList<String> tab_imag =new ArrayList<>() ;

    ArrayList <String>  tab_star =new ArrayList<>();

    ArrayList <String>  tab_description =new ArrayList<>();

    ArrayList <String>  tab_prix =new ArrayList<>();

    ArrayList <String>  tab_city =new ArrayList<>();

    ArrayList <String>  tab_id =new ArrayList<>();

    DecimalFormat df = new DecimalFormat("0.00");

    private ScrollView profil1;
    private Button seeMyPost,goback;
    private ConstraintLayout profil2;
    private ShapeableImageView myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataTakedFromData();
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profil);
        String getEmail =getIntent().getStringExtra("EmailOfUser");
        EmailOfUser=getEmail;
        ref = FirebaseDatabase.getInstance().getReference("Announces");


        dataTakenByEmail(EmailOfUser);

        myImage=findViewById(R.id.myimage);
        seeMyPost=findViewById(R.id.seeMyPost);
        profil1=findViewById(R.id.profil1);
        goback=findViewById(R.id.goback);
        profil2=findViewById(R.id.profil2);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profil.this,changeImageProfil.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);
            }
        });

        seeMyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profil1.setVisibility(View.GONE);
                profil2.setVisibility(View.VISIBLE);

            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profil2.setVisibility(View.GONE);
                profil1.setVisibility(View.VISIBLE);

            }
        });


        firstAndLastName=findViewById(R.id.firstAndLastName);
        email=findViewById(R.id.email);
        telephone=findViewById(R.id.phone);
        entreprise=findViewById(R.id.entreprise);
        address=findViewById(R.id.address);
        description=findViewById(R.id.description);
        logout=findViewById(R.id.logout);
        modifierBtn=findViewById(R.id.modifierBtn);
        



        modifierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profil.this,modiferProfil.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);

            }
        });


        returnbtn=findViewById(R.id.returnbtn);
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profil.this,feed.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);
            }
        });

    }
    private void DataTakedFromData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataUser : snapshot.getChildren()) {
                    if((dataUser.child("email").getValue().toString()).equals(EmailOfUser)){
                        firstNameData=(dataUser.child("firstName").getValue().toString())+" "+(dataUser.child("lastName").getValue().toString());
                        emailData=dataUser.child("email").getValue().toString();
                        telephoneData="+212 "+dataUser.child("phoneN").getValue().toString();
                        entrepriseData=dataUser.child("entreprise").getValue().toString();
                        addressData=dataUser.child("address").getValue().toString();
                        descriptionData=dataUser.child("description").getValue().toString();
                        firstAndLastName.setText(firstNameData);
                        email.setText(emailData);
                        telephone.setText(telephoneData);
                        entreprise.setText(entrepriseData);
                        address.setText(addressData);
                        description.setText(descriptionData);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }//fin

    public class Ressources_cli extends BaseAdapter {

        public Ressources_cli() {
        }

        @Override
        public int getCount() {
            return tab_id.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v;
            v=getLayoutInflater().inflate(R.layout.annonces,null);
            TextView t1=(TextView) v.findViewById(R.id.descrip);
            TextView t2=(TextView) v.findViewById(R.id.price);
            TextView textCity=(TextView) v.findViewById(R.id.city);
            ImageView img=(ImageView) v.findViewById(R.id.img1);
            ImageView star1 = (ImageView) v.findViewById(R.id.star1);
            ImageView star2 = (ImageView) v.findViewById(R.id.star2);
            ImageView star3 = (ImageView) v.findViewById(R.id.star3);
            ImageView star4 = (ImageView) v.findViewById(R.id.star4);
            ImageView star5 = (ImageView) v.findViewById(R.id.star5);
            //starGray
            ImageView starGray1 = (ImageView) v.findViewById(R.id.starGray1);
            ImageView starGray2 = (ImageView) v.findViewById(R.id.starGray2);
            ImageView starGray3 = (ImageView) v.findViewById(R.id.starGray3);
            ImageView starGray4 = (ImageView) v.findViewById(R.id.starGray4);
            ImageView starGray5 = (ImageView) v.findViewById(R.id.starGray5);
            t1.setText(tab_description.get(i));

            double Price = Double.parseDouble(tab_prix.get(i));;
            String PriceFormat = df.format(Price);
            t2.setText(PriceFormat+" DH");
            //img.setImageResource(tab_imag.get(i));

            //image start

            String linkImage = extractImageInLink(tab_imag.get(i));
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


            textCity.setText(tab_city.get(i));
            System.out.println("-----------------------------------------");
            System.out.println("star :"+tab_star.get(i));
            if(tab_star.get(i).equals("0")){
                starGray1.setVisibility(View.VISIBLE);
                starGray2.setVisibility(View.VISIBLE);
                starGray3.setVisibility(View.VISIBLE);
                starGray4.setVisibility(View.VISIBLE);
                starGray5.setVisibility(View.VISIBLE);
            }else if(tab_star.get(i).equals("1")){
                star1.setVisibility(View.VISIBLE);
                starGray2.setVisibility(View.VISIBLE);
                starGray3.setVisibility(View.VISIBLE);
                starGray4.setVisibility(View.VISIBLE);
                starGray5.setVisibility(View.VISIBLE);
            }else if(tab_star.get(i).equals("2")){
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                starGray3.setVisibility(View.VISIBLE);
                starGray4.setVisibility(View.VISIBLE);
                starGray5.setVisibility(View.VISIBLE);
            }else if(tab_star.get(i).equals("3")){
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                starGray4.setVisibility(View.VISIBLE);
                starGray5.setVisibility(View.VISIBLE);
            }else if(tab_star.get(i).equals("4")){
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                starGray5.setVisibility(View.VISIBLE);
            }else if(tab_star.get(i).equals("5")){
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
            }

            return v;
        }
    }


    private String extractImageInLink(String url) {
        Pattern pattern = Pattern.compile("o/(.+)\\?alt");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return (matcher.group(1)).replace("%20", " ").replace("%2F", "/");
        }
        return null;
    }

    private void dataTakenByEmail(String email) {
        ref.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tab_id.removeAll(tab_id);
                tab_star.removeAll(tab_star);
                tab_imag.removeAll(tab_imag);
                tab_description.removeAll(tab_description);
                tab_city.removeAll(tab_city);
                tab_prix.removeAll(tab_prix);
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    tab_id.add(ds.child("pid").getValue(String.class));
                    tab_star.add(ds.child("star").getValue(String.class));
                    tab_imag.add(ds.child("img1").getValue(String.class));
                    tab_description.add(ds.child("description").getValue(String.class));
                    tab_city.add(ds.child("city").getValue(String.class));
                    tab_prix.add(ds.child("price").getValue(String.class));
                }
                System.out.println("-----------------------------"+tab_id.size()+"--------------------------");
                ListView l = (ListView) findViewById(R.id.listAnnonce);
                //attacher class base adabter
                l.setAdapter(new profil.Ressources_cli());
                l.setDividerHeight(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}