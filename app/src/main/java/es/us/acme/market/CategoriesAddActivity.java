package es.us.acme.market;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import es.us.acme.market.entities.Category;
import es.us.acme.market.services.FirebaseDatabaseService;

public class CategoriesAddActivity extends AppCompatActivity {
    EditText categories_add_name_et;
    EditText categories_add_description_et;
    EditText categories_add_picture_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextInputLayout categories_add_name;
        final TextInputLayout categories_add_description;
        final TextInputLayout categories_add_picture;

        categories_add_name = findViewById(R.id.categories_add_name);
        categories_add_name_et = findViewById(R.id.categories_add_name_et);
        categories_add_description = findViewById(R.id.categories_add_description);
        categories_add_description_et = findViewById(R.id.categories_add_description_et);
        categories_add_picture = findViewById(R.id.categories_add_picture);
        categories_add_picture_et = findViewById(R.id.categories_add_picture_et);

        FloatingActionButton fab = findViewById(R.id.category_save);
        fab.setOnClickListener(view -> {
            if (categories_add_name_et.getText().length() == 0) {
                categories_add_name.setErrorEnabled(true);
                categories_add_name.setError(getString(R.string.categories_add_name_empty_error));
            } else if (categories_add_description_et.getText().length() == 0) {
                categories_add_description.setErrorEnabled(true);
                categories_add_description.setError(getString(R.string.categories_add_description_empty_error));
            } else if (categories_add_picture_et.getText().length() == 0) {
                new AlertDialog.Builder(this).setTitle(R.string.categories_add_picture_empty_title).setMessage(R.string.categories_add_picture_empty_msg)
                        .setPositiveButton(R.string.categories_add_picture_empty_save, (dialog, which) -> {
                            saveCategory();
                        }).setNegativeButton(R.string.categories_add_picture_empty_cancel,  (dialog, which) -> {}).show();
            }else{
                saveCategory();
            }
        });
    }

    private void saveCategory() {
        FirebaseDatabaseService.getServiceInstance().saveCategory(new Category(categories_add_name_et.getText().toString(), categories_add_description_et.getText().toString(), categories_add_picture_et.getText().toString()),
                (databaseError, databaseReference) -> {
                    if (databaseError != null){
                        Snackbar.make(categories_add_name_et, R.string.categories_add_failed, Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(categories_add_name_et, R.string.categories_add_completed, Snackbar.LENGTH_SHORT).show();
                        CategoriesAddActivity.this.finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (!categories_add_name_et.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this).setTitle(R.string.categories_add_sure_exit_title).setMessage(R.string.categories_add_sure_exit_msg)
                    .setPositiveButton(R.string.categories_add_sure_exit_yes, (dialog, which) -> {
                        this.finish();
                    }).setNegativeButton(R.string.categories_add_sure_exit_no, (dialog, which) -> {
            }).show();
        }
    }
}
