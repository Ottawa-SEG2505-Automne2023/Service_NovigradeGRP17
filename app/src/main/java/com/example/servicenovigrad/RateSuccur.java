package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateSuccur extends AppCompatActivity {
    RatingBar barre;
    Button noterService;
    DatabaseReference refTotal;
    DatabaseReference refNb;
    Double noteTotale;
    Double nbDeNotes;
    String nomSuccursale;
    String nomClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_succur);
        barre = (RatingBar) findViewById(R.id.rateBar);
        noterService = (Button) findViewById(R.id.noterService);
        Intent intent = getIntent();
        nomSuccursale = intent.getStringExtra("nomSuccur");
        nomClient = intent.getStringExtra("nomClient");
        refTotal = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference("succursales/"+nomSuccursale+"/noteTotale");
        refNb = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference("succursales/"+nomSuccursale+"/nbNotes");
        refTotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Long.class)!=null){
                    noteTotale = Double.valueOf(dataSnapshot.getValue(Long.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        refNb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                if(dataSnapshot2.getValue(Long.class)!=null){
                    nbDeNotes = Double.valueOf(dataSnapshot2.getValue(Long.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        noterService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noteTotale != null && nbDeNotes != null) {
                    refTotal.setValue((Double)noteTotale+barre.getRating());
                    refNb.setValue((Double)nbDeNotes+1);
                    finish();

                }
                else{
                    refTotal.setValue(Double.valueOf(barre.getRating()));
                    refNb.setValue((Double.valueOf(1)));
                    finish();}

            }
        });

    }
}