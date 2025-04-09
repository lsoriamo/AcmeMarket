package es.us.lsi.acme.market;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import es.us.lsi.acme.market.R;

public class RegisterActivity extends AppToolbarBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
