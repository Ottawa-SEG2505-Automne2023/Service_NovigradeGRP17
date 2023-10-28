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
    //Declaration des objets de l'activité
    Button register;
    Button login;
    TextView Welcome;
    TextView loginOrSignup;
    EditText username;
    EditText password;
    FirebaseDatabase baseDonnees;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialisation des objets d'instance
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        register=(Button) findViewById(R.id.register);
        login=(Button) findViewById(R.id.login);
        Welcome=(TextView) findViewById(R.id.Welcome);
        loginOrSignup =(TextView) findViewById(R.id.loginOrSignup);
        baseDonnees=FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com");
        //On click du bouton de connexion, qui lance une tentative de connexion avec le username et password dans les edit text
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(champNonVide(username.getText().toString()) && champNonVide(password.getText().toString())){
                    tentative(username.getText().toString(), password.getText().toString());
                }
                else{Toast.makeText(getApplicationContext(), "SVP remplissez tout les champs", Toast.LENGTH_LONG).show();}
            }
        });
        //On click du bouton d'inscription, qui envois directement à l'activité d'inscription
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent2);
            }
        });


    }
    public static boolean champNonVide(String texte){
        if (texte==null || texte.equals("")){
            return false;
        }
        else{return true;}
    }
    //methode appelee lors de la tentative de connexion, qui comparer si le mot de passe correspond a celui stocke dans la base
    //de donnees, et si c'est correspondant, cree un intent et passe le nom et le role de l'utilisateur en extra pour l'activité prochaine
    private void tentative(String username, String password){
        DatabaseReference refBase=FirebaseDatabase.getInstance().getReference();
        DatabaseReference refPass = refBase.child("users").child(username).child("mdp");
        DatabaseReference refRole = refBase.child("users").child(username).child("role");
        refPass.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pass = dataSnapshot.getValue(String.class);
                if(pass!=null && pass.equals(password)) {
                    refRole.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String role = dataSnapshot.getValue(String.class);
                            if(role.equals("Administrateur")){
                                Intent intent0 = new Intent(MainActivity.this, LoggedIn.class);
                                intent0.putExtra("nom", username);
                                startActivity(intent0);
                            }
                            else if(role.equals("Employe")) {
                                Intent intent1 = new Intent(MainActivity.this,LoggedIn.class);
                                intent1.putExtra("nom", username);
                                startActivity(intent1);
                            }
                            else{
                                Intent intent2 = new Intent(MainActivity.this,LoggedIn.class);
                                intent2.putExtra("nom", username);
                                startActivity(intent2);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else{Toast.makeText(getApplicationContext(),"Nom d'utilisateur/Mot de passe incorrect ou champs vides", Toast.LENGTH_LONG).show();}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }
}