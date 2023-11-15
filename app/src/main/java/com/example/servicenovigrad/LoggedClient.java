package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoggedClient extends AppCompatActivity {
    TextView afficheNomClient;
    String nomClient;
    Button btnAdd;
    Button btnServ;
    Button btnHeures;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_client);
        Intent intent = getIntent();        //On recupere le nom de l'employe et on le met dans str pour pouvoir l'utiliser plus tard
        nomClient = intent.getStringExtra("nom");
        afficheNomClient = (TextView) findViewById(R.id.afficheNomClient);
        afficheNomClient.setText(nomClient);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnServ = (Button) findViewById(R.id.btnServ);
        btnHeures = (Button) findViewById(R.id.btnHeures);
    }}







