package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewFormFilled extends AppCompatActivity {
    private ListView listView;
    private TextView title;
    private ImageView next;
    private ArrayList<Pair> formFilled;
    private FormFillingAdapter adapter;
    private String branchID;
    private String userID;
    private String requestKey;
    private String serviceRequested;
    private String serviceSelectedKey;
    private DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_form_filled);
        Bundle bundle = getIntent().getExtras();
        branchID = bundle.getString("branchID");
        userID = bundle.getString("userID");
        requestKey = bundle.getString("requestKey");
        serviceRequested = bundle.getString("serviceRequested");
        serviceSelectedKey = bundle.getString("serviceSelectedKey");
        listView = findViewById(R.id.listViewFormFilled);
        formFilled = new ArrayList<>();
        adapter = new FormFillingAdapter(this,R.layout.row_for_recycler_view,formFilled);
        title = findViewById(R.id.textViewFormFilledTitle);
        next = findViewById(R.id.imageViewMoveToDocumentsChecking);
        db = FirebaseDatabase.getInstance().getReference("Branches").child(branchID).child("Requests").child(requestKey).child("formFilled");
    }

    @Override
    protected void onStart() {
        super.onStart();
        title.setText(serviceRequested + " form");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewFormFilled.this,ViewDocumentFilled.class);
                intent.putExtra("branchID",branchID);
                intent.putExtra("userID",userID);
                intent.putExtra("requestKey",requestKey);
                intent.putExtra("serviceRequested",serviceRequested);
                intent.putExtra("serviceSelectedKey", serviceSelectedKey);
                startActivity(intent);
            }
        });
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String fieldAndFilling = dataSnapshot.getValue(String.class);
                    String[] split = fieldAndFilling.split("/");
                    Pair formField = new Pair (split[0],split[1]);
                    formFilled.add(formField);
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}