package arshsingh93.una;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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

    public static boolean FIRST_TIME_MAIN = false;

    private static int sTheme;
    public final static int THEME_GREEN = 0;
    public final static int THEME_BLUE = 1;
    public final static int THEME_RED = 2;

    /**
     * This is the current blog that the user is viewing. It is only reset when the
     * user looks at another blog.
     */
    private static Blog myCurrentBlog;

    private static List<Blog> myBlogList = new ArrayList<>();
    private static List<Blog> foreignBlogList = new ArrayList<>(); //list of blogs not written by the user

    public static ParseRelation<ParseObject> myBlogLikeRelations; //this will be set when user updates their like blogs
    private static List<ParseObject> myParseLikeBlogs; //this will be set when user updates their like blogs
    private static ArrayList<Blog> myBlogLikedList; //this will be set when user updates their like blogs

    /*
  * Gets set to true if there was no error while updating.
  * Gets set to false if there was an error while updating.
   */
    public static boolean updateDone = false; //set to true after updating

    /**
     * Gets set to the error message given while updating.
     */
    public static String blogError = "";

    /**
     * Local storage.
     */
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
    public static void loadMyBlogList() {
        Log.d("TheUtils", "In loadMyBlogList()");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.whereEqualTo("User", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> blogList, ParseException e) {
                if (e == null) {
                    for (ParseObject o : blogList) {
                        Blog b = new Blog(o);
                        myBlogList.add(b);
                        doneNotice(true);
                    }
                    Log.d("TheUtils", "In loadMyBlogList(), Retrieved " + blogList.size() + " blogs");
                } else {
                    doneNotice(false);
                    setBlogError(e.getMessage() + "");
                    Log.d("TheUtils", "In loadMyBlogList(), Error: " + e.getMessage());
                }
            }
        });

    }

    /**
     * Does not do work in background thread.
     * Update the list of blogs that this user has written.
     */
    public static void loadMyBlogList2() {
        Log.d("TheUtils", "In loadMyBlogList()");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.whereEqualTo("User", ParseUser.getCurrentUser());
        try {
            List<ParseObject> blogList = query.find();
            for (ParseObject o : blogList) {
                Blog b = new Blog(o);
                myBlogList.add(b);
                doneNotice(true);
            }
            Log.d("TheUtils", "In loadMyBlogList(), Retrieved " + blogList.size() + " blogs");

        } catch (ParseException e) {
            doneNotice(false);
            setBlogError(e.getMessage() + "");
            Log.d("TheUtils", "In loadMyBlogList(), Error: " + e.getMessage());
        }
    }

    /**
     * Called to update the variable that holds whether or not updating was succesful.
     *
     * @param theBool false if error, true otherwise.
     */
    public static void doneNotice(boolean theBool) {
        Log.d("TheUtils", "doneNotice entered. Current value is " + updateDone + ", with passed in value: " + theBool);
        updateDone = theBool;
        Log.d("TheUtils", "doneNotice exiting. Current value is " + updateDone);
    }

    /**
     * Called to update the string that holds the error value.
     *
     * @param theString error
     */
    public static void setBlogError(String theString) {
        Log.d("TheUtils", "setBlogError entered. Current value is " + blogError + ", with passed in value: " + theString);
        blogError = theString;
        Log.d("TheUtils", "setBlogError exiting. Current value is " + blogError);
    }

    /**
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
     * @return the list of my blogs
     */
    public static List<Blog> getMyBlogList() {
        return myBlogList;
    }


    /**
     * Update the list of blogs that this user has not written.
     */
    public static void loadForeignBlogList() {
        Log.d("TheUtils", "In loadForeignBlogList()");
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
                    doneNotice(true);
                    Log.d("TheUtils", "Retrieved " + blogList.size() + " foreign blogs");
                    Log.d("TheUtils", "I now have " + foreignBlogList.size() + " foreign blogs");
                } else {
                    doneNotice(false);
                    setBlogError(e.getMessage() + "");
                    Log.d("TheUtils", "Error: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Does not do work in background.
     * Update the list of blogs that this user has not written.
     */
    public static void loadForeignBlogList2() {
        Log.d("TheUtils", "In loadForeignBlogList()");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.whereNotEqualTo("User", ParseUser.getCurrentUser());
        try {
            List<ParseObject> blogList = query.find();
            foreignBlogList.clear(); //prevent multiple sets of blogs from being added on top of one another.
            for (ParseObject o : blogList) {
                Blog b = new Blog(o);
                foreignBlogList.add(b);
            }
            doneNotice(true);
            Log.d("TheUtils", "Retrieved " + blogList.size() + " foreign blogs");
            Log.d("TheUtils", "I now have " + foreignBlogList.size() + " foreign blogs");
        } catch (ParseException e) {
            doneNotice(false);
            setBlogError(e.getMessage() + "");
            Log.d("TheUtils", "Error: " + e.getMessage());
        }
    }




    public static final String MY_LIKE_BLOGS = "the blogs I like";

    /**
     * Loads and updates the relations of liked blogs as well as the related lists.
     * It also pins found query.
     */
    public static void loadLikedBlogs() {

        //TODO load from local first or save user before loading from online
        //ParseUser.getCurrentUser().getRelation("RandomMeaninglessStuffForTesting"); I thought this would create a new relation
        myBlogLikeRelations = ParseUser.getCurrentUser().getRelation("blogLikes");
        Log.d("TheUtils", "In loadLikedBlogs(), updateBlogLikeRelations, name of myBlogLikeRelations: " + myBlogLikeRelations + ".  PARSE API");
        myBlogLikeRelations.getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Log.d("TheUtils", "In loadLikedBlogs(), in done method. PARSE API");
                if (e == null) {
                    Log.d("TheUtils", "no ParseException");
                    myParseLikeBlogs = list;
                    updateBlogLikeList();
                    saveBlogLikePin(list);
                    doneNotice(true);
                } else {
                    doneNotice(false);
                    setBlogError(e.getMessage() + "");
                    Log.d("TheUtils", "In loadLikedBlogs(), ParseException for blogLikeRelation.getQuery(): " + e);
                }
            }
        });
    }

    /**
     * Does not do its work in background.
     * Loads and updates the relations of liked blogs as well as the related lists.
     *
     * It also pins found query.
     */
    public static void loadLikedBlogs2() {
        //ParseUser.getCurrentUser().getRelation("RandomMeaninglessStuffForTesting");  I thought this would create a new relation
        //TODO load from local first or save user before loading from online

        myBlogLikeRelations = ParseUser.getCurrentUser().getRelation("blogLikes");
        Log.d("TheUtils", "In loadLikedBlogs2(), updateBlogLikeRelations, name of myBlogLikeRelations: " + myBlogLikeRelations + ".  PARSE API");
        try {
            List<ParseObject> list = myBlogLikeRelations.getQuery().find();
            myParseLikeBlogs = list;
            updateBlogLikeList();
            saveBlogLikePin(list);
            doneNotice(true);
        } catch (ParseException e) {
            doneNotice(false);
            setBlogError(e.getMessage() + "");
            Log.d("TheUtils", "In loadLikedBlogs2(), ParseException for blogLikeRelation.getQuery(): " + e);
        }
    }

    /**
     * Saves the ParseObject list to ParseLocal.
     * @param blogLikeList a list of ParseObjects
     */
    public static void saveBlogLikePin(final List<ParseObject> blogLikeList) {
        Log.e("TheUtils", "In saveBlogLikePin, list size: " + blogLikeList.size());
        // Release any objects previously pinned for this query.
        ParseObject.unpinAllInBackground(MY_LIKE_BLOGS);
        // Add the latest results for this query to the cache.
        ParseObject.pinAllInBackground(MY_LIKE_BLOGS, blogLikeList);
        Log.e("TheUtils", "In saveBlogLikePin, pinning list");
    }

    /**
     * Search the pinned list of liked blog objects.
     * @return true if the current blog is in there.
     */
    public static boolean searchBlogLikeLocal() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog"); ///CHECK
        query.fromPin(MY_LIKE_BLOGS);
        try {
            List<ParseObject> list = query.find();
            Log.d("TheUtils", "In searchBlogLikeLocal(), list size is " + list.size());
            Log.d("TheUtils", "In searchBlogLikeLocal(), list is " + printList(list));
            if (list.contains(myCurrentBlog)) { //TODO check if this works properly. How does contains work?
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            Log.e("TheUtils", "searchBlogLikeLocal, ParseException: " + e.getMessage());
        }
        Log.e("TheUtils", "searchBlogLikeLocal, returning false");
        return false;
    }

    /**
     * This method just returns a String which holds the list of titles of the blogs.
     * Mainly used for testing purposes.
     * @param list is a list of ParseObjects (parse object blogs)
     * @return a String of blog titles.
     */
    private static String printList(List<ParseObject> list) {
        String total = "";
        for (int i = 0; i < list.size(); i++) {
            total += " "  + list.get(i).get("Title") + ", ";
        }
        return total;
    }

    /**
     * Loads liked blogs from device.
     */
    public static void loadBlogLikePinned() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog"); ///CHECK
        query.fromPin(MY_LIKE_BLOGS);
        try {
            List<ParseObject> list = query.find();
            Log.d("TheUtils", "In loadBlogLikePinned(), list size is " + list.size());
            Log.d("TheUtils", "In loadBlogLikePinned(), list is " + printList(list));
            myParseLikeBlogs = list;
            updateBlogLikeList();
            saveBlogLikePin(list);
            doneNotice(true);
        } catch (ParseException e) {
            doneNotice(false);
            setBlogError(e.getMessage() + "");
            Log.e("TheUtils", "loadhBlogLikePinned(), ParseException: " + e.getMessage());
        }
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
            loadLikedBlogs2();
            if (updateDone) {
                doneNotice(false);
            } //else error
        }
        Log.d("TheUtils", myBlogLikedList + " existsInBlogLikeList? only if its not null (2)");
        if (myBlogLikedList.contains(theBlog)) {
            return true;
        }
        Log.d("TheUtils", theBlog.getId() + " existsInBlogLikeList: false");
        return false;
    }



    /**
     * Uses the list of ParseObject blogs to update a list of Blogs that the user likes.
     */
    private static void updateBlogLikeList() { //TODO check this
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
        updateBlogLikeList();
        return myBlogLikedList; //TODO maybe return a clone? no
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
        if (theBool) {
            Log.d("TheUtils", "In updateVariousLikeBlogLists, theBool is true");
            int countZ2 = -999;
            try {
                countZ2 = myBlogLikeRelations.getQuery().count();
            } catch (ParseException e) {
                Log.d("TheUtils", "In updateVariousLikeBlogLists, error count1: " + e);
            }
            Log.d("TheUtils", "In updateVariousLikeBlogLists, relation size before adding: " + countZ2);

            myBlogLikeRelations.add(myCurrentBlog.getBlog());


            int countZ = -999;
            try {
                countZ = myBlogLikeRelations.getQuery().count();
            } catch (ParseException e) {
                Log.d("TheUtils", "In updateVariousLikeBlogLists, error count1: " + e);
            }
            Log.d("TheUtils", "In updateVariousLikeBlogLists, relation size after adding: " + countZ);
            myBlogLikeRelations.remove(myCurrentBlog.getBlog());
            int countZ3 = -999;
            try {
                countZ3 = myBlogLikeRelations.getQuery().count();
            } catch (ParseException e) {
                Log.d("TheUtils", "In updateVariousLikeBlogLists, error count2: " + e);
            }
            Log.d("TheUtils", "In updateVariousLikeBlogLists, after removal size of BlogLikeRelations :"
                    + countZ3);



            myParseLikeBlogs.add(myCurrentBlog.getBlog());
            myBlogLikedList.add(myCurrentBlog);
            saveBlogLikePin(myParseLikeBlogs);

        } else {

            int count1 = -999;
            try {
                count1 = myBlogLikeRelations.getQuery().count();
            } catch (ParseException e) {
                Log.d("TheUtils", "In updateVariousLikeBlogLists, error count1: " + e);
            }
            Log.d("TheUtils", "In updateVariousLikeBlogLists, before removal size of BlogLikeRelations :"
                    + count1);


            //first query the relation for the similar blog.
            ParseObject relateObject = new ParseObject("Blog"); //must initialize this before overwritting it in next line.
            try {
                //myBlogLikeRelations.remove(myBlogLikeRelations.getQuery().get(myCurrentBlog.getBlog().getObjectId()));

                //ParseUser.getCurrentUser().put("newRelationList", );
                relateObject = myBlogLikeRelations.getQuery().get(myCurrentBlog.getId());
                myBlogLikeRelations.remove(relateObject);
                Log.d("TheUtils", "In updateVariousLikeBlogLists, relateObject " + relateObject.getObjectId());
                Log.d("TheUtils", "In updateVariousLikeBlogLists, currentBlog: " + myCurrentBlog.getId());
                Log.d("TheUtils", "In updateVariousLikeBlogLists, currentBlog: " + myCurrentBlog.getBlog().getObjectId());
            } catch (ParseException e) {
                Log.d("TheUtils", "In updateVariousLikeBlogLists, error in remove or get: " + e);
            }
            //myBlogLikeRelations.remove(relateObject);
            int count2 = -999;
            try {
                count2 = myBlogLikeRelations.getQuery().count();
//                ParseRelation<ParseObject> myBlogLikeRelations2
//                myBlogLikeRelations2.add(myCurrentBlog.getBlog());
//
            } catch (ParseException e) {
                Log.d("TheUtils", "In updateVariousLikeBlogLists, error count2: " + e);
            }
            Log.d("TheUtils", "In updateVariousLikeBlogLists, after removal size of BlogLikeRelations :"
                    + count2);



            Log.d("TheUtils", "In updateVariousLikeBlogLists, before removal size of parseLikeBlogs :" + myParseLikeBlogs.size());
            myParseLikeBlogs.remove(myCurrentBlog.getBlog());
            Log.d("TheUtils", "In updateVariousLikeBlogLists, after removal size of parseLikeBlogs :" + myParseLikeBlogs.size());
            removeFromLikeList(myBlogLikedList); //TODO problem here, not removing
            saveBlogLikePin(myParseLikeBlogs);
        }
    }

    private static void removeFromLikeList(List<Blog> list) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(myCurrentBlog.getId())) {
                position = i;
                break;
            }
        }
        if (TheUtils.existsInBlogLikedList(TheUtils.getCurrentBlog())) {
            list.remove(position);
        }
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
        //updateBlogLikeList();
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