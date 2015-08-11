package arshsingh93.una;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import arshsingh93.una.model.Blog;

/**
 * Created by Student on 7/29/2015.
 */
public class TheUtils {
    private static int sTheme;
    public final static int THEME_GREEN = 0;
    public final static int THEME_BLUE = 1;
    public final static int THEME_RED = 2;

    private static Blog myCurrentBlog;

    private static ArrayList<Blog> myBlogLikedList; //this will be initialized when user clicks blog button in profilefragment.

    private static List<ParseObject> myParseBlogs;
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

    /**
     * Called for updating buttons and such.
     * @return the prefered color that buttons and stuff should be.
     */
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

    /**
     * Saves the color theme that the user clicked on.
     */
    private static void saveColorTheme() {
        sharedPreferences.edit().putInt("Theme", sTheme).commit(); //TODO change to .apply rather than .commit
    }

    /**
     * Updates the app to the color theme that the user prefers (when app is opened).....I think....
     * @param activity the activity that this method is called from.
     */
    public static void loadColorTheme(Activity activity) {
        initializePref(activity);
        if (sharedPreferences.contains("Theme")) {
            sTheme = sharedPreferences.getInt("Theme", 0);
        }
    }

    /**
     * Takes in a list of parseobjects which were queried from from Parse using the relations.
     * This method also updates the list of Blog objects that are stored in this class.
     * @param theParseBlogList ParseObject blogs.
     */
    public static void setMyParseBlogs(List<ParseObject> theParseBlogList) {
        myParseBlogs = theParseBlogList;
        updateBlogLikeList();
    }

    /**
     * Uses the list of ParseObject blogs to update a list of Blogs that the user likes.
     */
    private static void updateBlogLikeList() {
        myBlogLikedList = new ArrayList<>();
        for (ParseObject pObject: myParseBlogs) {
            Log.d("TheUtils", "size of myParseBlogs: " + myParseBlogs.size());
            Blog aBlog = new Blog(pObject);
            myBlogLikedList.add(aBlog);
        }
    }

    public static ArrayList<Blog> getBlogLikeList() {
        return myBlogLikedList; //TODO maybe return a clone?
    }



    public static void setCurrentBlog(Blog theBlog) {
        Log.d("TheUtils", "setCurrentBlog before: " + myCurrentBlog);
        myCurrentBlog = theBlog;
        Log.d("TheUtils", "setCurrentBlog after: " + myCurrentBlog);
    }

    public static Blog getCurrentBlog() {
        return myCurrentBlog;
    }









//
//    public static ArrayList<Blog> getMyBlogLikedList() {
//        return myBlogLikedList;
//    }
//
//    public static void setMyBlogLikedList(ArrayList<Blog> myBlogLikedList) {
//        TheUtils.myBlogLikedList = myBlogLikedList;
//    }
//
//    public static void addToMyBlogList(Blog theBlog) {
//        if (!checkMyBlogListForBlog(theBlog)) {
//            myBlogLikedList.add(theBlog);
//            Log.d("TheUtils", "size of list is now: " + myBlogLikedList.size());
//            theBlog.getBlog().increment("Vote", 1);
//            theBlog.getBlog().saveInBackground();
//            saveMyBlogList();
//        }
//    }
//
//
//    public static boolean checkAndRemoveBlog(Blog theBlog) {
//        Log.d("TheUtils", "CheckAndRemoveBlog");
//        Log.d("TheUtils", "size: " + myBlogLikedList.size());
//        if (checkMyBlogListForBlog(theBlog)) {
//            myBlogLikedList.remove(theBlog);
//            theBlog.getBlog().increment("Vote", -2);
//            theBlog.getBlog().saveInBackground();
//            Log.d("TheUtils", "size: " + myBlogLikedList.size());
//            saveMyBlogList();
//            return true; //return true if the user did already like this blog.
//        } else {
//            theBlog.getBlog().increment("Vote", -1);
//            theBlog.getBlog().saveInBackground();
//            return false; //return false if the user had not already liked this blog
//        }
//    }
//
//
//    public static boolean checkMyBlogListForBlog(Blog theblog) {
//        for (Blog b: myBlogLikedList) {
//            if (b.equalsBlog(theblog)) {
//                Log.d("TheUtils", "checkMyBlogListForBlog returned true");
//                return true;
//            }
//        }
//        Log.d("TheUtils", "checkMyBlogListForBlog returned false");
//        return false;
//    }
//
//    public static void saveMyBlogList() {
//        //TODO only save when internet is available
//        Blog[] list = myBlogLikedList.toArray(new Blog[myBlogLikedList.size()]);
//        ParseUser.getCurrentUser().put("LikedBlogs", list);
//        ParseUser.getCurrentUser().saveInBackground();
//    }

}
