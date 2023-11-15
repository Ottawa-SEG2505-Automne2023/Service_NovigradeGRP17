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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EditServices extends AppCompatActivity {
    ListView listeServices;
    ArrayList<String> liste ;
    DatabaseReference baseServices;
    Button btnAjout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_services);
        btnAjout=(Button) findViewById(R.id.btnAjout);
        listeServices=(ListView) findViewById(R.id.listeServices);
        baseServices = FirebaseDatabase.getInstance("https://novigrad-projet1-g09-default-rtdb.firebaseio.com").getReference().child("services");
        ArrayList<String> liste = new ArrayList<String>();
        ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(EditServices.this, android.R.layout.simple_expandable_list_item_1,liste);
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
        listeServices.setAdapter(adaptateur);

        listeServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nom = adaptateur.getItem(i);
                Intent intent = new Intent(EditServices.this, EditService.class);
                intent.putExtra("nom", nom);
                startActivity(intent);
                finish();
            }
        });
        btnAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ajout=new Intent(EditServices.this,CreateService.class);
                startActivity(ajout);
                finish();
            }
        });
    }


}