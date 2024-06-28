package com.example.fuelbee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupCPass;

    private Button signupButton;
    private TextView loginRedirect;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.email);
        signupPassword = findViewById(R.id.password);
        signupCPass = findViewById(R.id.confirmPass);
        signupButton = findViewById(R.id.btn_register);
        loginRedirect = findViewById(R.id.yesAccount);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String confirmpass = signupCPass.getText().toString().trim();

                if (user.isEmpty()){
                    signupEmail.setError("Email cannot be empty ");
                }

                if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                }

                if(confirmpass.isEmpty()){

                    signupCPass.setError("Please confirm your password");
                }

                if (!pass.equals(confirmpass)) {
                    signupCPass.setError("Passwords do not match");

                }

                else{
                    auth.createUserWithEmailAndPassword(user,confirmpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task. isSuccessful()){
                                Toast.makeText(Registration.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registration.this, Login.class));
                            } else{
                                Toast.makeText(Registration.this, "Signup Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
    }
}
