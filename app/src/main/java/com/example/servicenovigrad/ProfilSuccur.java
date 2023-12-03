package com.example.servicenovigrad;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ProfilSuccur extends AppCompatActivity {
    ListView listeServiceOffert;
    ArrayList<String> listeServ ;
    DatabaseReference baseSuccursales;
    String nomSucc;
    String nomEmp;
    TextView connecteSuccur;
    Button btnAddServiceSuccur;
    Button btnRemoveServiceSuccur;
    Button btnModifInfoSuccur;
    Button btnVoirDemandes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_succur);
        btnAddServiceSuccur = findViewById(R.id.btnAddServiceSuccur);
        btnVoirDemandes = findViewById(R.id.btnVoirDemandes);
        btnRemoveServiceSuccur = findViewById(R.id.btnRemoveServiceSuccur);
        btnModifInfoSuccur = findViewById(R.id.btnModifInfoSuccur);
        Intent intent = getIntent();
        nomSucc = intent.getStringExtra("nomSuccursale");
        nomEmp = intent.getStringExtra("nomEmploye");
        connecteSuccur = (TextView) findViewById(R.id.connecteSuccur);
        connecteSuccur.setText("Profil de la succursale Novigrad : "+nomSucc);
        listeServiceOffert=(ListView) findViewById(R.id.listeServiceOffert);
        baseSuccursales = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("succursales").child(nomSucc);
        listeServ = new ArrayList<String>();
        ArrayAdapter<String> adaptateurServ = new ArrayAdapter<String>(ProfilSuccur.this, android.R.layout.simple_expandable_list_item_1,listeServ);
        baseSuccursales.addChildEventListener(new ChildEventListener() {
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
                                listeServ.add(nomService);
                                adaptateurServ.notifyDataSetChanged();
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
                adaptateurServ.notifyDataSetChanged();
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
        listeServiceOffert.setAdapter(adaptateurServ);
        btnModifInfoSuccur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent0 = new Intent(ProfilSuccur.this, EditSuccur.class);
                intent0.putExtra("nomEmploye", nomEmp);
                intent0.putExtra("nomSuccursale", nomSucc);
                startActivity(intent0);
                finish();
            }
        });
        btnAddServiceSuccur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ProfilSuccur.this, AddService.class);
                intent2.putExtra("nomEmploye", nomEmp);
                intent2.putExtra("nomSuccursale", nomSucc);
                startActivity(intent2);
                finish();
            }
        });
        btnRemoveServiceSuccur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(ProfilSuccur.this, RemoveService.class);
                intent3.putExtra("nomEmploye", nomEmp);
                intent3.putExtra("nomSuccursale", nomSucc);
                startActivity(intent3);
                finish();
            }
        });
        btnVoirDemandes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(ProfilSuccur.this, Demandes.class);
                intent4.putExtra("nomEmploye", nomEmp);
                intent4.putExtra("nomSuccursale", nomSucc);
                startActivity(intent4);
            }
        });

    }
}