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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Register extends AppCompatActivity {

    String TAG = "TAG";
    EditText fullName,email, phoneNumber, password, repeatPassword,branchAddress,branchNumber;
    TextView login, loginAccount;
    TextView status;
    CheckBox employee, customer;
    Button register;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    String role;
    String branchKey;

    boolean addressIsMatching;
    boolean numberIsMatching;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = findViewById(R.id.editTextFullname);

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
        branchAddress =findViewById(R.id.editTextBranchAddress);
        branchAddress.setVisibility(View.GONE);
        branchNumber = findViewById(R.id.editTextBranchNumber);
        branchNumber.setVisibility(View.GONE);

        addressIsMatching = numberIsMatching  = false;



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
                branchAddress.setVisibility(View.VISIBLE);
                branchNumber.setVisibility(View.VISIBLE);
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = "Customer";
                employee.setChecked(false);
                branchAddress.setVisibility(View.GONE);
                branchNumber.setVisibility(View.GONE);
            }
        });


         // Registration process
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fStoreEmail = email.getText().toString().trim();
                String fStorePassword = password.getText().toString().trim();
                String repeatedPassword = repeatPassword.getText().toString().trim();
                final String fStoreFullName = fullName.getText().toString().trim();
                final String userPhoneNumber = phoneNumber.getText().toString().trim();
                final String bNumber = branchNumber.getText().toString().trim();
                final String bAddress = branchAddress.getText().toString().trim();



                if(TextUtils.isEmpty(fStoreEmail)){

                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(fStorePassword)){

                    password.setError("Password is required.");
                    return;
                }
                //check the name
                for (char x: fStoreFullName.toCharArray()){
                    Pattern pattern = Pattern.compile(x+"", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher("a b c d e f g h i j k l m n o p k r s t u v w x y z");
                    boolean result = matcher.find();
                    if(!result){
                        fullName.setError("The name can not contain a digit and a special character");
                        return;
                    }
                }


                //verify the phone number
                for (char x: userPhoneNumber.toCharArray()){
                    Pattern pattern = Pattern.compile(x+"", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(" 0 1 2 3 4 5 6 7 8 9");
                    boolean result = matcher.find();
                    if(!result || userPhoneNumber.length() != 10){
                        phoneNumber.setError("The phone number must take 10 digits");

                        return;
                    }
                }


                if(fStorePassword.length() <7){

                    password.setError("Password must be  seven(7) characters minimum ");
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

                if(role.compareTo("Employee")==0) {
                    for (char x : userPhoneNumber.toCharArray()) {
                        Pattern pattern = Pattern.compile(x + "", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(" 0 1 2 3 4 5 6 7 8 9");
                        boolean result = matcher.find();
                        if (!result || userPhoneNumber.length() != 10) {
                            branchNumber.setError("The phone number must take 10 digits");
                            return;
                        }
                    }

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Branches");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Branch branch = snapshot.getValue(Branch.class);
                                if (branch.getBranchAddress().compareTo(bAddress) == 0) {
                                    addressIsMatching = true;
                                    break;
                                }
                                if (branch.getBranchNumber().compareTo(bAddress) == 0) {
                                    numberIsMatching = true;
                                    break;
                                }

                            }
                            if (addressIsMatching) {
                                branchAddress.setError("This branch address is already in our database");
                                return;

                            } else if (numberIsMatching) {
                                branchAddress.setError("This branch address is already in our database");
                                return;

                            } else {

                                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Branches");
                                HashMap map = new HashMap<>();

                                map.put("BranchAddress", bAddress);
                                map.put("BranchNumber", bNumber);

                                dr.child(bNumber).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                progressBar.setVisibility(View.VISIBLE);


                fAuth.createUserWithEmailAndPassword(fStoreEmail, fStorePassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);

                            Map<String, Object> user = new HashMap<>();
                            user.put("FullName", fStoreFullName);
                            user.put("Email", fStoreEmail);
                            user.put("PhoneNumber",userPhoneNumber);
                            user.put("Role", role);

                           if(role.equals("Employee")){
                               user.put("BranchNumber", branchNumber.getText().toString());
                               user.put("BranchAddress", branchAddress.getText().toString());
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