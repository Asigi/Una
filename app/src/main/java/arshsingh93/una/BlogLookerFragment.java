package arshsingh93.una;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import arshsingh93.una.model.Blog;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class BlogLookerFragment extends Fragment {

    private String myBlogId;
    private String myBlogTitle;
    private String myBlogBody;
    private String myBlogAuthor;
    private String myBlogDate;
    private int myBlogVotes;

    private TextView blogTitleText;
    private TextView blogAuthorText;
    private TextView blogDateText;
    private TextView blogBodyText;
    private TextView blogVoteText;
    private ImageView blogVoteUpButton;
    private ImageView blogVoteDownButton;

    private Button myTestButton;

    private Boolean downVoteCast = false;



    private OnFragmentInteractionListener mListener;



    public BlogLookerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date now = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);

        myBlogId = getArguments().getString(BlogListFragment.BLOG_ID, "" + timeStamp);
        myBlogTitle = getArguments().getString(BlogListFragment.BLOG_TITLE, "Title");
        myBlogBody = getArguments().getString(BlogListFragment.BLOG_BODY, "Body");
        myBlogAuthor = getArguments().getString(BlogListFragment.BLOG_AUTHOR, "Author");
        myBlogDate = getArguments().getString(BlogListFragment.BLOG_DATE, "August 32, 2015");
        myBlogVotes = getArguments().getInt(BlogListFragment.BLOG_VOTE, 0);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blog_looker, container, false);

        blogTitleText = (TextView) v.findViewById(R.id.LookerTitle);
        blogBodyText =  (TextView) v.findViewById(R.id.LookerBody);
        blogVoteText =  (TextView) v.findViewById(R.id.LookerVoteCount);
        blogDateText =  (TextView) v.findViewById(R.id.LookerDate);
        blogAuthorText =  (TextView) v.findViewById(R.id.LookerAuthor);
        blogVoteUpButton = (ImageView) v.findViewById(R.id.LookerUpVoteButton);
        blogVoteDownButton = (ImageView) v.findViewById(R.id.LookerDownVoteButton);

        myTestButton = (Button) v.findViewById(R.id.LookerTestButton);

        blogTitleText.setText(myBlogTitle);
        blogAuthorText.setText("Written by: " + myBlogAuthor);
        blogDateText.setText(myBlogDate);
        blogVoteText.setText(myBlogVotes + "");
        blogBodyText.setText(myBlogBody);


        blogVoteUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogVoteDownButton.setBackgroundColor(0xffe0e0e0); //turn dislike button to gray color,
                blogVoteUpButton.setBackgroundColor(TheUtils.getProperColor());  //like button to user's preferred color


                ParseUser user = ParseUser.getCurrentUser();
                ParseRelation<ParseObject> relation = user.getRelation("blogLikes");
                relation.add(TheUtils.getCurrentBlog().getBlog());
                user.saveInBackground();



                //DISABLE THIS ABILITY IF USER OWNS THIS BLOG (set invisible?) Check if currentUser's name is equal to author.

//                if (!TheUtils.checkMyBlogListForBlog(TheUtils.getCurrentBlog())) { //if the user has not already liked this blog, then
//                    TheUtils.addToMyBlogList(TheUtils.getCurrentBlog()); //add this blog to user's liked blog list
//                    if (downVoteCast) {
//                        blogVoteText.setText((Integer.parseInt(blogVoteText.getText().toString()) + 2) + "");//increment the value that the user sees.
//                        myBlogVotes = Integer.parseInt(blogVoteText.getText().toString());
//                        downVoteCast = false;
//                    } else {
//                        blogVoteText.setText(((Integer.parseInt(blogVoteText.getText().toString())+ 1) + ""));//increment the value that the user sees.
//                        myBlogVotes = Integer.parseInt(blogVoteText.getText().toString());
//                    }
//                }

            }
        });

        blogVoteDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!downVoteCast) {
                    downVoteCast = true;
                    blogVoteDownButton.setBackgroundColor(TheUtils.getProperColor()); //turn dislike button to user's preferred color
                    blogVoteUpButton.setBackgroundColor(0xffe0e0e0); //turn like button to gray.

//                    if (TheUtils.checkAndRemoveBlog(TheUtils.getCurrentBlog())) {
//                        blogVoteText.setText(((Integer.parseInt(blogVoteText.getText().toString()) - 2) + "")); //decrement the value that the user sees.
//                        myBlogVotes = Integer.parseInt(blogVoteText.getText().toString());
//                    } else {
//                        blogVoteText.setText(((Integer.parseInt(blogVoteText.getText().toString()) - 1) + "")); //decrement the value that the user sees.
//                        myBlogVotes = Integer.parseInt(blogVoteText.getText().toString());
//                    }
                }
            }
        });



        return v;
    }




    @OnClick(R.id.LookerUpVoteButton)
    public void likeThisBlog(View view) {

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
