package arshsingh93.una;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import arshsingh93.una.model.Blog;


public class BlogListFragment extends ListFragment {

    public final static String SHOW = "show";
    public final static String BLOG_ID = "blog's id";
    public final static String LOAD_BLOG = "load blog";
    public final static String BLOG_TITLE = "blog title";
    public final static String BLOG_BODY = "blog body";
    public final static String LOOK_BLOG = "look at blog";
    public final static String BLOG_AUTHOR = "blog's author";
    public final static String BLOG_DATE = "blogs creation date";
    public final static String BLOG_VOTE = "blog's vote score";


    public final static String BLOG_WHAT = "one of the types of blogs"; //used to differentiate between user's and foreign
    public final static String BLOG_MINE = "My blogs";
    public final static String BLOG_LIKE = "Blogs I like";
    public final static String BLOG_FOREIGN = "Not my blogs";

    private OnFragmentInteractionListener mListener;
    private ListView myListView;

    private List<Blog> myBlogList = new ArrayList<>();
    private List<Blog> foreignBlogList = new ArrayList<>(); //list of blogs not written by the user
    private List<ParseObject> blogObjects = new ArrayList<>();

    String type = BLOG_MINE;

    private TextView myEmptyTextView;

    public BlogListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getArguments().getString(BLOG_WHAT, BLOG_MINE); //default will be BLOG_MINE
        if (type.equals(BLOG_MINE)) {
            updateBlogList(); //TODO also include the blogs that the user has liked.
            setListAdapter(new ArrayAdapter<Blog>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, myBlogList) {
            });
        } else {
            updateForeignBlogList();
            setListAdapter(new ArrayAdapter<Blog>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, foreignBlogList) {
            });
        }
    }

    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        final String blogId = getPositionOfBlog(position);

        String[] options = {"View this blog", "Edit this blog"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //open up non-editable blog
                    Intent intent = new Intent(getActivity(), NoTabActivity.class);
                    intent.putExtra(SHOW, LOOK_BLOG);
                    intent.putExtra(BLOG_ID, blogId); //not sure if I need this...

                    if (type == BLOG_MINE) {

                        intent.putExtra(BLOG_TITLE, (String) myBlogList.get(position).getBlog().get("Title"));
                        intent.putExtra(BLOG_BODY, (String) myBlogList.get(position).getBlog().get("Body"));
                        intent.putExtra(BLOG_AUTHOR, (String) myBlogList.get(position).getBlog().get("Writer"));
                        intent.putExtra(BLOG_VOTE, (int) myBlogList.get(position).getBlog().get("Vote"));
                        Date date = myBlogList.get(position).getBlog().getCreatedAt();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        String dateString = cal.getTime() + ""; //TODO update this to only show date, not time
                        intent.putExtra(BLOG_DATE, dateString);
                        TheUtils.setCurrentBlog(myBlogList.get(position)); // sets the current Blog in TheUtils.
                    } else if (type == BLOG_LIKE) {

                        intent.putExtra(BLOG_TITLE, (String) myBlogList.get(position).getBlog().get("Title"));
                        intent.putExtra(BLOG_BODY, (String) myBlogList.get(position).getBlog().get("Body"));
                        intent.putExtra(BLOG_AUTHOR, (String) myBlogList.get(position).getBlog().get("Writer"));
                        intent.putExtra(BLOG_VOTE, (int) myBlogList.get(position).getBlog().get("Vote"));
                        Date date = myBlogList.get(position).getBlog().getCreatedAt();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        String dateString = cal.getTime() + ""; //TODO update this to only show date, not time
                        intent.putExtra(BLOG_DATE, dateString);
                        TheUtils.setCurrentBlog(TheUtils.getBlogLikeList().get(position));
                    } else {

                        intent.putExtra(BLOG_TITLE, (String) foreignBlogList.get(position).getBlog().get("Title"));
                        intent.putExtra(BLOG_BODY, (String) foreignBlogList.get(position).getBlog().get("Body"));
                        intent.putExtra(BLOG_AUTHOR, (String) foreignBlogList.get(position).getBlog().get("Writer"));
                        intent.putExtra(BLOG_VOTE, (int) foreignBlogList.get(position).getBlog().get("Vote"));
                        Date date = foreignBlogList.get(position).getBlog().getCreatedAt();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        String dateString = cal.getTime() + ""; //TODO update this to only show date, not time
                        intent.putExtra(BLOG_DATE, dateString);
                        TheUtils.setCurrentBlog(foreignBlogList.get(position)); // sets the current Blog in TheUtils.
                    }

                    startActivity(intent);

                } else if (type == BLOG_MINE){
                    Intent intent = new Intent(getActivity(), NoTabActivity.class);
                    intent.putExtra(SHOW, LOAD_BLOG);
                    intent.putExtra(BLOG_TITLE, (String) myBlogList.get(position).getBlog().get("Title"));
                    intent.putExtra(BLOG_BODY, (String) myBlogList.get(position).getBlog().get("Body"));
                    startActivity(intent);
                } else {
                    //dont allow editing of other people's blogs so do nothing.
                }
            }
        });
        builder.show();
    }


    public void updateBlogList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.whereEqualTo("User", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> blogList, ParseException e) {
                if (e == null) {
                    blogObjects = new ArrayList<ParseObject>(blogList);
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

    public void updateForeignBlogList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.whereNotEqualTo("User", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> blogList, ParseException e) {
                if (e == null) {
                    for (ParseObject o: blogList) {
                        Blog b = new Blog(o);
                        foreignBlogList.add(b);
                    }
                    Log.d("Search Blogs", "Retrieved " + foreignBlogList.size() + " foreign blogs");
                } else {
                    Log.d("Search Blogs", "Error: " + e.getMessage());
                }
            }
        });
    }

    private String getPositionOfBlog(int position) {
        if (type == BLOG_MINE) {
            return  "ID is: " + myBlogList.get(position).getBlog().get("BlogIdNumber");
        } else {
            return  "ID is: " + foreignBlogList.get(position).getBlog().get("BlogIdNumber");
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);



        return view;
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
        public void onFragmentInteraction(String id);
    }

}
