package com.waters89gmail.dave.totalinventorycontrol.agent_activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.waters89gmail.dave.totalinventorycontrol.Dialogs.MultiUseDialog;
import com.waters89gmail.dave.totalinventorycontrol.MainActivity;
import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseContract;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseProvider;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.global_support.DialogUtils;

import java.util.Arrays;

/**
 * An activity to edit a BusinessAgent in the database or to add a new BusinessAgent to the database.
 */
public class EditAgentActivity extends AppCompatActivity implements MultiUseDialog.MultiUseDialogListener {

    private long agentId;
    private FloatingActionButton fab;

    private EditText agentNameEditText, agentAddressOneEditText,  agentAddressTwoEditText, agentEmailEditText, agentContactOneEditText,
            agentContactTwoEditText, companyNameEditText, agentWebSiteEditText;
    private Spinner preferredContactEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_agents_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //finding views and setting to variables for further reference
        agentNameEditText = (EditText) findViewById(R.id.agent_name_edit_text);
        agentAddressOneEditText = (EditText) findViewById(R.id.agent_address_one_edit_text);
        agentAddressTwoEditText = (EditText) findViewById(R.id.agent_address_two_edit_text);
        agentEmailEditText = (EditText) findViewById(R.id.agent_email_edit_text);
        agentContactOneEditText = (EditText) findViewById(R.id.agent_contact_one_edit_text);
        agentContactTwoEditText = (EditText) findViewById(R.id.agent_contact_two_edit_text);
        preferredContactEditText = (Spinner) findViewById(R.id.agent_pref_contact_edit_text);
        agentWebSiteEditText = (EditText) findViewById(R.id.agent_web_site_edit_text);
        companyNameEditText = (EditText) findViewById(R.id.agent_company_name_edit_text);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        getSupportActionBar().setTitle(getString(R.string.edit_agent_activity));

