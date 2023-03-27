package es.us.lsi.acme.market;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class AcmeMarketApplication extends Application {
    private static AcmeMarketApplication instance;

    public AcmeMarketApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }

    public static Context getContext() {
        return instance;
    }
}
