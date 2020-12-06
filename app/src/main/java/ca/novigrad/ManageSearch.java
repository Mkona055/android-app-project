package ca.novigrad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageSearch extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private SearchAdapter adapter;
    private DatabaseReference mUserDatabase;

    private String typeOfSearch;
    private String searchedText;

    private DocumentReference documentReference;
    private FirebaseFirestore fStore;
    private String userID;
    private String branchID;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_search);
        Bundle bundle = getIntent().getExtras();


         //Get the user ID
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();



        //Search in RealTime Database
        mUserDatabase = FirebaseDatabase.getInstance().getReference("Branches");
        typeOfSearch = bundle.getString("typeofSearch");
        searchedText = bundle.getString("searchText");

        setUpRecyclerView(searchedText,typeOfSearch);







    }

    private  void setUpRecyclerView(String searchText,String typeOfSearch) {
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