package ca.novigrad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageSearch extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference searchRef = db.collection("users");
    private SearchAdapter adapter;
    private DatabaseReference mUserDatabase;

    private EditText mSearchField;
    private ImageView mSearchBtn;

    String searchText ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_search);

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Branches");
        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn =  (ImageView) findViewById(R.id.imageViewbtn);


        searchText = mSearchField.getText().toString();

        setUpRecyclerView(searchText);



    }

    private  void setUpRecyclerView(String searchText) {
        Toast.makeText(ManageSearch.this, "Started Search", Toast.LENGTH_LONG).show();

        Query query = mUserDatabase.orderByChild("branchAddress").startAt(searchText).endAt(searchText + "\uf8ff"); //read the data inside the database

        FirebaseRecyclerOptions<SearchList> options = new FirebaseRecyclerOptions.Builder<SearchList>()
                .setQuery(query, SearchList.class)
                .build();
        adapter = new SearchAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.result_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }





    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


}