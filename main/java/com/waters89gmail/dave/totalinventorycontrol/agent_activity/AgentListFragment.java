package com.waters89gmail.dave.totalinventorycontrol.agent_activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.waters89gmail.dave.totalinventorycontrol.Adapters.AgentCursorAdapter;
import com.waters89gmail.dave.totalinventorycontrol.Dialogs.MultiUseDialog;
import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseContract;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseProvider;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.global_support.Permissions;

/**
 * A fragment to list business agent Items.
 */
public class AgentListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,ActivityCompat.OnRequestPermissionsResultCallback, MultiUseDialog.MultiUseDialogListener{

    private AgentCursorAdapter adapter;
    private ListView listView;
    private TextView emptyViewText;
    private ScrollView emptyView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AgentListFragment() {}

    @SuppressWarnings("unused")
    public static AgentListFragment newInstance(int columnCount) {
        return new AgentListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agents_list_fragment, container, false);

        //setting view variables by findView
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new AgentCursorAdapter(getActivity(),null,false);
        emptyView = (ScrollView) view.findViewById(R.id.empty_view);
        emptyViewText = (TextView) view.findViewById(R.id.empty_view_text);

        //setting adapter to list view and setting OnClickListener
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClick);

        //starting Cursor Loader and returning created view
        getActivity().getSupportLoaderManager().restartLoader(C.BUSINESS_AGENT_LIST_LOADER, null, this);
        return view;
    }

    /**
     * Overriding onResume to restart the loader to refresh cursor if frag has been recreated.
     */
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(C.BUSINESS_AGENT_LIST_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Implementing the OnItemClickListener from the AdapterView Class to listen for list item clicks to...
     * Get the clicked position and move the cursor to that position
     * Extract the data at the clicked position and create a new BusinessAgent object from the extracted data.
     * Send the BusinessAgent object as an intent extra to the AgentDetailActivity and start the activity.
     */
    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // moving the cursor the selected items position.
        Cursor cursor = ((AgentCursorAdapter)listView.getAdapter()).getCursor();
        cursor.moveToPosition(position);

        // Creating a new Business Agent Object with values from cursor at the selected position.
        BusinessAgent agent = new BusinessAgent(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
        cursor.close();

        // Starting Agent Detail activity with the selected Business Agent Object passed as an extra.
        Intent intent = new Intent(getContext(), AgentDetailActivity.class);
        intent.putExtra(C.AGENT_TO_VIEW, agent);
        startActivity(intent);
        }
    };

    // Passing params to loader @onCreateLoader to load all fields and rows of the BusinessAgentsTable
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DataBaseContract.BusinessAgentsTable._ID,
                DataBaseContract.BusinessAgentsTable.AGENT_NAME,
                DataBaseContract.BusinessAgentsTable.ADDRESS_1,
                DataBaseContract.BusinessAgentsTable.ADDRESS_2,
                DataBaseContract.BusinessAgentsTable.EMAIL,
                DataBaseContract.BusinessAgentsTable.CONTACT_1,
                DataBaseContract.BusinessAgentsTable.CONTACT_2,
                DataBaseContract.BusinessAgentsTable.PREFERRED_CONTACT,
                DataBaseContract.BusinessAgentsTable.WEBSITE,
                DataBaseContract.BusinessAgentsTable.COMPANY_NAME};
        Uri uri = DataBaseProvider.AGENTS_URI;
        return new CursorLoader(getContext(),uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //setting the appropriate views and visibility if the loader returned data(not null).
        String emptyViewMessage = (getString(R.string.empty_view_agents_list) + "\n" + getString(R.string.empty_view_get_started));
        if(data != null){
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter.swapCursor(data);
            adapter.notifyDataSetChanged();
            if(!data.moveToFirst()){
                emptyView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                emptyViewText.setText(emptyViewMessage);
            }
        }else{
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            emptyViewText.setText(emptyViewMessage);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    /**
     * Overriding custom onClickListener interface to handle dialog positive button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {}

    /**
     * Overriding custom onClickListener interface to handle dialog neutral button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNeutralClick(DialogFragment dialog) {}

    /**
     * Overriding custom onClickListener interface to handle dialog negative button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        switch (dialog.getTag()){
            //Handling the negative button click call back from phone permission rationale dialog.
            case C.PHONE_PERMISSIONS_DIALOG:
                Permissions.requestPhonePermission((AppCompatActivity) getContext());
        }
    }
}