        //setting text changed listeners for auto phone number formatting
        agentContactOneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        agentContactTwoEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // Check if intent is not null, if it is not null, extract the BusinessAgent Object.
        // Call the editActivity method and pass the
        if (getIntent() != null) {
            Intent intent = getIntent();
            BusinessAgent agent = (BusinessAgent) intent.getSerializableExtra(C.AGENT_TO_EDIT);

            populateUi(agent);

            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editActivity(view);
                    }
                });
            }
        }
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * A method to extract data from the BusinessAgent Object and populate the EditText Views and Spinners
     * @param agent the BusinessAgent Object to extract the values from to populate the TextViews and Spinner.
     */
    private void populateUi(BusinessAgent agent){

        String[] preferredContactArray = getResources().getStringArray(R.array.preferred_contact_array);
        if (agent != null) {
            agent.getmPreferredContact();
            agentId = agent.getmAgentId();
            agentNameEditText.setText(agent.getmAgentName());
            agentAddressOneEditText.setText(agent.getmAddress1());
            agentAddressTwoEditText.setText(agent.getmAddress2());
            agentEmailEditText.setText(agent.getmEmail());
            agentContactOneEditText.setText(agent.getmContact1());
            agentContactTwoEditText.setText(agent.getmContact2());
            int i = Arrays.asList(preferredContactArray).indexOf(agent.getmPreferredContact());
            preferredContactEditText.setSelection(i);
            agentWebSiteEditText.setText(agent.getmWebSite());
            companyNameEditText.setText(agent.getmCompanyName());
        }
    }

    /**
     *
     */
    private void editActivity(View view) {
        // Verifying that the user inputted data into all the required fields.
        if(verifyDataEntry(view)){
            BusinessAgent editedAgent = createAgent();

            String selection = DataBaseContract.BusinessAgentsTable._ID + DataBaseContract.WHERE_EQUALS;
            String[] selectionArgs = {String.valueOf(agentId)};

            //creating an intent to return the result to the calling activity
            Intent returnIntent = new Intent();

            Uri uri = DataBaseProvider.AGENTS_URI;
            if(getContentResolver().update(uri, DataBaseUtils.createAgentContentValues(editedAgent), selection, selectionArgs) > C.NULL) {
                returnIntent.putExtra(C.EDITED_AGENT, editedAgent);
                setResult(Activity.RESULT_OK, returnIntent);
            }else{
                setResult(Activity.RESULT_CANCELED, returnIntent);
            }
            finish();
        }
    }

    /**
     * Used to create a new instance of the Product Class
     * @return returns a new Product Object. <strong>*** NOTE:</strong> Returns a product with a null productId. <strong>***</strong>
     */
    private BusinessAgent createAgent() {
        return new BusinessAgent(agentNameEditText.getText().toString(),
                agentAddressOneEditText.getText().toString(),
                agentAddressTwoEditText.getText().toString(),
                agentEmailEditText.getText().toString(),
                agentContactOneEditText.getText().toString(),
                agentContactTwoEditText.getText().toString(),
                preferredContactEditText.getSelectedItem().toString(),
                agentWebSiteEditText.getText().toString(),
                companyNameEditText.getText().toString());
    }

    /**
     * A method to delete a BusinessAgent from the DataBase
     */
    private void deleteAgent(){
        // Calling method to delete both the BusinessAgent and the Agent's associations to Products.
        int[] result = DataBaseUtils.deleteAgent(this,agentId);

        //
        if(result[0] > C.NULL){
            // If the deletion was successful, saving the Object type deleted and the number of associations deleted to Shared Preferences.
            // The data saved to Shared Preferences is used in the MainActivity to inform user of the results via a snackBar.
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra(C.FROM_ACTIVITY,C.EDIT_AGENT);
            intent.putExtra(C.DELETED_TYPE,C.AGENT);
            intent.putExtra(C.COUNT,result[1]);
            navigateUpTo(intent);

        }else{
            Snackbar.make(fab.getRootView(), getString(R.string.deleted_agent_failed), Snackbar.LENGTH_LONG)
                    .setAction(C.ACTION, null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_agent_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra(C.FROM_ACTIVITY,C.EDIT_AGENT);
            navigateUpTo(intent);
            return true;
        }else if(id == R.id.action_delete){
            // showing dialog to get user confirmation for delete
            DialogUtils.showMultiUseDialog(this,C.DELETE_AGENT_DIALOG_TAG,R.string.delete_agent_dialog_massage,R.string.delete_supplier_dialog_positive,C.NULL,R.string.delete_supplier_dialog_negative);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method used to validate user input, to ensure required data for further app functionality is entered at the time of Object creation and at the time of editing.
     * @param view requires a view object to set the snackBar.
     * @return returns a boolean value of true if all the required conditions are met.
     * Returns false if all required conditions are not met, after displaying a snackBar prompting user for required action by requesting focus on missing view.
     */
    private boolean verifyDataEntry(View view){
        View viewToFocus;
        int messageRes;
        if(agentContactOneEditText.getText().toString().equals(C.EMPTY_STRING) && !agentContactTwoEditText.getText().toString().equals(C.EMPTY_STRING)){
            agentContactOneEditText.setText(agentContactTwoEditText.getText().toString());
            agentContactTwoEditText.setText(C.EMPTY_STRING);
        }
        if (!agentNameEditText.getText().toString().equals(C.EMPTY_STRING)){
            if(!agentEmailEditText.getText().toString().equals(C.EMPTY_STRING) ||
                    !agentContactOneEditText.getText().toString().equals(C.EMPTY_STRING)){
                if(!preferredContactEditText.getSelectedItem().toString().equals(getString(R.string.please_select))){
                    if(preferredContactEditText.getSelectedItem().toString().equals(getString(R.string.phone)) && agentContactOneEditText.getText().toString().equals(C.EMPTY_STRING)){
                        //if PHONE has been set as preferred method of contact with an empty contact1 field.
                        messageRes = R.string.null_phone_contact_reference;
                        viewToFocus = agentContactOneEditText;
                    }else{
                        if(preferredContactEditText.getSelectedItem().toString().equals(getString(R.string.email)) && agentEmailEditText.getText().toString().equals(C.EMPTY_STRING)){
                            //if EMAIL has been set as preferred method of contact with an empty email field.
                            messageRes = R.string.null_email_contact_reference;
                            viewToFocus = agentEmailEditText;
                        }else{
                            return true;
                        }
                    }
                }else{ //if a preferred method of contact has not been entered.
                    messageRes = R.string.specify_a_preferred_contact;
                    viewToFocus = preferredContactEditText;
                }
            }else{ //if contact info has not been entered.
                messageRes = R.string.must_enter_contact;
                viewToFocus = agentEmailEditText;
            }
        }else{ //if agent name has not been entered.
            messageRes = R.string.must_enter_name;
            viewToFocus = agentNameEditText;
        }
        snackBarToRequestFocus(view,messageRes,R.string.ok,viewToFocus);
        return false;
    }

    /**
     * Common method to show SnackBar and set focus on a view.
     * @param view requires a view object to set the snackBar.
     * @param messageStringRes int value of a string resource. Corresponding string is used as the message displayed in the snackBar.
     * @param actionBtnStringRes int value of a string resource. Corresponding string used as the text for the displayed action button.
     * @param fieldToFocus The action taken sets focus on a view, requires the view to accept focus.
     */
    private void snackBarToRequestFocus(View view, @StringRes int messageStringRes,@StringRes int actionBtnStringRes, final View fieldToFocus ){
        Snackbar.make(view, messageStringRes,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionBtnStringRes, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(fieldToFocus == (preferredContactEditText)){
                            preferredContactEditText.performClick();
                        }else{
                            fieldToFocus.requestFocus();
                        }
                    }
                })
                .show();
    }

    /**
     * Overriding custom onClickListener interface to handle dialog positive button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // handling positive click call back from delete agent confirmation dialog.
        deleteAgent();
    }

    /**
     * Overriding custom onClickListener interface to handle dialog button clicks.
     * @param dialog returns dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNeutralClick(DialogFragment dialog) {}

    /**
     * Overriding custom onClickListener interface to handle dialog negative button clicks.
     * @param dialog returns the  dialog for reference. Using dialog.getTag() to identify which dialog is registering the clicks.
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {}
}


