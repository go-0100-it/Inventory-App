package com.waters89gmail.dave.totalinventorycontrol.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;

public class EditInventoryDialog extends DialogFragment implements View.OnClickListener {

    private long productId;
    private int productQty;
    private EditText amountEditText;
    View view;

    public EditInventoryDialog() {}

    public void setArgs(long productId, int productQty){
        this.productId = productId;
        this.productQty = productQty;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(C.PRODUCT_ID, productId);
        outState.putInt(C.PRODUCT_QTY, productQty);
        super.onSaveInstanceState(outState);
    }

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     **/
    public interface EditInventoryDialogListener{
        void onDialogPositiveClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    EditInventoryDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the CustomDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the CustomDialogListener so we can send events to the host
            mListener = (EditInventoryDialogListener) activity;
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

        if (savedInstanceState != null) {
            this.productId = savedInstanceState.getLong(C.PRODUCT_ID);
            this.productQty = savedInstanceState.getInt(C.PRODUCT_QTY);
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.edit_inventory_dialog_frag, null, false);

        amountEditText = (EditText) view.findViewById(R.id.amount_edit_text);
        ImageButton upButton = (ImageButton) view.findViewById(R.id.up_button);
        ImageButton downButton = (ImageButton) view.findViewById(R.id.down_button);
        Button updateButton = (Button) view.findViewById(R.id.update_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);

        upButton.setOnClickListener(this);
        downButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.adjust_inventory_count))
                .setView(view);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.up_button:
                incrementCount();
                break;
            case R.id.down_button:
                decrementCount();
                break;
            case R.id.update_button:
               updateQuantity();
                break;
            case R.id.cancel_button:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * A method to update the product inventory count by user inputted amount.
     */
    private void updateQuantity() {
        // Checking if value is not a null string.
        if (!amountEditText.getText().toString().equals(C.EMPTY_STRING)) {
            int amountToModifyBy = Integer.parseInt(amountEditText.getText().toString());

            // Ensuring, if the amount entered is to reduce inventory, it will not be less than zero.
            if (amountToModifyBy * -1 <= productQty) {
                int newAmount = productQty + amountToModifyBy;
                //updating the product quantity.
                if (DataBaseUtils.updateProductQty(getContext(), productId, newAmount) > C.NULL) {
                    mListener.onDialogPositiveClick(EditInventoryDialog.this);

                } else {
                    Snackbar.make(view, "Something went wrong, inventory amount wasn't updated.", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dismiss();
                                }
                            })
                            .show();
                }
            } else {
                // Informing user the amount entered will reduce the inventory below zero.
                Snackbar.make(view, getString(R.string.error_inventory_less_than_zero), Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                amountEditText.setText("");
                            }
                        })
                        .show();
            }
        }else{
            dismiss();
        }
    }

    /**
     * A method to decrement the amount displayed in the EditText by one.
     */
    private void decrementCount() {
        int numToDecrease;
        if(amountEditText.getText().toString().equals(C.EMPTY_STRING)){
            numToDecrease = 0;
        }else {
            numToDecrease = Integer.parseInt(amountEditText.getText().toString());
        }
        // This prevents the amount from decrementing below the inventory count.
        if(numToDecrease * -1 < productQty) {
            if(numToDecrease - 1 == 0){
                amountEditText.setText("");
            }else{
                amountEditText.setText(String.valueOf(numToDecrease - 1));
            }
        }
    }

    /**
     * A method to increment the amount displayed in the EditText by one.
     */
    private void incrementCount(){
        int numToIncrease;
        if(amountEditText.getText().toString().equals(C.EMPTY_STRING)){
            numToIncrease = 0;
        }else {
            numToIncrease = Integer.parseInt(amountEditText.getText().toString());
        }
        if(numToIncrease + 1 == 0){
            amountEditText.setText("");
        }else{
        amountEditText.setText(String.valueOf(numToIncrease + 1));
        }
    }
}