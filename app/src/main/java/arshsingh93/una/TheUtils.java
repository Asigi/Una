package arshsingh93.una;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Student on 7/29/2015.
 */
public class TheUtils {
    private static int sTheme;
    public final static int THEME_GREEN = 0;
    public final static int THEME_BLUE = 1;
    public final static int THEME_RED = 2;

    private static SharedPreferences sharedPreferences;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        initializePref(activity);
        saveColorTheme();
        activity.finish();
        activity.getApplicationContext();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            case THEME_GREEN:
                activity.setTheme(R.style.GreenTheme);
                break;
            case THEME_BLUE:
                activity.setTheme(R.style.BlueTheme);
                break;
            case THEME_RED:
                activity.setTheme(R.style.RedTheme);
                break;
            default:
                activity.setTheme(R.style.GreenTheme);
                break;
        }
    }

    public static int getProperColor() {
            switch (sTheme) {
                case THEME_GREEN:
                    return 0xff56c367;
                case THEME_BLUE:
                    return 0xff00aac3;
                case THEME_RED:
                    return 0xffff4444; //just right
                    // return 0xffff0000; too red
                   // return 0xffc35b4e; too brown
            }
        return 0x56c367;
    }

    private static void initializePref(Activity activity) {
        if (sharedPreferences == null) {
            Log.e("TheUtils class", (sharedPreferences == null) + "");
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            Log.e("TheUtils class again", (sharedPreferences == null) + "");
        }
    }

    private static void saveColorTheme() {
        sharedPreferences.edit().putInt("Theme", sTheme).commit(); //TODO change to .apply rather than .commit
    }

    public static void loadColorTheme(Activity activity) {
        initializePref(activity);
        if (sharedPreferences.contains("Theme")) {
            sTheme = sharedPreferences.getInt("Theme", 0);
        }
    }

}
