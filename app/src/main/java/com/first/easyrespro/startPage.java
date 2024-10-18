package com.first.easyrespro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class startPage extends AppCompatActivity {
    private ImageButton btnOfferMaker,btnCustomer;
    private ConstraintLayout goaboutus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // menu hidden
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start_page);
        goaboutus=findViewById(R.id.goaboutus);
        btnCustomer=findViewById(R.id.btnCustomer);
        btnOfferMaker=findViewById(R.id.btnOfferMaker);

        goaboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutuspage = new Intent(startPage.this,aboutus.class);
                startActivity(aboutuspage);
            }
        });

        btnOfferMaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(startPage.this,login.class);
                intent.putExtra("returnPage","startPage");
                finish();
                startActivity(intent);
            }
        });
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(startPage.this,feed.class);
                intent.putExtra("EmailOfUser","null");
                finish();
                startActivity(intent);
            }
        });
    }
}