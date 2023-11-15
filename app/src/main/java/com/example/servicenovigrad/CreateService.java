package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateService extends AppCompatActivity {
    EditText editTextNewNom;
    EditText editTextNewForm;
    EditText editTextNewDoc;
    Button btnCree;
    FirebaseDatabase base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        editTextNewDoc = (EditText) findViewById(R.id.editTextNewDoc);
        editTextNewNom = (EditText) findViewById(R.id.editTextNewNom);
        editTextNewForm = (EditText) findViewById(R.id.editTextNewForm);
        btnCree=(Button)findViewById(R.id.btnCree);
        btnCree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!TextUtils.isEmpty(editTextNewNom.getText().toString())
                        && !TextUtils.isEmpty(editTextNewForm.getText().toString()) &&
                        !TextUtils.isEmpty(editTextNewDoc.getText().toString())){
                    base = FirebaseDatabase.getInstance("https://novigrad-projet1-g09-default-rtdb.firebaseio.com");
                    DatabaseReference newNomRef = base.getReference("services/"+editTextNewNom.getText().toString()+"/nom");
                    DatabaseReference newFormRef = base.getReference("services/"+editTextNewNom.getText().toString()+"/form");
                    DatabaseReference newDocRef = base.getReference("services/"+editTextNewNom.getText().toString()+"/doc");
                    newNomRef.setValue(editTextNewNom.getText().toString());
                    newFormRef.setValue(editTextNewForm.getText().toString());
                    newDocRef.setValue(editTextNewDoc.getText().toString());
                    Toast.makeText(getApplicationContext(), "Le service a été crée avec succés!", Toast.LENGTH_LONG).show();
                    Intent return1=new Intent(CreateService.this,EditServices.class);
                    startActivity(return1);
                    finish();

                }
                else{
                    Toast.makeText(getApplicationContext(), "SVP remplissez tout les champs", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}