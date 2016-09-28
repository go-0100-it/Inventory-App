package com.waters89gmail.dave.totalinventorycontrol.global_support;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.waters89gmail.dave.totalinventorycontrol.Dialogs.AssociateAgentDialog;
import com.waters89gmail.dave.totalinventorycontrol.Dialogs.EditInventoryDialog;
import com.waters89gmail.dave.totalinventorycontrol.Dialogs.MultiUseDialog;
import com.waters89gmail.dave.totalinventorycontrol.product_activity.Product;

public class DialogUtils {

    public DialogUtils(){}

    /**
     * A method to show a custom dialog.  This method allows multiple different uses through the same code block.
     * @param activity the activity calling the dialog.
     * @param dialogName a String value to refer to the dialog, used for getting the onButtonClickListener callbacks.
     * @param messageRes a String resource to display as the dialogs message.
     * @param positiveBtnRes a String resource to display as the positive buttons text.
     * @param neutralBtnRes a String resource to display as the neutral buttons text.
     * @param negativeBtnRes a String resource to display as the negative buttons text.
     */
    public static void showMultiUseDialog(AppCompatActivity activity, String dialogName, int messageRes, int positiveBtnRes, int neutralBtnRes, int negativeBtnRes){
        MultiUseDialog dialog = new MultiUseDialog();
        Bundle bundle = new Bundle();
            bundle.putInt(C.MESSAGE,messageRes);
            bundle.putInt(C.POSITIVE_BTN,positiveBtnRes);
            bundle.putInt(C.NEUTRAL_BTN,neutralBtnRes);
            bundle.putInt(C.NEGATIVE_BTN,negativeBtnRes);
        dialog.setArguments(bundle);

        dialog.show(activity.getSupportFragmentManager(), dialogName);
    }

    /**
     * A method to show a custom list dialog.
     * @param activity the activity calling the dialog.
     * @param product the product to associate the BusinessAgent to.
     */
    public static void showAssociateAgentDialog(AppCompatActivity activity,String dialogName, Product product){
        AssociateAgentDialog dialog = new AssociateAgentDialog();
        dialog.setArgs(product.getmProductId());
        dialog.show(activity.getSupportFragmentManager(), dialogName);
    }

    /**
     * A method to show a custom dialog used to edit the inventory count of a product.
     * @param activity the activity calling the dialog.
     * @param product the product to edit the inventory of.
     */
    public static void showEditInventoryDialog(AppCompatActivity activity,String dialogName, Product product){
        EditInventoryDialog dialog = new EditInventoryDialog();
        dialog.setArgs(product.getmProductId(),product.getmQtyOnHand());
        dialog.show(activity.getSupportFragmentManager(), dialogName);
    }
}
