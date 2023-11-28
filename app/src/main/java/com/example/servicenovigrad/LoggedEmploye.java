package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoggedEmploye extends AppCompatActivity {
    TextView afficheNom;
    TextView txtSuccur;
    TextView txtNomSuccur;
    Button btnSuccurExiste;
    Button btnNoSuccur;
    String str; //String ou on va stocker le nom de l'employé
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_employe);
        afficheNom = (TextView) findViewById(R.id.afficheNom);
        txtSuccur = (TextView) findViewById(R.id.txtSuccur);
        txtNomSuccur = (TextView) findViewById(R.id.txtNomSuccur);
        btnSuccurExiste = (Button) findViewById(R.id.btnSuccurExiste);
        btnNoSuccur = (Button) findViewById(R.id.btnNoSuccur);
        Intent intent = getIntent();        //On recupere le nom de l'employe et on le met dans str pour pouvoir l'utiliser plus tard
        String str = intent.getStringExtra("nom");
        afficheNom.setText(str);
        //Apres avoir afficher le nom de l'employé, on vérifie si il travail pour une succursale
        //en deux tests, premiérement, on vérifie que dans la base de données le champ de "succur"
        //qui est le nom de la succursale ou travail un employé existe et n'est pas null/vide
        //Puis, on vérifie que la succursale n'a pas été supprimé par un administrateur
        //en vérifiant que le champ du nom de la succursale dans la partie "succursales" de notre
        //base de données existe
        //Vérification que l'employé est lié à une succursale:
        DatabaseReference base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference();
        DatabaseReference refSuccur = base.child("users").child(str).child("succursale");
        refSuccur.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String succur = dataSnapshot.getValue(String.class);
                if(succur==null || succur.equals("")){
                    txtSuccur.setText("Vous n'êtes présentement affilié à aucune succursale.");
                    txtNomSuccur.setText("");
                    btnNoSuccur.setVisibility(View.VISIBLE);
                    btnSuccurExiste.setVisibility(View.GONE);
                }
                else{
                    //Vérification que la succursale existe:
                    DatabaseReference base2 = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference();
                    DatabaseReference refSuccurNom = base.child("succursales").child(succur).child("nomSuccur");
                    refSuccurNom.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String nomSuccur = dataSnapshot.getValue(String.class);
                            if(nomSuccur==null || nomSuccur.equals("")){
                                txtSuccur.setText("Vous n'êtes présentement affilié à aucune succursale.");
                                txtNomSuccur.setText("");
                                btnNoSuccur.setVisibility(View.VISIBLE);
                                btnSuccurExiste.setVisibility(View.GONE);
                            }
                            else{
                                txtSuccur.setText("Vous êtes présentement un employé de la succursale :");
                                txtNomSuccur.setText(nomSuccur);
                                btnNoSuccur.setVisibility(View.GONE);
                                btnSuccurExiste.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        btnSuccurExiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent1 = new Intent(LoggedEmploye.this, ProfilSuccur.class);
                intent1.putExtra("nomSuccursale", txtNomSuccur.getText());
                intent1.putExtra("nomEmploye", str);
                startActivity(intent1);
                finish();
            }
        });
        btnNoSuccur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(LoggedEmploye.this, CreateSuccur.class);
                intent2.putExtra("nomEmploye", str);
                startActivity(intent2);
                finish();
            }
        });


    }
}