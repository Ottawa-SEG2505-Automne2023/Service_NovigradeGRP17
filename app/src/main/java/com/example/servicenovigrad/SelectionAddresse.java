package com.example.servicenovigrad;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectionAddresse extends AppCompatActivity {
    String nomClient;
    EditText entreAddresse;
    Button btnRechercheAdd;
    DatabaseReference baseSucc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_addresse);
        Intent intent = getIntent();
        nomClient = intent.getStringExtra("nomClient");
        entreAddresse = (EditText) findViewById(R.id.entreAddresse);
        btnRechercheAdd = (Button) findViewById(R.id.btnRechercheAdd);
        baseSucc = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference().child("succursales");
        btnRechercheAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addresseCorrecte(entreAddresse.getText().toString())){
                    baseSucc.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                if(ds.getKey().equals("nomSuccur")){
                                    String nom = ds.getValue(String.class).toString();
                                    DatabaseReference refAddresse = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference("succursales/"+nom+"/addresse");
                                    refAddresse.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getValue(String.class)!=null && dataSnapshot.getValue(String.class).equals(entreAddresse.getText().toString())){
                                                Intent intent0 = new Intent(SelectionAddresse.this, SuccurTrouve.class);
                                                intent0.putExtra("nomClient", nomClient);
                                                intent0.putExtra("nomSucc", nom);
                                                startActivity(intent0);
                                                finish();
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
                else{
                    Toast.makeText(getApplicationContext(), "SVP saississez une addresse avec un nom et numéro de rue", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    public static boolean addresseCorrecte(String addresse){
        char[] caracteres = addresse.toCharArray();
        int lettrePresente=0;
        int chiffrePresent=0;
        for (char c: caracteres)
        {
            if(((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))){
                lettrePresente=1;
            }
            else if(((c >= '0') && (c <= '9'))){
                chiffrePresent=1;
            }
            if(lettrePresente==1 && chiffrePresent==1){
                return true;
            }
        }
        return false;
    }
}