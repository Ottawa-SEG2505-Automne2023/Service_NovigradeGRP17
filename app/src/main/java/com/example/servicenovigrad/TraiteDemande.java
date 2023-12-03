package com.example.servicenovigrad;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TraiteDemande extends AppCompatActivity {
    String nomDuClient;
    String nomDeLaSuccursale;
    String nomService;
    TextView txtViewTraite;
    TextView traiteFormulaire;
    TextView traiteDocuments;
    Button btnApprouve;
    Button btnRefus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traite_demande);
        txtViewTraite = (TextView) findViewById(R.id.txtViewTraite);
        traiteDocuments = (TextView) findViewById(R.id.traiteDocuments);
        traiteFormulaire = (TextView) findViewById(R.id.traiteFormulaire);
        btnApprouve = (Button) findViewById(R.id.btnApprouve);
        btnRefus = (Button) findViewById(R.id.btnRefus);
        Intent recupere = getIntent();
        nomDuClient = recupere.getStringExtra("nomClient");
        nomDeLaSuccursale = recupere.getStringExtra("nomSuccursale");
        nomService = recupere.getStringExtra("service");
        txtViewTraite.setText("Demande de "+nomService+"soumise par "+nomDuClient);
        DatabaseReference refBase= FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference();
        DatabaseReference refForm = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("formClient");
        refForm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tmp = dataSnapshot.getValue(String.class);
                traiteFormulaire.setText(tmp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference refDocu = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("docClient");
        refDocu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tmp1 = dataSnapshot.getValue(String.class);
                traiteDocuments.setText(tmp1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        btnApprouve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference refNomClient = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("nomClient");
                DatabaseReference refFormClient = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("formClient");
                DatabaseReference refDocClient = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("docClient");
                DatabaseReference refNomService = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("nomService");
                refNomClient.removeValue();
                refFormClient.removeValue();
                refDocClient.removeValue();
                refNomService.removeValue();
                Toast.makeText(getApplicationContext(), "Demande approuvée!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        btnRefus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference refNomClient = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("nomClient");
                DatabaseReference refFormClient = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("formClient");
                DatabaseReference refDocClient = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("docClient");
                DatabaseReference refNomService = refBase.child("demandes").child(nomDeLaSuccursale).child(nomService).child("nomService");
                refNomClient.removeValue();
                refFormClient.removeValue();
                refDocClient.removeValue();
                refNomService.removeValue();
                Toast.makeText(getApplicationContext(), "Demande refusée!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}