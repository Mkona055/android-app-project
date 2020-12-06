package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FillForm extends AppCompatActivity {
    private String branchID;
    private String userID;
    private String serviceSelectedKey;
    private ListView listView;
    private TextView serviceSelectedName;
    private Button next;
    private ArrayList<Pair> fieldNames;
    private FormFillingAdapter fillingAdapter;
    private DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);

        final Bundle bundle = getIntent().getExtras();
        // we get the employee information from MainActivity
        //branchID = bundle.getString("branchID");
        //userID = bundle.getString("userUID");
        next = findViewById(R.id.buttonNextToDocumentsFilling);
        serviceSelectedKey ="-MLBRlHznL1Ste49lNHQ"; //bundle.getString("serviceSelectedKey");
        serviceSelectedName = findViewById(R.id.textViewServiceSelectedForm);
        serviceSelectedName.setText("Driver's license form");//bundle.getString("serviceSelectedName") + " form");
        fieldNames = new ArrayList<>();
        fillingAdapter = new FormFillingAdapter(this,R.layout.row_for_recycler_view,fieldNames);
        listView = findViewById(R.id.listViewServiceForm);
        db = FirebaseDatabase.getInstance().getReference("Services").child(serviceSelectedKey);
        db.child("form").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String fieldName = dataSnapshot.getValue(String.class);
                    fieldNames.add(new Pair (fieldName));
                }
                listView.setAdapter(fillingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FillForm.this, FillDocument.class );
                intent.putExtra("userUID", userID);
                intent.putExtra("branchID", branchID);
                intent.putExtra("serviceSelectedKey", serviceSelectedKey);
                intent.putExtra("serviceSelectedName", bundle.getString("serviceSelectedName"));
                HashMap map = new HashMap<>();
                for(Pair p : fieldNames){
                    String fName = p.getFieldName();
                    String filling  = p.getFilling();
                    if(filling.compareTo(Pair.TO_FILL)==0){
                        Toast.makeText(getApplicationContext(),"field " + fName+ "must be filled ", Toast.LENGTH_LONG);
                        return;
                    }else{
                        map.put(fName,filling);
                    }
                }
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID).child("Requests");
                String requestKey = ref.push().getKey();
                intent.putExtra("requestKey",requestKey);
                ref.child(requestKey).updateChildren(map);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewFillFieldDialog(position);
            }
        });
    }

    private void viewFillFieldDialog(final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fill_field_dialog,null);
        final EditText fieldToFill = dialogView.findViewById(R.id.editTextFieldToFill);
        final String fieldName = fieldNames.get(position).getFieldName();
        final Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        dialogBuilder.setView(dialogView).setTitle(fieldName);
        fieldToFill.setHint("Enter " + fieldName);
        final AlertDialog b = dialogBuilder.create();
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(fieldToFill.getText().toString())){
                    fieldToFill.setError("This section must be filled");
                }
                if(fieldName.contains("Name")){
                    String regex = "^[a-zA-Z\\s]+";
                    if(!fieldToFill.getText().toString().matches(regex)){
                        fieldToFill.setError("The name section does not accept any special character and digits");
                    }else{
                        fieldNames.get(position).setFilling(fieldToFill.getText().toString());
                        b.dismiss();
                        fillingAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                if(fieldName.contains("number")){
                    // verification of the phone Number
                    String regex = "^\\d{10}$";
                    if(!fieldToFill.getText().toString().matches(regex)){
                        fieldToFill.setError("The phone number must take 10 digits ");
                    }else{
                        fieldNames.get(position).setFilling(fieldToFill.getText().toString());
                        b.dismiss();
                        fillingAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                if(fieldName.contains("Date")){
                    String regex = "[0-9][0-9]\\/[0-9][0-9]\\/[0-9][0-9][0-9][0-9]";
                    if(!fieldToFill.getText().toString().matches(regex)){
                        fieldToFill.setError("DD/MM/AAAA");
                    }else{
                        fieldNames.get(position).setFilling(fieldToFill.getText().toString());
                        b.dismiss();
                        fillingAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                if (fieldName.contains("Type of permit")){
                    String regex = "G[0-9]";
                    if(!fieldToFill.getText().toString().matches(regex)){
                        fieldToFill.setError("The format must be GX where X is a digit");
                    }else{
                        fieldNames.get(position).setFilling(fieldToFill.getText().toString());
                        b.dismiss();
                        fillingAdapter.notifyDataSetChanged();
                    }
                    return;

                }
                if(fieldName.contains("Address")){
                    String regex = "^\\d+(\\s[A-z]+\\s[A-z]+)+,+\\s[A-z]+,+\\s[A-z]+,+\\s+\\w+";
                    if(!fieldToFill.getText().toString().matches(regex)){
                        fieldToFill.setError("The format must be similar to \"123 Park Street, Camden, ME, 04843\" with spaces after each coma");
                    }else{
                        fieldNames.get(position).setFilling(fieldToFill.getText().toString());
                        b.dismiss();
                        fillingAdapter.notifyDataSetChanged();
                    }
                    return;
                }

            }
        });


        b.show();


    }


}