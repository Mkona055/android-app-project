package ca.novigrad;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
                }else{
                    stark1.setBackgroundColor(Color.WHITE);
                    set1 = false;
                }
            }
        });
        stark2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set2 && set1){
                    stark2.setBackgroundColor(Color.YELLOW);
                    set2 = true;
                }else{
                    stark2.setBackgroundColor(Color.WHITE);
                    set2 = false;
                }
            }
        });
        stark3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set3 && set1 && set2){
                    stark3.setBackgroundColor(Color.YELLOW);
                    set3 = true;
                }else{
                    stark3.setBackgroundColor(Color.WHITE);
                    set3 = false;
                }
            }
        });
        stark4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set4 && set1 && set2 && set3){
                    stark4.setBackgroundColor(Color.YELLOW);
                    set4 = true;
                }else{
                    stark4.setBackgroundColor(Color.WHITE);
                    set4 = false;
                }
            }
        });
        stark5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!set5 && set1 && set2 && set3 && set4){
                    stark5.setBackgroundColor(Color.YELLOW);
                    set5 = true;
                }else{
                    stark5.setBackgroundColor(Color.WHITE);
                    set5 = false;
                }
            }
        });



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap map = new HashMap<>();

                Intent intent = new Intent(RatingEmployes.this, MainActivity.class );
                intent.putExtra("userUID", userID);
                intent.putExtra("branchID", branchID);

                if (set5){
                    rate = 5;
                }else if(!set5 && set4){
                    rate =4;
                }else if(!set4 && set3){
                    rate =3;
                }else if(!set3 && set2){
                    rate =2;
                }else if(!set2 && set1){
                    rate =1;
                }
                map.put("Rate",rate);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID);
                ref.updateChildren(map);

                startActivity(intent);
            }
        });
    }
}