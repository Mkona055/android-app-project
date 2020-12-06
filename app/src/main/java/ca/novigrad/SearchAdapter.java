package ca.novigrad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchAdapter extends FirebaseRecyclerAdapter<SearchList, SearchAdapter.SearchHolder> {

    private Context context;
    private String test;


    public SearchAdapter(@NonNull FirebaseRecyclerOptions<SearchList> options) {
        super(options);
    }




    @Override
    protected void onBindViewHolder(@NonNull SearchHolder holder, int position, @NonNull SearchList model) {
        holder.branch_address.setText(model.getBranchAddress());
        holder.branch_id.setText(model.getBranchID());

         test = model.getBranchID();

        holder.SetItemClickLister(new ItemClickListener() {
            @Override
            public void OnItemClicked(View v, int layoutPosition) {

            //Toast.makeText(context,test , Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

            intent.putExtra("BranchID", test );

            }
        });

    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list,parent,false );
        context = parent.getContext();
        return new SearchHolder(view);

    }


    class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView branch_id, branch_address;
        private ItemClickListener itemClickListener;


        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            ItemClickListener itemClickListener;

            branch_id = itemView.findViewById(R.id.textViewBranchID4);
            branch_address = itemView.findViewById(R.id.textViewBranchAddress4);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            this.itemClickListener.OnItemClicked(v, getLayoutPosition());

        }

        public void SetItemClickLister(ItemClickListener ic){

            this.itemClickListener = ic;

        }

    }

}
