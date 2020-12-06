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

public class FormFillingAdapter extends ArrayAdapter<Pair> {
    private Context mContext;
    private int mResource;
    public FormFillingAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Pair> objects) {
        super(context, resource, objects);
        this.mContext = context ;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource, parent, false);
        TextView fieldName = convertView.findViewById(R.id.textViewFieldName);
        TextView filling = convertView.findViewById(R.id.textViewToFill);

        fieldName.setText(getItem(position).getFieldName());
        filling.setText(getItem(position).getFilling());
        return convertView;
    }
}
