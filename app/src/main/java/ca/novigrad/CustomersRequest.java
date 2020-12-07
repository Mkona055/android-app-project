package ca.novigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomersRequest extends AppCompatActivity {
    private String branchID;
    private String userID;
    private ListView listView;
    private ArrayList<Request> requests;
    private ArrayList<String> requestsKeys;
    private RequestAdapter requestAdapter;
    private DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_request);
        final Bundle bundle = getIntent().getExtras();

        branchID = bundle.getString("branchID");
        userID = bundle.getString("userUID");
        requests = new ArrayList<>();
        requestsKeys = new ArrayList<>();
        requestAdapter = new RequestAdapter(this,R.layout.row_for_requests,requests);
        listView = findViewById(R.id.listViewRequests);
        db = FirebaseDatabase.getInstance().getReference("Branches").child(branchID).child("Requests");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Request request = dataSnapshot.getValue(Request.class);
                    if (!request.isMissingDocuments()){
                        requests.add(request);
                        requestsKeys.add(dataSnapshot.getKey());
                    }

                }
                if (! (requests.size() == 0)){
                    listView.setAdapter(requestAdapter);
                }else{
                    listView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CustomersRequest.this,ViewFormFilled.class);
                intent.putExtra("branchID",branchID);
                intent.putExtra("userID",userID);
                intent.putExtra("requestKey",requestsKeys.get(position));
                intent.putExtra("serviceRequested",requests.get(position).getServiceRequested());
                intent.putExtra("serviceSelectedKey",requests.get(position).getServiceSelectedKey());
                startActivity(intent);
            }
        });
    }
}