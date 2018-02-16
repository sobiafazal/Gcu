package university.gardencity.gcu.services;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {
    private Context ctx;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEdit;

    public PreferenceManager(Context ctx) {
        this.ctx = ctx;
        sp = ctx.getSharedPreferences("General", Context.MODE_PRIVATE);
        spEdit = sp.edit();
    }

    public void saveString(String key, String value) {
        spEdit.putString(key, value);
        spEdit.commit();
    }

    public String getPreference(String key) {
        return sp.getString(key, "NA");
    }

    public void saveBoolean(String key, boolean value) {

    }

    public void removeLoginPreferences() {
        spEdit.clear();
        spEdit.commit();
    }
}
