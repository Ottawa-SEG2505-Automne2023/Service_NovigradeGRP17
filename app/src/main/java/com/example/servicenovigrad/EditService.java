package com.example.servicenovigrad;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditService extends AppCompatActivity {
    TextView textViewHaut;
    EditText editTextNom;
    EditText editTextForm;
    EditText editTextDoc;
    Button btnSave;
    Button btnDelete;
    DatabaseReference base;
    FirebaseDatabase firebase;
    DatabaseReference refForm;
    DatabaseReference refDoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);
        textViewHaut = (TextView) findViewById(R.id.textViewHaut);
        editTextNom = (EditText) findViewById(R.id.editTextNom);
        editTextForm = (EditText) findViewById(R.id.editTextForm);
        editTextDoc = (EditText) findViewById(R.id.editTextDoc);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        Intent intent = getIntent();
        String str = "";
        str = intent.getStringExtra("nom").toString();
        textViewHaut.setText("Vous êtes entrain de modifier le service : "+str);
        editTextNom.setText(str);
        base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference();
        refForm = base.child("services").child(str).child("form");
        refDoc = base.child("services").child(str).child("doc");
        refForm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String form = dataSnapshot.getValue(String.class);
                editTextForm.setText(form);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        refDoc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String doc = dataSnapshot.getValue(String.class);
                editTextDoc.setText(doc);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editTextNom.getText().toString())
                        && !TextUtils.isEmpty(editTextForm.getText().toString()) &&
                        !TextUtils.isEmpty(editTextDoc.getText().toString())){
                    FirebaseDatabase firebase = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com");
                    Intent intent1 = getIntent();
                    String str1 = "";
                    str1 = intent1.getStringExtra("nom").toString();
                    DatabaseReference nouveauNom = firebase.getReference("services/"+editTextNom.getText().toString()+"/nom");
                    DatabaseReference nouveauForm = firebase.getReference("services/"+editTextNom.getText().toString()+"/form");
                    DatabaseReference nouveauDoc = firebase.getReference("services/"+editTextNom.getText().toString()+"/doc");
                    DatabaseReference oldNom = firebase.getReference("services/"+str1+"/nom");
                    DatabaseReference oldForm = firebase.getReference("services/"+str1+"/form");
                    DatabaseReference oldDoc = firebase.getReference("services/"+str1+"/doc");
                    if(editTextNom.getText().toString().equals(str1)){
                        nouveauForm.setValue(editTextForm.getText().toString());
                        nouveauDoc.setValue(editTextDoc.getText().toString());

                    }
                    else{
                        nouveauForm.setValue(editTextForm.getText().toString());
                        nouveauDoc.setValue(editTextDoc.getText().toString());
                        nouveauNom.setValue(editTextNom.getText().toString());
                        oldNom.removeValue();
                        oldForm.removeValue();
                        oldDoc.removeValue();
                    }

                    Toast.makeText(getApplicationContext(), "Le service a été modifié avec succés!", Toast.LENGTH_LONG).show();
                    Intent return1 = new Intent(EditService.this, EditServices.class);
                    startActivity(return1);
                    finish();

                }
                else{
                    Toast.makeText(getApplicationContext(), "SVP remplissez tout les champs", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebase = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com");
                DatabaseReference nouveauNom = firebase.getReference("services/"+editTextNom.getText().toString()+"/nom");
                DatabaseReference nouveauForm = firebase.getReference("services/"+editTextNom.getText().toString()+"/form");
                DatabaseReference nouveauDoc = firebase.getReference("services/"+editTextNom.getText().toString()+"/doc");
                nouveauNom.removeValue();
                nouveauForm.removeValue();
                nouveauDoc.removeValue();
                Toast.makeText(getApplicationContext(), "Le service a été supprimé avec succés!", Toast.LENGTH_LONG).show();
                Intent return2=new Intent(EditService.this, EditServices.class);
                startActivity(return2);
                finish();
            }
        });
    }
}