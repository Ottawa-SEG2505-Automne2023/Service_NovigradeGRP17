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

public class RemoveService extends AppCompatActivity {
    ListView listeRemoveService;
    ArrayList<String> listeRemove ;
    DatabaseReference baseRemoveServices;
    String nomSucc;
    String nomEmp;
    TextView removeTexte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_service);
        Intent intent = getIntent();
        nomSucc = intent.getStringExtra("nomSuccursale");
        nomEmp = intent.getStringExtra("nomEmploye");
        removeTexte = (TextView) findViewById(R.id.removeTexte);
        removeTexte.setText("Profil de la succursale Novigrad : "+nomSucc);
        listeRemoveService=(ListView) findViewById(R.id.listeRemoveService);
        baseRemoveServices = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference().child("succursales").child(nomSucc);
        listeRemove = new ArrayList<String>();
        ArrayAdapter<String> adaptateurRemove = new ArrayAdapter<String>(RemoveService.this, android.R.layout.simple_expandable_list_item_1,listeRemove);
        baseRemoveServices.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String nomService = ds.getValue(String.class);
                    DatabaseReference refNomService = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference().child("services").child(nomService).child("nom");
                    refNomService.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String test = dataSnapshot.getValue(String.class);
                            if(test!=null && !test.equals("")){
                                listeRemove.add(nomService);
                                adaptateurRemove.notifyDataSetChanged();
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
                adaptateurRemove.notifyDataSetChanged();
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
        listeRemoveService.setAdapter(adaptateurRemove);
        listeRemoveService.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nom = adaptateurRemove.getItem(i);
                DatabaseReference serviceToRemove = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference().child("succursales").child(nomSucc).child("services").child(nom);
                serviceToRemove.removeValue();
                Intent retour=new Intent(RemoveService.this,ProfilSuccur.class);
                retour.putExtra("nomEmploye",nomEmp);
                retour.putExtra("nomSuccursale",nomSucc);
                startActivity(retour);
                finish();
            }
        });

    }
}