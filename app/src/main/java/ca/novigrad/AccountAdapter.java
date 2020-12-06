package ca.novigrad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AccountAdapter extends FirestoreRecyclerAdapter<AccountList, AccountAdapter.AccountHolder> {


    public AccountAdapter(@NonNull FirestoreRecyclerOptions<AccountList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AccountHolder holder, int position, @NonNull AccountList model) {
        holder.email.setText(model.getEmail());
        holder.fullName.setText(model.getFullName());
        holder.phone.setText(model.getPhoneNumber());
        holder.role.setText(model.getRole());
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list,parent,false );
        return new AccountHolder(view);

    }

    public void deleteItem(int position) {
      getSnapshots().getSnapshot(position).getReference().delete();
    }

    class AccountHolder extends RecyclerView.ViewHolder{

        private TextView fullName, email, phone, role;




        public AccountHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.textViewBranchID4);
            fullName = itemView.findViewById(R.id.textViewBranchAddress4);
            phone = itemView.findViewById(R.id.textViewPhoneNumber4);
            role = itemView.findViewById(R.id.textViewEmail4);


        }
    }

}
