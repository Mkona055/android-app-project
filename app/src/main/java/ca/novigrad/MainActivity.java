package ca.novigrad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MainActivity extends AppCompatActivity {


    private TextView fullName, email, hisName, role, phoneNumber, branchAddress,branchAddressNotToFill,branchNumber,branchNumberNotToFill;
    private FirebaseAuth fAuth;
    private DocumentReference documentReference;
    private FirebaseFirestore fStore;
    private String userID;
    private String branchID;
    private Button logout;
    private Button continueButton;
    private ImageView menutest;
    private boolean deliverServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//set reference to the Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
//set reference to  every component
        fullName = findViewById(R.id.textViewFullName);
        hisName = findViewById(R.id.textViewFullName1);
        role = findViewById(R.id.textViewRole);
        phoneNumber = findViewById(R.id.textViewPhoneNumbertoFill);
        email = findViewById(R.id.textViewEmailToFill);
        branchAddress = findViewById(R.id.textViewBranchAddress);
        branchAddressNotToFill = findViewById(R.id.textViewBranchAddressNotToFill);
        branchNumber = findViewById(R.id.textViewBranchNumber);
        branchNumberNotToFill =findViewById(R.id.textViewBranchNumberNotToFill);


        menutest = findViewById(R.id.glbmmenutest);

        logout = (Button) findViewById(R.id.buttonLogout);
        continueButton = findViewById(R.id.buttonContinue);

        userID = fAuth.getCurrentUser().getUid();
        deliverServices = false;

        //GET DATA FROM DATA BASE
        final Intent intent = new Intent(MainActivity.this,SelectServices.class);
        intent.putExtra("userUID",userID);

        documentReference = fStore.collection("users").document(userID);
        EventListener<DocumentSnapshot> eventListener = new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(fAuth.getCurrentUser()!= null){
                    fullName.setText(documentSnapshot.getString("FullName"));
                    hisName.setText(documentSnapshot.getString("FullName"));
                    email.setText(documentSnapshot.getString("Email"));
                    phoneNumber.setText(documentSnapshot.getString("PhoneNumber"));
                    role.setText(documentSnapshot.getString("Role"));

                    if (documentSnapshot.getString("Role").compareTo("Employee") == 0) {


                        branchAddress.setVisibility(View.VISIBLE);
                        branchAddress.setText(documentSnapshot.getString("BranchAddress"));
                        branchAddressNotToFill.setVisibility(View.VISIBLE);

                        branchNumber.setVisibility(View.VISIBLE);
                        branchNumber.setText(documentSnapshot.getString("BranchID"));
                        branchNumberNotToFill.setVisibility(View.VISIBLE);
                        branchID = documentSnapshot.getString("BranchID");
                        deliverServices = documentSnapshot.getBoolean("DeliverServices");


                    }else{
                        branchAddress.setVisibility(View.GONE);
                        branchAddressNotToFill.setVisibility(View.GONE);

                        branchNumber.setVisibility(View.GONE);
                        branchNumberNotToFill.setVisibility(View.GONE);
                    }

                }
            }
        };

        documentReference.addSnapshotListener(MainActivity.this, eventListener);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logout();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent (MainActivity.this, Login.class));
                finish();

            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role.getText().toString().compareTo("Administrator") == 0){
                    startActivity(new Intent (MainActivity.this, AdminManagement.class));
                }else if(role.getText().toString().compareTo("Employee") == 0){
                    if(!deliverServices){
                        intent.putExtra("branchID",branchID);
                        intent.putExtra("deliverServices",deliverServices);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MainActivity.this,BranchActivity.class);
                        intent.putExtra("branchID",branchID);
                        intent.putExtra("userUID",userID);
                        intent.putExtra("deliverServices",deliverServices);
                        startActivity(intent);
                    }

                }

            }
        });

        menutest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Manage.class));
            }
        });

    }


}


