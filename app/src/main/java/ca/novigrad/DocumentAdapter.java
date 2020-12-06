package ca.novigrad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DocumentAdapter extends ArrayAdapter<Image> {
    private Context mContext;
    private int mResource;

    public DocumentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Image> objects) {
        super(context, resource, objects);
        this.mContext = context ;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);
        ImageView imageView = convertView.findViewById(R.id.imageViewForDocument);
        TextView documentName = convertView.findViewById(R.id.textViewForDocument);
        documentName.setText(getItem(position).getDocumentName());
        if(!(getItem(position).getImage() == null)){
            imageView.setImageURI(getItem(position).getImage());
        }

        return convertView;
    }
}


