package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectServices extends AppCompatActivity {
    private DatabaseReference databaseReference ;
    private Button addButton;
    private ListView listViewServices;
    private ArrayList<Service> servicesInfo;
    private ArrayList<String> services;
    private ArrayAdapter<String> serviceAdapter;
    private String branchID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_services);

        Bundle bundle = getIntent().getExtras();

        // we will identify all services by their key.
        branchID = bundle.getString("branchID");
        // is used to store services information as Service object
        servicesInfo = new ArrayList<>();
        // is used to set up the listView using the serviceNames only
        services = new ArrayList<>();
        addButton = findViewById(R.id.buttonAddServicesSelected);
        listViewServices = findViewById(R.id.ListViewServiceToSelect);

        listViewServices.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // adapter for listView
        serviceAdapter = new ArrayAdapter<>(SelectServices.this, android.R.layout.simple_list_item_multiple_choice,services);
        databaseReference = FirebaseDatabase.getInstance().getReference("Services");


    }

    @Override
    protected void onStart() {
        super.onStart();

        //initializing the listVieW
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services.clear();
                servicesInfo.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Service service = postSnapshot.getValue(Service.class);
                    services.add(service.getServiceName());
                    servicesInfo.add(service);
                }
                listViewServices.setAdapter(serviceAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches").child(branchID);
                int countItems = servicesInfo.size();
                int count=0;
                SparseBooleanArray sBArray = listViewServices.getCheckedItemPositions();
                for(int i = 0 ; i < countItems ; i++){
                    if (sBArray.get(i)){
                        Service serviceSelected = servicesInfo.get(i);
                        HashMap map = new HashMap();
                        map.put("serviceOffered"+count ,serviceSelected.getServiceName());
                        count++;
                        db.child("servicesOffered").updateChildren(map);
                    }
                }
            }
        });
    }
}