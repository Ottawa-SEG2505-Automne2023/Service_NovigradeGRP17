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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RechercheHeures extends AppCompatActivity {
    TextView rechercheTexte;
    ListView listeRecherche;
    DatabaseReference baseRecherche;
    ArrayList<String> listeRechercheResultat;
    String heureOuverture;
    String heureFermeture;
    int heureOuv;
    int minuteOuv;
    int heureFer;
    int minuteFer;
    int rechercheHeureOuv;
    int rechercheMinuteOuv;
    int rechercheHeureFer;
    int rechercheMinuteFer;
    String nomClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_heures);
        Intent intent = getIntent();
        nomClient = intent.getStringExtra("nomClient");
        heureOuverture = intent.getStringExtra("heureMinuteOuv");
        heureFermeture = intent.getStringExtra("heureMinuteFer");
        heureOuv = intent.getIntExtra("heureOuv",0);
        minuteOuv = intent.getIntExtra("minuteOuv",0);
        heureFer = intent.getIntExtra("heureFer",0);
        minuteFer = intent.getIntExtra("minuteFer",0);
        rechercheTexte = (TextView) findViewById(R.id.rechercheTexte);
        rechercheTexte.setText("Voiçi la liste des succursales qui sont ouvertes de : "+heureOuverture+" jusqu'à "+heureFermeture);
        listeRecherche=(ListView) findViewById(R.id.listeRecherche);
        DatabaseReference refBase=FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference();
        baseRecherche = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("succursales");
        listeRechercheResultat = new ArrayList<String>();
        ArrayAdapter<String> adaptateurRecherche = new ArrayAdapter<String>(RechercheHeures.this, android.R.layout.simple_expandable_list_item_1,listeRechercheResultat);
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
                                    DatabaseReference refHeureOuv = refBase.child("succursales").child(nom).child("heureMinuteOuv");
                                    refHeureOuv.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String heureMinute = dataSnapshot.getValue(String.class);
                                            String tmp = "";   //String temporaire pour stocker les heures et minute d'ouverture pour donner des valeurs à heureOuv et minuteOuV
                                            for(int i=0; i<heureMinute.length(); i++){
                                                String chara = Character.toString(heureMinute.charAt(i));
                                                if(chara.equals(":")){
                                                    rechercheHeureOuv=Integer.parseInt(tmp);
                                                    tmp="";
                                                }
                                                else if(i==heureMinute.length()-1&&!tmp.equals("")){
                                                    rechercheMinuteOuv=Integer.parseInt(tmp);
                                                }
                                                else{
                                                    tmp=tmp+chara;
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                    DatabaseReference refHeureFer = refBase.child("succursales").child(nom).child("heureMinuteFer");
                                    refHeureFer.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String heureMinute1 = dataSnapshot.getValue(String.class);
                                            String tmp = "";   //String temporaire pour stocker les heures et minute d'ouverture pour donner des valeurs à heureOuv et minuteOuV
                                            for(int i=0; i<heureMinute1.length(); i++){
                                                String chara = Character.toString(heureMinute1.charAt(i));
                                                if(chara.equals(":")){
                                                    rechercheHeureFer=Integer.parseInt(tmp);
                                                    tmp="";
                                                }
                                                else if(i==heureMinute1.length()-1&&!tmp.equals("")){
                                                    rechercheMinuteFer=Integer.parseInt(tmp);
                                                }
                                                else{
                                                    tmp=tmp+chara;
                                                }
                                            }
                                            if((rechercheHeureOuv<heureOuv && rechercheHeureFer>heureFer) ||
                                                    (rechercheHeureOuv==heureOuv && rechercheHeureFer==heureFer && rechercheMinuteOuv<minuteOuv && rechercheMinuteFer>minuteFer)||
                                                    (rechercheHeureOuv==heureOuv && rechercheHeureFer>heureFer && rechercheMinuteOuv<minuteOuv)||
                                                    (rechercheHeureOuv<heureOuv && rechercheHeureFer==heureFer && rechercheMinuteFer>minuteFer)){
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
                Intent intent0 = new Intent(RechercheHeures.this, SuccurTrouve.class);
                String tnt = "";
                for(int e=0; e<nomSucc.length();e++){
                    if(!Character.toString(nomSucc.charAt(e)).equals("\n")){tnt+=Character.toString(nomSucc.charAt(e));}
                    else{e=nomSucc.length();}
                }
                intent0.putExtra("nomClient", nomClient);
                intent0.putExtra("nomSucc", tnt);
                startActivity(intent0);
                finish();
            }
        });
    }
}