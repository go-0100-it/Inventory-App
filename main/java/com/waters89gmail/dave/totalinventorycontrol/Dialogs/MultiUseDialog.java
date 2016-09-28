package com.waters89gmail.dave.totalinventorycontrol.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.waters89gmail.dave.totalinventorycontrol.global_support.C;

public class MultiUseDialog extends DialogFragment {

    int messageStringRes;
    int positiveBtnStringRes;
    int neutralBtnStringRes;
    int negativeBtnStringRes;

    public MultiUseDialog() {}

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     **/
    public interface MultiUseDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNeutralClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    MultiUseDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the CustomDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the CustomDialogListener so we can send events to the host
            mListener = (MultiUseDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        this.messageStringRes = getArguments().getInt(C.MESSAGE);
        this.positiveBtnStringRes = getArguments().getInt(C.POSITIVE_BTN);
        this.neutralBtnStringRes = getArguments().getInt(C.NEUTRAL_BTN);
        this.negativeBtnStringRes = getArguments().getInt(C.NEGATIVE_BTN);

        if (messageStringRes != C.NULL) {
            builder.setMessage(messageStringRes);
        }
        if (positiveBtnStringRes != C.NULL) {
            builder.setPositiveButton(positiveBtnStringRes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mListener.onDialogPositiveClick(MultiUseDialog.this);
                        }
                    });
        }
        if (neutralBtnStringRes != C.NULL) {
            builder.setNeutralButton(neutralBtnStringRes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mListener.onDialogNeutralClick(MultiUseDialog.this);
                }
            });
        }
        builder.setNegativeButton(negativeBtnStringRes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogNegativeClick(MultiUseDialog.this);
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}