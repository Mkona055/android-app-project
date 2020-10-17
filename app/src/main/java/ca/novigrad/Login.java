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

    EditText email, password;
    TextView register;
    Button login;
    FirebaseAuth fAuth;
    ProgressBar progressBar2;

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


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String theemail = email.getText().toString().trim();
                String thepassword = password.getText().toString().trim();

                //Field Validation start Here For LOGIN ACTIVITY
                if(TextUtils.isEmpty(theemail)){

                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(thepassword)){

                    password.setError("Password is required.");
                    return;
                }

                if(thepassword.length() <7){

                    password.setError("Password must be  seven(7) Characters minimum ");
                    return;
                }
                //Field Validation END Here for LOGIN ACTIVITY

                progressBar2.setVisibility(View.VISIBLE);


                // authenticate the user

                fAuth.signInWithEmailAndPassword(theemail, thepassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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