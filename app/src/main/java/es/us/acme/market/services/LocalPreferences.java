package es.us.acme.market.services;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import es.us.acme.market.AcmeMarketApplication;
import es.us.acme.market.LoginActivity;
import es.us.acme.market.entities.Actor;

public class LocalPreferences {
    private static final String PREFS_NAME = "AcmeSuperMarketPreferences";
    public static final boolean DEBUG_MODE = true;

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
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void clearLocalPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }

    public static String getLocalUserInformationId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            userLogout(false);
            return "";
        }
    }

    public static void userLogout(boolean complete) {
        FirebaseAuth.getInstance().signOut();
        if (complete) {
            LocalPreferences.clearLocalPreferences(AcmeMarketApplication.getContext());
        }
        Intent intent = new Intent(AcmeMarketApplication.getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        AcmeMarketApplication.getContext().startActivity(intent);
    }

    private enum LocalPreferencesValues {
        LocalUser
    }
}
