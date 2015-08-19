package arshsingh93.una;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import arshsingh93.una.model.Blog;

/**
 * This is the middle tab on the main activity.
 * It is temporarily called BlogDummy but will eventually be called SearchCreate.
 */
public class BlogDummyFragment extends Fragment {

    public static final String SHOW = "show";
    public static final String CREATE_BLOG = "Create blog";
    public static final String FIND_BLOGS = "Find blogs";

    private BlogHelper myBlogHelper;

    Button findBlogButton;
    Button createBlogButton;
    Button loadBlogButton;
    ListView myBlogList;

    private OnFragmentInteractionListener mListener;

    public BlogDummyFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myBlogHelper = new BlogHelper(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blog_dummy, container, false);

        myBlogList = (ListView) v.findViewById(R.id.dummyList);

        findBlogButton = (Button) v.findViewById(R.id.blogDummyFindBlogButton);
        findBlogButton.setBackgroundColor(TheUtils.getProperColor());
        createBlogButton = (Button) v.findViewById(R.id.blogDummyCreateBlogButton);
        createBlogButton.setBackgroundColor(TheUtils.getProperColor());
        loadBlogButton = (Button) v.findViewById(R.id.blogDummyLoadBlogButton);
        loadBlogButton.setBackgroundColor(TheUtils.getProperColor());





        findBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoTabActivity.class);
                intent.putExtra(SHOW, FIND_BLOGS);
                startActivity(intent);
            }
        });


        createBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoTabActivity.class);
                intent.putExtra(SHOW, CREATE_BLOG);
                startActivity(intent);
            }
        });

        /** load a blog from sqlite **/
        loadBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] columns = {
                        BlogHelper.COLUMN_BLOG_TITLE, BlogHelper.COLUMN_BLOG_BODY
                };


                SQLiteDatabase database = myBlogHelper.getWritableDatabase();
                database.beginTransaction();


                Cursor c = database.query(
                        BlogHelper.BLOGS_TABLE,
                        columns,
                        null,
                        null,
                        null,
                        null,
                        null
                );

                c.moveToFirst();
                List<Blog> list = new ArrayList<Blog>();
                for (int i = 0; i <c.getCount(); i++) {
                    String title = c.getString(0);
                    String body = c.getString(1);
                    Blog blog = new Blog(title, body, ParseUser.getCurrentUser());
                    list.add(blog);
                    c.moveToFirst();
                }

                myBlogList.setAdapter(new ArrayAdapter<Blog>(v.getContext(), android.R.layout.simple_expandable_list_item_1,
                        android.R.id.text1, list));

                database.setTransactionSuccessful();
                database.endTransaction();
                database.close();

                //open a new dialogfragment that holds a list
                    //when an item in that list is clicked, get the title and body and
                    //pass them into onContinueBlog() in blogWriterFragment.
                    //the onContinueBlog() will populate the blog page with the values that
                    //the user had stored.
            }
        });



        return v;
    }











    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
