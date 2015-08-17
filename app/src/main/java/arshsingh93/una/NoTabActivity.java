package arshsingh93.una;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import arshsingh93.una.Unused.colorListFragment;
import arshsingh93.una.model.Blog;


public class NoTabActivity extends ActionBarActivity implements colorListFragment.OnFragmentInteractionListener,
        BlogWriterFragment.OnFragmentInteractionListener,

        BlogLookerFragment.OnFragmentInteractionListener {

    private static String TAG = NoTabActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /** getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); **/ //this hides the small bar at the very top (with the battery,etc.).
        getSupportActionBar().hide();
        TheUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_no_tab);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        Intent intent = getIntent();

        if (intent.getStringExtra(ProfileFragment.SHOW).equals(ProfileFragment.SHOW_MY_BLOGS)) {
            /** Opens up a list of blogs that the user wrote **/
            Log.d(TAG, "SHOW_MY_BLOGS");
            fragment = new BlogListFragment();
            Bundle args = new Bundle();
            args.putString(BlogListFragment.BLOG_WHAT, BlogListFragment.BLOG_MINE);
            fragment.setArguments(args);

        } else if (intent.getStringExtra(ProfileFragment.SHOW).equals(ProfileFragment.SHOW_MY_LIKED_BLOGS)) {
            /**opens up a list of blogs that the user likes **/
            Log.d(TAG, "SHOW_MY_LIKED_BLOGS");
            fragment = new BlogListFragment();
            Bundle args = new Bundle();
            args.putString(BlogListFragment.BLOG_WHAT, BlogListFragment.BLOG_LIKE);
            fragment.setArguments(args);

        } else if (intent.getStringExtra(BlogDummyFragment.SHOW).equals(BlogDummyFragment.FIND_BLOGS)) {
            /** Opens up a fragment which lets you see a list of blogs that were written by someone else **/
            Log.d(TAG, "FIND_BLOGS");
            fragment = new BlogListFragment();
            Bundle args = new Bundle();
            args.putString(BlogListFragment.BLOG_WHAT, BlogListFragment.BLOG_FOREIGN);
            fragment.setArguments(args);

        } else if (intent.getStringExtra(BlogDummyFragment.SHOW).equals(BlogDummyFragment.CREATE_BLOG)) {
            /** Opens up a fragment which lets you create a blog **/
            Log.d(TAG, "CREATE_BLOG");
            fragment = new BlogWriterFragment();
            String title = "";
            String body = "";
            Bundle args = new Bundle();
            args.putString(BlogListFragment.BLOG_TITLE, title);
            args.putString(BlogListFragment.BLOG_BODY, body);
            fragment.setArguments(args);

        } else if (intent.getStringExtra(BlogListFragment.SHOW).equals(BlogListFragment.LOAD_BLOG)){
            /** Opens up a fragment which lets you edit a blog that you previously created.**/
            /**Note that this will eventually be changed. maybe**/
            Log.d(TAG, "LOAD_BLOG");
            fragment = new BlogWriterFragment();
            String title = intent.getStringExtra(BlogListFragment.BLOG_TITLE); //were good here
            String body = intent.getStringExtra(BlogListFragment.BLOG_BODY);
            Bundle args = new Bundle();
            args.putString(BlogListFragment.BLOG_TITLE, title);
            args.putString(BlogListFragment.BLOG_BODY, body); //were good here
            fragment.setArguments(args);

        } else if (intent.getStringExtra(BlogListFragment.SHOW).equals(BlogListFragment.LOOK_BLOG)) {
            /**Open up a fragment which lets you look at (but not edit) a blog **/
            Log.d(TAG, "LOOK_BLOG");
            fragment = new BlogLookerFragment();
            String id = intent.getStringExtra(BlogListFragment.BLOG_ID);
            String author = intent.getStringExtra(BlogListFragment.BLOG_AUTHOR);
            String title = intent.getStringExtra(BlogListFragment.BLOG_TITLE);
            String body = intent.getStringExtra(BlogListFragment.BLOG_BODY);
            String date = intent.getStringExtra(BlogListFragment.BLOG_DATE);
            int vote = intent.getIntExtra(BlogListFragment.BLOG_VOTE, -999);
            Bundle args = new Bundle();
            args.putString(BlogListFragment.BLOG_ID, id);
            args.putString(BlogListFragment.BLOG_TITLE, title);
            args.putString(BlogListFragment.BLOG_BODY, body);
            args.putString(BlogListFragment.BLOG_DATE, date);
            args.putString(BlogListFragment.BLOG_AUTHOR, author);
            args.putInt(BlogListFragment.BLOG_VOTE, vote);

            if (intent.getStringExtra(BlogListFragment.BLOG_WHAT).equals(BlogListFragment.BLOG_LIKE)) {
                args.putString(BlogListFragment.BLOG_WHAT, BlogListFragment.BLOG_LIKE);
            } else if (intent.getStringExtra(BlogListFragment.BLOG_WHAT).equals(BlogListFragment.BLOG_FOREIGN)) {
                args.putString(BlogListFragment.BLOG_WHAT, BlogListFragment.BLOG_FOREIGN);
            } else if (intent.getStringExtra(BlogListFragment.BLOG_WHAT).equals(BlogListFragment.BLOG_MINE)) {
                args.putString(BlogListFragment.BLOG_WHAT, BlogListFragment.BLOG_MINE);
            }

            fragment.setArguments(args);

        } else {
            /**This is just a default fragment **/
            Log.d(TAG, "colorList");
            fragment = new colorListFragment();
        }

        Log.d(TAG, "about to being transaction");
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.non_tab_container, fragment); //changed from .replace
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }















    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_no_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }



        //if (id == android.R.id.home)  {
            //if (NavUtils.getParentActivityName(this) != null) {
              //  NavUtils.navigateUpFromSameTask(this);

                //figure out how to go to the previous page instead of directly to home.
           // } NOTE: learned that the up button takes the user a whole screen previous whereas the back button
                //...(which is on all android devices) takes the user back only a little bit. Its better to remove the bar completely.
          //  return true;
       // }

        return super.onOptionsItemSelected(item);
    }

 **/


}
