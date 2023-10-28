package com.example.servicenovigrad;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoggedIn extends AppCompatActivity {

    TextView connexion;
    TextView txtViewNom;
    TextView txtViewRole;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        txtViewNom = (TextView) findViewById(R.id.txtViewNom);
        txtViewRole = (TextView) findViewById(R.id.txtViewRole);
        connexion = (TextView) findViewById(R.id.connexion);

        //on recupere l'intent pour recuperer les extras et les afficher
        Intent intent = getIntent();
        //on recuperer le nom de l'administrateur (mis en extra dans le intent
        //que l'on stocke dans str
        String str = intent.getStringExtra("nom");

        DatabaseReference refBase=FirebaseDatabase.getInstance().getReference();
        DatabaseReference refRole = refBase.child("users").child(str).child("role");
        refRole.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String role = dataSnapshot.getValue(String.class);
                    if (role != null) {
                        txtViewRole.setText(role);

                        connexion.setText("Connexion réussie! Bienvenue " + str + " sur votre compte Novigrad!");
                    } else {
                        // Handle the case where role is null or not found
                        txtViewRole.setText("Rôle inconnu");
                        connexion.setText("Connexion réussie! Bienvenue " + str + " sur votre compte Novigrad!");
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });

        txtViewNom.setText(str);
    }
}