package arshsingh93.una;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import arshsingh93.una.model.Blog;


public class BlogWriterFragment extends Fragment {

    EditText title;
    EditText body;

    Button saveButton;
    Button publishButton;

    private BlogHelper myBlogHelper;

    private OnFragmentInteractionListener mListener;

    private String myBlogTitle;
    private String myBlogBody;

    public BlogWriterFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BlogWriterFragmentTEST", "in onCreate method: ");
        myBlogTitle = getArguments().getString(BlogListFragment.BLOG_TITLE); //null here
        myBlogBody = getArguments().getString(BlogListFragment.BLOG_BODY);
        Log.d("BlogWriterFragmentTEST", "in onCreate method #2");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blog_writer, container, false);
        Log.d("BlogWriterFragmentTEST", "in onCreateView method");

        v.setFocusableInTouchMode(true);
        v.requestFocus();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        //TODO ask the writer if s/he is sure about going back. Warn about losing what they've written.
                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });

        myBlogHelper = new BlogHelper(getActivity());

        title = (EditText) v.findViewById(R.id.blogWriterTitle);
        body = (EditText) v.findViewById(R.id.blogWriterBody);
        title.setText(myBlogTitle);
        body.setText(myBlogBody);

        saveButton = (Button) v.findViewById(R.id.blogWriterSaveButton);
        saveButton.setBackgroundColor(TheUtils.getProperColor());
        publishButton = (Button) v.findViewById(R.id.blogWriterPublishButton);
        publishButton.setBackgroundColor(TheUtils.getProperColor());


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String bodyText = body.getText().toString();
                Log.d("BlogWriterFragment", titleText);

                if (titleText.isEmpty() || bodyText.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please enter something for both the title and the body of this blog")
                            .setTitle("Blog not saved")
                                    .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    SQLiteDatabase database = myBlogHelper.getWritableDatabase();
                    database.beginTransaction();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BlogHelper.COLUMN_BLOG_TITLE, titleText);
                    contentValues.put(BlogHelper.COLUMN_BLOG_BODY, bodyText);
                    database.insert(BlogHelper.BLOGS_TABLE, null, contentValues);

                    database.setTransactionSuccessful();
                    database.endTransaction();
                    database.close();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Your blog was saved onto your device")
                            .setTitle("Saved")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String bodyText = body.getText().toString();

                if (titleText.isEmpty() || bodyText.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please enter something for both the title and the body of this blog")
                            .setTitle("Blog not published")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    ParseUser user = ParseUser.getCurrentUser();
                    final Blog blog = new Blog(titleText, bodyText, user);
                    final ParseObject blogObject = blog.getBlog();
                    blogObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("BlogWriterFragmentTEST", blogObject.getObjectId());
                                String anId = blogObject.getObjectId();
                                blogObject.put("BlogIdNumber", anId);
                                blogObject.saveInBackground();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Your blog has been published!")
                                        .setTitle("Success")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(e.getMessage())
                                        .setTitle("Oops, something went wrong!")
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });


        return v;
    }


    /**
     * Loads the data from SQLite.
     * @param title is the title of the blog
     * @param body is the body of the blog
     */
    public void loadUp(String title, String body) {
        //TODO
    }












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
