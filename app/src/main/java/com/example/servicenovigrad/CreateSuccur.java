package com.example.servicenovigrad;



import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateSuccur extends AppCompatActivity {
    String nomEmp; //String ou l'on stocke le nom de l'employe pour l'associer à la succursale
    EditText infoNom;
    EditText infoVille;
    EditText infoAddresse;
    EditText infoNum;
    Button btnOuv;
    Button btnFer;
    Button btnCreeSuccur;
    int heureOuv; //Heures d'ouvertures et fermeture de la succursale
    int minuteOuv;
    int heureFer;
    int minuteFer;
    String heureMinuteOuv;
    String heureMinuteFer;
    FirebaseDatabase base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_succur);
        Intent intent = getIntent();
        nomEmp = intent.getStringExtra("nomEmploye");
        infoNom = (EditText) findViewById(R.id.infoNom);
        infoVille = (EditText) findViewById(R.id.infoVille);
        infoAddresse = (EditText) findViewById(R.id.infoAddresse);
        infoNum = (EditText) findViewById(R.id.infoNum);
        btnOuv = (Button) findViewById(R.id.btnOuv);
        btnFer = (Button) findViewById(R.id.btnFer);
        btnCreeSuccur = (Button) findViewById(R.id.btnCreeSuccur);
        btnOuv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        heureOuv = i;
                        minuteOuv = i1;
                        heureMinuteOuv = String.valueOf(heureOuv)+":"+String.valueOf(minuteOuv);
                        btnOuv.setText(String.valueOf(heureOuv)+":"+String.valueOf(minuteOuv));
                    }
                };
                TimePickerDialog timePickerDialogOuv = new TimePickerDialog(CreateSuccur.this, AlertDialog.THEME_HOLO_DARK, onTimeSetListener, heureOuv, minuteOuv, true);
                timePickerDialogOuv.setTitle("Sélectionnez l'heure à laquelle la succursale ouvre");
                timePickerDialogOuv.show();
            }
        });
        btnFer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        heureFer = i;
                        minuteFer = i1;
                        heureMinuteFer = String.valueOf(heureFer)+":"+String.valueOf(minuteFer);
                        btnFer.setText(String.valueOf(heureFer)+":"+String.valueOf(minuteFer));
                    }
                };
                TimePickerDialog timePickerDialogFer = new TimePickerDialog(CreateSuccur.this, AlertDialog.THEME_HOLO_DARK, onTimeSetListener, heureFer, minuteFer, true);
                timePickerDialogFer.setTitle("Sélectionnez l'heure à laquelle la succursale ferme");
                timePickerDialogFer.show();
            }
        });
        btnCreeSuccur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EditSuccur.champsCorrects(infoNom.getText().toString(), infoVille.getText().toString(),infoAddresse.getText().toString(),infoNum.getText().toString())
                        && EditSuccur.heuresCorrectes(heureOuv,heureFer))
                {
                    base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com/");
                    DatabaseReference employeSuccurRef = base.getReference("users/"+nomEmp+"/succursale");
                    DatabaseReference nomSuccurRef = base.getReference("succursales/"+infoNom.getText().toString()+"/nomSuccur");
                    DatabaseReference villeSuccurRef = base.getReference("succursales/"+infoNom.getText().toString()+"/ville");
                    DatabaseReference addresseSuccurRef = base.getReference("succursales/"+infoNom.getText().toString()+"/addresse");
                    DatabaseReference numSuccurRef = base.getReference("succursales/"+infoNom.getText().toString()+"/num");
                    DatabaseReference heureMinuteOuvRef = base.getReference("succursales/"+infoNom.getText().toString()+"/heureMinuteOuv");
                    DatabaseReference heureMinuteFerRef = base.getReference("succursales/"+infoNom.getText().toString()+"/heureMinuteFer");
                    employeSuccurRef.setValue(infoNom.getText().toString());
                    nomSuccurRef.setValue(infoNom.getText().toString());
                    villeSuccurRef.setValue(infoVille.getText().toString());
                    addresseSuccurRef.setValue(infoAddresse.getText().toString());
                    numSuccurRef.setValue(infoNum.getText().toString());
                    heureMinuteOuvRef.setValue(heureMinuteOuv);
                    heureMinuteFerRef.setValue(String.valueOf(heureMinuteFer));
                    Toast.makeText(getApplicationContext(), "Compte de succursale crée avec succés! Reconnectez vous SVP.", Toast.LENGTH_LONG).show();
                    Intent retour=new Intent(CreateSuccur.this,LoggedEmploye.class);
                    retour.putExtra("nom",nomEmp);
                    startActivity(retour);
                    finish();
                }
                else if (EditSuccur.heuresCorrectes(heureOuv,heureFer) == false){
                    Toast.makeText(getApplicationContext(), "Vous avez rentré des horraires inccorects, une succursale doit être ouverte au moins 1 heure par jour. Réessayez SVP.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "SVP remplissez tout les champs", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}