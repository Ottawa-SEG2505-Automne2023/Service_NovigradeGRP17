package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteAccount extends AppCompatActivity {
    Button btnSuppr;
    Button btnRetour;
    EditText editTxtSuppr;
    FirebaseDatabase base;
    Button btnSuccur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        base = FirebaseDatabase.getInstance("https://novigrad-projet1-g09-default-rtdb.firebaseio.com");
        editTxtSuppr = (EditText) findViewById(R.id.editTxtSuppr);
        btnSuppr=(Button)findViewById(R.id.btnSuppr);
        btnSuccur=(Button)findViewById(R.id.btnSuccur);
        btnRetour=(Button)findViewById(R.id.btnRetour);
        btnSuppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!TextUtils.isEmpty(editTxtSuppr.getText().toString())){
                    base = FirebaseDatabase.getInstance("https://novigrad-projet1-g09-default-rtdb.firebaseio.com");
                    DatabaseReference nomRef = base.getReference("users/"+editTxtSuppr.getText().toString()+"/name");
                    nomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String test = dataSnapshot.getValue(String.class);
                            if((test==null)==false) {
                                DatabaseReference nom = base.getReference("users/"+editTxtSuppr.getText().toString()+"/name");
                                DatabaseReference mdp = base.getReference("users/"+editTxtSuppr.getText().toString()+"/mdp");
                                DatabaseReference role = base.getReference("users/"+editTxtSuppr.getText().toString()+"/role");
                                DatabaseReference mail = base.getReference("users/"+editTxtSuppr.getText().toString()+"/email");
                                nom.removeValue();
                                mdp.removeValue();
                                role.removeValue();
                                mail.removeValue();
                                Toast.makeText(getApplicationContext(), "Compte supprimé avec succés !", Toast.LENGTH_LONG).show();
                                editTxtSuppr.setText("");
                                finish();
                            }
                            else{Toast.makeText(getApplicationContext(), "Compte introuvable, réessayer svp", Toast.LENGTH_LONG).show();}
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "SVP entrez le nom d'utilisateur d'un compte à supprimer", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();

            }
        });
        btnSuccur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(DeleteAccount.this, ListeSuccursales.class);
                startActivity(intent);
                finish();
            }
        });
    }}

