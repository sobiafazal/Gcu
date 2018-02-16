package university.gardencity.gcu.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Calendar;

public class MyFireBaseService extends FirebaseInstanceIdService {
    private String fb_token = "";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        fb_token = FirebaseInstanceId.getInstance().getToken();
        new ApplicationLogger().logDebug("Token", fb_token + " " + Calendar.getInstance());
        PreferenceManager prefMan = new PreferenceManager(getApplicationContext());
        prefMan.saveString("fb_token", fb_token);
        prefMan.saveString("fb_token_sent", "no");
    }
}
