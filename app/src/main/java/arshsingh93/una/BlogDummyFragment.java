package arshsingh93.una;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class BlogDummyFragment extends Fragment {

    public static final String SHOW = "show";
    public static final String CREATE_BLOG = "Create blog";

    Button findBlogButton;
    Button createBlogButton;
    Button loadBlogButton;

    private OnFragmentInteractionListener mListener;

    public BlogDummyFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blog_dummy, container, false);

        findBlogButton = (Button) v.findViewById(R.id.blogDummyFindBlogButton);
        findBlogButton.setBackgroundColor(TheUtils.getProperColor());
        createBlogButton = (Button) v.findViewById(R.id.blogDummyCreateBlogButton);
        createBlogButton.setBackgroundColor(TheUtils.getProperColor());
        loadBlogButton = (Button) v.findViewById(R.id.blogDummyLoadBlogButton);
        loadBlogButton.setBackgroundColor(TheUtils.getProperColor());


        createBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoTabActivity.class);
                intent.putExtra(SHOW, CREATE_BLOG);
                startActivity(intent);
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