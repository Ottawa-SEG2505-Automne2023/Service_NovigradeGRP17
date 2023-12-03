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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListeSuccursales extends AppCompatActivity {
    ListView listeSuccur;
    ArrayList<String> liste ;
    DatabaseReference baseSuccur;
    FirebaseDatabase base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_succursales);
        listeSuccur=(ListView) findViewById(R.id.listeSuccur);
        baseSuccur = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("succursales");
        ArrayList<String> liste = new ArrayList<String>();
        ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(ListeSuccursales.this, android.R.layout.simple_expandable_list_item_1,liste);
        baseSuccur.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.getKey().equals("nomSuccur")){
                        String nomSuccur = ds.getValue(String.class).toString();
                        liste.add(nomSuccur);
                        adaptateur.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adaptateur.notifyDataSetChanged();
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
        listeSuccur.setAdapter(adaptateur);
        listeSuccur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com");
                String nom = adaptateur.getItem(i);
                DatabaseReference nomRef = base.getReference("succursales/"+nom+"/nomSuccur");
                DatabaseReference villeRef = base.getReference("succursales/"+nom+"/ville");
                DatabaseReference addresseRef = base.getReference("succursales/"+nom+"/addresse");
                DatabaseReference numRef = base.getReference("succursales/"+nom+"/num");
                DatabaseReference heureMinuteOuvRef = base.getReference("succursales/"+nom+"/heureMinuteOuv");
                DatabaseReference heureMinuteFerRef = base.getReference("succursales/"+nom+"/heureMinuteFer");
                nomRef.removeValue();
                villeRef.removeValue();
                addresseRef.removeValue();
                numRef.removeValue();
                heureMinuteOuvRef.removeValue();
                heureMinuteFerRef.removeValue();
                Toast.makeText(getApplicationContext(), "Compte supprimé avec succés !", Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }
}