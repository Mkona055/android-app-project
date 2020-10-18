package ca.novigrad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MainActivity extends AppCompatActivity {


    TextView fullName, email, hisName, role, phoneNumber, employeeID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button logout;


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
        employeeID = findViewById(R.id.textViewEmailToFill);
        logout = findViewById(R.id.buttonLogout);

        userID = fAuth.getCurrentUser().getUid();


        //GET DATA FROM DATA BASE
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                fullName.setText(documentSnapshot.getString("FullName") + "");
                hisName.setText(documentSnapshot.getString("FullName") + "");
                email.setText(documentSnapshot.getString("Email") + "");
                phoneNumber.setText(documentSnapshot.getString("PhoneNumber") + "");
                role.setText(documentSnapshot.getString("Role") + "");
                if (documentSnapshot.getString("Role").equals("Employee")) {
                    employeeID.setVisibility(View.VISIBLE);
                    employeeID.setText(documentSnapshot.getString("IDEmployee") + "");
                }

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        });


    }
}


