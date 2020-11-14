package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;

public class SelectServices extends AppCompatActivity {
    private DatabaseReference databaseReference ;
    private Button finishButton;
    private ListView listViewServices;
    private ArrayList<Service> servicesInfo;
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
        setContentView(R.layout.activity_select_services);

        Bundle bundle = getIntent().getExtras();

        // we get the employee information from MainActivity
        branchID = bundle.getString("branchID");
        deliverServices = bundle.getBoolean("deliverServices");
        userID = bundle.getString("userUID");

        if (deliverServices){
            nextIndex = bundle.getInt("nextIndex");
        }else{
            nextIndex = 0;
        }
        Log.d("TAG", String.valueOf(deliverServices));

        servicesOffered = new ArrayList<>();
        // a reference to the employee information in the Firestore
        documentReference = FirebaseFirestore.getInstance().collection("users").document(userID);
        // is used to store services information as Service object
        servicesInfo = new ArrayList<>();
        // is used to set up the listView using the serviceNames only
        services = new ArrayList<>();
        finishButton = findViewById(R.id.buttonFinishSelectingServices);

        listViewServices = findViewById(R.id.ListViewServiceToSelect);
        // enable multiple choice on the listView
        listViewServices.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // adapter for listView
        serviceAdapter = new ArrayAdapter<>(SelectServices.this, android.R.layout.simple_list_item_multiple_choice,services);
        databaseReference = FirebaseDatabase.getInstance().getReference("Services");

        // is used when the user does not select any service
        noService = findViewById(R.id.textViewNumberOfServicesSelected);
        noService.setVisibility(View.GONE);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //initializing the listVieW

        if(deliverServices){

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Branches").child(branchID).child("servicesOffered");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    servicesOffered.clear();
                    for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                        String offered = dataSnapshot.getValue(String.class);

                        servicesOffered.add(offered);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    services.clear();
                    servicesInfo.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        Service service = postSnapshot.getValue(Service.class);
                        for (int i = 0 ; i <servicesOffered.size();i++){
                            if(servicesOffered.get(i).compareTo(service.getServiceName())==0){
                                break;
                            }else if (i == servicesOffered.size()-1){
                                services.add(service.getServiceName());
                                servicesInfo.add(service);

                            }
                        }


                    }
                    listViewServices.setAdapter(serviceAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }else{
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
        }
        if (servicesInfo.size()==0){
            noService.setVisibility(View.VISIBLE);
            noService.setText("No service available");
        }




        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Branches").child(branchID);
                int countItems = servicesInfo.size();
                boolean noServiceSelected = true;

                // this array contains boolean and sBArray.get(i) = true means that the item at position i in the listView has been selected
                SparseBooleanArray sBArray = listViewServices.getCheckedItemPositions();

                for(int i = 0 ; i < countItems ; i++){
                    if (sBArray.get(i)){
                        Log.d("TASSSSSSS", String.valueOf(nextIndex));
                        Service serviceSelected = servicesInfo.get(i);
                        HashMap map = new HashMap();
                        map.put("serviceOffered"+nextIndex ,serviceSelected.getServiceName());
                        // we add to the database the service selected by the employee
                        db.child("servicesOffered").updateChildren(map);

                        nextIndex++;
                        noServiceSelected = false;
                    }
                }
                if(noServiceSelected){
                    if(servicesInfo.size() == 0){
                        Intent intent = new Intent(SelectServices.this,BranchActivity.class);
                        intent.putExtra("branchID",branchID);
                        intent.putExtra("userUID",userID);
                        startActivity(intent);
                        finish();
                    }else{
                        if(deliverServices){
                            Toast.makeText(getApplicationContext(),"No service added",Toast.LENGTH_LONG);
                            Intent intent = new Intent(SelectServices.this,BranchActivity.class);
                            intent.putExtra("branchID",branchID);
                            intent.putExtra("userUID",userID);
                            startActivity(intent);
                            finish();
                        }else {
                            noService.setVisibility(View.VISIBLE);
                        }
                    }

                }else{
                    if(!deliverServices) {
                        HashMap map = new HashMap();
                        map.put("DeliverServices", true);
                        documentReference.update(map);
                    }
                    Intent intent = new Intent(SelectServices.this,BranchActivity.class);
                    intent.putExtra("branchID",branchID);
                    intent.putExtra("userUID",userID);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}