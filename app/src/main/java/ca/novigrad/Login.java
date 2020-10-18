package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private TextView register;
    private Button login;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email =  findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);

        register = findViewById(R.id.textRegister);

        login = findViewById(R.id.buttonLogin);

        fAuth = FirebaseAuth.getInstance();

        progressBar2 = findViewById(R.id.progressBarLogin);



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser()!= null){

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String verEmail = email.getText().toString().trim();
                String verPassword = password.getText().toString().trim();

                //Field Validation start Here For LOGIN ACTIVITY
                if(TextUtils.isEmpty(verEmail)){

                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(verPassword)){

                    password.setError("Password is required.");
                    return;
                }

                if(verPassword.length() <7){

                    password.setError("Password must be  seven(7) Characters minimum ");
                    return;
                }
                //Field Validation END Here for LOGIN ACTIVITY

                progressBar2.setVisibility(View.VISIBLE);


                // authenticate the user

                fAuth.signInWithEmailAndPassword(verEmail, verPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else{

                            Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }

                    }
                });

            }

        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

    }
}