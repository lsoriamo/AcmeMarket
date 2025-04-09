package es.us.lsi.acme.market;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import javax.annotation.Nullable;

public class AppToolbarBaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toolbar = findViewById(R.id.toolbar_global);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Esto ya se encarga del botón atrás de la Toolbar
        handleBackPressed();
        return true;
    }

    private void handleBackPressed() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
