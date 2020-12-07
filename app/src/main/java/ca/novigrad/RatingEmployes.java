package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class RatingEmployes extends AppCompatActivity {

    private ImageButton stark1, stark2, stark3, stark4, stark5;
    private double rate = 0;
    private boolean set1, set2,set3,set4,set5;

    private Button finish;
    private String branchID;
    private String userID;

    private FirebaseAuth fAuth;
    private DocumentReference documentReference;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_employes);
        Bundle bundle = getIntent().getExtras();
        branchID = bundle.getString("branchID");
        finish =findViewById(R.id.finishRating);
        stark2 = findViewById(R.id.btnStark2);
        stark3 = findViewById(R.id.btnStark3);
        stark4 = findViewById(R.id.btnStark4);
        stark5 = findViewById(R.id.btnStark5);
        stark1 = findViewById(R.id.btnStark1);
        set1 =false;
        set2 =false;
        set3 =false;
        set4 =false;
        set5 =false;

        stark1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set1) {
                    stark1.setBackgroundColor(Color.YELLOW);
                    set1 = true;

                }
                rate = 1 ;
                stark5.setBackgroundColor(Color.WHITE);
                stark4.setBackgroundColor(Color.WHITE);
                stark3.setBackgroundColor(Color.WHITE);
                stark2.setBackgroundColor(Color.WHITE);
                set2 = set3 = set4 = set5 = false;
            }
        });
        stark2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set2 ){
                    stark1.setBackgroundColor(Color.YELLOW);
                    stark2.setBackgroundColor(Color.YELLOW);
                    set1 = true;
                    set2 = true;

                }
                rate = 2;
                stark5.setBackgroundColor(Color.WHITE);
                stark4.setBackgroundColor(Color.WHITE);
                stark3.setBackgroundColor(Color.WHITE);
                set3 = set4 = set5 = false;
            }
        });
        stark3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set3 ){
                    stark1.setBackgroundColor(Color.YELLOW);
                    stark2.setBackgroundColor(Color.YELLOW);
                    stark3.setBackgroundColor(Color.YELLOW);
                    set1 = true;
                    set2 = true;
                    set3 = true;

                }
                rate = 3;
                stark5.setBackgroundColor(Color.WHITE);
                stark4.setBackgroundColor(Color.WHITE);
                set4 = set5 = false;
            }
        });
        stark4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set4 ){
                    stark4.setBackgroundColor(Color.YELLOW);
                    stark1.setBackgroundColor(Color.YELLOW);
                    stark2.setBackgroundColor(Color.YELLOW);
                    stark3.setBackgroundColor(Color.YELLOW);
                    set1 = true;
                    set2 = true;
                    set3 = true;
                    set4 = true;
                }
                rate = 4;
                stark5.setBackgroundColor(Color.WHITE);
                set5 = false;
            }
        });
        stark5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set5){
                    stark5.setBackgroundColor(Color.YELLOW);
                    stark4.setBackgroundColor(Color.YELLOW);
                    stark1.setBackgroundColor(Color.YELLOW);
                    stark2.setBackgroundColor(Color.YELLOW);
                    stark3.setBackgroundColor(Color.YELLOW);
                    set1 = true;
                    set2 = true;
                    set3 = true;
                    set4 = true;
                    set5 = true;
                }
                rate = 5;
            }
        });



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap map = new HashMap<>();

                final Intent intent = new Intent(RatingEmployes.this, GreetingActivity.class );
                intent.putExtra("userUID", userID);
                intent.putExtra("branchID", branchID);

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID);
                ref.child("rating").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String rating = snapshot.getValue(String.class);
                            if(rating.compareTo("No ratings yet") == 0){

                            }else{
                                rate = (rate + Double.parseDouble(rating.split("/")[0].trim()))/2 ;

                            }
                            map.put("rating",rate+"/5");
                            ref.updateChildren(map);

                            startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}