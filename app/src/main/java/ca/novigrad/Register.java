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

public class Register extends AppCompatActivity {

    private String TAG = "TAG";
    private EditText fullName,email, phoneNumber, password, repeatPassword,branchAddress,branchID,customerAddress;
    private TextView login, loginAccount;
    private CheckBox employee, customer;
    private Button register;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;
    private FirebaseFirestore fStore;
    private String userID;
    private String role;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //reference to component of the register
        fullName = findViewById(R.id.editTextFullname);

        email =  findViewById(R.id.editTextEmail);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        password = findViewById(R.id.editTextPassword);
        repeatPassword = findViewById(R.id.editTextRepeatPassword);

        login = findViewById(R.id.textLogin);
        loginAccount = findViewById(R.id.textLoginAccount);

        employee = findViewById(R.id.checkBoxEmployee);
        customer = findViewById(R.id.checkBoxCustomer);

        register = findViewById(R.id.buttonRegister);
        //reference to the Firebase
        fAuth = FirebaseAuth.getInstance();


        progressBar = findViewById(R.id.progressBarRegister);
        fStore = FirebaseFirestore.getInstance();
        branchAddress =findViewById(R.id.editTextBranchAddress);
        branchAddress.setVisibility(View.GONE);
        branchID = findViewById(R.id.editTextBranchID);
        branchID.setVisibility(View.GONE);
        customerAddress = findViewById(R.id.editTextCustomerAddress);
        customerAddress.setVisibility(View.GONE);

        // if the user is already login
        if(fAuth.getCurrentUser()!= null){

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }
        //set what happen when we chose to be register like employee
        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = "Employee";
                customer.setChecked(false);
                branchAddress.setVisibility(View.VISIBLE);
                branchID.setVisibility(View.VISIBLE);
                customerAddress.setVisibility(View.GONE);

                if(!employee.isChecked() && !customer.isChecked()){
                    branchAddress.setVisibility(View.GONE);
                    branchID.setVisibility(View.GONE);
                    customerAddress.setVisibility(View.GONE);
                }
            }
        });
        //set what happen when we chose to be register like customer

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                role = "Customer";
                employee.setChecked(false);
                customerAddress.setVisibility(View.VISIBLE);
                branchAddress.setVisibility(View.GONE);
                branchID.setVisibility(View.GONE);
                if(!employee.isChecked() && !customer.isChecked()){
                    customerAddress.setVisibility(View.GONE);
                    branchAddress.setVisibility(View.GONE);
                    branchID.setVisibility(View.GONE);
                }
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
                final String bNumber = branchID.getText().toString().trim();
                final String bAddress = branchAddress.getText().toString().trim();
                final String cAddress = customerAddress.getText().toString().trim();

                if(!formIsValid(fStoreEmail, fStorePassword,repeatedPassword, fStoreFullName, userPhoneNumber,
                        bNumber,cAddress,bAddress)){
                    return;
                };

                if(role.compareTo("Employee")==0) {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Branches");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {

                        /* we have to verify if the address and the number are already inside our database
                        if not, we have to store them*/
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            boolean addressIsMatching = false;
                            boolean numberIsMatching = false;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Branch branch = snapshot.getValue(Branch.class);
                                if (branch.getBranchAddress().compareTo(bAddress) == 0) {
                                    addressIsMatching = true;
                                    break;
                                }
                                if (branch.getBranchID().compareTo(bNumber) == 0) {
                                    numberIsMatching = true;
                                    break;
                                }

                            }
                            if (addressIsMatching) {
                                branchAddress.setError("This branch address is already in our database");
                                return;

                            } else if (numberIsMatching) {
                                branchID.setError("This branch address is already in our database");
                                return;

                            } else {

                                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Branches");
                                HashMap map = new HashMap<>();

                                map.put("branchAddress", bAddress);
                                map.put("branchID", bNumber);

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


                //if all data entered by the user were correct, we stock them inside our FireStore and register the user
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
                            user.put("DeliverServices",false);

                           if(role.equals("Employee")){
                               user.put("BranchID", branchID.getText().toString());
                               user.put("BranchAddress", branchAddress.getText().toString());
                           }else{
                               user.put("Address",cAddress);
                           }

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for" + userID);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else{

                            Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        //the progress bar is just see when we finish the process of registration
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });


        //if we already have an account, we go to the login page to get inside our account
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


    }
    public boolean formIsValid(String fStoreEmail, String fStorePassword, String repeatedPassword, String fStoreFullName, String userPhoneNumber,
                               String bNumber, String cAddress, String bAddress){

        //Verification of the Full Name
        String regex = "^[a-zA-Z\\s]+";
        if(!fStoreFullName.matches(regex)){
            fullName.setError("The name section does not accept any special character and digits");
            return false;
        }
        //verification of the email
        // email incorrect format will be handled by the firebase while creating the account;
        if(TextUtils.isEmpty(fStoreEmail)){

            email.setError("Email is Required.");
            return false ;
        }

        // verification of the phone Number
        regex = "^\\d{10}$";
            if(!userPhoneNumber.matches(regex)){
                phoneNumber.setError("The phone number must take 10 digits ");
                return false;
            }


        //verification of the password
        if(TextUtils.isEmpty(fStorePassword)){

            password.setError("Password is required.");
            return false;
        }
        //verification of the password it must be more than 7 characters
        if(fStorePassword.length() <7){

            password.setError("Password must be  seven(7) characters minimum ");
            return false;
        }

        //the password has to be the same in both editText
        if(!fStorePassword.equals(repeatedPassword)){

            repeatPassword.setError("The passwords do not not match");
            return false;
        }

        //user has to chose which kind of account he wants to create
        if (!employee.isChecked() && !customer.isChecked()){
            employee.setError("Please select the type of account you would like to create");
            customer.setError("Please select the type of account you would like to create");
            return false;
        }

        //verify that the branch ID has 4 digits and the address is not empty
        if(role.compareTo("Employee") == 0) {
            if(TextUtils.isEmpty(bAddress)){
                branchAddress.setError("An address must be entered");
                return false;
            }
            regex = "^\\d{4}$";
            if(!bNumber.matches(regex)){
                branchID.setError("Please enter a 4 digits number");
                return false;
            }
        }else{
            // verify that the address field is not empty
            if (TextUtils.isEmpty(cAddress)){
                customerAddress.setError("An address must be entered");
                return false;
            }
        }
        return true;

    }

}