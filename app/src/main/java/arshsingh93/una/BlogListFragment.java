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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import arshsingh93.una.model.Blog;


public class BlogListFragment extends ListFragment {

    public final static String SHOW = "show";
    public final static String LOAD_BLOG = "load blog";
    public final static String BLOG_TITLE = "blog title";
    public final static String BLOG_BODY = "blog body";
    public final static String LOOK_BLOG = "look at blog";
    public final static String BLOG_AUTHOR = "blog's author";
    public final static String BLOG_DATE = "blogs creation date";

    private OnFragmentInteractionListener mListener;
    private ListView myListView;

    private List<Blog> myBlogList = new ArrayList<>();
    private List<ParseObject> blogObjects = new ArrayList<>();

    private TextView myEmptyTextView;

    public BlogListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateBlogList();

        setListAdapter(new ArrayAdapter<Blog>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, myBlogList) {

        });
    }

    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        String blogId = "ID is: " + myBlogList.get(position).getBlog().get("BlogIdNumber"); //got it working
        Log.d("BlogListFragment", "I clicked on a blog: " + blogId);

        String[] options = {"View this blog", "Edit this blog"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //open up non-editable blog
                    Intent intent = new Intent(getActivity(), NoTabActivity.class);
                    intent.putExtra(SHOW, LOOK_BLOG);
                    intent.putExtra(BLOG_TITLE, (String) myBlogList.get(position).getBlog().get("Title"));
                    intent.putExtra(BLOG_BODY, (String) myBlogList.get(position).getBlog().get("Body"));
                    intent.putExtra(BLOG_AUTHOR, (String) myBlogList.get(position).getBlog().get("Writer"));
                    intent.putExtra(BLOG_DATE, myBlogList.get(position).getBlog().getCreatedAt());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), NoTabActivity.class);
                    intent.putExtra(SHOW, LOAD_BLOG);
                    intent.putExtra(BLOG_TITLE, (String) myBlogList.get(position).getBlog().get("Title"));
                    intent.putExtra(BLOG_BODY, (String) myBlogList.get(position).getBlog().get("Body"));
                    startActivity(intent);
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
