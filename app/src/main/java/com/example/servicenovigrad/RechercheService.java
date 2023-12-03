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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RechercheService extends AppCompatActivity {
    String service;
    String nomClient;
    ListView listeRecherche;
    DatabaseReference baseRecherche;
    TextView rechercheTexte;
    ArrayList<String> listeRechercheResultat;
    Double noteTotale;
    Double nbDeNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_service);
        Intent intent = getIntent();
        service = intent.getStringExtra("service");
        nomClient = intent.getStringExtra("nomClient");
        rechercheTexte = (TextView) findViewById(R.id.rechercheTexte);
        rechercheTexte.setText("Voiçi la liste des succursales qui proposent le service : "+service);
        listeRecherche=(ListView) findViewById(R.id.listeRecherche);
        baseRecherche = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("succursales");
        listeRechercheResultat = new ArrayList<String>();
        ArrayAdapter<String> adaptateurRecherche = new ArrayAdapter<String>(RechercheService.this, android.R.layout.simple_expandable_list_item_1,listeRechercheResultat);
        baseRecherche.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.getKey().equals("nomSuccur")){
                        String nom = ds.getValue(String.class);
                        DatabaseReference baseRecherche2 = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("succursales").child(nom);
                        baseRecherche2.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                for(DataSnapshot ds2 : snapshot.getChildren()) {
                                    if(ds2.getValue(String.class).equals(service)){
                                        DatabaseReference refTotal = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference("succursales/"+nom+"/noteTotale");
                                        DatabaseReference refNb = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference("succursales/"+nom+"/nbNotes");
                                        refTotal.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getValue(Double.class)!=null){
                                                    noteTotale = Double.valueOf(dataSnapshot.getValue(Double.class));
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                        refNb.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getValue(Double.class)!=null){
                                                    nbDeNotes = Double.valueOf(dataSnapshot.getValue(Double.class));
                                                    if(noteTotale!=null&&nbDeNotes!=null){
                                                        Double moyenne = noteTotale/nbDeNotes;
                                                        listeRechercheResultat.add(nom +"\nNote globale pour cette succursale : "+String.valueOf(moyenne)+" / 5 ");
                                                        adaptateurRecherche.notifyDataSetChanged();

                                                    }

                                                }
                                                else{
                                                    listeRechercheResultat.add(nom);
                                                    adaptateurRecherche.notifyDataSetChanged();

                                                }

                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });



                                    }
                                }
                            }
                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                adaptateurRecherche.notifyDataSetChanged();
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
                    }


                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adaptateurRecherche.notifyDataSetChanged();
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
        listeRecherche.setAdapter(adaptateurRecherche);
        listeRecherche.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nomSucc = adaptateurRecherche.getItem(i);
                String tnt = "";
                for(int e=0; e<nomSucc.length();e++){
                    if(!Character.toString(nomSucc.charAt(e)).equals("\n")){tnt+=Character.toString(nomSucc.charAt(e));}
                    else{e=nomSucc.length();}
                }
                Intent intent0 = new Intent(RechercheService.this, SuccurTrouve.class);
                intent0.putExtra("nomClient", nomClient);
                intent0.putExtra("nomSucc", tnt);
                startActivity(intent0);
                finish();
            }
        });
    }
}