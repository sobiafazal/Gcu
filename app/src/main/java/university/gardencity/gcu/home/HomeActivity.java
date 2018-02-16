package university.gardencity.gcu.home;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import university.gardencity.gcu.R;
import university.gardencity.gcu.constants.AppConstants;
import university.gardencity.gcu.constants.UrlConstants;
import university.gardencity.gcu.home.adapter.ViewPagerAdapter;
import university.gardencity.gcu.login.LoginActivity;
import university.gardencity.gcu.managers.NetworkRequestManager;
import university.gardencity.gcu.managers.ServiceGenerator;
import university.gardencity.gcu.model.GenericResponse;
import university.gardencity.gcu.model.SendFirebaseTokenModel;
import university.gardencity.gcu.services.ApplicationLogger;
import university.gardencity.gcu.services.PreferenceManager;

import static university.gardencity.gcu.constants.UrlConstants.GCU_HOME;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,
        DialogInterface.OnClickListener, NotificationsFragment.OnNewNotificationListener, NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mHomeViewPager;
    private Toolbar mHomeToolbar;
    private TabLayout mHomeTabLayout;
    private ApplicationLogger mLogger;
    private DrawerLayout mHomeDrawerLayout;
    private NavigationView mHomeNavigationView;
    private PreferenceManager preferenceManager;
    private TextView mToolbarTitle;
    private AppCompatImageButton mToolbarMenu;
    private AppCompatImageView mToolbarIcon;
    private AppCompatImageView mToolbarSignOut;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog alertDialog;
    private TextView mUserName;
    private ImageView mUserPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferenceManager = new PreferenceManager(this);
        mLogger = new ApplicationLogger();
        mHomeViewPager = (ViewPager) findViewById(R.id.home_view_pager);
        mHomeToolbar = (Toolbar) findViewById(R.id.home_actionBar);
        mHomeTabLayout = (TabLayout) findViewById(R.id.home_tab_layout);
        mHomeDrawerLayout = (DrawerLayout) findViewById(R.id.home_navigation_drawer);
        mHomeNavigationView = (NavigationView) findViewById(R.id.home_navigation_view);
        mUserName = (TextView) mHomeNavigationView.getHeaderView(0).findViewById(R.id
                .app_header_user_name);
        mUserPic = (ImageView) mHomeNavigationView.getHeaderView(0).findViewById(R.id
                .app_header_profile_pic);

        mHomeNavigationView.setNavigationItemSelectedListener(this);
        alertDialog = new AlertDialog.Builder(this).setTitle("Logout Confirmation").setMessage
                ("Are you sure yo want to logout").setPositiveButton("Logout", this)
                .setNegativeButton
                        ("Cancel", this).create();

        initView();
    }

    @Override
    protected void onStart() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        super.onStart();
    }

    private void subscribeUserToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.NOT_TOPIC_COLLEGE);
        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.NOT_TOPIC_GLOBAL);
    }

    private void initView() {
        initToolbar();
        ViewPagerAdapter mVPAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        mHomeViewPager.setAdapter(mVPAdapter);
        mVPAdapter.notifyDataSetChanged();

        mHomeTabLayout.setupWithViewPager(mHomeViewPager);
        mHomeTabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
        saveTokenOnServer();
    }

    private void saveTokenOnServer() {
        String token = preferenceManager.getPreference("fb_token");
        String isTokenSent = preferenceManager.getPreference("fb_token_sent");
        String check = "no";
        if (isTokenSent.equalsIgnoreCase(check)) {

            SendFirebaseTokenModel model = new SendFirebaseTokenModel();
            model.setEmail(preferenceManager.getPreference("user_email"));
            model.setFbToken(token);

            NetworkRequestManager man = ServiceGenerator.getRestWebService();

            Call<GenericResponse> call = man.saveToken(model);
            call.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    handleResponse(response);
                    subscribeUserToTopic();
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {

                }
            });
        }
    }

    private void handleResponse(Response<GenericResponse> response) {
        GenericResponse resp = response.body();
        switch (resp.getStatus()) {
            case "1":
                Toast.makeText(HomeActivity.this, "Token saved successfully.",
                        Toast.LENGTH_SHORT).show();
                preferenceManager.saveString("fb_token_sent", "yes");
                break;
            case "2":
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
                break;
            case "3":
                Toast.makeText(this, "Incorrect parameters", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initToolbar() {
        mToolbarMenu = (AppCompatImageButton) mHomeToolbar.findViewById(R.id.app_toolbar_btn_menu);
        mToolbarIcon = (AppCompatImageView) mHomeToolbar.findViewById(R.id.app_toolbar_icon);
        mToolbarSignOut = (AppCompatImageView) mHomeToolbar.findViewById(R.id.app_toolbar_btn_sign_out);

        mToolbarMenu.setOnClickListener(this);
        mToolbarIcon.setOnClickListener(this);
        mToolbarSignOut.setOnClickListener(this);

        mToolbarTitle = (TextView) mHomeToolbar.findViewById(R.id.app_toolbar_label);
        String label = "Hi " + preferenceManager.getPreference("user_name");
        mUserName.setText(label);
        Picasso.with(this).load(preferenceManager.getPreference("user_profile_pic")).into(mUserPic);
        setSupportActionBar(mHomeToolbar);
        ActionBar ab = getSupportActionBar();
        try {
            ab.setDisplayShowTitleEnabled(false);

        } catch (NullPointerException ex) {
            mLogger.logError("Home Activity: Line 43", ex.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_toolbar_btn_menu:
                mHomeDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.app_toolbar_icon:
                openViewInBrowser(GCU_HOME);
                break;
            case R.id.app_toolbar_btn_sign_out:
                alertDialog.show();
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            PreferenceManager prefMan = new PreferenceManager(HomeActivity.this);
                            mGoogleApiClient.clearDefaultAccountAndReconnect();
                            prefMan.removeLoginPreferences();
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(HomeActivity.this, "Can't sign out",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alertDialog = null;
    }

    @Override
    public void onNewNotificationRequest() {
        Toast.makeText(this, "Generate new notification", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, SendNotificationActivity.class));

    }

    // Menu item handler
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_gen_courses:
                startActivity(new Intent(this, CourseActivity.class));
                break;
            case R.id.menu_gen_placement:
                startActivity(new Intent(this, PlacementActivity.class));
                break;
            case R.id.menu_gen_contact:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.id.menu_gen_360_view:
                openViewInBrowser(UrlConstants.GCU_360_VIEW);
                break;
        }
        return true;
    }

    private void openViewInBrowser(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            new ApplicationLogger().logDebug("Placement", ex.getMessage());
        }
    }
}
