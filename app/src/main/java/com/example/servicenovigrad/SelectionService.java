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

import java.util.ArrayList;

public class SelectionService extends AppCompatActivity {
    ListView listeService;
    ArrayList<String> liste ;
    DatabaseReference baseServices;
    String nomClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_service);
        Intent intent = getIntent();
        nomClient = intent.getStringExtra("nomClient");
        listeService=(ListView) findViewById(R.id.listeService);
        baseServices = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("services");
        ArrayList<String> liste = new ArrayList<String>();
        ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(SelectionService.this, android.R.layout.simple_expandable_list_item_1,liste);
        baseServices.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.getKey().equals("nom")){
                        String nom = ds.getValue(String.class).toString();
                        liste.add(nom);
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
        listeService.setAdapter(adaptateur);
        listeService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nom = adaptateur.getItem(i);
                Intent intent = new Intent(SelectionService.this, RechercheService.class);
                intent.putExtra("nomClient", nomClient);
                intent.putExtra("service", nom);
                startActivity(intent);
            }
        });
    }
}