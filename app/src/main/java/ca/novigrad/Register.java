package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {

    String TAG = "TAG";
    EditText fullName,username,email, phoneNumber, password, repeatPassword,employeeID;
    TextView login, loginAccount;
    TextView status;
    CheckBox employee, customer;
    Button register;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    String role;
    boolean isMatching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        fullName = findViewById(R.id.editTextFullname);
        username = findViewById(R.id.editTextUsername);
        email =  findViewById(R.id.editTextEmail);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        password = findViewById(R.id.editTextPassword);
        repeatPassword = findViewById(R.id.editTextRepeatPassword);

        login = findViewById(R.id.textLogin);
        loginAccount = findViewById(R.id.textLoginAccount);
        status =findViewById(R.id.textStatus);

        employee = findViewById(R.id.checkBoxEmployee);
        customer = findViewById(R.id.checkBoxCustomer);

        register = findViewById(R.id.buttonRegister);

        fAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBarRegister);
        fStore = FirebaseFirestore.getInstance();
        employeeID =findViewById(R.id.editTextEmployeeID);
        employeeID.setVisibility(View.GONE);

        isMatching =false;


        // if the user is already login
        if(fAuth.getCurrentUser()!= null){

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = "Employee";
                customer.setChecked(false);
                employeeID.setVisibility(View.VISIBLE);
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = "Customer";
               employee.setChecked(false);
               employeeID.setVisibility(View.GONE);
            }
        });


         // Registration process
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fStoreEmail = email.getText().toString().trim();
                String fStorePassword = password.getText().toString().trim();
                String repeatedPassword = repeatPassword.getText().toString().trim();
                final String fStoreFullName = fullName.getText().toString();
                final String userPhoneNumber = phoneNumber.getText().toString();




                if(TextUtils.isEmpty(fStoreEmail)){

                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(fStorePassword)){

                    password.setError("Password is required.");
                    return;
                }

                if(fStorePassword.length() <7){

                    password.setError("Password must be  seven(7) Characters minimum ");
                    return;
                }

                if(!fStorePassword.equals(repeatedPassword)){

                    repeatPassword.setError("The passwords do not not match");
                    return;
                }
                if (!employee.isChecked() && !customer.isChecked()){
                    employee.setError("Please select the type of account you would like to create");
                    customer.setError("Please select the type of account you would like to create");
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EmployeesID");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            EmployeeID idFromDataBase = snapshot.getValue(EmployeeID.class);
                            //Toast.makeText(Register.this,employeeID.getText().toString() + " " + idFromDataBase.getId() + " " + employeeID.getText().toString().compareTo(idFromDataBase.getId()) , Toast.LENGTH_LONG).show();
                            if (! idFromDataBase.isAttributed() && employeeID.getText().toString().compareTo(idFromDataBase.getId()) == 0){
                                isMatching =true;
                                break;
                            }

                        }
                        if(!isMatching) {
                            //Toast.makeText(Register.this, mEmployeesID.get(0), Toast.LENGTH_SHORT).show();
                            employeeID.setError("This ID is not in our database. Please contact the administrator of your branch to have a valid Employee ID");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                else{
//                    Query query  = FirebaseDatabase.getInstance().getReference("EmployeesID")
//                            .orderByChild("id")
//                            .equalTo(employeeID.getText().toString().trim());
//                    query.orderByValue().
//                }


                progressBar.setVisibility(View.VISIBLE);


                fAuth.createUserWithEmailAndPassword(fStoreEmail, fStorePassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);

                            Map<String, Object> user = new HashMap<>();
                           user.put("FullName ", fStoreFullName);
                           user.put("Email ", fStoreEmail);
                           user.put("PhoneNumber ",userPhoneNumber);
                           user.put("Role ", role);

                           if(role.equals("Employee")){
                               user.put("IDEmployee ", employeeID.getText().toString());
                           }

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for" + userID);
                                }
                            });



                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else{

                            Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


    }
}