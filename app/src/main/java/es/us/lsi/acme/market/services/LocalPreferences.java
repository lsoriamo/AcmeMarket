package es.us.lsi.acme.market.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;

import es.us.lsi.acme.market.AcmeMarketApplication;
import es.us.lsi.acme.market.entities.Actor;

public class LocalPreferences {
    private static final String PREFS_NAME = "AcmeSuperMarketPreferences";

    private static LocalPreferences localPreferences;

    public static LocalPreferences getLocalPreferencesInstance() {
        if (localPreferences == null) {
            localPreferences = new LocalPreferences();
        }
        return localPreferences;
    }

    private LocalPreferences() {

    }

    public boolean saveLocalUserInformation(Context context, Actor ownUser) {
        if (ownUser == null || ownUser.get_id().isEmpty())
            return false;

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        try {
            ObjectMapper mapper = new ObjectMapper();
            editor.putString(LocalPreferencesValues.LocalUser.name(), mapper.writeValueAsString(ownUser));
            editor.apply();
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void clearLocalPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }

    public static String getLocalUserInformationId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            userLogout();
            return "";
        }
    }

    public static void userLogout() {
        FirebaseAuth.getInstance().signOut();
        LocalPreferences.clearLocalPreferences(AcmeMarketApplication.getContext());
    }

    private enum LocalPreferencesValues {
        LocalUser
    }
}
