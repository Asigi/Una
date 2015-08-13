package arshsingh93.una;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
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

    private static List<Blog> myBlogList = new ArrayList<>();
    private static List<Blog> foreignBlogList = new ArrayList<>(); //list of blogs not written by the user

    private static ParseRelation<ParseObject> myBlogLikeRelations; //this will be set when user updates their like blogs
    private static List<ParseObject> myParseLikeBlogs; //this will be set when user updates their like blogs
    private static ArrayList<Blog> myBlogLikedList; //this will be set when user updates their like blogs


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

    /**
     * Set the theme of the activity, according to the configuration.
     */
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
     *
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
     *
     * @param activity the activity that this method is called from.
     */
    public static void loadColorTheme(Activity activity) {
        initializePref(activity);
        if (sharedPreferences.contains("Theme")) {
            sTheme = sharedPreferences.getInt("Theme", 0);
        }
    }




    //===================================================================================================================


    /**
     * Update the list of blogs that this user has written.
     */
    public static void updateBlogList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.whereEqualTo("User", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> blogList, ParseException e) {
                if (e == null) {
                    for (ParseObject o : blogList) {
                        Blog b = new Blog(o);
                        myBlogList.add(b);

                    }
                    Log.d("BlogListFragmentTEST", "Retrieved " + blogList.size() + " blogs");
                } else {
                    Log.d("BlogListFragmentTEST", "Error: " + e.getMessage());
                }
            }
        });
    }

    /**
     *
     * @param position of the blog in the list
     * @return Blog corresponding to the position.
     */
        public static Blog getFromBlogList(String theType, int position) {
            Log.d("TheUtils", "in getFromBlogList");
            if (theType == BlogListFragment.BLOG_MINE) {
                Log.d("TheUtils", "in getFromBlogList blog_mine");
                return myBlogList.get(position);
            } else if (theType == BlogListFragment.BLOG_FOREIGN) {
                Log.d("TheUtils", "in getFromBlogList, blog_foreign list: " + foreignBlogList);
                Log.d("TheUtils", "blog_foreign, likelist: " + myBlogLikedList);
                return foreignBlogList.get(position);
            } else if (theType == BlogListFragment.BLOG_LIKE) {
                Log.d("TheUtils", "in getFromBlogList, myBlogLikedList is " + myBlogLikedList);
                return myBlogLikedList.get(position);
            } else {
                Log.d("TheUtils", "in getFromBlogList ELSE");
                return new Blog("TheUtils mistake", "getFromBlogList method", ParseUser.getCurrentUser());
            }
        }

    /**
     *
     * @return the list of my blogs
     */
    public static List<Blog> getMyBlogList() {
        return myBlogList;
    }


    /**
     * Update the list of blogs that this user has not written.
     */
    public static void updateForeignBlogList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.whereNotEqualTo("User", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> blogList, ParseException e) {
                if (e == null) {
                    foreignBlogList.clear(); //prevent multiple sets of blogs from being added on top of one another.
                    for (ParseObject o : blogList) {
                        Blog b = new Blog(o);
                        foreignBlogList.add(b);
                    }
                    Log.d("TheUtils", "Retrieved " + blogList.size() + " foreign blogs");
                    Log.d("TheUtils", "I now have " + foreignBlogList.size() + " foreign blogs");
                } else {
                    Log.d("TheUtils", "Error: " + e.getMessage());
                }
            }
        });
    }

    public static final String MY_LIKE_BLOGS = "the blogs I like";

    /**
     * Loads and updates the relations of liked blogs as well as the related lists.
     * It also caches queries to ParseLocal.
     */
    public static void loadLikedBlogs() {
        myBlogLikeRelations = ParseUser.getCurrentUser().getRelation("blogLikes");
        Log.d("TheUtils", "In updateBlogLikeRelations, name of myBlogLikeRelations: " + myBlogLikeRelations);

        myBlogLikeRelations.getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Log.d("TheUtils", "loadLikeBlogs, in done method");
                if (e == null) {
                    Log.d("TheUtils", "no ParseException");
                    myParseLikeBlogs = list;
                    updateBlogLikeList();
                    saveBlogLikeLocal(list);
                } else {
                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder(theContext);
                    builder.setTitle("Error");
                    builder.setMessage("" + e);
                    builder.show();
                     */

                    Log.d("TheUtils", "ParseException for blogLikeRelation.getQuery(): " + e);
                }
            }
        });
    }

    /**
     * Saves the ParseObject list to ParseLocal.
     * @param blogLikeList a list of ParseObjects
     */
    public static void saveBlogLikeLocal(final List<ParseObject> blogLikeList) {
        // Release any objects previously pinned for this query.
        ParseObject.unpinAllInBackground(MY_LIKE_BLOGS, blogLikeList, new DeleteCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("TheUtils", "In saveBlogLikeLocal, unpinAll, error: " + e);
                    return;
                }
                // Add the latest results for this query to the cache.
                ParseObject.pinAllInBackground(MY_LIKE_BLOGS, blogLikeList);
            }
        });
    }


    /**
     * Adds an object to the blogLike relation list.
     * @param theObject is a ParseObject that is a blog on Parse.
     */
    private static void addToBlogLikeRelation(ParseObject theObject) {
        //Log.d("TheUtils", "theObject parameter is null: " + (theObject == null));
        //Log.d("TheUtils", "myBlogLikeRelations is null: " + (myBlogLikeRelations == null));
        myBlogLikeRelations.add(theObject);
    }

    /**
     * Checks if the blog exists in the list of liked blogs.
     * @param theBlog the blog to check
     * @return true if exists, false otherwise
     */
    public static boolean existsInBlogLikedList(Blog theBlog) {
        Log.d("TheUtils", "theBlog id: " + theBlog.getId());
        Log.d("TheUtils", myBlogLikedList + " existsInBlogLikeList? only if its not null");
        if (myBlogLikedList == null) {
            loadLikedBlogs();
        }
        Log.d("TheUtils", myBlogLikedList + " existsInBlogLikeList? only if its not null");
        if (myBlogLikedList.contains(theBlog)) {
            return true;
        }
        Log.d("TheUtils", theBlog.getId() + " existsInBlogLikeList: false");
        return false;
    }



    /**
     * Uses the list of ParseObject blogs to update a list of Blogs that the user likes.
     */
    private static void updateBlogLikeList() {
        myBlogLikedList = new ArrayList<>();
        for (ParseObject pObject : myParseLikeBlogs) {
            Blog aBlog = new Blog(pObject);
            myBlogLikedList.add(aBlog);
        }
        Log.d("TheUtils", "size of myParseLikeBlogs: " + myParseLikeBlogs.size());
        Log.d("TheUtils", "myBlogLikedList: " +  myBlogLikedList);
    }

    /**
     * Returns the list of liked blogs.
     * @return the list that contains the stuff I like.
     */
    public static ArrayList<Blog> getBlogLikeList() {
        return myBlogLikedList; //TODO maybe return a clone?
    }

    /**
     * This updates the list of Blog objects (not ParseObject) that this user likes.
     * TODO right now it is not necessary to call this method from BlogLookerFragment when user likes a blog because the
     * TODO ... list of blogs that the user likes is always updated when the user clicks on "blogs" in their profile page.
     * TODO Something to think about: what happens if user is viewing a blog, likes it, and then clicks back button?
     * TODO ...Will that blog be added to the list of blogs the user sees when he backs out of the blog?
     * TODO ...JUST REALIZED: A user would't like a blog that is already on his like list. Instead, think about this:
     * TODO ...What if the user opens their like list, unlikes a blog and then goes back? Will the blog be removed from their
     * TODO ...list of liked blogs?
     * TODO ...ANS: I dont think so, because blogLikeList would not be updated, although blogLikeRelation would be.
     * @param theBlog the blog that the user liked.
     */
    private static void addToBlogLikeList(Blog theBlog) {
        myBlogLikedList.add(theBlog);
    }

    /**
     * I wrote this method to help keep my local parse store correct.
     * @param theObject a parse object blog.
     */
    public static void addToParseLikeBlogs(ParseObject theObject) {
        myParseLikeBlogs.add(theObject);
    }


    /**
     * Updates the various lists associated with liking blogs.
     * @param theBool depending on the boolean value, the current blog will be
     *                added to the like lists (if boolean is true) or the current
     *                blog will be removed from the like lists (if boolean is false).
     */
    public static void updateVariousLikeBlogLists(boolean theBool) {
        myBlogLikeRelations.add(myCurrentBlog.getBlog());
        myParseLikeBlogs.add(myCurrentBlog.getBlog());
        myBlogLikedList.add(myCurrentBlog);
    }


    /**
     * Sets the current blog that the user is looking at.
     * @param theBlog
     */
    public static void setCurrentBlog(Blog theBlog) {
        Log.d("TheUtils", "setCurrentBlog before: " + myCurrentBlog);
        myCurrentBlog = theBlog;
        Log.d("TheUtils", "setCurrentBlog after: " + myCurrentBlog);
    }

    /**
     * NOTE: current blog is set when a user chooses to look at a blog. It is not reset until the
     * user chooses to look at another blog.
     * @return the current blog that the user is looking at or the last blog the user looked at.
     */
    public static Blog getCurrentBlog() {
        return myCurrentBlog;
    }

    public static void incrementVote(int theNum) {
        myCurrentBlog.getBlog().increment("Vote", theNum);


    }




    /**
     *
     * @return the list of foreign blogs
     */
    public static List<Blog> getForeignBlogList() {
        return foreignBlogList;
    }

























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