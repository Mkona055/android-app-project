package ca.novigrad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BranchActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> services;
    private ArrayList<String> schedule;
    private ArrayList<String> servicesOfferedID;
    private ArrayAdapter<String> serviceAdapter;
    private TextView mondayTime;
    private TextView tuesdayTime;
    private TextView wednesdayTime;
    private TextView thursdayTime;
    private TextView fridayTime;
    private TextView saturdayTime;
    private TextView sundayTime;
    private TextView addService;
    private TextView updateSchedule;
    private String branchID;
    private String userID;
    private int nextIndex;

    private Button logout;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        Bundle bundle = getIntent().getExtras();
        branchID = bundle.getString("branchID");
        userID = bundle.getString("userUID");
        listView = findViewById(R.id.listViewServicesBranch);
        services = new ArrayList<>();
        servicesOfferedID = new ArrayList<>();
        schedule = new ArrayList<>();
        serviceAdapter = new ArrayAdapter<>(BranchActivity.this,android.R.layout.simple_list_item_1,services);

        mondayTime = findViewById(R.id.textViewMondayTime);
        tuesdayTime = findViewById(R.id.textViewTuesdayTime);
        wednesdayTime = findViewById(R.id.textViewWednesdayTime);
        thursdayTime = findViewById(R.id.textViewThursdayTime);
        fridayTime = findViewById(R.id.textViewFridayTime);
        saturdayTime = findViewById(R.id.textViewSaturdayTime);
        sundayTime = findViewById(R.id.textViewSundayTime);
        addService = findViewById(R.id.textViewAddService);
        updateSchedule = findViewById(R.id.textViewUpdateSchedule);
        logout = findViewById(R.id.btnLogOut);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logout();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent (BranchActivity.this, Login.class));
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // while initializing the listview we take the last key of the serviceOffered this key will be used to find the nextIndex of the next service we are looking to add
        databaseReference.child("servicesOffered").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String extractLastKey = null;
                services.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String serviceOffered = dataSnapshot.getValue(String.class);
                    services.add(serviceOffered);
                    servicesOfferedID.add(dataSnapshot.getKey());
                    extractLastKey= dataSnapshot.getKey();
                }
                try{
                    // this index represent the next serviceOfferedIndex used in SelectServiceActivity
                    nextIndex = Integer.parseInt(extractLastKey.replaceAll("serviceOffered"," ").trim()) + 1 ;
                }catch (NumberFormatException e){}
                listView.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("schedule").addValueEventListener(new ValueEventListener() {
            // setting the schedule display
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DaySchedule daySchedule = dataSnapshot.getValue(DaySchedule.class);
                    setDaySchedule(daySchedule,schedule);

                }
                // the schedule array gathers all the schedules of each day sorted in alphabetical order
                mondayTime.setText(schedule.get(1));
                tuesdayTime.setText(schedule.get(5));
                wednesdayTime.setText(schedule.get(6));
                thursdayTime.setText(schedule.get(4));
                fridayTime.setText(schedule.get(0));
                saturdayTime.setText(schedule.get(2));
                sundayTime.setText(schedule.get(3));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BranchActivity.this,SelectServices.class);
                intent.putExtra("branchID",branchID);
                intent.putExtra("userUID",userID);
                intent.putExtra("deliverServices",true);
                intent.putExtra("nextIndex",nextIndex);
                startActivity(intent);
            }
        });

        updateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BranchActivity.this,Schedule.class);
                intent.putExtra("branchID",branchID);
                intent.putExtra("userUID",userID);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = new Intent(BranchActivity.this,CustomersRequest.class);
                intent.putExtra("branchID",branchID);
                intent.putExtra("userUID",userID);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String serviceOfferedID = servicesOfferedID.get(position);

                deleteDialog(serviceOfferedID);
                return true;
            }
        });

    }

    public void setDaySchedule(DaySchedule daySchedule, ArrayList schedule) {
        if(daySchedule.getStartingTime().contains("Closed") || daySchedule.getFinishingTime().contains("Closed")){
            schedule.add("Closed");
        }else if(daySchedule.getStartingTime().contains("Not defined yet") || daySchedule.getFinishingTime().contains("Not defined yet")){
            schedule.add("Not defined yet");
        }else{
            schedule.add(daySchedule.getStartingTime()+" to " + daySchedule.getFinishingTime());
        }

    }


    public void deleteDialog(final String serviceOfferedID ){


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_services_dialoge,null);

        dialogBuilder.setView(dialogView).setTitle("Delete service");
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                servicesOfferedID.remove(serviceOfferedID);
                DatabaseReference dbReference = databaseReference.child("servicesOffered").child(serviceOfferedID);
                dbReference.removeValue();
                serviceAdapter.notifyDataSetChanged();
            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.show();


    }




}