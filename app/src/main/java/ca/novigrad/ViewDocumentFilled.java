package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewDocumentFilled extends AppCompatActivity {
    private ListView listView;
    private TextView title;
    private ImageView next;
    private ArrayList<Image> formFilled;
    private ArrayList<String> docs;
    private DocumentAdapter adapter;
    private String branchID;
    private String userID;
    private String requestKey;
    private String serviceRequested;
    private String serviceSelectedKey;
    private DatabaseReference db;
    private DatabaseReference db2;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_document_filled);
        Bundle bundle = getIntent().getExtras();
        branchID = bundle.getString("branchID");
        userID = bundle.getString("userID");
        requestKey = bundle.getString("requestKey");
        serviceRequested = bundle.getString("serviceRequested");
        serviceSelectedKey = bundle.getString("serviceSelectedKey");
        listView = findViewById(R.id.listViewDocumentFilled);
        formFilled = new ArrayList<>();
        adapter = new DocumentAdapter(this,R.layout.row_for_recycler_view,formFilled);
        title = findViewById(R.id.textViewFormFilledTitle);
        next = findViewById(R.id.imageViewMoveToDocumentsChecking);
        db = FirebaseDatabase.getInstance().getReference("Services").child(serviceSelectedKey).child("document");
        db2 = FirebaseDatabase.getInstance().getReference("Branches").child(branchID).child("Requests").child(requestKey);
        storageReference = FirebaseStorage.getInstance().getReference().child("images/" + requestKey);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String docName = dataSnapshot.getValue(String.class);
                    docs.add(docName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        title.setText(serviceRequested + " documents");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApproveDenyDialog();

            }
        });
        for(String docName :docs){
            downloadFile(docName);
        }
        listView.setAdapter(adapter);
    }

    private void downloadFile(final String docName) {
        storageReference.child(docName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                formFilled.add(new Image(uri,docName));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void openApproveDenyDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.approve_deny_dialog,null);

        dialogBuilder.setView(dialogView).setTitle("Request's decision");
        dialogBuilder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap map = new HashMap();
                map.put("status","Denied");
                db2.updateChildren(map);
                Intent intent = new Intent(ViewDocumentFilled.this,CustomersRequest.class);
                intent.putExtra("branchID",branchID);
                intent.putExtra("userID",userID);
                intent.putExtra("requestKey",requestKey);
                intent.putExtra("serviceRequested",serviceRequested);
                intent.putExtra("serviceSelectedKey",serviceSelectedKey);
                startActivity(intent);

            }
        });
        dialogBuilder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap map = new HashMap();
                map.put("status","Approved");
                db2.updateChildren(map);
            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.show();
    }

}