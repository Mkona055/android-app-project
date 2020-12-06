package ca.novigrad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter<Request> {
    private Context mContext;
    private int mResource;

    public RequestAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Request> objects) {
        super(context, resource, objects);
        this.mContext = context ;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);
        TextView requestSender = convertView.findViewById(R.id.textViewRequestSender);
        TextView serviceRequested = convertView.findViewById(R.id.textViewTypeOfRequest);
        TextView status = convertView.findViewById(R.id.textViewStatus);
        requestSender.setText(getItem(position).getSender());
        serviceRequested.setText(getItem(position).getServiceRequested());
        status.setText(getItem(position).getStatus());


        return convertView;
    }
}
