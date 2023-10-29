// The main package for the application
package com.example.servicenovigrad;

// Importing necessary libraries and modules
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.*;

// Register activity class, for user registration
public class RegisterActivity extends AppCompatActivity {

    // UI elements and Firebase database reference declaration
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

        // Initializing UI elements
        newUsername = (EditText) findViewById(R.id.newUsername);
        newPassword = (EditText) findViewById(R.id.newPassword);
        email = (EditText) findViewById(R.id.email);
        client = (Button) findViewById(R.id.client);
        employe = (Button) findViewById(R.id.employe);

        // Setting up onClick listener for the client registration button
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validating form data before registration
                if (!TextUtils.isEmpty(newUsername.getText().toString())
                        && !TextUtils.isEmpty(newPassword.getText().toString())
                        && !TextUtils.isEmpty(email.getText().toString())
                        && estEmail(email.getText().toString())) {

                    // Firebase database instance and reference setup
                    base = FirebaseDatabase.getInstance("https://servicenovigrad-9d027-default-rtdb.firebaseio.com");
                    DatabaseReference newUserNameRef = base.getReference("users/" + newUsername.getText().toString() + "/name");
                    DatabaseReference newUserPassRef = base.getReference("users/" + newUsername.getText().toString() + "/mdp");
                    DatabaseReference newUserRoleRef = base.getReference("users/" + newUsername.getText().toString() + "/role");
                    DatabaseReference newUserEmailRef = base.getReference("users/" + newUsername.getText().toString() + "/email");

                    // Setting values in Firebase database
                    newUserNameRef.setValue(newUsername.getText().toString());
                    newUserPassRef.setValue(newPassword.getText().toString());
                    newUserRoleRef.setValue("Client");
                    newUserEmailRef.setValue(email.getText().toString());

                    // Redirecting to the main activity after successful registration
                    Intent return1 = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(return1);
                    finish();

                } else {
                    // Showing error toast messages based on the validation
                    if (!estEmail(email.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "SVP rentrez une adresse mail correcte", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "SVP remplissez tout les champs", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // Setting up onClick listener for the employee registration button (Similar logic as the client registration)
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

    // Utility function to validate email format
    public static boolean estEmail(String email) {
        if (!email.contains("@") || !email.contains(".")) {
            return false;
        } else {
            int indexAt = -1;
            int indexDot = -1;
            for (int i = 0; i < email.length(); i++) {
                if ((email.charAt(i) == '@' && indexAt != -1) || (email.charAt(i) == '.' && indexDot != -1)) {
                    return false;
                } else if (email.charAt(i) == '@' && indexAt == -1) {
                    indexAt = i;
                } else if (email.charAt(i) == '.' && indexDot == -1) {
                    indexDot = i;
                }
            }
            if (indexAt < indexDot) {
                return true;
            } else {
                return false;
            }
        }
    }
}
