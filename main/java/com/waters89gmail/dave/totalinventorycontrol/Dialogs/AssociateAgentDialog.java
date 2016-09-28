package com.waters89gmail.dave.totalinventorycontrol.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.waters89gmail.dave.totalinventorycontrol.Adapters.AssociateAgentDialogAdapter;
import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseContract;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseProvider;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;

/**
 * A Custom Dialog Fragment for listing all BusinessAgents available for association.
 */
public class AssociateAgentDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private AssociateAgentDialogAdapter adapter;
    private long productId;

    ListView listView;

    public AssociateAgentDialog() {}

    /**
     * setting the productId from the Args passed in.
     * @param productId _ID of the product for association.
     */
    public void setArgs(long productId){
        this.productId = productId;
    }

    /**
     * Overriding to persist values through activity lifecycle.
     * @param outState bundle to save @onSaveInstantState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(C.PRODUCT_ID, productId);
        super.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            this.productId = savedInstanceState.getInt(C.PRODUCT_ID);
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.agents_list_fragment, null, false);

        adapter = new AssociateAgentDialogAdapter(getActivity(),null,false);

        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = ((AssociateAgentDialogAdapter)listView.getAdapter()).getCursor();
                cursor.moveToPosition(position);
                // calling method to associate the BusinessAgent selected from list to product.  Displaying results via snackBar.
                if(DataBaseUtils.associateAgentToProduct(getContext(),productId,cursor.getLong(0))){
                    listView.setOnItemClickListener(null);
                    Snackbar.make(view, getString(R.string.association_successful), Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dismiss();
                                }
                            })
                            .show();
                }else{
                    Snackbar.make(view, getString(R.string.association_failed), Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.associate_agent_list_title))
                .setView(view);
        getActivity().getSupportLoaderManager().restartLoader(C.ASSOCIATE_AGENT_DIALOG_LOADER, null, this);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DataBaseContract.BusinessAgentsTable._ID,
                DataBaseContract.BusinessAgentsTable.AGENT_NAME,
                DataBaseContract.BusinessAgentsTable.COMPANY_NAME};
        Uri uri = DataBaseProvider.AGENTS_URI;
        return new CursorLoader(getContext(),uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null){
            adapter.changeCursor(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}