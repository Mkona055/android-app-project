package ca.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ManageBranch extends AppCompatActivity {


    private FirebaseFirestore firStore;
    private RecyclerView rcView;


    private FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_branch);




        firStore = FirebaseFirestore.getInstance();
        rcView = findViewById(R.id.rViewBranchList);


        //GET DATA FROM DATA BASE
        Query query = firStore.collection("users");


       FirestoreRecyclerOptions<BranchList> options = new FirestoreRecyclerOptions.Builder<BranchList>()
              .setQuery(query, BranchList.class)
               .build();


       adapter = new FirestoreRecyclerAdapter<BranchList, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_list,parent,false );
                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull BranchList model) {
                holder.email.setText(model.getEmail());
                holder.fullName.setText(model.getFullName());
                holder.phone.setText(model.getPhoneNumber());

            }
        };

        rcView.setHasFixedSize(true);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(adapter);

    }

    private class ProductViewHolder extends RecyclerView.ViewHolder{
        private TextView fullName, email, phone;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.textViewEmail3);
            fullName = itemView.findViewById(R.id.textViewFullName3);
            phone = itemView.findViewById(R.id.textViewPhoneNumber3);


        }
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