package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;


public class ManageService extends AppCompatActivity {
    private DatabaseReference databaseReference ;
    private Button addButton;
    private ListView listViewServices;
    private ArrayList<Service> servicesInfo;
    private ArrayList<String> services;
    private  ArrayAdapter<String> serviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_service);

        // is used to store services information as Service object
        servicesInfo = new ArrayList<>();
        // is used to set up the listView using the serviceNames only
        services = new ArrayList<>();
        addButton = findViewById(R.id.buttonAddServices);
        listViewServices = findViewById(R.id.ListViewServices);
        // adapter for listView
        serviceAdapter = new ArrayAdapter<>(ManageService.this, android.R.layout.simple_list_item_1,services);
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
        // when one service is selected we show a dialog to update or delete the service
        listViewServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Service serviceClicked = servicesInfo.get(position);
                String serviceID = serviceClicked.getServiceID();
                showUpdateDeleteDialog(serviceID);
            }
        });
        // when one service is long clicked we show a dialog to view the service
        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Service serviceClicked = servicesInfo.get(position);
                String serviceID = serviceClicked.getServiceID();
                viewServiceDialog(serviceID);

                return true;
            }
        });

    }

//the dialog to update or delete the service using is ID i
    public  void showUpdateDeleteDialog(final String serviceID){

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
                updateServiceName(serviceID,name);


                Intent intent = new Intent(ManageService.this,Form.class);
                intent.putExtra("serviceID", serviceID);
                startActivity(intent);// add progress bar
                finish();
                b.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteService(serviceID);
                b.dismiss();
            }
        });
    }

    // the dialog to view the service using is ID i
    public void viewServiceDialog(String serviceID) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.layout_viewservice_dialog,null);
        final ListView form = dialogView.findViewById(R.id.ListViewForm);
        final ListView listViewDocuments = dialogView.findViewById(R.id.ListViewDocument);
        final ArrayList<String> formElements = new ArrayList<>();
        final ArrayAdapter<String> formElementsAdapter = new ArrayAdapter<>(ManageService.this, android.R.layout.simple_list_item_1,formElements);
        final ArrayList<String> documents = new ArrayList<>();
        final ArrayAdapter<String> documentsAdapter = new ArrayAdapter<>(ManageService.this, android.R.layout.simple_list_item_1,documents);

        dialogBuilder.setView(dialogView).setTitle("Update Service ").setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog b = dialogBuilder.create();

        final DatabaseReference refForm = databaseReference.child(serviceID).child("form");;
        refForm.addListenerForSingleValueEvent(new ValueEventListener() {
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

        DatabaseReference refDoc = databaseReference.child(serviceID).child("documents");;
        refDoc.addListenerForSingleValueEvent(new ValueEventListener() {
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

        b.show();
    }

    // the dialog to create a service
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
                String serviceID = databaseReference.push().getKey();
                if(TextUtils.isEmpty(name)){
                    serviceName.setError("Error - Please enter a name for your service ");
                }else{
                    HashMap map = new HashMap<>();

                    map.put("serviceName", name);
                    databaseReference.child(serviceID).updateChildren(map);

                    map.clear();
                    map.put("serviceID", serviceID);
                    databaseReference.child(serviceID).updateChildren(map);


                    map.clear();
                    map.put("fieldName1","First Name :");
                    map.put("fieldName2","Second Name :");
                    map.put("fieldName3","Date of birth :");
                    map.put("fieldName4","Address :");
                    databaseReference.child(serviceID).child("form").updateChildren(map);

                    map.clear();
                    map.put("documentName1","Proof of Residence(Picture of electricity bill or bank statement confirming the address) :");
                    databaseReference.child(serviceID).child("documents").updateChildren(map);
                    b.dismiss();
                    Intent intent = new Intent(ManageService.this,Form.class);
                    intent.putExtra("serviceID",serviceID);
                    startActivity(intent);// add progress bar
                    finish();

                }
            }
        });

    }

    private void updateServiceName(final String serviceID , final String newServiceName){

        databaseReference.child(serviceID).child("serviceName").setValue(newServiceName);

    }

    private void deleteService(final String serviceID){
        final DatabaseReference ref = databaseReference.child(serviceID);
        ref.removeValue();
    }


}