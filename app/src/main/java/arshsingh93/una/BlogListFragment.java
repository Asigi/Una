package arshsingh93.una;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.Date;

import arshsingh93.una.model.Blog;


public class BlogListFragment extends ListFragment {

    public SwipeRefreshLayout mySwipeLayout;


    public final static String SHOW = "show";
    public final static String BLOG_ID = "blog's id";
    public final static String LOAD_BLOG = "load blog";
    public final static String BLOG_TITLE = "blog title";
    public final static String BLOG_BODY = "blog body";
    public final static String LOOK_BLOG = "look at blog";
    public final static String BLOG_AUTHOR = "blog's author";
    public final static String BLOG_DATE = "blogs creation date";
    public final static String BLOG_VOTE = "blog's vote score";

    public final static String BLOG_TYPE = "the type of blog (mine, liked, foreign, etc)";

    public final static String BLOG_WHAT = "one of the types of blogs"; //used to differentiate between user's and foreign
    public final static String BLOG_MINE = "My blogs";
    public final static String BLOG_LIKE = "Blogs I like";
    public final static String BLOG_FOREIGN = "Not my blogs";

    String type = BLOG_MINE;

    /**
     * default constructor.
     */
    public BlogListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(BLOG_WHAT, BLOG_MINE); //default will be BLOG_MINE
        if (type.equals(BLOG_MINE)) {

            setListAdapter(new ArrayAdapter<Blog>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, TheUtils.getMyBlogList()) {
            });
        }  else if (type.equals(BLOG_LIKE)) {
            /** Show the list of blogs that the user likes. This list is already updated in TheUtils. **/

            Log.d("BlogListFragment", "size of liked list is: " + TheUtils.getBlogLikeList().size());
            setListAdapter(new ArrayAdapter<Blog>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, TheUtils.getBlogLikeList()) {
            });
        } else if (type.equals(BLOG_FOREIGN)){

            setListAdapter(new ArrayAdapter<Blog>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, TheUtils.getForeignBlogList()) {
            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_item, container, false);
        View view = inflater.inflate(R.layout.fragment_blog_list, container, false);


        //TODO set empty text from here?
        //TODO set scrollview
        mySwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeLayout.setOnRefreshListener(myOnRefreshListener);
        mySwipeLayout.setColorSchemeColors(TheUtils.getProperColor());

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() { //TODO this has multiple imports, check all types.
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        getActivity().finish(); // :) this is good.
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });



        return view;
    }

    /**
     * Figures out what to do when an item in the list of blogs is clicked.
     * @param l the list
     * @param v the view
     * @param position the position in the list
     * @param id and id which I am not using.
     */
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        String[] options = {"View this blog", "Edit this blog"};
        //TODO if this blog is not the user's then go straight to showing the blog rather than show this pop-up.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //open up non-editable blog
                    Intent intent = new Intent(getActivity(), NoTabActivity.class);
                    intent.putExtra(SHOW, LOOK_BLOG);
                    intent.putExtra(BLOG_WHAT, type);
                    Blog aBlog = TheUtils.getFromBlogList(type, position);
                    intent.putExtra(BLOG_TITLE, (String) aBlog.getBlog().get("Title"));
                    intent.putExtra(BLOG_BODY, (String) aBlog.getBlog().get("Body"));
                    intent.putExtra(BLOG_AUTHOR, (String) aBlog.getBlog().get("Writer"));
                    intent.putExtra(BLOG_VOTE, aBlog.getBlog().get("Vote") + "");
                    Date date = aBlog.getBlog().getCreatedAt();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    String dateString = cal.getTime() + ""; //TODO update this to only show date, not time
                    intent.putExtra(BLOG_DATE, dateString);
                    TheUtils.setCurrentBlog(aBlog); // sets the current Blog in TheUtils.
                    startActivity(intent);

                } else if (type == BLOG_MINE) { //EDIT the blog
                    Intent intent = new Intent(getActivity(), NoTabActivity.class);
                    intent.putExtra(SHOW, LOAD_BLOG);
                    Blog aBlog = TheUtils.getFromBlogList(BLOG_MINE, position);
                    intent.putExtra(BLOG_TITLE, (String) aBlog.getBlog().get("Title"));
                    intent.putExtra(BLOG_BODY, (String) aBlog.getBlog().get("Body"));
                    startActivity(intent);
                } else {
                    //dont allow editing of other people's blogs so do nothing.
                }
            }
        });
        builder.show();
    }


    private SwipeRefreshLayout.OnRefreshListener myOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //do update
            //TODO Check if internet is available.
            //TODO load based on what the value of "type" is.

            if (type == BLOG_LIKE) {
                TheUtils.loadLikedBlogs2();
                if (TheUtils.updateDone) {
                    TheUtils.doneNotice(false);
                    //reload list
                    TheUtils.loadBlogLikePinned();
                    if (TheUtils.updateDone) {
                        TheUtils.doneNotice(false);

                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), NoTabActivity.class);
                        intent.putExtra(ProfileFragment.SHOW, ProfileFragment.SHOW_MY_LIKED_BLOGS);
                        startActivity(intent);


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Error").setMessage(TheUtils.blogError).show();
                        TheUtils.setBlogError("");
                    }
                    // Reload current fragment

                    //finish()
                    //intent = noTab, BLOG_LIKE
                    //add data?
                    //reload this page

                } else { //error occured
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error").setMessage(TheUtils.blogError).show();
                    TheUtils.setBlogError("");
                }

            } else if (type == BLOG_MINE) {
                TheUtils.loadMyBlogList2();
                if (TheUtils.updateDone) {
                    TheUtils.doneNotice(false);
                    //reload list from online because the user might want to see the new vote count.
                    //reload comments as well? How about allowing user to reload comments. Make viewing comments optional on all blogs.

                } else { //error occured
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error").setMessage(TheUtils.blogError).show();
                    TheUtils.setBlogError("");
                }

            } else if (type == BLOG_FOREIGN) {
                TheUtils.loadForeignBlogList2();
                if (TheUtils.updateDone) {
                    //reload list
                    TheUtils.doneNotice(false);
                } else { //error occured
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error").setMessage(TheUtils.blogError).show();
                    TheUtils.setBlogError("");
                }

            }
        }
    };













//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(String id);
//    }
//
//
//
}
