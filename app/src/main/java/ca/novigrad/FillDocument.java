package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class FillDocument extends AppCompatActivity {
    private String branchID;
    private String userID;
    private String requestKey;
    private String serviceSelectedKey;
    private ListView listView;
    private TextView serviceSelectedName;
    private Button next;
    private ArrayList<Image> documents;
    private DocumentAdapter documentAdapter;
    private DatabaseReference db;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Image tmpImage;
    public Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_document);
        final Bundle bundle = getIntent().getExtras();
        // we get the employee information from MainActivity
        //branchID = bundle.getString("branchID");
        //userID = bundle.getString("userUID");
        requestKey = "test15";//bundle.getString("requestKey");
        next = findViewById(R.id.buttonContinueToRating);
        serviceSelectedKey ="-MLBRlHznL1Ste49lNHQ"; //bundle.getString("serviceSelectedKey");
        serviceSelectedName = findViewById(R.id.textViewServiceSelectedDocument);
        serviceSelectedName.setText("Driver's license document");//bundle.getString("serviceSelectedName") + " document");

        documents = new ArrayList<>();
        documentAdapter = new DocumentAdapter(this,R.layout.row_for_document_upload,documents);
        listView = findViewById(R.id.listViewDocumentToUpload);
        db = FirebaseDatabase.getInstance().getReference("Services").child(serviceSelectedKey);
        db.child("documents").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String document = dataSnapshot.getValue(String.class);
                    documents.add(new Image(null,document));
                }
                listView.setAdapter(documentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Image document : documents){
                    if(document.getImage()==null){
                        Toast.makeText(FillDocument.this,"Upload an image for " + document.getDocumentName(),Toast.LENGTH_LONG);
                        return;
                    }
                }
                HashMap map = new HashMap();
                map.put("serviceSelectedKey",serviceSelectedKey);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID).child("Requests").child(requestKey);
                ref.updateChildren(map);
                Intent intent = new Intent(FillDocument.this, RatingEmployes.class );
                intent.putExtra("userUID", userID);
                intent.putExtra("branchID", branchID);
//                intent.putExtra("serviceSelectedKey", serviceSelectedKey);
//                intent.putExtra("serviceSelectedName", bundle.getString("serviceSelectedName"));
//                HashMap map = new HashMap<>();
//                for(Pair p : fieldNames){
//                    String fName = p.getFieldName();
//                    String filling  = p.getFilling();
//                    if(filling.compareTo(Pair.TO_FILL)==0){
//                        Toast.makeText(getApplicationContext(),"field " + fName+ "must be filled ", Toast.LENGTH_LONG);
//                        return;
//                    }else{
//                        map.put(fName,filling);
//                    }
//                }
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID).child("Requests");
                String requestKey = ref.push().getKey();
                intent.putExtra("requestKey",requestKey);
                ref.child(requestKey).updateChildren(map);
                startActivity(intent);
            }
        });


    }

    private void uploadPicture(String path) {
        final ProgressDialog pD = new ProgressDialog(this);
        pD.setTitle("Uploading Image ...");
        pD.show();
        StorageReference riversRef = storageReference.child("images/" + requestKey).child(path);
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pD.dismiss();
                        Snackbar.make((findViewById(android.R.id.content)),"Image Uploaded", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pD.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed To Upload",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        pD.setMessage("Percentage " + (int) progressPercent + "%");
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tmpImage = documents.get(position);
                choosePicture();
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult( intent , 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null  && data.getData()!= null){
            imageUri = data.getData();
            tmpImage.setImage(imageUri);
            documentAdapter.notifyDataSetChanged();
            uploadPicture(tmpImage.getDocumentName());
            
        }
    }
}