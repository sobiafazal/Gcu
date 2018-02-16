package university.gardencity.gcu.home;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import university.gardencity.gcu.R;
import university.gardencity.gcu.constants.AppConstants;
import university.gardencity.gcu.services.ApplicationLogger;

public class SendNotificationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ApplicationLogger mLogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        mLogger = new ApplicationLogger();
        toolbar = (Toolbar) findViewById(R.id.send_notification_toolbar);
        TextView mToolbarTitle = (TextView) toolbar.findViewById(R.id.child_toolbar_title);
        mToolbarTitle.setText(AppConstants.TOOLBAR_TITLE_SEND_NOTIFICATION);
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        try {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.ic_back_icon);
            ab.setDisplayShowTitleEnabled(false);

        } catch (NullPointerException ex) {
            mLogger.logDebug("SendNotification", ex.getMessage());
        }
    }
}
