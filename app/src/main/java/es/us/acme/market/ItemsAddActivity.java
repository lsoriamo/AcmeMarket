package es.us.acme.market;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.util.ArrayList;

import es.us.acme.market.entities.Category;
import es.us.acme.market.entities.Item;
import es.us.acme.market.services.FirebaseDatabaseService;

public class ItemsAddActivity extends AppCompatActivity {

    private EditText items_add_name_et;
    private EditText items_add_description_et;
    private EditText items_add_picture_et;
    private EditText items_add_price_et;
    private TextInputLayout items_add_price;
    private Spinner items_add_currency;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextInputLayout items_add_name;
        final TextInputLayout items_add_description;
        final TextInputLayout items_add_picture;

        if (getIntent().getStringExtra(ItemsActivity.EXTRA_CATEGORY) != null){
            Gson gson = new Gson();
            category = gson.fromJson(getIntent().getStringExtra(ItemsActivity.EXTRA_CATEGORY), Category.class);
        }else{
            finish();
        }

        items_add_name = findViewById(R.id.items_add_name);
        items_add_name_et = findViewById(R.id.items_add_name_et);
        items_add_description = findViewById(R.id.items_add_description);
        items_add_description_et = findViewById(R.id.items_add_description_et);
        items_add_picture = findViewById(R.id.items_add_picture);
        items_add_picture_et = findViewById(R.id.items_add_picture_et);
        items_add_price = findViewById(R.id.items_add_price);
        items_add_price_et = findViewById(R.id.items_add_price_et);

        items_add_currency = findViewById(R.id.items_currency);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        items_add_currency.setAdapter(adapter);
        items_add_currency.setSelection(0);

        FloatingActionButton fab = findViewById(R.id.items_save);
        fab.setOnClickListener(view -> {
            if (items_add_name_et.getText().length() == 0) {
                items_add_name.setErrorEnabled(true);
                items_add_name.setError(getString(R.string.items_add_name_empty_error));
            } else if (items_add_price_et.getText().length() == 0) {
                items_add_price.setErrorEnabled(true);
                items_add_price.setError(getString(R.string.items_add_price_empty_error));
            } else if (items_add_description_et.getText().length() == 0) {
                items_add_description.setErrorEnabled(true);
                items_add_description.setError(getString(R.string.items_add_description_empty_error));
            } else if (items_add_picture_et.getText().length() == 0) {
                new AlertDialog.Builder(this).setTitle(R.string.items_add_picture_empty_title).setMessage(R.string.items_add_picture_empty_msg)
                        .setPositiveButton(R.string.items_add_picture_empty_save, (dialog, which) -> {
                            saveItem();
                        }).setNegativeButton(R.string.items_add_picture_empty_cancel,  (dialog, which) -> {}).show();
            }else{
                saveItem();
            }
        });
    }

    private void saveItem() {
        Double price = 0d;
        try{
            price = Double.parseDouble(items_add_price_et.getText().toString());
        }catch (ClassCastException ignored){
            items_add_price.setErrorEnabled(true);
            items_add_price.setError(getString(R.string.items_add_price_error));
            return;
        }
        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();
        Item item = new Item("", false, items_add_name_et.getText().toString(), items_add_description_et.getText().toString(), price, new ArrayList<>(), items_add_picture_et.getText().toString(), items_add_currency.getSelectedItem().toString(), category.getName());
        firebaseDatabaseService.saveItem(item, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Snackbar.make(items_add_name_et, R.string.items_add_failed, Snackbar.LENGTH_LONG).show();
            }else{
                item.setSku(databaseReference.getKey());
                firebaseDatabaseService.saveItemInCategory(item,
                        (databaseError2, databaseReference2) -> {
                            if (databaseError2 != null) {
                                Snackbar.make(items_add_name_et, R.string.items_add_failed, Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(items_add_name_et, R.string.items_add_completed, Snackbar.LENGTH_SHORT).show();
                                ItemsAddActivity.this.finish();
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!items_add_name_et.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this).setTitle(R.string.items_add_sure_exit_title).setMessage(R.string.items_add_sure_exit_msg)
                    .setPositiveButton(R.string.items_add_sure_exit_yes, (dialog, which) -> {
                        this.finish();
                    }).setNegativeButton(R.string.items_add_sure_exit_no, (dialog, which) -> {
            }).show();
        }else{
            finish();
        }
    }
}
