package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Déclaration des composants UI
    Button register;
    Button login;
    TextView Welcome;
    TextView loginOrSignup;
    EditText username;
    EditText password;
    // Déclaration de la base de données Firebase
    FirebaseDatabase baseDonnees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des composants UI
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        Welcome = (TextView) findViewById(R.id.Welcome);
        loginOrSignup = (TextView) findViewById(R.id.loginOrSignup);

        // Initialisation de la base de données Firebase
        baseDonnees = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com");

        // Gestion de l'événement de clic pour le bouton de connexion
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Vérification de la non-vacuité des champs
                if(champNonVide(username.getText().toString()) && champNonVide(password.getText().toString())){
                    // Tentative de connexion
                    tentative(username.getText().toString(), password.getText().toString());
                } else {
                    // Affichage d'un message si les champs sont vides
                    Toast.makeText(getApplicationContext(), "SVP remplissez tout les champs", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Gestion de l'événement de clic pour le bouton d'inscription
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Transition vers l'activité d'inscription
                Intent intent2 = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent2);
            }
        });
    }

    // Méthode pour vérifier si un texte est non vide
    public static boolean champNonVide(String texte){
        if (texte == null || texte.equals("")){
            return false;
        } else {
            return true;
        }
    }

    // Méthode pour gérer la tentative de connexion
    private void tentative(String username, String password){
        DatabaseReference refBase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference refPass = refBase.child("users").child(username).child("mdp");
        DatabaseReference refRole = refBase.child("users").child(username).child("role");

        // Vérification du mot de passe dans la base de données
        refPass.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pass = dataSnapshot.getValue(String.class);
                if (pass != null && pass.equals(password)) {
                    // Si le mot de passe est correct, récupération du rôle
                    refRole.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String role = dataSnapshot.getValue(String.class);
                            // En fonction du rôle, redirection vers l'activité appropriée
                            if (role.equals("Administrateur")) {
                                Intent intent0 = new Intent(MainActivity.this, LoggedAdmin.class);
                                intent0.putExtra("nom", username);
                                startActivity(intent0);
                            } else if (role.equals("Employe")) {
                                Intent intent1 = new Intent(MainActivity.this, LoggedEmploye.class);
                                intent1.putExtra("nom", username);
                                startActivity(intent1);
                            } else {
                                Intent intent2 = new Intent(MainActivity.this, LoggedClient.class);
                                intent2.putExtra("nom", username);
                                startActivity(intent2);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Gestion de l'erreur si nécessaire
                        }
                    });
                } else {
                    // Si le mot de passe est incorrect, affichage d'un message d'erreur
                    Toast.makeText(getApplicationContext(), "Nom d'utilisateur/Mot de passe incorrect ou champs vides", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gestion de l'erreur si nécessaire
            }
        });
    }
}
