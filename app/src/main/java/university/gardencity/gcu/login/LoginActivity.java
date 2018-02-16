package university.gardencity.gcu.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import university.gardencity.gcu.R;
import university.gardencity.gcu.home.HomeActivity;
import university.gardencity.gcu.login.view.LoginView;
import university.gardencity.gcu.services.ApplicationLogger;
import university.gardencity.gcu.services.PreferenceManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginView {
    private SignInButton mGoogleSignInButton;
    private int SIGN_IN_REQUEST_CODE = 101;
    private Intent mGoogleSignInIntent;
    private ApplicationLogger mLogger;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLogger = new ApplicationLogger();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        progressDialog = new ProgressDialog(this);
        initView();
    }

    private void initView() {
        mGoogleSignInButton = (SignInButton) findViewById(R.id.login_button_googleSignIn);
        mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);
        mGoogleSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mLogger.logDebug("Login", "Started login process");

        progressDialog.setTitle("Signing in with google");
        progressDialog.setMessage("Please wait while we connect to your google account.");
        progressDialog.show();
        mGoogleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(mGoogleSignInIntent, SIGN_IN_REQUEST_CODE);
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailure() {

    }

    @Override
    public void onServerError() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult mResult = Auth.GoogleSignInApi
                    .getSignInResultFromIntent(data);
            progressDialog.dismiss();
            handleSignInResult(mResult);
        }
    }

    private void handleSignInResult(GoogleSignInResult mResult) {
        mLogger.logDebug("Login", "result: " + mResult.getStatus().getStatusMessage());
        if (mResult.isSuccess()) {
            GoogleSignInAccount signInAccount = mResult.getSignInAccount();
            PreferenceManager prefManager = new PreferenceManager(this);
            try {
                prefManager.saveString("user_name", signInAccount.getDisplayName());
                prefManager.saveString("user_email", signInAccount.getEmail());
                prefManager.saveString("user_last_name", signInAccount.getFamilyName());
                prefManager.saveString("user_profile_pic", String.valueOf(signInAccount.getPhotoUrl()));
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } catch (NullPointerException ex) {
                mLogger.logError("Login", ex.getMessage());
            }
        } else {
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_LONG).show();
        }
    }
}
