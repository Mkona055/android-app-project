package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class FillDocument extends AppCompatActivity {
    private String branchID;
    private String userID;
    private String serviceSelectedKey;
    private ListView listView;
    private TextView serviceSelectedName;
    private Button next;
    private ArrayList<Image> documents;
    private DocumentAdapter documentAdapter;
    private DatabaseReference db;
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
        next = findViewById(R.id.buttonContinueToRating);
        serviceSelectedKey ="-MLBRlHznL1Ste49lNHQ"; //bundle.getString("serviceSelectedKey");
        serviceSelectedName = findViewById(R.id.textViewServiceSelectedDocument);
        serviceSelectedName.setText("Driver's license document");//bundle.getString("serviceSelectedName") + " document");

        documents = new ArrayList<>();
        documentAdapter = new DocumentAdapter(this,R.layout.row_for_document_upload,documents);
        listView = findViewById(R.id.listViewServiceForm);
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(FillForm.this, FillDocument.class );
//                intent.putExtra("userUID", userID);
//                intent.putExtra("branchID", branchID);
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
//                String requestKey = ref.push().getKey();
//                intent.putExtra("requestKey",requestKey);
//                ref.child(requestKey).updateChildren(map);
//                startActivity(intent);
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
        }
    }
}