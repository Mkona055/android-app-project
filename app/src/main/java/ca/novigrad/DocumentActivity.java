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

public class DocumentActivity extends AppCompatActivity {

    private String serviceID;
    private EditText documentName;
    private Button addDocument, finish;
    private ListView listViewDocuments;
    private DatabaseReference dr;
    private ArrayList<String> documents;
    private ArrayAdapter<String> documentsAdapter;
    private int numberOfDocument ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        Bundle bundle = getIntent().getExtras();
        serviceID = bundle.getString("serviceID");

        documentName = findViewById(R.id.editTextDocumentToAdd);
        addDocument = findViewById(R.id.buttonAddDocument);
        finish = findViewById(R.id.buttonFinish);
        listViewDocuments = findViewById(R.id.ListeViewOfDocument);
        documents = new ArrayList<>();

        documentsAdapter = new ArrayAdapter<>(DocumentActivity.this,  android.R.layout.simple_expandable_list_item_1, documents);
        dr = FirebaseDatabase.getInstance().getReference("Services");

        numberOfDocument = 1;

        addDocument.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                numberOfDocument++;
                if(TextUtils.isEmpty(documentName.getText().toString().trim())){
                    documentName.setError("Error - The field name is empty");

                }else{

                    HashMap map = new HashMap<>();
                    map.put("documentName" + numberOfDocument, documentName.getText().toString().trim());
                    dr.child(serviceID).child("documents").updateChildren(map);
                    documentName.setText(null);

                }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageService.class));
                finish();
            }
        });
    }

    protected void onStart() {
        super.onStart();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services").child(serviceID).child("documents");;
        databaseReference.addValueEventListener(new ValueEventListener() {

            // start by clearing the editext and then set the listview of the document
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
        listViewDocuments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showUpdateDeleteDialog(documents.get(position));
            }
        });

    }

    //open a dialog page to manage documents
    private void showUpdateDeleteDialog(final String documentToModify){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_updatedelete_dialog,null);

        final EditText newDocName  = dialogView.findViewById(R.id.editTextUpdateFieldName);
        final Button update = dialogView.findViewById(R.id.buttonUpdate);
        final Button delete = dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setView(dialogView).setTitle("Update Document ");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // this button run only if the edit text is not empty
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newDocName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    newDocName.setError("This section must be filled in order to update");
                }else{
                    updateDocument(documentToModify, name);
                    b.dismiss();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDocument(documentToModify);
                b.dismiss();
            }
        });
    }

    // we use a key of the field to change it
    private void updateDocument (final String documentToUpdate , final String newName){


        final DatabaseReference ref = dr.child(serviceID).child("documents");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String keyOfDocToUpdate = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String doc = snapshot.getValue(String.class);
                    if(doc.equals(documentToUpdate)){
                        keyOfDocToUpdate = snapshot.getKey(); // we make the change
                        break;
                    }

                }

                HashMap map = new HashMap<>();
                map.put(keyOfDocToUpdate,newName);  // we put inside the database
                ref.updateChildren(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //we delete the reference of the fied inside the database
    private void deleteDocument (final String docToDelete){
        final DatabaseReference ref = dr.child(serviceID).child("documents");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String keyOfDocToDelete = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String doc = snapshot.getValue(String.class);
                    if(doc.equals(docToDelete)){
                        keyOfDocToDelete = snapshot.getKey();
                        break;
                    }

                }

                ref.child(keyOfDocToDelete).removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}