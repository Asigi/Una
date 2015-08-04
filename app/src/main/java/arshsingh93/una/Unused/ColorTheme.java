package arshsingh93.una.Unused;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Student on 7/24/2015.
 */
public class ColorTheme {

    public static List<cTheme> CT_LIST = new ArrayList<>();



    static {
        CT_LIST.add(new cTheme("Green", "#119110", "#119121", "#11913f"));
        CT_LIST.add(new cTheme("Red", "#911101", "#911132", "#912123"));
        CT_LIST.add(new cTheme("Blue", "#00e034", "#159125", "#30b212"));

    }


    /**
     * A cTheme is an inner class used to represent one theme which the user can set the app to.
     */
    public static class cTheme {
        public String color;  //the name of the theme that will be displayed to user so that s/he can choose a theme.
        public String rgbTab; //the color for the tabs in the main activity
        public String rgbBar; //the color for the bar that holds the 'Una' and the options menu
        public String rgbOther; //the color for some other things

        //NOTE: each of these String values will be slightly different from each other. Maybe tab will be #ffffff but Bar will be #ffff10, etc.

        public cTheme(String theColor, String theTab, String theBar, String theOther) {
            color = theColor;
            rgbTab = theTab;
            rgbBar = theBar;
            rgbOther = theOther;
        }


        public String toString() {
            return color;
        }

    }
}
