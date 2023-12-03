package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Formulaire extends AppCompatActivity {
    String nomClient;
    String nomSucc;
    String nomService;
    TextView txtViewDemande;
    TextView textViewFormulaire;
    TextView textViewDocuments;
    DatabaseReference base;
    String formulaire;
    String documents;
    DatabaseReference refFormulaire;
    DatabaseReference refDocuments;
    EditText editTextDocuments;
    EditText editTextFormulaire;
    Button btnEnvoyerDemande;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);
        Intent intent = getIntent();
        nomClient = intent.getStringExtra("nomClient");
        nomSucc = intent.getStringExtra("nomSuccur");
        nomService = intent.getStringExtra("serviceDemande");
        txtViewDemande = (TextView) findViewById(R.id.txtViewDemande);
        textViewFormulaire = (TextView) findViewById(R.id.textViewFormulaire);
        textViewDocuments = (TextView) findViewById(R.id.textViewDocuments);
        editTextDocuments = (EditText) findViewById(R.id.editTextDocuments);
        editTextFormulaire = (EditText) findViewById(R.id.editTextFormulaire);
        btnEnvoyerDemande = (Button) findViewById(R.id.btnEnvoyerDemande);
        txtViewDemande.setText("Entrez les différentes informations nécessaire pour votre demande de "+nomService);
        base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference();
        refFormulaire = base.child("services").child(nomService).child("form");
        refDocuments = base.child("services").child(nomService).child("doc");
        refFormulaire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                formulaire = dataSnapshot.getValue(String.class);
                textViewFormulaire.setText("Entrez les informations sur le demandeur suivant : "+formulaire);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        refDocuments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                documents = dataSnapshot.getValue(String.class);
                textViewDocuments.setText("Entrez les documents requis : "+documents);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        btnEnvoyerDemande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(champsRemplis(editTextFormulaire.getText().toString(),editTextDocuments.getText().toString())){
                    DatabaseReference refNomClient = base.child("demandes").child(nomSucc).child(nomService).child("nomClient");
                    DatabaseReference refFormClient = base.child("demandes").child(nomSucc).child(nomService).child("formClient");
                    DatabaseReference refDocClient = base.child("demandes").child(nomSucc).child(nomService).child("docClient");
                    DatabaseReference refNomService = base.child("demandes").child(nomSucc).child(nomService).child("nomService");
                    refNomClient.setValue(nomClient);
                    refFormClient.setValue(editTextFormulaire.getText().toString());
                    refDocClient.setValue(editTextDocuments.getText().toString());
                    refNomService.setValue(nomService);
                    Toast.makeText(getApplicationContext(), "Demande soumise avec succes!", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "SVP remplissez tout les champs", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static boolean champsRemplis(String formul, String docu){
        if(formul!=null && docu!=null && !formul.equals("") && !docu.equals("")){
            return true;
        }
        else{
            return false;
        }
    }
}