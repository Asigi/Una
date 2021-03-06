package arshsingh93.una;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener,
        ProfileFragment.OnFragmentInteractionListener, BlogDummyFragment.OnFragmentInteractionListener {


    private static final String TAG = MainActivity.class.getSimpleName();

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TheUtils.loadColorTheme(this);
        TheUtils.onActivityCreateSetTheme(this);

        /*
        if (!TheUtils.FIRST_TIME_MAIN) {

            //TODO How about instead of loading all lists right at the beginning, create a boolean variable for whether each list
            //TODO ...has been loaded. If its false then load it from online when user clicks on the appropriate button.
            //TODO ...When app first opens, just load from local datastore instead.
            //TODO ...This way, a whole bunch of API calls aren't wasted right off the bat.

            //TODO IDEA. Load every single blog right off the bat. And then, sort them into my, liked, foreign after loading?
            //TODO ...The downside with this idea is that, a max of 1000 parse objects are returned in a query.

            //TODO first check for internet connection.

            TheUtils.loadLikedBlogs2();
            if (TheUtils.updateDone == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error").setMessage(TheUtils.blogError).show();
            }
            TheUtils.doneNotice(false); //reset
            TheUtils.setBlogError(""); //reset

            TheUtils.loadMyBlogList2();
            if (TheUtils.updateDone == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error").setMessage(TheUtils.blogError).show();
            }
            TheUtils.doneNotice(false); //reset
            TheUtils.setBlogError(""); //reset

            TheUtils.loadForeignBlogList2();
            if (TheUtils.updateDone == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Error").setMessage(TheUtils.blogError).show();
            }
            TheUtils.doneNotice(false); //reset
            TheUtils.setBlogError(""); //reset


            TheUtils.FIRST_TIME_MAIN = true;
        }

 */

        //TODO try using saveEventually for users who arent in data network. Do this somewhere else.


        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "Current user is null so move to login screen");
            //navigateToLogin();
            navigateToEnter();
        } else {
            Log.i(TAG, currentUser.getUsername());
            Toast.makeText(this,"Hello " + currentUser.get("origName"), Toast.LENGTH_LONG ).show();
        }


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });


        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
           // navigateToLogin();
            navigateToEnter();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    private void navigateToEnter() {
        Intent intent = new Intent(this, EnterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //this prevents you from getting back to the previous page.
        startActivity(intent);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {


    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private Fragment[] myMainFragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.e(TAG, "here in sectionsPagerAdapter constructor, before profilefragment construction");
            myMainFragments = new Fragment[3];
            myMainFragments[2] = new ProfileFragment();
            myMainFragments[1] = new BlogDummyFragment();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (position == 1) {
                return myMainFragments[1];
            }
            if (position == 2) {
                return myMainFragments[2];
            }


            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }

    }




    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
