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

public class AddService extends AppCompatActivity {
    ListView listeAllServices;
    ArrayList<String> listeAll ;
    DatabaseReference baseAllServices;
    String nomSucc;
    String nomEmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        listeAllServices=(ListView) findViewById(R.id.listeAllServices);
        Intent intent = getIntent();
        nomSucc = intent.getStringExtra("nomSuccursale");
        nomEmp = intent.getStringExtra("nomEmploye");
        baseAllServices = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("services");
        ArrayList<String> listeAll = new ArrayList<String>();
        ArrayAdapter<String> adaptateur = new ArrayAdapter<String>(AddService.this, android.R.layout.simple_expandable_list_item_1,listeAll);
        baseAllServices.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.getKey().equals("nom")){
                        String nom = ds.getValue(String.class).toString();
                        listeAll.add(nom);
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
        listeAllServices.setAdapter(adaptateur);
        listeAllServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nom = adaptateur.getItem(i);
                DatabaseReference add = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference().child("succursales").child(nomSucc).child("services").child(nom);
                add.setValue(nom);
                Toast.makeText(getApplicationContext(), "Service ajouté avec succes!", Toast.LENGTH_LONG).show();
                Intent retour=new Intent(AddService.this,ProfilSuccur.class);
                retour.putExtra("nomEmploye",nomEmp);
                retour.putExtra("nomSuccursale",nomSucc);
                startActivity(retour);
                finish();
            }
        });
    }
}