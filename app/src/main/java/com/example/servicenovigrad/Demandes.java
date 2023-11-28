package com.example.servicenovigrad;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Demandes extends AppCompatActivity {
    String nomSucc;
    String nomEmp;
    ListView listeDemandes;
    DatabaseReference baseDemandes;
    ArrayList<String> arrayListDemandes;
    String nomServiceDemande;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demandes);
        Intent intent = getIntent();
        nomSucc = intent.getStringExtra("nomSuccursale");
        nomEmp = intent.getStringExtra("nomEmploye");
        listeDemandes=(ListView) findViewById(R.id.listeDemandes);
        baseDemandes = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference().child("demandes").child(nomSucc);
        arrayListDemandes = new ArrayList<String>();
        ArrayAdapter<String> adaptateurDemandes = new ArrayAdapter<String>(Demandes.this, android.R.layout.simple_expandable_list_item_1,arrayListDemandes);
        baseDemandes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    nomServiceDemande = ds.getValue(String.class);
                    DatabaseReference refNomClientDemande = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference().child("demandes").child(nomSucc).child(nomServiceDemande).child("nomClient");
                    refNomClientDemande.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(nomServiceDemande!=null&& dataSnapshot.getValue(String.class)!=null) {
                                String nomServiceEtClient = "Demande de "+nomServiceDemande+" par : "+dataSnapshot.getValue(String.class);
                                arrayListDemandes.add(nomServiceEtClient);
                                adaptateurDemandes.notifyDataSetChanged();};

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adaptateurDemandes.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listeDemandes.setAdapter(adaptateurDemandes);
        listeDemandes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nom = adaptateurDemandes.getItem(i);
                String nomDuClient = "";
                int deuxPointsPasse = 0;
                for(int e=0; e<nom.length();e++){
                    if(Character.toString(nom.charAt(e)).equals(":")){
                        deuxPointsPasse=1;
                    }
                    else if (!Character.toString(nom.charAt(e)).equals(":") && deuxPointsPasse==0){
                        e++;
                        continue;
                    }
                    else if (!Character.toString(nom.charAt(e)).equals(":") && deuxPointsPasse==1){
                        nomDuClient+=Character.toString(nom.charAt(e));
                    }
                }
                Intent intent0 = new Intent(Demandes.this, TraiteDemande.class);
                intent0.putExtra("nomClient", nomDuClient);
                intent0.putExtra("nomSuccursale", nomSucc);
                intent0.putExtra("service",nomServiceDemande);
                startActivity(intent0);
                finish();
            }
        });
    }
}