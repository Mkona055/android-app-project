package ca.novigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class Search extends AppCompatActivity {
    private Spinner spinner;
    private ImageView searchButton;
    private EditText searchFor;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userID");

        spinner = findViewById(R.id.spinnerTypeOfSearch);
        searchButton = findViewById(R.id.imageViewbtnSearch);
        searchFor = findViewById(R.id.editTextSearch_field);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.typeOfSearch, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(searchFor.getText().toString())){
                    searchFor.setError("Search field is empty");
                    return;
                }
                Intent intent = new Intent(Search.this,ManageSearch.class);
                intent.putExtra("typeofSearch",spinner.getSelectedItem().toString());
                intent.putExtra("searchText",searchFor.getText().toString());
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
    }
}