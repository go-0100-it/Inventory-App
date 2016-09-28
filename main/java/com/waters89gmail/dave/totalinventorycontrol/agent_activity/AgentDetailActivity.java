package com.waters89gmail.dave.totalinventorycontrol.agent_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waters89gmail.dave.totalinventorycontrol.R;
import com.waters89gmail.dave.totalinventorycontrol.global_support.C;
import com.waters89gmail.dave.totalinventorycontrol.product_activity.ProductListFragment;


/**
 * An activity to display the selected Business Agent's details. The activity enables the user to edit the details and view the product that the Agent has been associated to.
 */
public class AgentDetailActivity extends AppCompatActivity {

    BusinessAgent agent;

    TextView agentName, address1, address2, email, contact1, contact2, preferredContact, webSite, companyName;
    LinearLayout address1Layout,  address2Layout, emailLayout, contact1Layout, contact2Layout, preferredContactLayout, webSiteLayout, companyNameLayout;
    AppBarLayout appBar;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolBar;

    //saving the BusinessAgent Object to re-use if the activity needs to be recreated.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(C.AGENT_SAVED_INSTANT, agent);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_detail);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //checking to see if this activity has been created already, if has not been created extract BusinessAgent Object from extras.
        //If has been created extract BusinessAgent Object from the bundle saved @onSaveInstantState.
        if (savedInstanceState == null) {
            //checking to see if intent exists, if it exists extract extras
            if (getIntent() != null) {
                Intent intent = getIntent();
                agent = (BusinessAgent) intent.getSerializableExtra(C.AGENT_TO_VIEW);
            }
        }else{
            agent = (BusinessAgent) savedInstanceState.getSerializable(C.AGENT_SAVED_INSTANT);
        }
        //finding views and setting to variables
        agentName = (TextView) findViewById(R.id.agent_name);
        address1 = (TextView) findViewById(R.id.address_one);
        address2 = (TextView) findViewById(R.id.address_two);
        email = (TextView) findViewById(R.id.email);
        contact1 = (TextView) findViewById(R.id.contact_one);
        contact2 = (TextView) findViewById(R.id.contact_two);
        preferredContact = (TextView) findViewById(R.id.preferred_contact);
        webSite = (TextView) findViewById(R.id.web_site);
        companyName = (TextView) findViewById(R.id.company_name);

        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolBar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //finding layout views and setting to variables.  These will be used to set visibility if corresponding TextViews are empty.
        address1Layout = (LinearLayout) findViewById(R.id.address_one_layout);
        address2Layout = (LinearLayout) findViewById(R.id.address_two_layout);
        emailLayout = (LinearLayout) findViewById(R.id.email_layout);
        contact1Layout = (LinearLayout) findViewById(R.id.contact_one_layout);
        contact2Layout = (LinearLayout) findViewById(R.id.contact_two_layout);
        preferredContactLayout = (LinearLayout) findViewById(R.id.preferred_contact_layout);
        webSiteLayout = (LinearLayout) findViewById(R.id.website_layout);
        companyNameLayout = (LinearLayout) findViewById(R.id.company_name_layout);

        //setting up view if agent is not null.
        if (agent != null) {
            setUpUi(agent);
        }
        //setting onClickListener for the editFab button.
        final FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.edit_fab);
        if (editFab != null) {
            editFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Calling EditAgentActivity to allow user to edit the BusinessAgent details.
                    //Passing a BusinessAgent Object as an extra with intent, and expecting a modified BusinessAgent Object as returned data, if result not cancelled
                    Intent intent = new Intent(AgentDetailActivity.this, EditAgentActivity.class);
                    intent.putExtra(C.AGENT_TO_EDIT, agent);
                    startActivityForResult(intent,C.EDIT_AGENT_REQUEST_CODE);
                }
            });
        }

        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //showing the toolBar title only when fully collapsed.
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset > collapsingToolBar.getMeasuredHeight()/-1.5) {
                    collapsingToolBar.setTitle(C.EMPTY_STRING);
                }else{
                    collapsingToolBar.setTitle(getString(R.string.agent_detail_activity));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ProductListFragment.class));
            return true;
        }else if (id == R.id.action_delete) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Getting results returned from intents started with startActivityForResults
     * @param requestCode returns the request code set when starting intent, so we know which intent is returning the results
     * @param resultCode returns either RESULT_OK or RESULT_CANCELLED, RESULT_OK is returned if the desired actions have occurred or if the desired data is being return.
     * @param data returns the intent data containing the extras passed back from the activity started for results.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //getting results from activities started for results.
        if (requestCode == C.EDIT_AGENT_REQUEST_CODE) {
            // this code runs if this result is from the EditAgentActivity
            if(resultCode == Activity.RESULT_OK){
                agent = (BusinessAgent) data.getSerializableExtra(C.EDITED_AGENT);
                setUpUi(agent);
                Snackbar.make(toolbar, getString(R.string.saved_changes), Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(toolbar, getString(R.string.changes_not_saved), Snackbar.LENGTH_LONG)
                        .setAction(C.ACTION, null).show();
            }
        }
    }

    /**
     * This method sets the text of the UI TextViews with the extracted values from the BusinessAgent Object.
     * Also, setting visibility of corresponding LayoutViews to GONE if values are null or to VISIBLE if not null.
     * @param agent The BusinessAgent Object to extract values that will be used to populate the UI TextViews.
     */
    private void setUpUi(BusinessAgent agent){

        agentName.setText(agent.getmAgentName());

        if(agent.getmAddress1().equals(C.EMPTY_STRING)){
            address1Layout.setVisibility(View.GONE);
        }else{
            address1.setText(agent.getmAddress1());
            address1Layout.setVisibility(View.VISIBLE);
        }
        if(agent.getmAddress2().equals(C.EMPTY_STRING)){
            address2Layout.setVisibility(View.GONE);
        }else{
            address2.setText(agent.getmAddress2());
            address2Layout.setVisibility(View.VISIBLE);
        }
        if(agent.getmEmail().equals(C.EMPTY_STRING)){
            emailLayout.setVisibility(View.GONE);
        }else{
            email.setText(agent.getmEmail());
            emailLayout.setVisibility(View.VISIBLE);
        }
        if(agent.getmContact1().equals(C.EMPTY_STRING)){
            contact1Layout.setVisibility(View.GONE);
        }else{
            contact1.setText(agent.getmContact1());
            contact1Layout.setVisibility(View.VISIBLE);
        }
        if(agent.getmContact2().equals(C.EMPTY_STRING)){
            contact2Layout.setVisibility(View.GONE);
        }else{
            contact2.setText(agent.getmContact2());
            contact2Layout.setVisibility(View.VISIBLE);
        }

        preferredContact.setText(agent.getmPreferredContact());

        if(agent.getmWebSite().equals(C.EMPTY_STRING)){
            webSiteLayout.setVisibility(View.GONE);
        }else{
            webSite.setText(agent.getmWebSite());
            webSiteLayout.setVisibility(View.VISIBLE);
        }
        if(agent.getmCompanyName().equals(C.EMPTY_STRING)){
            companyNameLayout.setVisibility(View.GONE);
        }else{
            companyName.setText(agent.getmCompanyName());
            companyNameLayout.setVisibility(View.VISIBLE);
        }
    }
}