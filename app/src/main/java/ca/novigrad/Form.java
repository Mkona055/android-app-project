package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Form extends AppCompatActivity {
    private String serviceID;
    private EditText fieldName;
    private Button addField, next;
    private ListView form;
    private DatabaseReference dr;
    private ArrayList<String> fieldNames;
    private ArrayAdapter<String> fieldNamesAdapter;
    private int numberOfFields ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Bundle bundle = getIntent().getExtras();
        serviceID = bundle.getString("serviceID");

        fieldName = findViewById(R.id.editTextFieldName);
        addField = findViewById(R.id.buttonAddField);
        next = findViewById(R.id.buttonNextToPaper);
        form = findViewById(R.id.ListView);
        fieldNames = new ArrayList<>();
        fieldNamesAdapter = new ArrayAdapter<>(Form.this,  android.R.layout.simple_list_item_1, fieldNames);
        dr = FirebaseDatabase.getInstance().getReference("Services");

        numberOfFields = 4;

        addField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                numberOfFields++;
                if(TextUtils.isEmpty(fieldName.getText().toString().trim())){
                    fieldName.setError("Error - The field name is empty");

                }else{

                    HashMap map = new HashMap<>();
                    map.put("fieldName" + numberOfFields, fieldName.getText().toString().trim());
                    dr.child(serviceID).child("Form").updateChildren(map);
                    fieldName.setText(null);

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Form.this,DocumentActivity.class);
                intent.putExtra("serviceID", serviceID);
                startActivity(intent);// add progress bar
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceID).child("Form");;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fieldNames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String fieldName = snapshot.getValue(String.class);
                    fieldNames.add(fieldName);
                }
                form.setAdapter(fieldNamesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        form.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showUpdateDeleteDialog(fieldNames.get(position));
            }
        });

    }

    private void showUpdateDeleteDialog(final String fieldToModify){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_updatedelete_dialog,null);

        final EditText newFieldName  = dialogView.findViewById(R.id.editTextUpdateFieldName);
        final Button update = dialogView.findViewById(R.id.buttonUpdate);
        final Button delete = dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setView(dialogView).setTitle("Update Field ");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newFieldName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    newFieldName.setError("This section must be filled in order to update");
                }else{
                    updateFieldName(fieldToModify, name);
                    b.dismiss();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFieldName(fieldToModify);
                b.dismiss();
            }
        });
    }

    private void updateFieldName(final String fieldToUpdate , final String newName){


        final DatabaseReference ref = dr.child(serviceID).child("Form");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String keyOfFieldToUpdate = "";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String field = snapshot.getValue(String.class);
                            if(field.equals(fieldToUpdate)){
                                keyOfFieldToUpdate = snapshot.getKey();
                                break;
                            }

                        }

                        HashMap map = new HashMap<>();
                        map.put(keyOfFieldToUpdate,newName);
                        ref.updateChildren(map);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void deleteFieldName (final String fieldToDelete){
        final DatabaseReference ref = dr.child(serviceID).child("Form");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String keyOfFieldToDelete = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String field = snapshot.getValue(String.class);
                    if(field.equals(fieldToDelete)){
                        keyOfFieldToDelete = snapshot.getKey();
                        break;
                    }

                }

                ref.child(keyOfFieldToDelete).removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}