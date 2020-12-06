package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SelectServicesForCustomer extends AppCompatActivity {
    private DatabaseReference databaseReference ;
    private Button finishButton;
    private ListView listViewServices;
    private ArrayList<Service> servicesInfo;
    private ArrayList<String> servicesKey;
    private ArrayList<String> services;
    private ArrayList<String> servicesOffered;
    private ArrayAdapter<String> serviceAdapter;
    private String branchID;
    private String userID;
    private int nextIndex;
    private TextView noService;
    private DocumentReference documentReference ;
    private boolean deliverServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_services_for_customer);

        Bundle bundle = getIntent().getExtras();

        // we get the employee information from MainActivity
        branchID = bundle.getString("branchID");
        userID = bundle.getString("userUID");


        servicesOffered = new ArrayList<>();
        documentReference = FirebaseFirestore.getInstance().collection("users").document(userID);
        servicesInfo = new ArrayList<>();
        services = new ArrayList<>();
        servicesKey = new ArrayList<>();
        listViewServices = findViewById(R.id.ListViewServiceToSelectCustomer);

        // adapter for listView
        serviceAdapter = new ArrayAdapter<>(SelectServicesForCustomer.this, android.R.layout.simple_list_item_1,services);
        databaseReference = FirebaseDatabase.getInstance().getReference("Branches");

        // is used when the user does not select any service
        noService = findViewById(R.id.textViewNumberOfServicesSelected);
        noService.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // while initializing the listview we take the last key of the serviceOffered this key will be used to find the nextIndex of the next service we are looking to add
        databaseReference.child(branchID).child("servicesOffered").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                services.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String serviceOffered = dataSnapshot.getValue(String.class);
                    services.add(serviceOffered);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Services");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                servicesInfo.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Service service = dataSnapshot.getValue(Service.class);
                    for(String serviceName:services){
                        if (service.getServiceName().compareTo(serviceName)==0){
                            servicesInfo.add(service);
                            servicesKey.add(dataSnapshot.getKey());
                        }
                    }

                }
                services.clear();
                for(Service service : servicesInfo){
                    services.add(service.getServiceName());
                }
                listViewServices.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listViewServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectServicesForCustomer.this, FillForm.class);
                intent.putExtra("userUID", userID);
                intent.putExtra("branchID", branchID);
                intent.putExtra("serviceSelectedKey", servicesKey.get(position));
                intent.putExtra("serviceSelectedName", servicesInfo.get(position).getServiceName());
                startActivity(intent);
            }
        });
    }
}