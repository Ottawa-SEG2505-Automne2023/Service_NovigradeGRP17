package com.example.servicenovigrad;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditSuccur extends AppCompatActivity {
    String nomEmp; //String ou l'on stocke le nom de l'employe pour l'associer à la succursale
    String nomSucc; //String ou l'on stocke le nom de la succursale pour acceder aux valeurs dans la base de donnees firebase
    EditText infoModifNom;
    EditText infoModifVille;
    EditText infoModifAddresse;
    EditText infoModifNum;
    Button btnModifOuv;
    Button btnModifFer;
    Button btnModifSuccur;
    int heureOuv; //Heures d'ouvertures et fermeture de la succursale
    int minuteOuv;
    int heureFer;
    int minuteFer;
    FirebaseDatabase base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_succur);
        Intent intent = getIntent();
        nomEmp = intent.getStringExtra("nomEmploye");
        nomSucc = intent.getStringExtra("nomSuccursale");
        infoModifNom = (EditText) findViewById(R.id.infoModifNom);
        infoModifVille = (EditText) findViewById(R.id.infoModifVille);
        infoModifAddresse = (EditText) findViewById(R.id.infoModifAddresse);
        infoModifNum = (EditText) findViewById(R.id.infoModifNum);
        btnModifOuv = (Button) findViewById(R.id.btnModifOuv);
        btnModifFer = (Button) findViewById(R.id.btnModifFer);
        btnModifSuccur = (Button) findViewById(R.id.btnModifSuccur);
        heureOuv=0;
        heureFer=0;
        DatabaseReference refBase=FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/").getReference();
        DatabaseReference refNom = refBase.child("succursales").child(nomSucc).child("nomSuccur");
        refNom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nom = dataSnapshot.getValue(String.class);
                infoModifNom.setText(nom);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference refVille = refBase.child("succursales").child(nomSucc).child("ville");
        refVille.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ville = dataSnapshot.getValue(String.class);
                infoModifVille.setText(ville);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference refAddresse = refBase.child("succursales").child(nomSucc).child("addresse");
        refAddresse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String addresse = dataSnapshot.getValue(String.class);
                infoModifAddresse.setText(addresse);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference refNum = refBase.child("succursales").child(nomSucc).child("num");
        refNum.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String num = dataSnapshot.getValue(String.class);
                infoModifNum.setText(num);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference refHeureOuv = refBase.child("succursales").child(nomSucc).child("heureMinuteOuv");
        refHeureOuv.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String heureMinute = dataSnapshot.getValue(String.class);
                String tmp = "";   //String temporaire pour stocker les heures et minute d'ouverture pour donner des valeurs à heureOuv et minuteOuV
                for(int i=0; i<heureMinute.length(); i++){
                    String chara = Character.toString(heureMinute.charAt(i));
                    if(chara.equals(":")){
                        heureOuv=Integer.parseInt(tmp);
                        tmp="";
                    }
                    else if(i==heureMinute.length()-1&&!tmp.equals("")){
                        minuteOuv=Integer.parseInt(tmp);
                    }
                    else{
                        tmp=tmp+chara;
                    }
                }
                btnModifOuv.setText(heureMinute);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference refHeureFer = refBase.child("succursales").child(nomSucc).child("heureMinuteFer");
        refHeureFer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String heureMinute1 = dataSnapshot.getValue(String.class);
                String tmp = "";   //String temporaire pour stocker les heures et minute d'ouverture pour donner des valeurs à heureOuv et minuteOuV
                for(int i=0; i<heureMinute1.length(); i++){
                    String chara = Character.toString(heureMinute1.charAt(i));
                    if(chara.equals(":")){
                        heureFer=Integer.parseInt(tmp);
                        tmp="";
                    }
                    else if(i==heureMinute1.length()-1&&!tmp.equals("")){
                        minuteFer=Integer.parseInt(tmp);
                    }
                    else{
                        tmp=tmp+chara;
                    }
                }
                btnModifFer.setText(heureMinute1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        btnModifOuv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        heureOuv = i;
                        minuteOuv = i1;
                        btnModifOuv.setText(String.valueOf(heureOuv)+":"+String.valueOf(minuteOuv));
                    }
                };
                TimePickerDialog timePickerDialogOuv = new TimePickerDialog(EditSuccur.this, AlertDialog.THEME_HOLO_DARK, onTimeSetListener, heureOuv, minuteOuv, true);
                timePickerDialogOuv.setTitle("Sélectionnez l'heure à laquelle la succursale ouvre");
                timePickerDialogOuv.show();
            }
        });
        btnModifFer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        heureFer = i;
                        minuteFer = i1;
                        btnModifFer.setText(String.valueOf(heureFer)+":"+String.valueOf(minuteFer));
                    }
                };
                TimePickerDialog timePickerDialogFer = new TimePickerDialog(EditSuccur.this, AlertDialog.THEME_HOLO_DARK, onTimeSetListener, heureFer, minuteFer, true);
                timePickerDialogFer.setTitle("Sélectionnez l'heure à laquelle la succursale ferme");
                timePickerDialogFer.show();
            }
        });
        btnModifSuccur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(champsCorrects(infoModifNom.getText().toString(), infoModifVille.getText().toString(),infoModifAddresse.getText().toString(),infoModifNum.getText().toString())
                        && heuresCorrectes(heureOuv,heureFer)){
                    base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com");
                    DatabaseReference nomRef = base.getReference("succursales/"+nomSucc+"/nomSuccur");
                    DatabaseReference villeRef = base.getReference("succursales/"+nomSucc+"/ville");
                    DatabaseReference addresseRef = base.getReference("succursales/"+nomSucc+"/addresse");
                    DatabaseReference numRef = base.getReference("succursales/"+nomSucc+"/num");
                    DatabaseReference heureMinuteOuvRef = base.getReference("succursales/"+nomSucc+"/heureMinuteOuv");
                    DatabaseReference heureMinuteFerRef = base.getReference("succursales/"+nomSucc+"/heureMinuteFer");
                    DatabaseReference baseServices = FirebaseDatabase.getInstance("https://novigrad-projet1-g09-default-rtdb.firebaseio.com").getReference().child("succursales").child(nomSucc);
                    baseServices.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                if(!ds.getKey().equals("nomSuccur") && !ds.getKey().equals("ville") && !ds.getKey().equals("addresse")
                                        && !ds.getKey().equals("num") && !ds.getKey().equals("heureMinuteOuv") && !ds.getKey().equals("heureMinuteFer")){
                                    DatabaseReference newService = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference("succursales/"+infoModifNom.getText().toString()+"/services/"+ds.getValue(String.class));

                                    newService.setValue(ds.getValue(String.class));
                                    DatabaseReference baseDemandes = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference().child("demandes").child(nomSucc);
                                    baseDemandes.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            for(DataSnapshot ds2 : snapshot.getChildren()) {
                                                DatabaseReference base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference();
                                                DatabaseReference newrefNomClient = base.child("demandes").child(infoModifNom.getText().toString()).child(ds2.getValue(String.class)).child("nomClient");
                                                DatabaseReference newrefFormClient = base.child("demandes").child(infoModifNom.getText().toString()).child(ds2.getValue(String.class)).child("formClient");
                                                DatabaseReference newrefDocClient = base.child("demandes").child(infoModifNom.getText().toString()).child(ds2.getValue(String.class)).child("docClient");
                                                DatabaseReference newrefNomService = base.child("demandes").child(infoModifNom.getText().toString()).child(ds2.getValue(String.class)).child("nomService");
                                                DatabaseReference oldrefNomClient = base.child("demandes").child(nomSucc).child(ds2.getValue(String.class)).child("nomClient");
                                                DatabaseReference oldrefFormClient = base.child("demandes").child(nomSucc).child(ds2.getValue(String.class)).child("formClient");
                                                DatabaseReference oldrefDocClient = base.child("demandes").child(nomSucc).child(ds2.getValue(String.class)).child("docClient");
                                                DatabaseReference oldrefNomService = base.child("demandes").child(nomSucc).child(ds2.getValue(String.class)).child("nomService");
                                                oldrefNomClient.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String value = snapshot.getValue(String.class);
                                                        newrefNomClient.setValue(value);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                oldrefFormClient.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String value = snapshot.getValue(String.class);
                                                        newrefFormClient.setValue(value);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                oldrefDocClient.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String value = snapshot.getValue(String.class);
                                                        newrefDocClient.setValue(value);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                oldrefNomService.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String value = snapshot.getValue(String.class);
                                                        newrefNomService.setValue(value);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                oldrefNomClient.removeValue();
                                                oldrefFormClient.removeValue();
                                                oldrefDocClient.removeValue();
                                                oldrefNomService.removeValue();
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
                                    DatabaseReference oldService = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com").getReference("succursales/"+nomSucc+"/services/"+ds.getValue(String.class));
                                    oldService.removeValue();
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
                    nomRef.removeValue();
                    villeRef.removeValue();
                    addresseRef.removeValue();
                    numRef.removeValue();
                    heureMinuteOuvRef.removeValue();
                    heureMinuteFerRef.removeValue();
                    DatabaseReference employeSuccurRef = base.getReference("users/"+nomEmp+"/succursale");
                    DatabaseReference nomSuccurRef = base.getReference("succursales/"+infoModifNom.getText().toString()+"/nomSuccur");
                    DatabaseReference villeSuccurRef = base.getReference("succursales/"+infoModifNom.getText().toString()+"/ville");
                    DatabaseReference addresseSuccurRef = base.getReference("succursales/"+infoModifNom.getText().toString()+"/addresse");
                    DatabaseReference numSuccurRef = base.getReference("succursales/"+infoModifNom.getText().toString()+"/num");
                    DatabaseReference heureMinuteModifOuvRef = base.getReference("succursales/"+infoModifNom.getText().toString()+"/heureMinuteOuv");
                    DatabaseReference heureMinuteModifFerRef = base.getReference("succursales/"+infoModifNom.getText().toString()+"/heureMinuteFer");
                    employeSuccurRef.setValue(infoModifNom.getText().toString());
                    nomSuccurRef.setValue(infoModifNom.getText().toString());
                    villeSuccurRef.setValue(infoModifVille.getText().toString());
                    addresseSuccurRef.setValue(infoModifAddresse.getText().toString());
                    numSuccurRef.setValue(infoModifNum.getText().toString());
                    heureMinuteModifOuvRef.setValue(btnModifOuv.getText().toString());
                    heureMinuteModifFerRef.setValue(btnModifFer.getText().toString());
                    Toast.makeText(getApplicationContext(), "Informations sur la succursale modifiées avec succés !", Toast.LENGTH_LONG).show();
                    Intent retour=new Intent(EditSuccur.this,LoggedEmploye.class);
                    retour.putExtra("nom",nomEmp);
                    startActivity(retour);
                    finish();
                }
                else{
                    if(!heuresCorrectes(heureOuv, heureFer)){
                        Toast.makeText(getApplicationContext(), "Vous avez rentré des horraires inccorects, une succursale doit être ouverte au moins 1 heure par jour. Réessayez SVP.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Vous avez rentré des informations incorrectes, SVP revoyez le format des informations.", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }
    public static boolean champsCorrects(String nom, String ville, String addresse, String num){
        if(nom==null || nom.equals("") ){
            return false;
        }
        else if(ville==null || ville.equals("") ){
            return false;
        }
        else if(addresse==null || addresse.equals("")){
            return false;
        }
        else if(num==null || num.equals("") ){
            return false;
        }
        else{
            return true;
        }
    }
    public static boolean heuresCorrectes(int heureOuv, int heureFer){
        if (heureOuv==heureFer){
            return false;
        }
        else{
            return true;
        }
    }
}