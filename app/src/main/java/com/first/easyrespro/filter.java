package com.first.easyrespro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class filter extends AppCompatActivity {
    ImageButton goToFeed;
    ImageButton dropFilter;
    ConstraintLayout cadre;
    Button btnFilter;
    TextView Max;
    TextView Min;
    TextView City;
    Spinner catigoriefilter;
    Spinner catigorieEtoile;
    ImageButton compteBtn;
    private String EmailOfUser;


    ArrayList<Integer> dataIndex =new ArrayList <>();

    int etatDeCadre=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_filter);

        String getEmail =getIntent().getStringExtra("EmailOfUser");
        EmailOfUser=getEmail;
        System.out.println("------------------------------------------");
        System.out.println(getEmail);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }


        ArrayList <Integer>  tab_imag =new ArrayList<>() ;
        tab_imag.add(R.drawable.sp);
        tab_imag.add(R.drawable.marrakech);
        tab_imag.add(R.drawable.mm);

        ArrayList <Integer>  tab_star =new ArrayList<>();
        tab_star.add(5);
        tab_star.add(3);
        tab_star.add(2);

        ArrayList <String>  tab_nom =new ArrayList<>();
        tab_nom.add("Hotel Marrakech 5 etoile a cote de ...");
        tab_nom.add("Hotel Rabat 5 etoile a cote de ...");
        tab_nom.add("Hotel Fes 5 etoile a cote de ...");

        ArrayList <Double>  tab_prix =new ArrayList<>();
        tab_prix.add(150.00);
        tab_prix.add(250.00);
        tab_prix.add(170.00);

        ArrayList <String>  tab_city =new ArrayList<>();
        tab_city.add("Marrakech");
        tab_city.add("Rabat");
        tab_city.add("Casablanca");

        ArrayList <String>  tab_catigorie =new ArrayList<>();
        tab_catigorie.add("SALLE DE SPORT");
        tab_catigorie.add("hotel");
        tab_catigorie.add("MAISON");




        ListView l = (ListView) findViewById(R.id.listOfAnnonce);
        //attacher class base adabter
        l.setAdapter(new Annoncement(tab_imag,tab_star,tab_nom,tab_prix,tab_city));

        goToFeed=findViewById(R.id.filterToFeed);
        compteBtn=(ImageButton) findViewById(R.id.compteBtn);
        dropFilter=findViewById(R.id.dropbutton);
        cadre=findViewById(R.id.cadrefilter);
        btnFilter=findViewById(R.id.btnFilter);
        Max=findViewById(R.id.Max);
        Min=findViewById(R.id.Min);
        City=findViewById(R.id.City);
        catigoriefilter=(Spinner) findViewById(R.id.spinnerCatigorieInput);
        catigorieEtoile=(Spinner) findViewById(R.id.spinnerEtoile);
        //combo box code start
        ArrayList<String> valueFilterCatigorie = new  ArrayList<>();
        valueFilterCatigorie.add("Chose one");
        valueFilterCatigorie.add("HOTEL");
        valueFilterCatigorie.add("RESTAURANTE");
        valueFilterCatigorie.add("MAISON");
        valueFilterCatigorie.add("SALLE DE SPORT");
        RemplirCombobox(valueFilterCatigorie,catigoriefilter);
        //combo box code end
        ArrayList<String> valueEtoile = new  ArrayList<>();
        valueEtoile.add("Chose one");
        valueEtoile.add("5 Stars");
        valueEtoile.add("4 Stars");
        valueEtoile.add("3 Stars");
        valueEtoile.add("2 Stars");
        valueEtoile.add("1 Star");
        RemplirCombobox(valueEtoile,catigorieEtoile);

        l.setDividerHeight(0);

        compteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(filter.this,login.class);
                startActivity(intent);
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList <Integer>  stockImageFilter =new ArrayList<>();
                ArrayList <Integer>  stockStarFilter =new ArrayList<>();
                ArrayList <String>  stockNomFilter =new ArrayList<>();
                ArrayList <Double>  stockPrixFilter =new ArrayList<>();
                ArrayList <String>  stockCityFilter =new ArrayList<>();

                // delate data if you repat searching
                dataIndex.removeAll(dataIndex);
                stockImageFilter.removeAll(stockImageFilter);
                stockStarFilter.removeAll(stockStarFilter);
                stockNomFilter.removeAll(stockNomFilter);
                stockPrixFilter.removeAll(stockPrixFilter);
                stockCityFilter.removeAll(stockCityFilter);

                ArrayList <Integer>  catigorieIndexUsed =new ArrayList<>();
                ArrayList <Integer>  starIndexUsed =new ArrayList<>();
                ArrayList <Integer>  prixMaxIndexUsed =new ArrayList<>();
                ArrayList <Integer>  prixMinIndexUsed =new ArrayList<>();
                ArrayList <Integer>  cityIndexUsed =new ArrayList<>();

                // delate data if you repat searching
                catigorieIndexUsed.removeAll(stockImageFilter);
                starIndexUsed.removeAll(stockStarFilter);
                prixMaxIndexUsed.removeAll(stockPrixFilter);
                prixMinIndexUsed.removeAll(stockPrixFilter);
                cityIndexUsed.removeAll(stockCityFilter);









                if(Max.getText().length()==0&&Min.getText().length()==0&&City.getText().length()==0&&catigorieEtoile.getSelectedItem().toString()=="Chose one"&&catigoriefilter.getSelectedItem().toString()=="Chose one"){
                    Toast.makeText(filter.this, "Empty Field", Toast.LENGTH_LONG).show();
                }else if(Max.getText().length()!=0||Min.getText().length()!=0||City.getText().length()!=0||catigorieEtoile.getSelectedItem().toString()!="Chose one"||catigoriefilter.getSelectedItem().toString()!="Chose one"){
                    int i=0;
                    if(Min.getText().length()!=0){
                        prixMinIndexUsed=minPrix(tab_prix);
                        if(dataIndex.size()!=0){
                            dataIndex.retainAll(prixMinIndexUsed);
                        }else{
                            dataIndex=prixMinIndexUsed;
                        }
                    }
                    if(Max.getText().length()!=0){
                        prixMaxIndexUsed=maxPrix(tab_prix);
                        if(dataIndex.size()!=0){
                            dataIndex.retainAll(prixMaxIndexUsed);
                        }else{
                            dataIndex=prixMaxIndexUsed;
                        }
                    }
                    if(City.getText().length()!=0){
                        cityIndexUsed=choseCity(tab_city);
                        if(dataIndex.size()!=0){
                            dataIndex.retainAll(cityIndexUsed);
                        }else{
                            dataIndex=cityIndexUsed;
                        }
                    }
                    if(catigoriefilter.getSelectedItem().toString().length()!=0 &&"chose one".length()!=(catigoriefilter.getSelectedItem().toString()).length()){
                        catigorieIndexUsed=choseCatigorie(tab_catigorie);
                        if(dataIndex.size()!=0){
                            dataIndex.retainAll(catigorieIndexUsed);
                        }else{
                            dataIndex=catigorieIndexUsed;
                        }
                    }
                    if(catigorieEtoile.getSelectedItem().toString().length()!=0&&"chose one".length()!=(catigorieEtoile.getSelectedItem().toString()).length()){
                        starIndexUsed=choseStar(tab_star);
                        if(dataIndex.size()!=0){
                            dataIndex.retainAll(starIndexUsed);
                        }else{
                            dataIndex=starIndexUsed;
                        }
                    }



                    if(dataIndex.size()==0){
                        l.setAdapter(new noresultat());
                    }








                    for (int data : dataIndex) {
                        stockImageFilter.add(tab_imag.get(data));
                        stockStarFilter.add(tab_star.get(data));
                        stockNomFilter.add(tab_nom.get(data));
                        stockPrixFilter.add(tab_prix.get(data));
                        stockCityFilter.add(tab_city.get(data));
                        ListView l = (ListView) findViewById(R.id.listOfAnnonce);
                        //attacher class base adabter
                        l.setAdapter(new Annoncement(stockImageFilter, stockStarFilter, stockNomFilter, stockPrixFilter, stockCityFilter));
                    }

                }

            }
        });

        dropFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etatDeCadre==0){
                    cadre.setVisibility(View.VISIBLE);
                    dropFilter.setImageDrawable(getResources().getDrawable(R.drawable.filterhau));                    etatDeCadre++;
                }else if(etatDeCadre==1){
                    cadre.setVisibility(View.GONE);
                    dropFilter.setImageDrawable(getResources().getDrawable(R.drawable.filterbat));
                    etatDeCadre--;
                }

            }
        });

        goToFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changepage = new Intent(filter.this,feed.class);
                changepage.putExtra("EmailOfUser",EmailOfUser);
                finish();
                startActivity(changepage);
            }
        });



    }// start function

    public void RemplirCombobox(ArrayList<String> tab,Spinner ComboBoxComponent){
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,tab);
        ComboBoxComponent.setAdapter(adapter);
    }
    public class Annoncement extends BaseAdapter {

        private ArrayList <Integer>  tab_imag ;
        private ArrayList <Integer>  tab_star ;
        private ArrayList <String>  tab_nom ;
        private ArrayList <Double>  tab_prix ;
        private ArrayList <String>  tab_city ;



        public Annoncement(ArrayList <Integer>  tab_imag,ArrayList <Integer>  tab_star,ArrayList <String>  tab_nom,ArrayList <Double>  tab_prix,ArrayList <String>  tab_city) {
            this.tab_imag=tab_imag;
            this.tab_star=tab_star;
            this.tab_nom=tab_nom;
            this.tab_prix=tab_prix;
            this.tab_city=tab_city;
        }


        @Override
        public int getCount() {
            return tab_nom.size();
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
            t1.setText(tab_nom.get(i));
            t2.setText(String.valueOf(tab_prix.get(i))+" DH");
            img.setImageResource(tab_imag.get(i));
            textCity.setText(tab_city.get(i));
            if(tab_star.get(i)==0){
                star1.setVisibility(View.INVISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }else if(tab_star.get(i)==1){
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }else if(tab_star.get(i)==2){
                star3.setVisibility(View.INVISIBLE);
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }else if(tab_star.get(i)==3){
                star4.setVisibility(View.INVISIBLE);
                star5.setVisibility(View.INVISIBLE);
            }else if(tab_star.get(i)==4){
                star5.setVisibility(View.INVISIBLE);
            }

            return v;
        }
    }

    public class noresultat extends BaseAdapter {


        public noresultat() {

        }


        @Override
        public int getCount() {
            return 1;
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
            v=getLayoutInflater().inflate(R.layout.noresult,null);
            return v;
        }
    }

    public ArrayList <Integer> maxPrix(ArrayList <Double> tab){
        ArrayList <Integer> Index = new ArrayList <Integer>();
        ListView l = (ListView) findViewById(R.id.listOfAnnonce);
        Index.removeAll(Index);
        int i=0;

        for (Double prix : tab) {
            if (prix <= Double.parseDouble(Max.getText().toString())) {
                Index.add(i);
            }
            i++;

        }
        return Index;
    }

    public ArrayList <Integer> minPrix(ArrayList <Double> tab){
        ArrayList <Integer> Index = new ArrayList <Integer>();
        Index.removeAll(Index);
        int i=0;

        for (Double prix : tab) {
            if (prix >= Double.parseDouble(Min.getText().toString())) {
                Index.add(i);
            }
            i++;

        }
        return Index;
    }

    public ArrayList <Integer> choseCity(ArrayList <String> tab){
        ArrayList <Integer> Index = new ArrayList <Integer>();
        Index.removeAll(Index);
        int i=0;
        String citySearch =City.getText().toString();

        for (String city : tab) {
            if ((city.toLowerCase()).equals(citySearch.toLowerCase())) {
                Index.add(i);
            }
            i++;

        }
        return Index;
    }

    public ArrayList <Integer> choseCatigorie(ArrayList <String> tab){
        ArrayList <Integer> Index = new ArrayList <Integer>();
        Index.removeAll(Index);
        int i=0;
        String catigorieSearch =catigoriefilter.getSelectedItem().toString();

        for (String catig : tab) {
            if ((catig.toLowerCase()).equals(catigorieSearch.toLowerCase())) {
                Index.add(i);
            }
            i++;

        }
        return Index;
    }

    public ArrayList <Integer> choseStar(ArrayList <Integer> tab){
        int i=0;

        ArrayList <Integer> Index = new ArrayList <Integer>();
        Index.removeAll(Index);
        int searchStar =starNumber(catigorieEtoile.getSelectedItem().toString());

        for (int Star : tab) {
            if (Star==searchStar) {
                Index.add(i);
            }
            i++;

        }
        return Index;
    }

    public int starNumber(String txtStar){
        int number=0;
        number=Integer.parseInt(String.valueOf(txtStar.charAt(0)));
        return number;
    }




}