package arshsingh93.una;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ListDialogFragment extends DialogFragment {

    Object[] myList;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        myList = getArguments().getParcelableArray("SavedBlogs");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose which blog to continue editing");
               /** .setItems(some Array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Resources res = getActivity().getResources();

                    }
                }); **/
        return builder.create();
    }


}

