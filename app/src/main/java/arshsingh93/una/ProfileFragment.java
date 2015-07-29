package arshsingh93.una;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;


public class ProfileFragment extends Fragment {


    public static final String SHOW = "show"; //TODO delete this once settings button has been moved to options menu.
    public static final String SHOW_COLOR_OPTIONS = "show color options";

    Button settingButton;
    Button groupButton;
    Button leadButton;
    Button blogButton;
    Button tbdButton;

    TextView username;
    private OnFragmentInteractionListener mListener;


    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        username = (TextView) v.findViewById(R.id.profileUsernameText);
        ParseUser currentUser = ParseUser.getCurrentUser();
        username.setText( (String) currentUser.get("origName")); //just added this.
        username.setTextColor(TheUtils.getProperColor());

        groupButton = (Button) v.findViewById(R.id.profileGroupButton);
        groupButton.setBackgroundColor(TheUtils.getProperColor());
        leadButton = (Button) v.findViewById(R.id.profileLeadButton);
        leadButton.setBackgroundColor(TheUtils.getProperColor());
        blogButton = (Button) v.findViewById(R.id.profileBlogButton);
        blogButton.setBackgroundColor(TheUtils.getProperColor());
        tbdButton = (Button) v.findViewById(R.id.button4);
        tbdButton.setBackgroundColor(TheUtils.getProperColor());

        settingButton = (Button) v.findViewById(R.id.profileSettingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoTabActivity.class);
                intent.putExtra(SHOW, SHOW_COLOR_OPTIONS);
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
            Log.e(ProfileFragment.class.getSimpleName(), "EXCEPTION inside onAttach of profile fragment: " + e);
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
