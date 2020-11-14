package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Schedule extends AppCompatActivity {
    private Spinner startingTimeMonday;
    private Spinner startingTimeTuesday;
    private Spinner startingTimeWednesday;
    private Spinner startingTimeThursday;
    private Spinner startingTimeFriday;
    private Spinner startingTimeSaturday;
    private Spinner startingTimeSunday;
    private Spinner finishingTimeMonday;
    private Spinner finishingTimeTuesday;
    private Spinner finishingTimeWednesday;
    private Spinner finishingTimeThursday;
    private Spinner finishingTimeFriday;
    private Spinner finishingTimeSaturday;
    private Spinner finishingTimeSunday;
    private Button finish;
    private String branchID;
    private String userID;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // we get the branch ID from the previous activity
        Bundle bundle = getIntent().getExtras();
        branchID = bundle.getString("branchID");
        userID = bundle.getString("userUID");
        // reference from the database of the branch schedule
        db = FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID).child("schedule");

        // initializing the spinners to select the starting time of each day
        startingTimeMonday = findViewById(R.id.spinnerStartingMonday);
        startingTimeTuesday = findViewById(R.id.spinnerStartingTuesday);
        startingTimeWednesday= findViewById(R.id.spinnerStartingWednesday);
        startingTimeThursday = findViewById(R.id.spinnerStartingThursday);
        startingTimeFriday = findViewById(R.id.spinnerStartingFriday);
        startingTimeSaturday = findViewById(R.id.spinnerStartingSaturday);
        startingTimeSunday = findViewById(R.id.spinnerStartingSunday);

        // initializing the spinners to select the finishing time of each day
        finishingTimeMonday = findViewById(R.id.spinnerFinishMonday);
        finishingTimeTuesday = findViewById(R.id.spinnerFinishTuesday);
        finishingTimeWednesday = findViewById(R.id.spinnerFinishWednesday);
        finishingTimeThursday = findViewById(R.id.spinnerFinishThursday);
        finishingTimeFriday = findViewById(R.id.spinnerFinishFriday);
        finishingTimeSaturday = findViewById(R.id.spinnerFinishSaturday);
        finishingTimeSunday = findViewById(R.id.spinnerFinishSunday);

        // we need an adapter for each spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.hoursWithClosed, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startingTimeMonday.setAdapter(adapter);
        startingTimeTuesday.setAdapter(adapter);
        startingTimeWednesday.setAdapter(adapter);
        startingTimeThursday.setAdapter(adapter);
        startingTimeFriday.setAdapter(adapter);
        startingTimeSaturday.setAdapter(adapter);
        startingTimeSunday.setAdapter(adapter);

        finishingTimeMonday.setAdapter(adapter);
        finishingTimeTuesday.setAdapter(adapter);
        finishingTimeWednesday.setAdapter(adapter);
        finishingTimeThursday.setAdapter(adapter);
        finishingTimeFriday.setAdapter(adapter);
        finishingTimeSaturday.setAdapter(adapter);
        finishingTimeSunday.setAdapter(adapter);


        finish = findViewById(R.id.buttonFinishToBranchActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // By using hashmap we are saving the schedule of the branch in the database
                    String mondayStart = startingTimeMonday.getSelectedItem().toString().replaceAll("to"," ".trim());
                    String tuesdayStart = startingTimeTuesday.getSelectedItem().toString().replaceAll("to"," ".trim());
                    String wednesdayStart = startingTimeWednesday.getSelectedItem().toString().replaceAll("to"," ".trim());
                    String thursdayStart = startingTimeThursday.getSelectedItem().toString().replaceAll("to"," ".trim());
                    String fridayStart = startingTimeFriday.getSelectedItem().toString().replaceAll("to"," ".trim());
                    String saturdayStart = startingTimeSaturday.getSelectedItem().toString().replaceAll("to"," ".trim());
                    String sundayStart = startingTimeSunday.getSelectedItem().toString().replaceAll("to"," ".trim());

                    String mondayFinish = finishingTimeMonday.getSelectedItem().toString();
                    String tuesdayFinish = finishingTimeTuesday.getSelectedItem().toString();
                    String wednesdayFinish = finishingTimeWednesday.getSelectedItem().toString();
                    String thursdayFinish = finishingTimeThursday.getSelectedItem().toString();
                    String fridayFinish = finishingTimeFriday.getSelectedItem().toString();
                    String saturdayFinish = finishingTimeSaturday.getSelectedItem().toString();
                    String sundayFinish = finishingTimeSunday.getSelectedItem().toString();

                    // here we are making sure of the fact that if the user select "Closed" both starting and finishing times must be closed

                    if(mondayStart.compareTo("Closed") == 0 && mondayFinish.compareTo("Closed") != 0){
                        // A spinner cannot set an error we are doing a conversion to TextView
                        ((TextView)finishingTimeMonday.getSelectedView()).setError("The finishing time must be closed too");

                    }else if(mondayStart.compareTo("Closed") != 0 && mondayFinish.compareTo("Closed") == 0){
                        ((TextView)startingTimeMonday.getSelectedView()).setError("The starting time must be closed too");

                    }else if(tuesdayStart.compareTo("Closed") == 0 && tuesdayFinish.compareTo("Closed") != 0){
                        ((TextView)finishingTimeTuesday.getSelectedView()).setError("The finishing time must be closed too");

                    }else if(tuesdayStart.compareTo("Closed") != 0 && tuesdayFinish.compareTo("Closed") == 0){
                        ((TextView)startingTimeTuesday.getSelectedView()).setError("The starting time must be closed too");

                    }else if(wednesdayStart.compareTo("Closed") == 0 && wednesdayFinish.compareTo("Closed") != 0){
                        ((TextView)finishingTimeWednesday.getSelectedView()).setError("The finishing time must be closed too");

                    }else if(wednesdayStart.compareTo("Closed") != 0 && wednesdayFinish.compareTo("Closed") == 0){
                        ((TextView)startingTimeWednesday.getSelectedView()).setError("The starting time must be closed too");

                    }else if(thursdayStart.compareTo("Closed") == 0 && thursdayFinish.compareTo("Closed") != 0){
                        ((TextView)finishingTimeThursday.getSelectedView()).setError("The finishing time must be closed too");

                    }else if(thursdayStart.compareTo("Closed") != 0 && thursdayFinish.compareTo("Closed") == 0) {
                        ((TextView) startingTimeThursday.getSelectedView()).setError("The starting time must be closed too");

                    }else if(fridayStart.compareTo("Closed") == 0 && fridayFinish.compareTo("Closed") != 0){
                        ((TextView)finishingTimeFriday.getSelectedView()).setError("The finishing time must be closed too");

                    }else if(fridayStart.compareTo("Closed") != 0 && fridayFinish.compareTo("Closed") == 0) {
                        ((TextView) startingTimeFriday.getSelectedView()).setError("The starting time must be closed too");

                    }else if(saturdayStart.compareTo("Closed") == 0 && saturdayFinish.compareTo("Closed") != 0){
                        ((TextView)finishingTimeSaturday.getSelectedView()).setError("The finishing time must be closed too");

                    }else if(saturdayStart.compareTo("Closed") != 0 && saturdayFinish.compareTo("Closed") == 0) {
                        ((TextView) startingTimeSaturday.getSelectedView()).setError("The starting time must be closed too");

                    }else if(sundayStart.compareTo("Closed") == 0 && sundayFinish.compareTo("Closed") != 0){
                        ((TextView)finishingTimeSunday.getSelectedView()).setError("The finishing time must be closed too");

                    }else if(sundayStart.compareTo("Closed") != 0 && sundayFinish.compareTo("Closed") == 0) {
                        ((TextView) startingTimeSunday.getSelectedView()).setError("The starting time must be closed too");

                    }else{

                        HashMap map = new HashMap();

                        map.put("startingTime",mondayStart);
                        map.put("finishingTime",mondayFinish);
                        db.child("Monday").updateChildren(map);
                        map.clear();


                        map.put("startingTime",tuesdayStart);
                        map.put("finishingTime",tuesdayFinish);
                        db.child("Tuesday").updateChildren(map);
                        map.clear();


                        map.put("startingTime",wednesdayStart);
                        map.put("finishingTime",wednesdayFinish);
                        db.child("Wednesday").updateChildren(map);
                        map.clear();

                        map.put("startingTime",thursdayStart);
                        map.put("finishingTime",thursdayFinish);
                        db.child("Thursday").updateChildren(map);
                        map.clear();

                        map.put("startingTime",fridayStart);
                        map.put("finishingTime",fridayFinish);
                        db.child("Friday").updateChildren(map);
                        map.clear();

                        map.put("startingTime",saturdayStart);
                        map.put("finishingTime",saturdayFinish);
                        db.child("Saturday").updateChildren(map);
                        map.clear();

                        map.put("startingTime",sundayStart);
                        map.put("finishingTime",sundayFinish);
                        db.child("Sunday").updateChildren(map);

                        Intent intent = new Intent(Schedule.this,BranchActivity.class);
                        intent.putExtra("branchID",branchID);
                        intent.putExtra("userUID",userID);
                        startActivity(intent);
                    }

                }
        });

    }
}