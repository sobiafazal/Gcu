package university.gardencity.gcu.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import university.gardencity.gcu.R;
import university.gardencity.gcu.constants.AppConstants;
import university.gardencity.gcu.home.model.AboutModel;
import university.gardencity.gcu.managers.NetworkRequestManager;
import university.gardencity.gcu.managers.ServiceGenerator;
import university.gardencity.gcu.services.ApplicationLogger;

import static university.gardencity.gcu.constants.AppConstants.PLACEMENT_ID;

public class PlacementActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ApplicationLogger mLogger;
    private TextView mContent;
    private ProgressBar mProgress;
    private Button mKnowMore;
    ScrollView mMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement);
        mLogger = new ApplicationLogger();
        toolbar = (Toolbar) findViewById(R.id.activity_placement_toolbar);
        TextView mToolbarTitle = (TextView) toolbar.findViewById(R.id.child_toolbar_title);
        mToolbarTitle.setText(AppConstants.TOOLBAR_TITLE_PLACEMENT_ACTIVITY);
        initToolbar();

        mContent = (TextView) findViewById(R.id.activity_placement_content);
        mProgress = (ProgressBar) findViewById(R.id.activity_placement_progress);
        mMain = (ScrollView) findViewById(R.id.activity_placement_scrollView);
        mKnowMore = (Button) findViewById(R.id.activity_placement_btn);

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        try {
            ab.setDisplayShowTitleEnabled(false);

        } catch (NullPointerException ex) {
            mLogger.logDebug("SendNotification", ex.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPlacementContent();
    }

    private void getPlacementContent() {
        mProgress.setVisibility(View.VISIBLE);
        mMain.setVisibility(View.GONE);
        NetworkRequestManager nMan = ServiceGenerator.getRestWebService();
        Call<AboutModel> call = nMan.getPlacementContent(PLACEMENT_ID);
        call.enqueue(new Callback<AboutModel>() {
            @Override
            public void onResponse(Call<AboutModel> call, Response<AboutModel> response) {
                setContent(response.body());
            }

            @Override
            public void onFailure(Call<AboutModel> call, Throwable t) {
                Toast.makeText(PlacementActivity.this, "Something went wrong.\nPlease try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setContent(final AboutModel cont) {
        this.mContent.setText(cont.getAbout());
        mKnowMore.setText(cont.getLink());

        mKnowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWebsite(cont.getLinkAction());
            }
        });

        mProgress.setVisibility(View.GONE);
        mMain.setVisibility(View.VISIBLE);
    }

    private void startWebsite(String linkAction) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkAction));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            new ApplicationLogger().logDebug("Placement", ex.getMessage());
        }
    }
}
