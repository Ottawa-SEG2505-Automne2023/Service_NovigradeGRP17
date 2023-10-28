package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class RegisterActivity extends AppCompatActivity {
    Button client;
    Button employe;
    EditText newUsername;
    EditText newPassword;
    EditText email;
    FirebaseDatabase base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //initialisation des objets d'instance
        newUsername=(EditText) findViewById(R.id.newUsername);
        newPassword=(EditText) findViewById(R.id.newPassword);
        email=(EditText) findViewById(R.id.email);
        client=(Button) findViewById(R.id.client);
        employe=(Button) findViewById(R.id.employe);
        //On click du boutton de création de compte client, ou les infos, apres vérification sont rentrés dans la base de données
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!TextUtils.isEmpty(newUsername.getText().toString())
                        && !TextUtils.isEmpty(newPassword.getText().toString()) &&
                        !TextUtils.isEmpty(email.getText().toString()) && estEmail(email.getText().toString()) ){
                    base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com");
                    DatabaseReference newUserNameRef = base.getReference("users/"+newUsername.getText().toString()+"/name");
                    DatabaseReference newUserPassRef = base.getReference("users/"+newUsername.getText().toString()+"/mdp");
                    DatabaseReference newUserRoleRef = base.getReference("users/"+newUsername.getText().toString()+"/role");
                    DatabaseReference newUserEmailRef = base.getReference("users/"+newUsername.getText().toString()+"/email");
                    newUserNameRef.setValue(newUsername.getText().toString());
                    newUserPassRef.setValue(newPassword.getText().toString());
                    newUserRoleRef.setValue("Client");
                    newUserEmailRef.setValue(email.getText().toString());
                    Intent return1=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(return1);
                    finish();

                }
                else{
                    if(!estEmail(email.getText().toString())){
                        Toast.makeText(getApplicationContext(), "SVP rentrez une adresse mail correcte", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "SVP remplissez tout les champs", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        //On click du boutton de création de compte employe, ou les infos, apres vérification sont rentrés dans la base de données
        employe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!TextUtils.isEmpty(newUsername.getText().toString())
                        && !TextUtils.isEmpty(newPassword.getText().toString()) &&
                        !TextUtils.isEmpty(email.getText().toString()) && estEmail(email.getText().toString()) ){
                    base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com");
                    DatabaseReference newUserNameRef = base.getReference("users/"+newUsername.getText().toString()+"/name");
                    DatabaseReference newUserPassRef = base.getReference("users/"+newUsername.getText().toString()+"/mdp");
                    DatabaseReference newUserRoleRef = base.getReference("users/"+newUsername.getText().toString()+"/role");
                    DatabaseReference newUserEmailRef = base.getReference("users/"+newUsername.getText().toString()+"/email");
                    newUserNameRef.setValue(newUsername.getText().toString());
                    newUserPassRef.setValue(newPassword.getText().toString());
                    newUserRoleRef.setValue("Employe");
                    newUserEmailRef.setValue(email.getText().toString());
                    Intent return2=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(return2);
                    finish();

                }
                else{
                    if(!estEmail(email.getText().toString())){
                        Toast.makeText(getApplicationContext(), "SVP rentrez une adresse mail correcte", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "SVP remplissez tout les champs", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public static boolean estEmail(String email){
        if (!email.contains("@") || !email.contains(".")) {
            return false;
        } else {
            int indexAt=-1;
            int indexDot=-1;
            for (int i=0; i<email.length(); i++){
                if((email.charAt(i)=='@'&&indexAt!=-1) || (email.charAt(i)=='.'&&indexDot!=-1)){
                    return false;
                }
                else if (email.charAt(i)=='@'&&indexAt==-1){
                    indexAt=i;
                }
                else if (email.charAt(i)=='.'&&indexDot==-1){
                    indexDot=i;
                }
            }
            if(indexAt<indexDot){
                return true;
            }
            else{
                return false;
            }
        }
    }


}
