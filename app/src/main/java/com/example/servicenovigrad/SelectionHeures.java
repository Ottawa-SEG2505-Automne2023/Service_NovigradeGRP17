package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class SelectionHeures extends AppCompatActivity {
    String nomClient;
    Button btnOuv;
    Button btnFer;
    Button btnRechercheHeure;
    int heureOuv;
    int minuteOuv;
    int heureFer;
    int minuteFer;
    String heureMinuteOuv;
    String heureMinuteFer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_heures);
        Intent intent = getIntent();
        nomClient = intent.getStringExtra("nomClient");
        btnOuv = (Button) findViewById(R.id.btnRechercheOuv);
        btnFer = (Button) findViewById(R.id.btnRechercheFer);
        btnRechercheHeure = (Button) findViewById(R.id.btnRechercheHeure);
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
                TimePickerDialog timePickerDialogOuv = new TimePickerDialog(SelectionHeures.this, AlertDialog.THEME_HOLO_DARK, onTimeSetListener, heureOuv, minuteOuv, true);
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
                TimePickerDialog timePickerDialogFer = new TimePickerDialog(SelectionHeures.this, AlertDialog.THEME_HOLO_DARK, onTimeSetListener, heureFer, minuteFer, true);
                timePickerDialogFer.setTitle("Sélectionnez l'heure à laquelle la succursale ferme");
                timePickerDialogFer.show();
            }
        });
        btnRechercheHeure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recherche = new Intent (SelectionHeures.this, RechercheHeures.class);
                recherche.putExtra("nomClient",nomClient);
                recherche.putExtra("heureOuv", heureOuv);
                recherche.putExtra("minuteOuv", minuteOuv);
                recherche.putExtra("heureFer", heureFer);
                recherche.putExtra("minuteFer",minuteFer);
                recherche.putExtra("heureMinuteOuv",heureMinuteOuv);
                recherche.putExtra("heureMinuteFer", heureMinuteFer);
                startActivity(recherche);
                finish();
            }
        });
    }
}