package ca.novigrad;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchAdapter extends FirebaseRecyclerAdapter<SearchList, SearchAdapter.SearchHolder> {


    public SearchAdapter(@NonNull FirebaseRecyclerOptions<SearchList> options) {
        super(options);
    }




    @Override
    protected void onBindViewHolder(@NonNull SearchHolder holder, int position, @NonNull SearchList model) {
        holder.branch_address.setText(model.getBranchAddress());
        holder.branch_id.setText(model.getBranchID());




    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false );
        return new SearchHolder(view);

    }


    class SearchHolder extends RecyclerView.ViewHolder{

        private TextView branch_id, branch_address;
       // private ImageView add_branch;







        public SearchHolder(@NonNull View itemView) {
            super(itemView);

            branch_id = itemView.findViewById(R.id.textViewBranchID4);
            branch_address = itemView.findViewById(R.id.textViewBranchAddress4);

          //  add_branch  = itemView.findViewById(R.id.imageView_add);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), MainActivity.class);
                    i.putExtra("branchID", model.getBranchID());
                    v.getContext().startActivity(i);
                }
            });



        }
    }

}
