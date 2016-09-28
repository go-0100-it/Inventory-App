package com.waters89gmail.dave.totalinventorycontrol.agent_activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseProvider;
import com.waters89gmail.dave.totalinventorycontrol.database.DataBaseUtils;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.product_activity.Product;

/**
 * An activity to add new BusinessAgents.
 */
public class AddAgentsActivity extends AppCompatActivity {

    //boolean variable set true if this activity was called to create an agent for association as a product supplier
    private boolean requestedAssociation = false;

    private EditText agentNameEditText, agentAddressOneEditText,  agentAddressTwoEditText, agentEmailEditText, agentContactOneEditText,
            agentContactTwoEditText, companyNameEditText,agentWebSiteEditText;
    private Spinner preferredContactSpinner;
    private Product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_agents_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if ( getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //checking if intent extras exist.
        if (getIntent() != null) {
            Intent intent = getIntent();
            //if extras exist, checking if extra is a product, if true then this activity was called as a request to create an agent for association to this product.
            //setting requestedAssociation variable accordingly
            if(intent.hasExtra(C.PRODUCT_FOR_ASSOCIATION)) {
                product = (Product) intent.getSerializableExtra(C.PRODUCT_FOR_ASSOCIATION);
                requestedAssociation = true;
            }
        }
        getSupportActionBar().setTitle(getString(R.string.new_agent_activity));
        // finding views and setting to variables for reference
        agentNameEditText = (EditText) findViewById(R.id.agent_name_edit_text);
        agentAddressOneEditText = (EditText) findViewById(R.id.agent_address_one_edit_text);
        agentAddressTwoEditText = (EditText) findViewById(R.id.agent_address_two_edit_text);
        agentEmailEditText = (EditText) findViewById(R.id.agent_email_edit_text);
        agentContactOneEditText = (EditText) findViewById(R.id.agent_contact_one_edit_text);
        agentContactTwoEditText = (EditText) findViewById(R.id.agent_contact_two_edit_text);
        preferredContactSpinner = (Spinner) findViewById(R.id.agent_pref_contact_edit_text);
        agentWebSiteEditText = (EditText) findViewById(R.id.agent_web_site_edit_text);
        companyNameEditText = (EditText) findViewById(R.id.agent_company_name_edit_text);

        //setting text changed listeners for auto phone number formatting
        agentContactOneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        agentContactTwoEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        getSupportActionBar().setTitle(getString(R.string.new_agent_activity));

        if (fab != null) {
            //setting onClick listener for the "SAVE" floating action button.
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //separated functional code for better readability
                    addActivity(view);
                }
            });
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Core function of this activity.
     * @param view The snackBar requires a view as a reference to a parent view
     */
    private void addActivity(View view) {

        //calling method to check user input.  User must fill in all fields and add picture.
        if (verifyDataEntry(view)) {
            //if user input is valid, enter data into database via content provider.  This method returns _ID of the row entered.
            long agentId = ContentUris.parseId(getContentResolver().insert(DataBaseProvider.AGENTS_URI, DataBaseUtils.createAgentContentValues(createAgent())));
            //result from above method will return C.NO_RESULT if entry failed.
            if (agentId  !=  C.NO_RESULT) {
                //If Agent creation was called via an association request, then creating the association by entering agentId and productId into database as a key pair.
                //resetting variable to false and returning intent to calling activity.
                if(requestedAssociation){
                    DataBaseUtils.associateAgentToProduct(AddAgentsActivity.this,product.getmProductId(),agentId);
                    requestedAssociation = false;
                }
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                Snackbar.make(view, getString(R.string.add_agent_failed), Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
            }
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
                preferredContactSpinner.getSelectedItem().toString(),
                agentWebSiteEditText.getText().toString(),
                companyNameEditText.getText().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, AgentListFragment.class));
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
        //If contact 1 has been left blank and contact 2 has data, copy text from contact 2 into contact 1 and clear contact 2.
        if(agentContactOneEditText.getText().toString().equals(C.EMPTY_STRING) && !agentContactTwoEditText.getText().toString().equals(C.EMPTY_STRING)){
            agentContactOneEditText.setText(agentContactTwoEditText.getText().toString());
            agentContactTwoEditText.setText(C.EMPTY_STRING);
        }
        //Checking to ensure that the Agent name field is not blank
        if (!agentNameEditText.getText().toString().equals(C.EMPTY_STRING)){
            //Checking to ensure that the user has entered either an email or a contact number.
            if(!agentEmailEditText.getText().toString().equals(C.EMPTY_STRING) ||
                    !agentContactOneEditText.getText().toString().equals(C.EMPTY_STRING)){
                //Checking to ensure that the preferred contact has been selected and not left as the default "Please select"
                if(!preferredContactSpinner.getSelectedItem().toString().equals(getString(R.string.please_select))){
                    //Checking to ensure that the user has entered the data corresponding the preferred contact selected.
                    if(preferredContactSpinner.getSelectedItem().toString().equals(getString(R.string.phone)) && agentContactOneEditText.getText().toString().equals(C.EMPTY_STRING)){
                        //if PHONE has been set as preferred method of contact with an empty contact1 field prompt user to take action.
                        messageRes = R.string.null_phone_contact_reference;
                        viewToFocus = agentContactOneEditText;
                    }else{
                        if(preferredContactSpinner.getSelectedItem().toString().equals(getString(R.string.email)) && agentEmailEditText.getText().toString().equals(C.EMPTY_STRING)){
                            //if EMAIL has been set as preferred method of contact with an empty email field prompt user to take action.
                            messageRes = R.string.null_email_contact_reference;
                            viewToFocus = agentEmailEditText;
                        }else{
                            //Return true as all required data has been entered appropriately by the user.
                            return true;
                        }
                    }
                }else{ //if a preferred method of contact has not been entered, prompt user to take action with snackBar.
                    messageRes = R.string.specify_a_preferred_contact;
                    viewToFocus = preferredContactSpinner;
                }
            }else{ //if contact info has not been entered, prompt user to take action with snackBar.
                messageRes = R.string.must_enter_contact;
                viewToFocus = agentEmailEditText;
            }
        }else{ //if agent name has not been entered, prompt user to take action with snackBar.
            messageRes = R.string.must_enter_name;
            viewToFocus = agentNameEditText;
        }
        //Return false because either, all required data has not been entered or has not been entered appropriately.
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
    private void snackBarToRequestFocus(View view, int messageStringRes, int actionBtnStringRes, final View fieldToFocus ){
        Snackbar.make(view, messageStringRes,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionBtnStringRes, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(fieldToFocus == (preferredContactSpinner)){
                            preferredContactSpinner.performClick();
                        }else{
                            fieldToFocus.requestFocus();
                        }
                    }
                })
                .show();
    }
}
