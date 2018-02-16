package university.gardencity.gcu.services;

import android.util.Log;

public class ApplicationLogger {

    public void logDebug(String tag, String value) {
        Log.wtf(tag, value);
    }

    public void logInfo(String tag, String value) {
        Log.i(tag, value);
    }

    public void logError(String tag, String value) {
        Log.e(tag, value);
    }
}
