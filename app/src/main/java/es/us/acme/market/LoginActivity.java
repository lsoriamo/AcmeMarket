package es.us.acme.market;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.us.acme.market.entities.Actor;
import es.us.acme.market.services.FirebaseDatabaseService;
import es.us.acme.market.services.LocalPreferences;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_READ_CONTACTS = 0x457;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private static final Integer RC_SIGN_IN = 0x124;
    private Button signinButtonGoogle;
    private Button signinButtonMail;
    private Button login_button_register;
    private ProgressBar progressBar;
    private AutoCompleteTextView login_email_et;
    private EditText login_pass_et;
    private ScrollView login_main_layout;
    private TextInputLayout login_email;
    private TextInputLayout login_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.login_progress);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        login_main_layout = findViewById(R.id.login_main_layout);
        signinButtonGoogle = findViewById(R.id.login_button_google);
        signinButtonMail = findViewById(R.id.login_button_mail);
        login_email_et = findViewById(R.id.login_email_et);
        login_pass_et = findViewById(R.id.login_pass_et);
        login_button_register = findViewById(R.id.login_button_register);
        login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_pass);
        signinButtonGoogle.setOnClickListener(v -> attempLoginGoogle());
        signinButtonMail.setOnClickListener(v -> {
            attemptLoginEmail();
        });

        login_button_register.setOnClickListener(l -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(MainActivity.EXTRA_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
            startActivity(intent);
        }

    }

    /*  Gestión de métodos de registro y autentivación */

    private void attemptLoginEmail() {
        login_email.setError(null);
        login_pass.setError(null);
        if (login_email_et.getText().length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(login_email_et.getText().toString()).matches()) {
            login_email.setErrorEnabled(true);
            login_email.setError(getString(R.string.login_email_not_valid_short));
        } else if (login_pass_et.getText().length() == 0) {
            login_pass.setErrorEnabled(true);
            login_pass.setError(getString(R.string.login_pass_not_valid_short));
        } else {
            signInEmail();
        }
    }

    private void attempLoginGoogle() {
        hideLoginButton(true);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /* Acceso y registro mediante Firebase Auth con email y pass */

    private void signInEmail() {
        hideLoginButton(true);
        if (mAuth == null)
            mAuth = FirebaseAuth.getInstance();

        if (mAuth != null) {
            mAuth.signInWithEmailAndPassword(login_email_et.getText().toString(), login_pass_et.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (!task.isSuccessful() || task.getResult().getUser() == null) {
                            showErrorDialogMail();
                        } else if (!task.getResult().getUser().isEmailVerified()) {
                            showErrorEmailVerified(task.getResult().getUser());
                        } else {
                            FirebaseUser userFirebase = task.getResult().getUser();
                            Actor user = new Actor(userFirebase.getUid(), userFirebase.getDisplayName(), "", userFirebase.getEmail(), userFirebase.getPhoneNumber(), Actor.ActorRole.CONSUMER, Calendar.getInstance().getTime());
                            checkUserDatabaseLogin(user);
                        }
                    });
        } else {
            showGooglePlayServicesError();
        }
    }

    private void showGooglePlayServicesError() {
        Snackbar.make(login_button_register, R.string.login_play_services_error, Snackbar.LENGTH_LONG).setAction(R.string.login_play_services_error_go, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en")));
                }
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showErrorDialog();
    }

    /* Registro y acceso con botón de Google */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null && result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                assert acct != null;
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                if (mAuth == null)
                    mAuth = FirebaseAuth.getInstance();

                if (mAuth != null) {
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser userFirebase = task.getResult().getUser();
                                    Actor user = new Actor(userFirebase.getUid(), acct.getGivenName(), acct.getFamilyName(), acct.getEmail(), "", Actor.ActorRole.CONSUMER, Calendar.getInstance().getTime());
                                    checkUserDatabaseLogin(user);
                                } else {
                                    showErrorDialog();
                                }
                            });
                } else {
                    showGooglePlayServicesError();
                }
            } else {
                showErrorDialog();
            }
        }
    }

    /* Transición a la pantalla principal */

    private void checkUserDatabaseLogin(Actor user) {
        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance(user.get_id());
        LocalPreferences localPreferences = LocalPreferences.getLocalPreferencesInstance();
        firebaseDatabaseService.getUser().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null && localPreferences.saveLocalUserInformation(LoginActivity.this, dataSnapshot.getValue(Actor.class))) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra(MainActivity.EXTRA_USER_ID, user.get_id());
                    startActivity(intent);
                    finish();
                } else {
                    FirebaseDatabaseService.getServiceInstance().saveUser(user).addOnCompleteListener(listener -> {
                        if (listener.isSuccessful() && localPreferences.saveLocalUserInformation(LoginActivity.this, user)) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra(MainActivity.EXTRA_USER_ID, user.get_id());
                            startActivity(intent);
                            finish();
                        } else {
                            showErrorDialog();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showErrorDialog();
            }
        });
    }

    /* Diálogos de errores derivados del acceso mediante Firebase */

    private void showErrorDialog() {
        hideLoginButton(false);
        Snackbar.make(signinButtonGoogle, R.string.login_error, Snackbar.LENGTH_SHORT).show();
    }

    private void showErrorDialogMail() {
        hideLoginButton(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(LoginActivity.this.getString(R.string.login_error_mail))
                .setNeutralButton(R.string.login_error_mail_retry, (dialog, which) -> {
                })
                .setNegativeButton(R.string.login_error_reset_pass, (dialog, which) -> {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(login_email_et.getText().toString())
                            .addOnCompleteListener(task -> {
                                Snackbar.make(login_email_et,
                                        String.format(getString(R.string.register_reset_mail_sent), login_email_et.getText().toString()), Snackbar.LENGTH_LONG).show();
                            }).addOnFailureListener(e -> {
                        Snackbar.make(login_email_et, getString(R.string.register_reset_mail_error), Snackbar.LENGTH_LONG).show();
                    });
                })
                .setPositiveButton(R.string.login_error_mail_register, (dialog, which) -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class))).show();
    }

    private void showErrorEmailVerified(FirebaseUser user) {
        hideLoginButton(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(LoginActivity.this.getString(R.string.login_error_email_verified))
                .setNegativeButton(R.string.login_error_email_verified_yes, (dialog, which) -> user.sendEmailVerification().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Snackbar.make(login_email_et,
                                String.format(getString(R.string.register_verification_mail_sent), user.getEmail()), Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(login_email_et,
                                getString(R.string.register_verification_mail_error), Snackbar.LENGTH_LONG).show();
                    }
                }))
                .setPositiveButton(R.string.login_error_email_verified_no, (dialog, which) -> {

                }).show();
    }

    /* Mostgrar u ocultar barra de progreso y componentes */

    private void hideLoginButton(boolean hide) {
        TransitionSet transitionSet = new TransitionSet();
        Transition layoutFade = new AutoTransition();
        layoutFade.setDuration(1000);
        transitionSet.addTransition(layoutFade);

        if (hide) {
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
            progressBar.setVisibility(View.VISIBLE);
            signinButtonGoogle.setVisibility(View.GONE);
            signinButtonMail.setVisibility(View.GONE);
            login_button_register.setVisibility(View.GONE);
            signinButtonGoogle.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            login_email_et.setEnabled(false);
            login_pass_et.setEnabled(false);
        } else {
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
            progressBar.setVisibility(View.GONE);
            signinButtonGoogle.setVisibility(View.VISIBLE);
            signinButtonMail.setVisibility(View.VISIBLE);
            login_button_register.setVisibility(View.VISIBLE);
            signinButtonGoogle.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            login_email_et.setEnabled(true);
            login_pass_et.setEnabled(true);
            login_main_layout.setVisibility(View.VISIBLE);
        }
    }

    /* Gestión de los permisos para el acceso a los correos electrónicos registrados en el dispositivo. Esto facilitará la elección del correo de login o registro */

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(login_email_et, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, v -> requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS));
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // populateAutoComplete();
            }
        }
    }


}

