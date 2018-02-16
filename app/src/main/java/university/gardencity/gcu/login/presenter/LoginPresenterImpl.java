package university.gardencity.gcu.login.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import university.gardencity.gcu.login.LoginActivity;
import university.gardencity.gcu.login.view.LoginView;


public class LoginPresenterImpl implements LoginPresenter {
    private LoginView lView;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;

    public LoginPresenterImpl(final LoginView lView, LoginActivity ctx) {
        this.lView = lView;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .enableAutoManage(ctx, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        lView.onServerError();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    @Override
    public Intent validateUser() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        return googleSignInIntent;
    }
}
