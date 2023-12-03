package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SuccurTrouve extends AppCompatActivity {
    TextView choixSuccur;
    String nomClient;
    String nomSuccur;
    ListView selectService;
    ArrayList<String> listeSelect ;
    DatabaseReference baseSelect;
    Button btnNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succur_trouve);
        choixSuccur = findViewById(R.id.choixSuccur);
        btnNote = (Button) findViewById(R.id.btnNote);
        Intent intent0 = getIntent();
        nomClient = intent0.getStringExtra("nomClient");
        nomSuccur = intent0.getStringExtra("nomSucc");
        choixSuccur.setText("Vous avez choisi la succursale : "+nomSuccur);
        selectService=(ListView) findViewById(R.id.listeSelectService);
        baseSelect = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("succursales").child(nomSuccur);
        listeSelect = new ArrayList<String>();
        ArrayAdapter<String> adaptateurSelect = new ArrayAdapter<String>(SuccurTrouve.this, android.R.layout.simple_expandable_list_item_1,listeSelect);
        baseSelect.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String nomService = ds.getValue(String.class);
                    DatabaseReference refNomService = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("services").child(nomService).child("nom");
                    refNomService.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String test = dataSnapshot.getValue(String.class);
                            if(test!=null && !test.equals("")){
                                listeSelect.add(nomService);
                                adaptateurSelect.notifyDataSetChanged();
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
                adaptateurSelect.notifyDataSetChanged();
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
        selectService.setAdapter(adaptateurSelect);
        selectService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nom = adaptateurSelect.getItem(i);
                Intent intent2 = new Intent(SuccurTrouve.this, Formulaire.class);
                intent2.putExtra("nomClient",nomClient);
                intent2.putExtra("nomSuccur", nomSuccur);
                intent2.putExtra("serviceDemande",nom);
                startActivity(intent2);
                finish();
            }
        });
        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(SuccurTrouve.this, RateSuccur.class);
                intent3.putExtra("nomSuccur", nomSuccur);
                intent3.putExtra("nomClient", nomClient);
                startActivity(intent3);
            }
        });
    }
}