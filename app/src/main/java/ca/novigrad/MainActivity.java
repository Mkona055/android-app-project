package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MainActivity extends AppCompatActivity {


    private TextView fullName, email, hisName, role, phoneNumber, employeeID,employeeIDNotToFill;
    private FirebaseAuth fAuth;
    private DocumentReference documentReference;
    private FirebaseFirestore fStore;
    private String userID;
    private Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.textViewFullName);
        hisName = findViewById(R.id.textViewFullName1);
        role = findViewById(R.id.textViewRole);
        phoneNumber = findViewById(R.id.textViewPhoneNumbertoFill);
        email = findViewById(R.id.textViewEmailToFill);
        employeeID = findViewById(R.id.textViewEmployeeIDtoFill);
        employeeIDNotToFill = findViewById(R.id.textViewEmployeeID);


        logout = (Button) findViewById(R.id.buttonLogout);

        userID = fAuth.getCurrentUser().getUid();
        //GET DATA FROM DATA BASE
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
                        employeeID.setVisibility(View.VISIBLE);
                        employeeID.setText(documentSnapshot.getString("IDEmployee"));
                    }else{
                        employeeID.setVisibility(View.GONE);
                        employeeIDNotToFill.setVisibility(View.GONE);
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

    }

}


