package arshsingh93.una;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ProfileFragment extends Fragment {

    public static final String SHOW = "show"; //TODO delete this once settings button has been moved to options menu.
    public static final String SHOW_COLOR_OPTIONS = "show color options";
    public static final String SHOW_MY_BLOGS = "show my blogs";

    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int CHOOSE_PHOTO_REQUEST = 1;
    public static final int MEDIA_TYPE_IMAGE = 4;
    //Button settingButton;
    public Button groupButton;
    public Button leadButton;
    public Button blogButton;
    public Button tbdButton;
    public ImageView profilePic;
    public TextView username;
    private OnFragmentInteractionListener mListener;
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
                        Log.e("ProfileFragment", "Failed to get directory");
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
                    Log.d("ProfileFragment", "file path is null");
                    return null;
                }
                Log.d("ProfileFragment", "File: " + Uri.fromFile(mediaFile));
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



    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ProfileFragment", "Here in onActivityResult with requestCode: " + requestCode + " , resultCode: " +
                resultCode  + " , data: " + data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == CHOOSE_PHOTO_REQUEST) {
                if (data == null) {
                    Toast.makeText(getActivity(), "Sorry, there was an error", Toast.LENGTH_LONG).show();
                } else {
                    mMediaUri = data.getData();
                    Log.d("ProfileFragment", "Here in onActivityResult's if if else.");
                    setImage(mMediaUri);
                }
            } else {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                getActivity().sendBroadcast(mediaScanIntent);
                setImage(mMediaUri);
                Log.d("ProfileFragment", "Here in onActivityResult's if else.");
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
        ParseFile picFile = (ParseFile) ParseUser.getCurrentUser().get("profilePic");
        picFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profilePic.setImageBitmap(bitmap);
                } else {
                    //unable to load image. //TODO
                }
            }
        });

        groupButton = (Button) v.findViewById(R.id.profileGroupButton);
        groupButton.setBackgroundColor(TheUtils.getProperColor());
        leadButton = (Button) v.findViewById(R.id.profileLeadButton);
        leadButton.setBackgroundColor(TheUtils.getProperColor());
        tbdButton = (Button) v.findViewById(R.id.button4);
        tbdButton.setBackgroundColor(TheUtils.getProperColor());

        blogButton = (Button) v.findViewById(R.id.profileBlogButton);
        blogButton.setBackgroundColor(TheUtils.getProperColor());
        blogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoTabActivity.class);
                intent.putExtra(SHOW, SHOW_MY_BLOGS);
                startActivity(intent);
            }
        });

        return v;
    }


    private void setImage(Uri theUri) {
        Log.d("ProfileFragment", "Here in setImage with uri: " + theUri);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), theUri);
            Log.d("ProfileFragment", "bitmap is: " + bitmap.toString());
            profilePic.setImageBitmap(bitmap);

            //send to parse
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] array = stream.toByteArray();
            ParseFile file = new ParseFile("profilePic.jpeg", array);
            file.saveInBackground();
            ParseUser.getCurrentUser().put("profilePic", file);
            ParseUser.getCurrentUser().saveInBackground(); //is this necessary?


        } catch (FileNotFoundException e) {
            Log.e("ProfileFragment", "Error: " + e);
        } catch (IOException e) {
            Log.e("ProfileFragment", "Error: " + e);
        }
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
            Log.e("ProfileFragment", "EXCEPTION inside onAttach of profile fragment: " + e);
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
