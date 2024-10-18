package com.first.easyrespro;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class feed extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    ImageButton gotomenu ;
    ImageButton goFromFeedToMenu;
    ShapeableImageView loginUserBtn;
    private String EmailOfUser;
    ScrollView listAnnonceLoading;
    ListView listAnnonce;


    ArrayList<String> tab_imag =new ArrayList<>() ;

    ArrayList <String>  tab_star =new ArrayList<>();

    ArrayList <String>  tab_description =new ArrayList<>();

    ArrayList <String>  tab_prix =new ArrayList<>();

    ArrayList <String>  tab_city =new ArrayList<>();

    ArrayList <String>  tab_id =new ArrayList<>();

    DecimalFormat df = new DecimalFormat("0.00");

    DatabaseReference ref;
    StorageReference storageRefImage ;
    ShimmerFrameLayout shimmerFrameLayout;
    private ImageButton rechercheButton,hotelButton,restoButton,homeButton,sportButton;
    private EditText bareDeRecherche;




    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_feed);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();

        listAnnonceLoading = findViewById(R.id.listAnnonceLoading);
        listAnnonce = findViewById(R.id.listAnnonce);

        ref = FirebaseDatabase.getInstance().getReference("Announces");
        datataked();
        String getEmail =getIntent().getStringExtra("EmailOfUser");
        EmailOfUser=getEmail;
        System.out.println("------------------------------------------");
        System.out.println(getEmail);



        // start left menu

        hotelButton = findViewById(R.id.hotelButton);
        restoButton = findViewById(R.id.restoButton);
        homeButton = findViewById(R.id.homeButton);
        sportButton = findViewById(R.id.sportButton);

        hotelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(feed.this,filter.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);
            }
        });
        restoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(feed.this,filter.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(feed.this,filter.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);
            }
        });
        sportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(feed.this,filter.class);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        gotomenu = findViewById(R.id.gotomenu);
        rechercheButton = findViewById(R.id.rechercheButton);
        bareDeRecherche = findViewById(R.id.bareDeRecherche);

        rechercheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAnnonceLoading.setVisibility(View.VISIBLE);
                listAnnonce.setVisibility(View.GONE);
                datatakedBySearch();
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.close_menu);


        gotomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Log.i("MENU_DRAWER_TAG","home item is clicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profil:
                        Log.i("MENU_DRAWER_TAG","search item is clicked");
                        finish();
                        if(EmailOfUser.equals("null")){
                            Intent intent = new Intent(feed.this,login.class);
                            intent.putExtra("returnPage","feed");
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(feed.this,profil.class);
                            intent.putExtra("EmailOfUser",EmailOfUser);
                            startActivity(intent);
                        }
                        break;
                    case R.id.addpost:
                        finish();
                        if(EmailOfUser.equals("null")){
                            Intent intent = new Intent(feed.this,login.class);
                            intent.putExtra("returnPage","feed");
                            startActivity(intent);
                        }else{
                            Intent addpostPage = new Intent(feed.this,addPost.class);
                            addpostPage.putExtra("EmailOfUser",EmailOfUser);
                            startActivity(addpostPage);
                        }
                        break;
                    case R.id.filter:
                        finish();
                        Intent filterpage = new Intent(feed.this,filter.class);
                        filterpage.putExtra("EmailOfUser",EmailOfUser);
                        startActivity(filterpage);
                        break;
                    case R.id.reclamtion:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        String emailreclame = "reseasy0@gmail.com";
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("message/rfc822");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailreclame});
                        startActivity(Intent.createChooser(emailIntent, "Send email"));
                        break;
                    case R.id.contactus:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        String emailcontact = "reseasy0@gmail.com";
                        Intent emailIntent2 = new Intent(Intent.ACTION_SEND);
                        emailIntent2.setType("message/rfc822");
                        emailIntent2.putExtra(Intent.EXTRA_EMAIL, new String[]{emailcontact});
                        startActivity(Intent.createChooser(emailIntent2, "Send email"));
                        break;
                    case R.id.aboutus:
                        Intent aboutuspage = new Intent(feed.this,aboutus.class);
                        startActivity(aboutuspage);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.youtube:
                        String videoUrl = "https://youtube.com/@companyeasyres";
                        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                        youtubeIntent.setPackage("com.google.android.youtube");
                        startActivity(youtubeIntent);
                        break;
                    case R.id.logout:
                        Intent gologinpage = new Intent(feed.this,login.class);
                        gologinpage.putExtra("returnPage","startPage");
                        finish();
                        startActivity(gologinpage);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.exit:
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                }



                return true;
            }
        });




        // end left menu








        loginUserBtn=findViewById(R.id.loginUserBtn);

        if(!EmailOfUser.equals("null")){
            loginUserBtn.setImageResource(R.drawable.money);
        }


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }



            


        loginUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if(EmailOfUser.equals("null")){
                    Intent intent = new Intent(feed.this,login.class);
                    intent.putExtra("returnPage","feed");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(feed.this,profil.class);
                    intent.putExtra("EmailOfUser",EmailOfUser);
                    startActivity(intent);
                }


            }
        });

        ListView l = (ListView) findViewById(R.id.listAnnonce);
        //attacher class base adabter
        l.setAdapter(new Ressources_cli());
        l.setDividerHeight(0);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String idAnnounce=tab_id.get(position);
                Intent intent = new Intent(feed.this,UniteAnnounce.class);
                intent.putExtra("idAnnounce",idAnnounce);
                intent.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(intent);

            }
        });




    }// start function
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

    private void datataked(){
        ref.addValueEventListener(new ValueEventListener() {
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
                ListView l = (ListView) findViewById(R.id.listAnnonce);
                //attacher class base adabter
                l.setAdapter(new Ressources_cli());
                l.setDividerHeight(0);
                listAnnonceLoading.setVisibility(View.GONE);
                listAnnonce.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void datatakedBySearch(){
        String textSearch = bareDeRecherche.getText().toString().trim();

        // Reference to the Firebase Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // Create a query to filter the results by first name starting with "s"
        Query query = database.child("Announces").orderByChild("description").startAt(textSearch).endAt(textSearch+"\uf8ff");

        // Attach a listener to the query to get the filtered results
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tab_id.removeAll(tab_id);
                tab_star.removeAll(tab_star);
                tab_imag.removeAll(tab_imag);
                tab_description.removeAll(tab_description);
                tab_city.removeAll(tab_city);
                tab_prix.removeAll(tab_prix);
                // Iterate through the filtered results
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    tab_id.add(ds.child("pid").getValue(String.class));
                    tab_star.add(ds.child("star").getValue(String.class));
                    tab_imag.add(ds.child("img1").getValue(String.class));
                    tab_description.add(ds.child("description").getValue(String.class));
                    tab_city.add(ds.child("city").getValue(String.class));
                    tab_prix.add(ds.child("price").getValue(String.class));
                }
                ListView l = (ListView) findViewById(R.id.listAnnonce);
                //attacher class base adabter
                l.setAdapter(new Ressources_cli());
                l.setDividerHeight(0);
                listAnnonceLoading.setVisibility(View.GONE);
                listAnnonce.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });


        //fin



    }

    private String extractImageInLink(String url) {
        Pattern pattern = Pattern.compile("o/(.+)\\?alt");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return (matcher.group(1)).replace("%20", " ").replace("%2F", "/");
        }
        return null;
    }



}