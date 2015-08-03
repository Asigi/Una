package arshsingh93.una;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ProfileFragment extends Fragment {


    public static final String SHOW = "show"; //TODO delete this once settings button has been moved to options menu.
    public static final String SHOW_COLOR_OPTIONS = "show color options";
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int CHOOSE_PHOTO_REQUEST = 1;

    public static final int MEDIA_TYPE_IMAGE = 4;

    protected Uri mMediaUri;

    protected DialogInterface.OnClickListener mDialogInterface = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0: //Take photo
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    if (mMediaUri == null) {
                        //display an error
                        Toast.makeText(getActivity(), //I am not sure about this getApplicationContext thing.
                                "There was a problem accessing your devices external storage", Toast.LENGTH_LONG).show();
                    }
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                    setImage(mMediaUri);
                    break;
                case 1: //Take Choose Photo
                    Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    choosePhotoIntent.setType("image/*");
                    startActivityForResult(choosePhotoIntent, CHOOSE_PHOTO_REQUEST);
                    break;
            }
        }

        private Uri getOutputMediaFileUri(int theMediaType) {
            if (isExternalStorageAvailable()) {
                //get the Uri

                //1. Get external storage directory
                String appName = getActivity().getString(R.string.app_name);
                File mediaStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        appName);

                //2. Create our subdirectory
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.e(this.getClass().getSimpleName(), "Failed to get directory");
                    }
                }

                //3. Create a file name
                //4. Create the file
                File mediaFile;
                Date now = new Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(now);
                String path = mediaStorageDir.getPath() + File.separator;
                if (theMediaType == MEDIA_TYPE_IMAGE) {
                    mediaFile = new File(path + "IMG_" + timeStamp + ".jpg");
                } //else if video type...but i'm not using videos here so this is good enough.
                else {
                    Log.d(this.getClass().getSimpleName(), "file path is null");
                    return null;
                }

                Log.d(this.getClass().getSimpleName(), "File: " + Uri.fromFile(mediaFile));

                //5. Return the files Uri
                return Uri.fromFile(mediaFile);
            } else {
                return null;
            }
        }

        private boolean isExternalStorageAvailable() {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void setImage(Uri theUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), theUri);
            profilePic.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            Log.e(getClass().getSimpleName(), "Error: " + e);
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Error: " + e);
        }
    }



    //Button settingButton;
    Button groupButton;
    Button leadButton;
    Button blogButton;
    Button tbdButton;

    ImageView profilePic;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == CHOOSE_PHOTO_REQUEST) {
                if (data == null) {
                    Toast.makeText(getActivity(), "Sorry, there was an error", Toast.LENGTH_LONG).show();
                } else {
                    mMediaUri = data.getData();
                }
            } else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                getActivity().sendBroadcast(mediaScanIntent);
            }
        } else if (resultCode != getActivity().RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Sorry, there was an error", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        username = (TextView) v.findViewById(R.id.profileUsernameText);
        ParseUser currentUser = ParseUser.getCurrentUser();
        username.setText((String) currentUser.get("origName")); //just added this.
        username.setTextColor(TheUtils.getProperColor());

        profilePic = (ImageView) v.findViewById(R.id.profilePicture);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setItems(R.array.camera_choices, mDialogInterface);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        groupButton = (Button) v.findViewById(R.id.profileGroupButton);
        groupButton.setBackgroundColor(TheUtils.getProperColor());
        leadButton = (Button) v.findViewById(R.id.profileLeadButton);
        leadButton.setBackgroundColor(TheUtils.getProperColor());
        blogButton = (Button) v.findViewById(R.id.profileBlogButton);
        blogButton.setBackgroundColor(TheUtils.getProperColor());
        tbdButton = (Button) v.findViewById(R.id.button4);
        tbdButton.setBackgroundColor(TheUtils.getProperColor());


        /**
        settingButton = (Button) v.findViewById(R.id.profileSettingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoTabActivity.class);
                intent.putExtra(SHOW, SHOW_COLOR_OPTIONS);
                startActivity(intent);
            }
        });
         **/

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
