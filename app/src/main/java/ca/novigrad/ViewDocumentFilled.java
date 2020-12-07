package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewDocumentFilled extends AppCompatActivity {
    private ListView listView;
    private TextView title;
    private ImageView next;
    private ArrayList<Image> documents;
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
        docs = new ArrayList<>();
        documents = new ArrayList<>();
        adapter = new DocumentAdapter(this,R.layout.row_for_document_upload,documents);
        title = findViewById(R.id.textViewDocumentFilledTitle);
        next = findViewById(R.id.imageViewOpenDialogJudge);
        db = FirebaseDatabase.getInstance().getReference("Services").child(serviceSelectedKey).child("documents");
        db2 = FirebaseDatabase.getInstance().getReference("Branches").child(branchID).child("Requests").child(requestKey);
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();
        title.setText(serviceRequested + " documents");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String docName = dataSnapshot.getValue(String.class);
                    docs.add(docName);
                }

                for(final String docName :docs){
                    downloadFile(docName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApproveDenyDialog();

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openViewDocumentDialog(position);
            }
        });
    }

    private void openViewDocumentDialog(int position) {
        Image image = documents.get(position);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_document,null);
        dialogBuilder.setView(dialogView).setTitle(image.getDocumentName());

        ImageView imageView = dialogView.findViewById(R.id.imageViewDocumentChosen);
        Picasso.get().load(image.getImage()).into(imageView);

        final AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void downloadFile(final String docName) {
        storageReference.child("images/").child(requestKey+"/").child(docName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                documents.add(new Image(uri,docName));
                if(documents.size() == docs.size()){
                    listView.setAdapter(adapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewDocumentFilled.this,"Pictures loading failed",Toast.LENGTH_SHORT).show();
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
                finish();

            }
        });
        dialogBuilder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap map = new HashMap();
                map.put("status","Approved");
                db2.updateChildren(map);
                Intent intent = new Intent(ViewDocumentFilled.this,CustomersRequest.class);
                intent.putExtra("branchID",branchID);
                intent.putExtra("userID",userID);
                intent.putExtra("requestKey",requestKey);
                intent.putExtra("serviceRequested",serviceRequested);
                intent.putExtra("serviceSelectedKey",serviceSelectedKey);
                startActivity(intent);
                finish();
            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.show();
    }

}