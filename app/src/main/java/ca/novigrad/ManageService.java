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


public class ManageService extends AppCompatActivity {
    private DatabaseReference databaseReference ;
    private Button addButton;
    private ListView listViewServices;
    private ArrayList<String> services;
    private  ArrayAdapter<String> serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_service);

        services = new ArrayList<>();
        addButton = findViewById(R.id.buttonAddServices);
        listViewServices = findViewById(R.id.ListViewServices);
        serviceAdapter = new ArrayAdapter<>(ManageService.this, android.R.layout.simple_expandable_list_item_1,services);
        databaseReference = FirebaseDatabase.getInstance().getReference("Services");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newServiceDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Service service = postSnapshot.getValue(Service.class);
                    services.add(service.getServiceName());
                }
                listViewServices.setAdapter(serviceAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
//        listViewServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String servicename;
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String keyOfFieldToUpdate = "";
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                            String key = snapshot.getKey();
//                            snapshot.getChildren().
//
//
//
//                        }
//
//                        HashMap map = new HashMap<>();
//                        map.put(keyOfFieldToUpdate,newName);
//                        ref.updateChildren(map);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                viewServiceDialog(services.get(position));
//
//            }
//        });
        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showUpdateDeleteDialog(services.get(position));
                return true;
            }
        });
    }

    public  void showUpdateDeleteDialog(final String serviceToModify){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_updatedelet_services,null);

        final EditText newService  = dialogView.findViewById(R.id.editTextUpdateServices);
        final Button update = dialogView.findViewById(R.id.buttonUpdateServices);
        final Button delete = dialogView.findViewById(R.id.buttonDeleteServices);

        dialogBuilder.setView(dialogView).setTitle("Update Service ");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newService.getText().toString().trim();
                updateServiceName(serviceToModify,name);


                Intent intent = new Intent(ManageService.this,Form.class);
                intent.putExtra("serviceCreated", serviceToModify);
                startActivity(intent);// add progress bar
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteService(serviceToModify);
                b.dismiss();
            }
        });
    }


    public void viewServiceDialog(String serviceName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_viewservice_dialog,null);

        final ListView form = dialogView.findViewById(R.id.ListViewForm);
        final ListView listViewDocuments = dialogView.findViewById(R.id.ListViewDocument);
        final ArrayList<String> formElements = new ArrayList<>();
        final ArrayAdapter<String> formElementsAdapter = new ArrayAdapter<>(ManageService.this, android.R.layout.simple_expandable_list_item_1,formElements);
        final ArrayList<String> documents = new ArrayList<>();
        final ArrayAdapter<String> documentsAdapter = new ArrayAdapter<>(ManageService.this, android.R.layout.simple_expandable_list_item_1,documents);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName).child("Form");;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                formElements.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String fieldName = snapshot.getValue(String.class);
                    formElements.add(fieldName);
                }
                form.setAdapter(formElementsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceName).child("Documents");;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                documents.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String fieldName = snapshot.getValue(String.class);
                    documents.add(fieldName);
                }
                listViewDocuments.setAdapter(documentsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dialogBuilder.setView(dialogView).setTitle("Update Service ").setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void newServiceDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_newservice_dialog,null);

        final EditText serviceName = dialogView.findViewById(R.id.editTextUpdateFieldName);

        final Button cancel = dialogView.findViewById(R.id.buttonUpdate);
        final Button next = dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setView(dialogView).setTitle("Create new Service");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = serviceName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    serviceName.setError("Error - Please enter a name for your service ");
                }else{
                    HashMap map = new HashMap<>();

                    map.put("serviceName", name);
                    databaseReference.child(name).updateChildren(map);

                    map.clear();
                    map.put("fieldName1","First Name :");
                    map.put("fieldName2","Second Name :");
                    map.put("fieldName3","Date of birth :");
                    map.put("fieldName4","Address :");
                    databaseReference.child(name).child("Form").updateChildren(map);

                    map.clear();
                    map.put("documentName1","Proof of Residence(Picture of electricity bill or bank statement confirming the address :");
                    databaseReference.child(name).child("Documents").updateChildren(map);
                    b.dismiss();
                    Intent intent = new Intent(ManageService.this,Form.class);
                    intent.putExtra("serviceCreated",name);
                    startActivity(intent);// add progress bar
                    finish();

                }
            }
        });

    }

    private void updateServiceName(final String serviceToUpdate , final String newServiceName){


        final DatabaseReference ref = databaseReference.child(serviceToUpdate).child("serviceName");
        ref.setValue(newServiceName);



    }

    private void deleteService(final String serviceToDelete){
        final DatabaseReference ref = databaseReference.child(serviceToDelete);
        ref.removeValue();
    }


}