package com.ddarji.assignment2.birthdaycalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

public class EditBirthdayFragment extends DialogFragment {

    public static EditBirthdayFragment newInstance(Long i, String n, String b){
        EditBirthdayFragment dialog = new EditBirthdayFragment();
        Bundle args = new Bundle();
        args.putLong("id", i);
        args.putString("name", n);
        args.putString("birthday", b);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View myInflatedView = inflater.inflate(R.layout.fragment_enter_birthday, null);

        ((TextView) myInflatedView.findViewById(R.id.txtID)).setText(args.getLong("id")+"");
        ((EditText) myInflatedView.findViewById(R.id.txtName)).setText(args.getString("name"));
        ((EditText) myInflatedView.findViewById(R.id.txtBirthday)).setText(args.getString("birthday"));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(myInflatedView)
                .setTitle(R.string.editBirthday)
                // Add action buttons
                .setPositiveButton(R.string.editBirthday, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(EditBirthdayFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(EditBirthdayFragment.this);
                    }
                });

        Log.d("EditDialog", "ID: " + args.getLong("id"));
        Log.d("EditDialog", "Name: " + args.getString("name"));
        Log.d("EditDialog", "Birthday: " + args.getString("birthday"));

        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
