package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoggedAdmin extends AppCompatActivity {

    TextView connexion;
    TextView txtViewNom;
    TextView txtViewRole;
    Button btnSupprimer;
    Button btnServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_admin);
        txtViewNom = (TextView) findViewById(R.id.txtViewNom);
        txtViewRole = (TextView) findViewById(R.id.txtViewRole);
        connexion = (TextView) findViewById(R.id.connexion);
        //on recupere l'intent pour recuperer les extras et les afficher
        Intent intent = getIntent();
        //on recuperer le nom de l'administrateur (mis en extra dans le intent
        //que l'on stocke dans str
        String str = "";
        str = intent.getStringExtra("nom");
        //Initialisation du boutton de suppression de compte pour l'administrateur
        btnSupprimer = (Button) findViewById(R.id.btnSupprimer);
        btnServices = (Button) findViewById(R.id.btnServices);
        txtViewNom.setText(str.toString());
        txtViewRole.setText("Administrateur Novigrad");
        connexion.setText("Connexion réussie! Bienvenue " + str +" sur votre compte Novigrad!");
        //On click du boutton de suppresion de compte et de modification de services
        btnSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(LoggedAdmin.this, DeleteAccount.class);
                startActivity(intent);
            }
        });
        btnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(LoggedAdmin.this, EditServices.class);
                startActivity(intent2);
            }
        });
    }
}